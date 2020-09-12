#### ehcache介绍

在`java`中有很多技术都可以实现缓存功能，最简单直接就是使用`java`自带的`Map`容器，或者就是使用现有的缓存框架，例如`memcache`,`ehcache` ,以及非常热门的`redis`。这里介绍`ehcache`的主要是因为它真的很方便，而且`memcache`和`redis`都需要额外搭建服务，更适合分布式部署的项目以便于各个模块之间的使用共有的缓存内容。而`ehcache`主要是内存缓存，也可以缓存到磁盘中，速度快，效率高，功能也强大，适合我们一般的单个项目使用。

#### spring boot 配置ehcache

在`spring boot`中配置`ehcahce`主要有以下四步：

1. `pom.xml`中添加依赖；
2. 配置`ehcache.xml`配置文件；
3. 开启缓存；
4. 利用注解使用缓存。

下面将详细介绍每一步。

##### 一、添加依赖

要想在`spring boot`中使用缓存，首先需要开启缓存，然后添加`ehcache`的依赖，所以需要在`pom.xml`中添加如下依赖项：

```xml
<!--开启缓存-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<!-- EhCache -->
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```

**NOTE:**在第一次添加依赖时，可能报红，连上外网，然后点击Reload All Maven Projects按钮可解决。

添加了依赖之后，`spring boot`会自动默认加载`src/mian/resources`目录下的`ehcache.xml`文件，所以需要在该目录下手动创建该文件，这里给出测试样例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">
	 
	 <!-- 磁盘缓存文件路径 -->
	 <diskStore path="java.io.tmpdir"/>
	 
	 <!-- 默认配置 -->
	 <defaultCache eternal="false"
	    maxElementsInMemory="1000"
	    overflowToDisk="false"
	    diskPersistent="false"
	    timeToIdleSeconds="0"
	    timeToLiveSeconds="600"
	    memoryStoreEvictionPolicy="LRU"/>
	    
	 <!-- 自定义配置 -->
	 <cache name="userCache"
	   eternal="false"
	   maxElementsInMemory="1000"
	   overflowToDisk="false"
	   diskPersistent="false"
	   timeToIdleSeconds="0"
	   timeToLiveSeconds="600"
	   memoryStoreEvictionPolicy="LRU"/>
</ehcache>
```

下面介绍样例中出现的三个节点；

+ `<diskStore>`：这个节点是非必须的，只有在使用了磁盘存储的情况下才需要配置，表示缓存文件在磁盘中保存的路径，该路径通过`path`属性来指定，磁盘缓存使用的文件后缀名是`*.data`和`*.index`,主要有以下几个值：

  + `user.home`:用户主目录
  + `user.dir`:用户当前的工作目录
  + `java.io.tmpdir`:默认临时路径
  + `ehcache.disk.store.dir`:cache的配置目录
  + 自定义绝对路径

  如果对于这几个目录不熟悉，可以在`java`中获取，如下：

  ```Java
  public static void main(String[] args) {
  	System.out.println(System.getProperty("user.home"));
  	System.out.println(System.getProperty("user.dir"));
  	System.out.println(System.getProperty("java.io.tmpdir"));
  }
  ```

  下面是我本机打印出来的路径，仅做参考：

  ```java
  C:\Users\胡发伟
  D:\Si_tech\Code\ecop-cust-parent
  C:\Users\胡发伟\AppData\Local\Temp\
  ```

  **NOTE：这里需要注意一点，要想某个对象被缓存到磁盘中，需要该对象实现序列化接口**。

- `<ehcache>`：自定义缓存区，可以有零个或者多个，重要属性如下：
  - `name`：缓存区名字，必须属性，用来区分缓存区的唯一标识。
  - `eternal`:设置缓存区中的内容是否永久有效，可选值`true`或`false`，如果选择`true`那么设置的`timeToIdleSeconds`以及`timeToLiveSeconds`将失效。
  - `maxElementsInMemory`:该缓存区中最多可以存放的对象数量，超过这个数量时，会根据`overflowToDisk`属性的值有不同的操作。
  - `overflowToDisk`:缓存对象超出最大数量时是否启用磁盘保存，可选值`true`或`false`，值为`true`时，会将超出的内容缓存到磁盘中，为`false`时则会根据`memoryStoreEvictionPolicy`属性配置的策略替换掉原来的内容。
  - `diskPersistent`:磁盘存储是否在虚拟机重启后持续存在，默认是`false`,如果为`true`系统在初始化时会将磁盘中的内容加载到缓存。
  - `timeToIdleSeconds`:设置一个元素在过期前的空闲时间(单位:秒)，即访问该元素的最大间隔时间，超过这个时间该元素就会被清除,默认值为`0`，表示一个元素可以无限的空闲。
  - `timeToLiveSeconds`:设置一个元素在缓存区中的生存时间(单位:秒)，即从创建到清除的时间，超过这个时间，该元素就会被清除，默认值为`0`，表示一个元素可以无限的保存。
  - `memoryStoreEvictionPolicy`:缓存存储与清除策略。即达到`maxElementsInMemory`限制并且`overflowToDisk`值为`false`时`ehcache`就会根据这个属性的值执行相应的清空策略，该属性有以下三个值分别代表`ehcache`的三种缓存清理策略，默认值为`LRU`：
    1. `FIFO`:先进先出策略`(First In First Out)`。
    2. `LFU`:最少被使用`(Less Frequently Used)`，所有的缓存元素都会有一个属性记录该元素被使用的次数，清理元素时最小的那个将会被清除。
    3. `LRU`:最近最少使用`(Least Resently Used)`,所有缓存的元素都会有一个属性记录最后一次使用的时间，清理元素时时间最早的那个元素将会被清除。
  - `diskExpiryThreadIntervalSeconds`:磁盘缓存的清理线程运行间隔,默认是120秒。
  - `diskSpoolBufferSizeMB`:设置磁盘缓存区的大小，默认为30MB。
  - `maxEntriesLocalDisk`:设置磁盘缓存区最多能存放元素的数量。
- `<defaultCache>`:默认缓存区，即是一个`name`属性为`default`的`<ehcache>`节点，属性和`<ehcache>`节点都一样，一个`ehcache.xml`文件中只能有一个`<defaultCache>`节点，当我们没有自定义的`<ehcache>`时，默认使用该缓存区。

**NOTE：对于defaultCache这里有需要注意的地方，因为它是一个特殊的，所以我们在自定义缓存区的时候不能再定义名为default的,并且在使用的时候也不能通过value=default来指定默认的缓存区。**

这里补充一点，项目中如果不想使用默认的路径以及名字我们也可以自定义`ehcache`配置文件的名字以及路径，在`application.properties`配置文件中配置如下内容：

```properties
#后边的路径可以自己指定
spring.cache.ehcache.config=classpath:ehcache.xml
```

##### 二、开启缓存

`spring boot`中开启缓存非常简单，只需要在在启动类上添加一个`@EnableCaching`注解即可。

```java
@EnableCaching
@EnableSwagger2
@SpringBootApplication
public class EcopCustApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcopCustApplication.class, args);
	}

}
```

##### 三、使用注解

`spring boot`中使用`ehcache`缓存主要是通过注解来使用，而且我们一般在`service`实现层使用缓存功能，常用的注解如下：

| 注解        | 解释                                                         |
| ----------- | ------------------------------------------------------------ |
| @Cacheable  | 在方法执行前 Spring 先查看缓存中是否有数据，若有，则直接返回缓存数据；若无数据，调用方法将方法返回值放入缓存中 |
| @CachePut   | 无论怎样，都会将方法的返回值放到缓存中。                     |
| @CacheEvict | 将一条或多条数据从缓存中删除                                 |
| @Caching    | 可以通过 @Caching 注解组合多个注解策略在一个方法上           |

**NOTE: @Cacheable、@CachePut、@CacheEvict 都有 value 属性，指定的是要使用的缓存名称；key 属性指定的是数据在缓存中存储的键。**

+ ###### @Cacheable

该注解主要用在方法上边，每当程序进入被该注解标记的方法时，系统会首先判断缓存中是否存在相同`key`的元素，如果存在就直接返回缓存区中存放的值，**并且不会执行方法的内容**，如果不存在就执行该方法，并且判断是否需要将返回值添加到缓存区中，常用属性：

`value`:指定使用哪个缓存区，就是在配置文件里边配置的`<ehcache>`节点的`name`属性对应的值，可以指定多个值。

```Java
//指定一个
@Cacheable(value="userCache")
//指定多个
@Cacheable(value={"userCache","userCache2"})
```

`key`:缓存元素的`key`，需要按照`SpEL`表达式编写，这个一般按照指定方法的参数来确定。

```java
//#p0表示将第一个参数当成key,也可以直接写参数名字例如：#id,两者表达意思一样
@Cacheable(value="userCache",key="#p0")
public SysUser getById(Integer id){//内容省略...};
```

`condition`:添加缓存的条件，需要按照`SpEL`表达式编写，仅当该属性返回`true`时才添加缓存。

```java
 //仅当id>10时才缓存
 @Cacheable(value="userCache",key="#p0",condition="#p0>10")
 public SysUser getById(Integer id){//内容省略...};
```

+ ###### @CachePut

该注解主要用在方法上边，能够根据方法的参数以及返回值以及自定义的条件判断是否添加缓存，该注解标记的方法一定会执行，其属性与`@Cacheable`一致。

```java
@CachePut(value="userCache",key="#entity.id")
public SysUser insertSysuser(SysUser entity) {
	// TODO Auto-generated method stub		
    //省略内容
}
```

+ ###### @CacheEvict

  该注解主要用在方法上边，能根据条件对缓存进行清空，常用属性如下：

  + `value`:同上
  + `key`：同上
  + `condition`：同上
  + `allEntries`:是否清空所有缓存内容，默认为`false`，如果设置为`true`,那么在方法执行完成之后并且满足`condition`条件时会清空该缓存区的所有内容。
  + `beforeInvocation`：清除内容操作是否发生在方法执行之前，默认为`false`，表示清除操作在方法执行完之后再进行，如果方法执行过程中抛出异常，那么清除操作就不执行，如果为`true`，则表示在方法执行之前执行清除操作。

  ```java
  @CacheEvict(value="userCache",key="#p0",allEntries=false, beforeInvocation=true)
  public int deleteByPrimarykey(Integer key) {
  	// TODO Auto-generated method stub
      //省略内容
  }
  ```

##### 四、测试

上边介绍了`spring boot`配置`ehcache`的步骤，接下来测试缓存效果：

##### 场景描述：连续查询两次数据库，若缓存中有数据，则直接返回缓存数据；若无数据，调用方法将方法返回值放入缓存中。

Controller层：

```java
@Slf4j
@RequestMapping("/api/v1/acct/")
@Api(tags = "客户资料")
@Service("qryBuildingListInfoControllerSvc")
public class QryBuildingListInfoControllerImpl implements IQryBuildingListInfoController {

    @Autowired
    private IQryBulidingListInfoService qryBulidingListInfoService;

    @PostMapping("qryBulidingListInfo")
    @ApiOperation("楼宇信息查询")
    @ResponseBody
    @Override
    public OutDTO<QryBulidingListInfoRsp> qryBulidingListInfo(@RequestBody InDTO<PubReq<QryBulidingListInfoReq>> inDTO) {

        QryBulidingListInfoReq busiInfo = inDTO.getBody().getBusiInfo();

        QryBulidingListInfoRsp qryBulidingListInfoRsp = new QryBulidingListInfoRsp();
        try {
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");

            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return OutDTOBuild.builder().build(qryBulidingListInfoRsp);
    }
}
```

Service层:

```java
@Slf4j
@Component
public class QryBulidingListInfoServiceImpl implements IQryBulidingListInfoService {

    @Autowired
    private BsBulidinglistInfoMapper bsBulidinglistInfoMapper;

    @Cacheable(value="userCache",key="#p0")
    @Override
    public List<BsBulidinglistInfo> qryBulidingListInfo(QryBulidingListInfoReq busiInfo) {

        String buildingName = busiInfo.getBulidingName();//楼宇名称
        List<BsBulidinglistInfo> list = new ArrayList<BsBulidinglistInfo>();
        try {
            log.info("去数据库查询了数据！");
            list = bsBulidinglistInfoMapper.qryBulidingListInfoDao(buildingName);
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return list;
    }
}
```

**预期效果：**执行查询操作两次，访问数据库一次。

```java
2020-09-07 17:09:30.545  INFO 12080 --- [io-8891-exec-10] s.c.c.i.c.QryBulidingListInfoServiceImpl : 去数据库查询了数据！
2020-09-07 17:09:30.698  INFO 12080 --- [io-8891-exec-10] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
2020-09-07 17:09:30.699  INFO 12080 --- [io-8891-exec-10] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
```

可以很直白的看出，执行了两次查询操作，但是从数据库中取数据的操作就执行了一次，可见还有一次直接从缓存中取数据，达到了我们预期的效果。

##### 场景描述：对BUILDING_NO为"10001"的数据做两次修改以及两次查询操作，并且在执行修改操作时缓存了数据，执行该方法之前，BUILDING_NO为"10001"的数据不在缓存中。

Controller层：

```java
@Slf4j
@RequestMapping("/api/v1/acct/")
@Api(tags = "客户资料")
@Service("qryBuildingListInfoControllerSvc")
public class QryBuildingListInfoControllerImpl implements IQryBuildingListInfoController {

    @Autowired
    private IQryBulidingListInfoService qryBulidingListInfoService;

    @PostMapping("qryBulidingListInfo")
    @ApiOperation("楼宇信息查询")
    @ResponseBody
    @Override
    public OutDTO<QryBulidingListInfoRsp> qryBulidingListInfo(@RequestBody InDTO<PubReq<QryBulidingListInfoReq>> inDTO) {

        QryBulidingListInfoReq busiInfo = inDTO.getBody().getBusiInfo();

        QryBulidingListInfoRsp qryBulidingListInfoRsp = new QryBulidingListInfoRsp();
        try {
            //第一次修改
            qryBulidingListInfoService.updateBulidingInfo("1", Long.valueOf("10001"));
            //第一次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");

            //第二次修改
            qryBulidingListInfoService.updateBulidingInfo("2", Long.valueOf("10001"));
            //第二次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return OutDTOBuild.builder().build(qryBulidingListInfoRsp);
    }
}
```

Service层：

```java
@Slf4j
@Component
public class QryBulidingListInfoServiceImpl implements IQryBulidingListInfoService {

    @Autowired
    private BsBulidinglistInfoMapper bsBulidinglistInfoMapper;

    @Cacheable(value="userCache",key="#p0")
    @Override
    public List<BsBulidinglistInfo> qryBulidingListInfo(QryBulidingListInfoReq busiInfo) {

        String buildingName = busiInfo.getBulidingName();//楼宇名称
        List<BsBulidinglistInfo> list = new ArrayList<BsBulidinglistInfo>();
        try {
            log.info("去数据库查询了数据！");
            list = bsBulidinglistInfoMapper.qryBulidingListInfoDao(buildingName);
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return list;
    }

    @CachePut(value="userCache",key="#valueof1")
    @Override
    public void updateBulidingInfo(String valueOf, Long valueof1) {
        log.info("更新了数据库数据！！");
        bsBulidinglistInfoMapper.updateBulidingNo(valueOf, valueof1);
    }
}
```

**预期效果：**

1、服务器启动，第一次执行楼宇查询服务时，执行两次修改操作，访问两次数据库，两次查询操作第一次访问数据库，第二次不访问数据库直接从缓存中读取数据；

```java
2020-09-08 10:08:07.089  INFO 3520 --- [nio-8891-exec-8] s.c.c.i.c.QryBulidingListInfoServiceImpl : 更新了数据库数据！！
2020-09-08 10:08:07.157  INFO 3520 --- [nio-8891-exec-8] s.c.c.i.c.QryBulidingListInfoServiceImpl : 去数据库查询了数据！
2020-09-08 10:08:07.221  INFO 3520 --- [nio-8891-exec-8] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
2020-09-08 10:08:07.222  INFO 3520 --- [nio-8891-exec-8] s.c.c.i.c.QryBulidingListInfoServiceImpl : 更新了数据库数据！！
2020-09-08 10:08:07.258  INFO 3520 --- [nio-8891-exec-8] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
```

2、服务器不停机或不重启再次执行楼宇查询服务时，执行两次修改操作，访问两次数据库，两次查询操作不访问数据库。

```java
2020-09-08 10:14:56.318  INFO 3520 --- [nio-8891-exec-4] s.c.c.i.c.QryBulidingListInfoServiceImpl : 更新了数据库数据！！
2020-09-08 10:14:56.352  INFO 3520 --- [nio-8891-exec-4] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
2020-09-08 10:14:56.352  INFO 3520 --- [nio-8891-exec-4] s.c.c.i.c.QryBulidingListInfoServiceImpl : 更新了数据库数据！！
2020-09-08 10:14:56.393  INFO 3520 --- [nio-8891-exec-4] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行的楼宇！
```

新增操作和更新操作原理一样都是使用`@CachePut`注解。

##### 场景描述：测试删除数据清除相应缓存功能，缓存区中有BUILDING_NAME 为"工商银行-丰台支行-镇国寺支行（语音专线"的一条数据，删除BUILDING_NAME 为 "工商银行-丰台支行-镇国寺支行（语音专线）"的数据，再做查询操作。

Controller层：

```java
@Slf4j
@RequestMapping("/api/v1/acct/")
@Api(tags = "客户资料")
@Service("qryBuildingListInfoControllerSvc")
public class QryBuildingListInfoControllerImpl implements IQryBuildingListInfoController {

    @Autowired
    private IQryBulidingListInfoService qryBulidingListInfoService;

    @PostMapping("qryBulidingListInfo")
    @ApiOperation("楼宇信息查询")
    @ResponseBody
    @Override
    public OutDTO<QryBulidingListInfoRsp> qryBulidingListInfo(@RequestBody InDTO<PubReq<QryBulidingListInfoReq>> inDTO) {

        QryBulidingListInfoReq busiInfo = inDTO.getBody().getBusiInfo();

        QryBulidingListInfoRsp qryBulidingListInfoRsp = new QryBulidingListInfoRsp();
        try {
            //第一次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");

            //删除数据
            qryBulidingListInfoService.deleteBulidingInfo("工商银行-丰台支行-镇国寺支行（语音专线）");
            //第二次查询
            qryBulidingListInfoRsp.setBulidingList(qryBulidingListInfoService.qryBulidingListInfo(busiInfo));
            log.info("查询了名为" + busiInfo.getBulidingName() + "的楼宇！");
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return OutDTOBuild.builder().build(qryBulidingListInfoRsp);
    }
}
```

Service层：

```java
@Slf4j
@Component
public class QryBulidingListInfoServiceImpl implements IQryBulidingListInfoService {

    @Autowired
    private BsBulidinglistInfoMapper bsBulidinglistInfoMapper;

    @Cacheable(value="userCache",key="#busiInfo.bulidingName")
    @Override
    public List<BsBulidinglistInfo> qryBulidingListInfo(QryBulidingListInfoReq busiInfo) {

        String buildingName = busiInfo.getBulidingName();//楼宇名称
        List<BsBulidinglistInfo> list = new ArrayList<BsBulidinglistInfo>();
        try {
            log.info("去数据库查询了数据！");
            list = bsBulidinglistInfoMapper.qryBulidingListInfoDao(buildingName);
        }catch (Exception e){
            throw new BusiException("9999", "查询楼宇信息失败！！！");
        }
        return list;
    }
    
        @CacheEvict(value = "userCache", key = "#s", beforeInvocation=true)
    @Override
    public void deleteBulidingInfo(String s) {
        log.info("删除了数据库数据！！");
        bsBulidinglistInfoMapper.deleteBulidingInfo(s);
    }
}
```

**预期效果：**删除数据访问数据库一次，并清除缓存区中那个相应的数据，因为清除了缓存区的内容所以查询数据会访问数据库一次，但是数据库中相应的内容也已经被删除，所以查询不到任何数据。

```
2020-09-08 12:34:13.755  INFO 14980 --- [nio-8891-exec-7] s.c.c.i.c.QryBulidingListInfoServiceImpl : 去数据库查询了数据！
2020-09-08 12:34:13.878  INFO 14980 --- [nio-8891-exec-7] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行-丰台支行-镇国寺支行（语音专线）的楼宇！
2020-09-08 12:34:13.879  INFO 14980 --- [nio-8891-exec-7] s.c.c.i.c.QryBulidingListInfoServiceImpl : 删除了数据库数据！！
2020-09-08 12:34:13.958  INFO 14980 --- [nio-8891-exec-7] s.c.c.i.c.QryBulidingListInfoServiceImpl : 去数据库查询了数据！
2020-09-08 12:34:14.038  INFO 14980 --- [nio-8891-exec-7] .c.i.c.QryBuildingListInfoControllerImpl : 查询了名为工商银行-丰台支行-镇国寺支行（语音专线）的楼宇！
```

日志输出的内容与预期结果相符。