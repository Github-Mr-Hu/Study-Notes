#### 前言

上周在进行楼宇查询服务开发中，学习歌华代码时，发现在查询语句后拼接有where 1 = 1，当时直接拿来用了并不知道它的具体作用，今天SQL语句中这类拼接方式进行学习。

楼宇查询服务中对应的SQL语句为;

```xml
  <select id="qryBulidingListInfoDao" parameterType="java.lang.String" resultType="com.sitech.cbn.cust.po.BsBulidinglistInfo">
    SELECT
    BUILDING_NO as BUILDINGNO,
    BUILDING_NAME as BUILDINGNAME,
    BUILDING_ADDR as BUILDINGADDR,
    DATE_FORMAT(CREAT_DATE,'%Y-%m-%d %H:%i:%s') as CREATDATE
    FROM
    bs_bulidinglist_info
    WHERE
    1=1
    <if test="bulidingName != null">
      and BUILDING_NAME LIKE CONCAT('%', #{bulidingName},'%')
    </if>
  </select>
```



#### 一、常见形式

+ where 1 = 1
+ where 1 = 0

这种条件在执行前，就会被计算出true 或者false；主要是为了拼凑动态的sql语句，是为了避免where 关键字后面的第一个词直接就是 “and”而导致语法错误，是为了后面附加and …方便程序逻辑处理用的。

#### 二、详细介绍

+ ##### where 1 = 1

where 1=1; 这个条件始终为True，在不定数量查询条件情况下，1=1可以很方便的规范语句。为了进一步描述使用where 1=1的好处，下面分别从对不使用where 1=1和使用where 1=1两种情况进行讨论。

+ 不使用where 1=1的困扰

举个例子，在进行楼宇查询时，需要用户自行选择并输入查询关键词，那么，按平时的查询语句的动态构造，代码大体如下：

```java
string MySqlStr = "select * from bs_bulidinglist_info where";

　　if(null != bulidingName.Text || "" != bulidingName.Text)
　　{
　　　　MySqlStr = MySqlStr+ "BUILDING_NAME = " + "'bulidingName.Text'";
　　}
```

对于上述语句就存在两种情况:

 1、if条件成立

SQL语句就为：

```sql
SELECT * FROM bs_bulidinglist_info where BUILDING_NAME = "工商银行-丰台支行-镇国寺支行（语音专线）"
```

可以看得出来，这是一条完整的正确的SQL查询语句，能够正确的被执行，并根据数据库是否存在记录，返回数据。

2、if条件不成立

如果上述的两个IF判断语句不成立，那么，最终的MySqlStr动态构造语句变为：

```sql
　select * from table where
```

现在，来看一下这条语句，由于where关键词后面需要使用条件，但是这条语句根本就不存在条件，所以，该语句就是一条错误的语句，肯定不能被执行，不仅报错，同时还不会查询到任何数据。

上述的两种假设，代表了现实的应用，说明了语句的构造存在问题，不足以应付灵活多变的查询条件。

+  使用where 1 = 1的好处

对上述语句进行修改：

```java
string MySqlStr = "select * from bs_bulidinglist_info where 1 = 1";

　　if(null != bulidingName.Text || "" != bulidingName.Text)
　　{
　　　　MySqlStr = MySqlStr+ "BUILDING_NAME = " + "'bulidingName.Text'";
　　}
```

同样也存在两种情况：

1、if条件成立

SQL语句就为：

```sql
SELECT * FROM bs_bulidinglist_info where BUILDING_NAME = "工商银行-丰台支行-镇国寺支行（语音专线）"
```

很明显，该语句是一条正确的语句，能够正确执行，如果数据库有记录，肯定会被查询到。

2、if条件不成立

如果if条件都不成立，那么，语句变为：

```sql
select * from bs_bulidinglist_info where 1 = 1
```

现在这条语句，由于where 1=1 是为True的语句，该条语句语法正确，能够被正确执行，它的作用相当于：

```sql
select * from bs_bulidinglist_info
```

即返回表中所有数据。

综上所述：如果用户在条件查询页面中，不选择任何字段、不输入任何关键词，那么，必将返回表中所有数据；如果用户在页面中，选择了部分字段并且输入了部分查询关键词，那么，就按用户设置的条件进行查询。

+ ##### where 1 = 0

这个条件始终为false，结果不会返回任何数据，只有表结构，可用于快速建表；

例如：

```sql
SELECT * FROM bs_bulidinglist_info where 1 = 0
```

 该select语句主要用于读取表的结构而不考虑表中的数据，这样节省了内存，因为可以不用保存结果集。

同理：

```sql
create table bs_bulidinglist_info as select * from oldtable where 1=0; 
```

创建一个新表，新表的结构与查询的表的结构一样。





