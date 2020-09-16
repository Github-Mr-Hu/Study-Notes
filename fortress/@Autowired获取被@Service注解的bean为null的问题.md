在进行fortress定时任务开发时，发现使用@Autowired获取被@Service注解的bean时为null，出现的错误位置的代码如下：

```java
    @Override
    public boolean execute() throws Throwable {
        log.info("进入execute方法啦！！！");
        fortressExampleTimingJobService.execute(taskId);
        conn.commit();
        return true;
    }
```

具体错误如下：

```java
2020-09-15 11:30:44.242 [TimingScheduler_Runnable-0] ERROR com.sitech.fortress.task.TaskTimerRunnable.run(TaskTimerRunnable.java:156) - null
java.lang.NullPointerException: null
	at com.sitech.echofortress.dataflow.FortressExampleTimingJob.execute(FortressExampleTimingJob.java:47) ~[classes/:?]
	at com.sitech.fortress.task.TaskTimerRunnable.run(TaskTimerRunnable.java:44) [fortress-server-3.1.0.jar:3.1.0]
	at com.sitech.fortress.utils.BlockThreadPoolImpl$RunnableThread.run(BlockThreadPoolImpl.java:363) [fortress-server-3.1.0.jar:3.1.0]
2020-09-15 11:30:44.248 [TimingScheduler_Runnable-0] ERROR com.sitech.fortress.task.error.TaskReportError.report(TaskReportError.java:22) - Report error to /ZK/taskerrors/jobhufw/jobhufw_1

```

前后反复使用注解来获取bean都为null，后通过学习开完成的守护类代码，发现通过手动获取bean可解决该问题，具体实现方式如下:

```java
fortressExampleTimingJobService = LocalContextFactory.getInstance().getBean(FortressExampleTimingJobService.class);
```

问题得到了解决，但是仍不理解为什么会获取的bean为null。通过上网查找资料，大致明白了是什么原因导致？

首先对代码逻辑进行分析：

+ 日志可正常打印信息---"进入execute方法啦！！！"，说明程序已进入execute方法；‘
+ 返回空指针，说明没有找到bean，此时使用@Autowired注解来获取被@Service注解的bean，尝试使用其它注解来获取bean失败；
+ 手动实现获取bean，程序执行成功。

在上述三步中，问题出现在第二步，虽然bean获取失败，但此时bean肯定是已经存在于spring容器中。

通过上网查找资料了解到，出现bean为null的情况主要是由于spring容器中根本没有完成对注解bean的扫描，即Spring容器还没有加载完Bean，而程序就去调用了。因为使用注解bean的优先级肯定没有框架中的contextListener的优先级高，contextListener初始化的时候根据@Autowired进行扫描的话，得到的结果就为null。要想解决这个问题除了上述了方法外，还有一种方式就是可以通过ApplicationContextAware来getBean，Bean的name就是@Service注解的类的小写全称。