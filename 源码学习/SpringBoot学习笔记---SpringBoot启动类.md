# SpringBoot学习笔记之启动类

### SpringBoot入口类

```java
@SpringBootApplication
public class EcopBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcopBaseApplication.class, args);
	}
}
```

这是我们日常使用springboot开发见到次数最多的引导类了，完成这个类的编写，就完成了一个springboot项目的框架，springboot就会自动为我们完成一些默认配置，并帮我们初始化上下文容器，但细节我们是不知道的。

```Java
SpringApplication.run(EcopBaseApplication.class, args);
```

上面这一句看似简简单单的代码，却实实在在的体现了SPringBoot的强大，在对其学习后，对SpringBoot的启动过程大致梳理如下：

- 准备阶段
  - 参数初始化
  - 推断应用类型
  - 加载ApplicationContextInitializer系列初始化器
  - 加载ApplicationListener系列监听器
  - 推断应用入口类（main函数所在类）
- 运行阶段
  - 初始化容器启动计时器，开始计时
  - 初始化上下文ConfigurableApplicationContext、异常收集器
  - 配置系统参数"java.awt.headless"
  - 获取SpringApplicationRunListener系列监听器
  - 遍历所有SpringApplicationRunListener系列监听器，广播ApplicationStartingEvent
  - 处理args参数
  - 准备环境（创建、配置、绑定环境、广播ApplicationEnvironmentPreparedEvent）
  - 配置忽略Bean信息
  - 打印Banner
  - 根据应用类型创建上下文
  - 获取SpringBootExceptionReporter系列异常收集器
  - 上下文前置处理（执行ApplicationContextInitializer系列初始化器、加载资源、广播ApplicationPreparedEvent）
  - 刷新上下文
  - 启动完成，打印用时
  - 遍历前面设置的ConfigurableApplicationContext监听器，发布ApplicationStartedEvent
  - 回调实现了ApplicationRunner或CommandLineRunner接口的Runners
  - 遍历前面设置好的SpringApplicationRunListener监听器，发布ApplicationReadyEvent

完成上述整个操作后，整个SpringBoot项目已经启动完成，我们可以看到，整个过程中Spring的事件驱动机制起着举足轻重的作用，有了这个机制我们可以知晓容器的启动过程，并且可以监听到某些事件，对容器中我们关心的实例做进一步处理，因此深入理解事件驱动机制很有必要，它将帮助我们更好的理解和使用这个Spring框架体系。