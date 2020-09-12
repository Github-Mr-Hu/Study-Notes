# HashMap

在最近的代码开发中老是用到Map，但都是停留在简单的应用上，为了巩固Map基础和深入理解HashMap的原理，对HashMap的源代码进行了学习。由于集客系统开发是基于JDK1.8开发的，因此着重学习JDK1.8中HashMap的实现。

### 什么是HashMap?

- HashMap是最常见的Map，一个存储键值对的集合，它根据键的hashCode来存取数据，可以根据键直接获取对应的值，具有很快的访问速度，遍历时取得数据的顺序是随机的；HashMap允许键值是null，但只允许一条记录的键为null，允许多条记录的值为null；HashMap是线程不安全的，及统一时刻有多个线程写HashMap，会导致数据异常，如果要同步，可通过collection的SynhronizedMap或concurrectHashMap方法使得HashMap具有同步的能力。
- 组成：底层的数据结构由数组+链表+红黑树组成的。

### 属性

##### 静态属性：

```java
/* HashMap的容量必须是2的幂，默认的初始容量为16 */
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

/* 最大容量 */
static final int MAXIMUM_CAPACITY = 1 << 30;

/* 默认的负载因子 */
static final float DEFAULT_LOAD_FACTOR = 0.75f;

/* 树化阈值。当添加元素到桶中，如果桶中链表长度被添加到至少8，链表就转换为红黑树 */
static final int TREEIFY_THRESHOLD = 8;

/* 红黑树转链表的阙值 */
static final int UNTREEIFY_THRESHOLD = 6;

/**
 * 最小树形化阈值
 * 当HashMap中的table的长度大于64的时候，这时候才会允许桶内的链表转成红黑树（要求桶内的链表长度达到8）
 * 如果只是桶内的链表过长，而table的长度小于64的时候
 * 此时应该是执行resize方法，将table进行扩容，而不是链表转红黑树
 * 最小树形化阈值至少应为链表转红黑树阈值的四倍
 */
static final int MIN_TREEIFY_CAPACITY = 64;
```

##### 静态内部节点类：

- HashMap 是通过拉链法来解决冲突的，当链表到一定长度再转换成红黑树。以下是链表节点：

```Java
/**
 * Basic hash bin node, used for most entries.  (See below for
 * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
 */
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }
    public final String toString() { return key + "=" + value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}
```

##### 非静态属性：

```Java
/* 存放元素的数组 */
transient Node<K,V>[] table;

/* 存放具体元素的集 */
transient Set<Map.Entry<K,V>> entrySet;

/* Map中键值对个数 */
transient int size;

/* 修改次数 */
transient int modCount;

/**
 * 通过容量*加载因子计算得到，当键值对的个数大于这个值（{@link HashMap#size} >= {@link HashMap#threshold}）就进行扩容。
 * 这样做是为了减少哈希冲突。
 * 浪费了一定的空间，但换来的是查找效率的提高。
 */
int threshold;

/**
 * HashMap的负载因子
 * 负载因子控制这HashMap中table数组的存放数据的疏密程度
 * 负载因子越接近1，那么存放的数据越密集，导致查找元素效率低下
 * 负载因子约接近0，那么存放的数据越稀疏，导致数组空间利用率低下
 */
final float loadFactor;
```

### 构造方法：

##### HashMap()

```java
public HashMap() {
    //使用默认的参数
    //默认的负载因子、默认的容量
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```

默认的构造函数里面并没有对`table`数组进行初始化，这个操作是在`java.util.HashMap#putVal`方法进行的。

##### HashMap(int)

```Java
public HashMap(int initialCapacity) {
    // 调用重载构造函数
    // 指定初始容量，默认的负载因子
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```

##### HashMap(int, float)

```java
public HashMap(int initialCapacity, float loadFactor) {
    //指定初始容量，指定负载因子
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        // // 指定的初始容量大于最大容量，则取最大容量
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    //手动指定HashMap的容量的时候，HashMap的阈值设置跟负载因子无关
    this.threshold = tableSizeFor(initialCapacity);//tableSizeFor这个方法的作用是，根据指定的	 容量，大于指定容量的最小的2幂的值,比如说，给定15，返回16；给定30，返回32
}
```

###### tableSizeFor方法：

```java 
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

##### HashMap(map)

```Java
public HashMap(Map<? extends K, ? extends V> m) {
    //使用默认的负载因子
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}
```

putMapEntries函数的处理流程为：

- 如果table为空，则重新计算扩容上限；
- 如果HashMap的扩容上限小于指定Map的`size`，那么执行`resize`进行扩容；
- 将指定Map中所有的键值通过`putVal`方法放到HashMap中。

源码如下：

```Java
final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
    int s = m.size();
    if (s > 0) {
        //判断table是否已经实例化
        if (table == null) { // pre-size
            //计算m的扩容上限
            float ft = ((float)s / loadFactor) + 1.0F;
            //检查扩容上限是否大于HashMap的最大容量
            int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                     (int)ft : MAXIMUM_CAPACITY);
            if (t > threshold)
                //m的扩容上限大于当前HashMap的扩容上限，则需要重新调整
                threshold = tableSizeFor(t);
        }
        else if (s > threshold)
            //m的扩容上限大于当前HashMap的扩容上限，则需要重新调整
            resize();
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            //将m中所有的键值对添加到HashMap中
            K key = e.getKey();
            V value = e.getValue();
            putVal(hash(key), key, value, false, evict);
        }
    }
}
```

### 常用操作：

##### put操作:

```java
public V put(K key, V value) {
    //添加一个键值对到哈希表中，如果键值已经存在，用新的value值替换原来的value值
    return putVal(hash(key), key, value, false, true);
}
```

putVal方法源码如下：

```java
/**
 * Implements Map.put and related methods
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to put
 * @param onlyIfAbsent if true, don't change existing value
 * @param evict if false, the table is in creation mode.
 * @return previous value, or null if none 如果结点已经存在，返回之前的value,否则返回null
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //表为空初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    //index=(n - 1)&hash,对应桶中位置没有结点则new一个结点
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        //判断首结点和要插入结点是否相等，首先判断hash值是否相同，再用equals()判断
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //如果不相等，是树节点，红黑树插入
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //是链表，遍历链表尾插法插入
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    //如果链表长度大于等于8，树形化
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                //遍历时发现结点已存在，跳出循环
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        //已存在结点且onlyIfAbsent是false，将value赋值给已存在结点，返回旧的value
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    //HashMap同样要维护modCount
    ++modCount;
    //判断是否需要扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

![](D:\Si_tech\学习资料\学习笔记\Map-put.png)

##### get操作：

```Java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}
```

getNode方法源码如下:

```java 
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    //先判断，只有当哈希表不为空，且表的长度大于0，且key对应的位置有结点时，再进行操作，否则直接返回null
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        //先判断第一个结点与key是否匹配，如果匹配，返回第一个结点
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            //如果是树节点，进行红黑树的查找过程
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            //否则遍历链表查找
            do {
                //判断结点和key是否匹配
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

其它操作暂未使用，待后面遇到后添加。