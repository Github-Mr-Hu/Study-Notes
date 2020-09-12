####   前言

今天在修改客户经理变更申请测试类时，遇到使用new实例化来调用方法出现空指针异常问题，具体问题代码如下：

```java
@SpringBootTest(classes = EcopCustApplication.class)
@RunWith(SpringRunner.class)
public class ApplyChangeCustManagerControllerImplTest {

    @Test
    public void applyChangeCustManagerTest() {
        ApplyChangeCustManagerReq applyChangeCustManagerReq = new ApplyChangeCustManagerReq();
        CustList custList = new CustList();
        UserList userList = new UserList();
        custList.setCustId(Long.valueOf("123"));
        userList.setIdNo(Long.valueOf("123"));

        List<CustList> cList = new ArrayList<>();
        cList.add(custList);

        List<UserList> ulist = new ArrayList<>();
        ulist.add(userList);
        
        applyChangeCustManagerReq.setCustList(cList);
        applyChangeCustManagerReq.setUserList(ulist);
        applyChangeCustManagerReq.setOpNote("xxx操作此业务");

        OprInfo oprInfo = new OprInfo();
        oprInfo.setOpCode("m196");
        oprInfo.setGroupId("101");
        oprInfo.setLoginName("张xxx");
        oprInfo.setLoginNo("sitech");
        oprInfo.setLoginPhone("13099999999");
        oprInfo.setOpNote("xxx操作此业务");
        oprInfo.setTenancyCode("008");
		//使用new实例化对象
        IApplyChangeCustManagerController applyChangeCustManagerController = new ApplyChangeCustManagerControllerImpl();

        PubReq<ApplyChangeCustManagerReq> pubReq = new PubReq<>();
        pubReq.setOprInfo(oprInfo);
        pubReq.setBusiInfo(applyChangeCustManagerReq);

        InDTO<PubReq<ApplyChangeCustManagerReq>> inDTO = new InDTO<PubReq<ApplyChangeCustManagerReq>>();
        inDTO.setBody(pubReq);

        try {
           applyChangeCustManagerController.applyChangeCustManager(inDTO);
        } catch (Exception e) {
            throw new BusiException("9999", "客户经理变更提交异常！！！");
        }

    }
}
```

产生问题：

![](D:\Si_tech\学习资料\学习笔记\Spring自动注入(@Autowired)与new实例的区别.jpg)

通过debug模式运行代码发现，必传参数busiInfo和oprInfo中的参数获取正常，但是无法进入业务层，出现空指针异常。将new实例化改为使用Spring注解后，该问题解决，具体修改代码如下：

```java
@SpringBootTest(classes = EcopCustApplication.class)
@RunWith(SpringRunner.class)
public class ApplyChangeCustManagerControllerImplTest {
    //使用Spring注解
    @Autowired
    IApplyChangeCustManagerController applyChangeCustManagerController;

    @Test
    public void applyChangeCustManagerTest() {
        ApplyChangeCustManagerReq applyChangeCustManagerReq = new ApplyChangeCustManagerReq();
        CustList custList = new CustList();
        UserList userList = new UserList();
        custList.setCustId(Long.valueOf("123"));
        userList.setIdNo(Long.valueOf("123"));

        List<CustList> cList = new ArrayList<>();
        cList.add(custList);

        List<UserList> ulist = new ArrayList<>();
        ulist.add(userList);
        
        applyChangeCustManagerReq.setCustList(cList);
        applyChangeCustManagerReq.setUserList(ulist);
        applyChangeCustManagerReq.setOpNote("xxx操作此业务");

        OprInfo oprInfo = new OprInfo();
        oprInfo.setOpCode("m196");
        oprInfo.setGroupId("101");
        oprInfo.setLoginName("张xxx");
        oprInfo.setLoginNo("sitech");
        oprInfo.setLoginPhone("13099999999");
        oprInfo.setOpNote("xxx操作此业务");
        oprInfo.setTenancyCode("008");
		//使用new实例化对象
//        IApplyChangeCustManagerController applyChangeCustManagerController = new ApplyChangeCustManagerControllerImpl();

        PubReq<ApplyChangeCustManagerReq> pubReq = new PubReq<>();
        pubReq.setOprInfo(oprInfo);
        pubReq.setBusiInfo(applyChangeCustManagerReq);

        InDTO<PubReq<ApplyChangeCustManagerReq>> inDTO = new InDTO<PubReq<ApplyChangeCustManagerReq>>();
        inDTO.setBody(pubReq);

        try {
           applyChangeCustManagerController.applyChangeCustManager(inDTO);
        } catch (Exception e) {
            throw new BusiException("9999", "客户经理变更提交异常！！！");
        }

    }
}
```

为了加强对Spring自动注解和new实例化区别的理解，避免后面遇到这类问题，下面将对它们进行分析和总结。

#### 一、原因分析

为什么在new对象跟自动注入对象同时使用或单独使用new对象时会空指针，还有就是new对象怎么处理才不会出现空指针的问题。

**根本原因就在当spring框架帮我们管理的时候会帮我们自动的初始化接下来用到的一些属性，而通过用new实例的方法去做，在实例中用到的某些属性可能就需要我们自己去给set值做一个初始化，否则就有可能产生空指针的错误。**

**NOTE：**

当同时使用new对象和自动注入对象时，自动注入对象会失效（在代码执行到new对象实例化代码时，注入调用方式就会失效，spring框架就不会替我们去管理它）；

new一个对象的时候，初始化顺序是，父类静态块>子类静态块> 父类属性(先系统默认值，后直接你赋予的值) >父类构造器>子类属性>子类构造器。

#### 二、如何处理new对象实例化而不出现空指针异常？

+ 第一种办法，就是让实例中的调用的方法中不存在使用另一个对象的情况；
+ 第二种办法，我们自己给new的对象做初始化。

通过这两种方式处理过后，即使不由框架为我们管理，也可以达到我们的目的，避免出现空指针的问题。