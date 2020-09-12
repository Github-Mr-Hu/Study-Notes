#### 前言

在最近的接口开发中，遇到了where 和having两个SQL关键字，感觉自身理解不深刻，故对他们继续了学习，首先对**where** 和**having**进行一个简单的说明，内容如下：

+ **where** 不能放在**group by** 后面 ；

+ **having**是跟**group by** 连在一起用的，放在**group by**后面，此时的作用相当于**where** ；

+ **where** 后面的条件中不能有聚集函数，比如SUM(),AVG()等， **where** 和**having**都是对查询结果的一种筛选，说的书面点就是设定条件的语句。

下面将分别对它们的具体用法和异同点进行分析。

#### 一、聚合函数

聚合函数有时候也叫统计函数，它们的作用通常是对一组数据的统计，比如说求最大值，最小值，总数，平均值（ MAX,MIN，COUNT, AVG）等。这些函数和其它函数的根本区别就是它们一般作用在多条记录上。

+ ##### 举例：

```sql
SELECT SUM(BUILDING_NO) FROM bs_bulidinglist_info
```

查询结果为：

```sql
SUM(BUILDING_NO)|
----------------|
           80028|
```

这里的SUM作用是统计bs_bulidinglist_info（楼宇信息表）表中BUILDING_NO（楼宇号）字段的总和，结果就是该查询只返回一个结果，即楼宇号总和。通过使用GROUP BY 子句，可以让SUM 和 COUNT 这些函数只对属于这一组的数据起作用。

#### 二、where子句

where子句仅仅用于从from子句中返回的值，from子句返回的每一行数据都会用where子句中的条件进行判断筛选。where子句中允许使用比较运算符（>,<,>=,<=,<>,!=|等）和逻辑运算符（and，or，not）。用法比较简单。

#### 三、having子句

having子句通常是与order by 子句一起使用的。因为having的作用是对使用group by进行分组统计后的结果进行进一步的筛选。举个例子：现在需要找到楼宇号大于10000的楼宇号？ 第一步：

```sql
select BUILDING_NO from bs_bulidinglist_info group by BUILDING_NO
```

执行结果如下：

```sql
BUILDING_NO|
-----------|
      10000|
      10001|
      10002|
      10003|
      10004|
      10005|
      10006|
      10007|
```

可以看出得到了想要的结果。不过现在如果想要楼宇号大于10000？那么可以使用having来帮我们来进一步筛选。 故需要进行第二步：

```sql
select BUILDING_NO from bs_bulidinglist_info group by BUILDING_NO having BUILDING_NO > 10000
```

筛选结果如下:

```sql
BUILDING_NO|
-----------|
      10001|
      10002|
      10003|
      10004|
      10005|
      10006|
      10007|
```

#### 四、对比where子句和having子句，深入理解

在查询过程中聚合语句(sum,min,max,avg,count)要比having子句优先执行，简单的理解为只有有了统计结果后我才能执行筛选。where子句在查询过程中执行优先级别优先于聚合语句(sum,min,max,avg,count)，因为它是一句一句筛选的。HAVING子句可以让我们筛选成组后的对各组数据筛选。，而WHERE子句在聚合前先筛选记录。

+ ##### 举例

  现在我们想要AM_ID = "peifei"，BUILDING_NO > 10002的楼宇号？ 我们这样分析：通过where子句筛选出AM_ID = "peifei"的楼宇号，然后在对剩下的楼宇号进行筛选，使用having子句对统计结果进行筛选。

  ```sql
  select BUILDING_NO from bs_bulidinglist_info where AM_ID = "peifei" group by BUILDING_NO having BUILDING_NO > 10002
  ```

  筛选结果为：

  ```sql
  BUILDING_NO|
  -----------|
        10003|
        10004|
        10005|
  ```

#### 五、异同点

它们的相似之处就是定义搜索条件，不同之处是where子句为单个筛选而having子句与组有关，而不是与单个的行有关。 最后:理解having子句和where子句最好的方法就是基础select语句中的那些句子的处理次序：where子句只能接收from子句输出的数据，而having子句则可以接受来自group by，where或者from子句的输入。