[TOC]

#### 1、编写任务实现代码:

+ 守护类任务，继承TableTaskBase类，重写其中的方法;
+ 定时类任务，实现TimerTaskExecutor接口，重写其中的方法；

其中守护类任务的流转流程如下：

```java
{
        Prepare();
        updateDspList();
        list=qryDspList();
        for(list){
        executeBusi();
        updateDspByKey();
        }
}
```

定时类任务的流转流程如下：

```java
{
        Prepare();
        execute();
}
```

NOTE：无论是守护类还是定时类任务都会重写cleanup()方法，该方法中可以添加如关闭数据库连接等操作，也可以为空，不影响任务执行。

#### 2、修改配置文件：

+ captain.id，节点管理者标识文件，内容形式如下：

  ```java
  captain1
  ```

  当存在多个节点时，可在里面添加，添加形式如captain2等，***每台主机都不一样。***

+ worker.id，Worker实例号文件，环境部署时需要修改为0，从0开始，会自动增加；

+ fortress.conf，fortress平台配置文件，包含zookeeper相关的内容；

+ log4j2.xml，定义Commander和Captain框架的日志输出策略，包括日志保存路径，文件名称，保留等。

+ zkInitConfig.json，***重点关注***，集群注册信息，用于注册到zookeeper上，根据需求去按照要求设计自己的注册文件。主要包含内容如下：

  + cluster，集群配置；
  + captainGroups，节点组配置；
  + captains：节点管理者配置；
  + projects：工程配置；***重点关注以下配置：***
    + projectId：工程标识，用来作为zookeeper的路径，所以必须是字母和数字；
    + envRunClass：worker启动时的初始化类名。在此类中可以完成project的初始化操作，如加载spring配置文件等；
    + homePath：对应所开发的工程名配置；
    + jobGroups：作业组配置；
      + jobGroupId：作业组名称。用来作为zookeeper的路径，所以必须是字母和数字；
      + jobs：作业清单配置；
        + jobId：作业名称。用来作为zookeeper的路径，所以必须是字母和数字；
        + type：作业类型。取值范围：daemon（守护类）、timer（定时类）；
        + isValid：是否有效，取值范围：true, false。只有有效的job才会启动；
        + cron：当type为timer时生效，具体形式可参见[cron表达式在线生成工具](http://www.pppet.net/)；

**NOTE:** 在进行工程配置，即projects配置时应注意以下事项：

+ 当新增工程时，注意修改projectId，以和他人区分开来；
+ 修改工程路径配置，即homePath，当未修改时，会报classNotFoundException错误；

#### 3、上传代码至服务器

+ 在服务器captain1同级目录下新建工程名，在工程中分别新建***bin***、***conf*** 、***classes*** 、***lib*** 文件夹，具体创建shell命令如下：

  ```shell
  $ mkdir project 
  $ cd project
  $ mkdir bin
  $ mkdir conf 
  $ mkdir lib 
  $ mkdir classes 
  ```

+ 本地编译代码，可使用IDEA进行代码编译；
+ 将编译好的代码，上传至服务器创建的工程目录下的classes文件夹中。若使用IDEA，可直接使用工具进行编译，会在工程目录下生成对应的target文件夹，打开其中的classes文件夹，将里面的内容上传至服务目录下的classes文件夹中；
+ 上传jar包至服务器工程目录下的lib文件夹中，同理使用IDEA可将target文件夹内lib中的内容上传至服务器对应文件夹中，其中需要将log4j相关jar包共4个全部屏蔽或删除；
+ 上传fortress.log4j2.xml文件至服务器工程目录下的conf文件夹，用于配置该工程的日志记录信息，方便出错时进行查看。

#### 4、运行fortress环境

+ 运行regZk.sh，修改配置文件后，都需要进行注册，具体shell命令为：

  ```shell
  sh regZk.sh
  ```

+ 运行startCommander.sh，具体shell命令为：

  ```shell
  sh startCommander.sh
  ```

+ 运行startCaptain.sh，具体shell命令为：

  ```shell
  sh startCaptain.sh
  ```

  其中startCommander.sh和startCaptain.sh运行顺序没有严格顺序要求。

#### 5、关闭fortress环境

+ 运行stopCommander.sh，具体shell命令为：

  ```shell
  sh stopCommander.sh
  ```

+ 运行stopCaptain.sh，具体shell命令为：

  ```shell
  sh stopCaptain.sh
  ```

**NOTE:**  在关闭fortress环境是必须先停止stopCommander.sh，在关闭stopCaptain.sh，否则会触发负载均衡，当存在多个节点时，会将对应执行任务的进程转移至其它服务节点，为了减少其他节点压力，必须先关闭Commander。

#### 6、调试期间遇到的问题

参见学习贴

[@Autowired获取被@Servic]: http://eip.teamshub.com/t/4900621

