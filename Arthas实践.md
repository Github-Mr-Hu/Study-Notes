#### 概述：

Arthas 的功能点非常的多（详见下方大图），这里就不一一的讲解了，可以参考使用文档 ，具体详细资料可参见官方文档：

+ Arthas官方文档：https://arthas.aliyun.com/doc/en/dashboard.html

- Jetbrains插件：[https](https://plugins.jetbrains.com/plugin/13581-arthas-idea) : [//plugins.jetbrains.com/plugin/13581-arthas-idea](https://plugins.jetbrains.com/plugin/13581-arthas-idea)
- 插件文档：[https](https://www.yuque.com/docs/share/fa77c7b4-c016-4de6-9fa3-58ef25a97948?#) : [//www.yuque.com/docs/share/fa77c7b4-c016-4de6-9fa3-58ef25a97948?#](https://www.yuque.com/docs/share/fa77c7b4-c016-4de6-9fa3-58ef25a97948?#)
- 插件Github：[https](https://github.com/WangJi92/arthas-idea-plugin) : [//github.com/WangJi92/arthas-idea-plugin](https://github.com/WangJi92/arthas-idea-plugin)

![](D:\Si_tech\学习资料\学习笔记\arthas.png)

#### 安装

由于我是用的系统为Windows，因此这里只阐述Windows系统下如何安装Arthas。

+ ##### Arthas下载

  最新版本，点击下载： [![https://img.shields.io/maven-central/v/com.taobao.arthas/arthas-packaging.svg?style=flat-square](https://img.shields.io/maven-central/v/com.taobao.arthas/arthas-packaging.svg?style=flat-square)](https://arthas.aliyun.com/download/latest_version)

  最新版本文档，单击下载：[![https://img.shields.io/maven-central/v/com.taobao.arthas/arthas-packaging.svg?style=flat-square](https://img.shields.io/maven-central/v/com.taobao.arthas/arthas-packaging.svg?style=flat-square)](https://arthas.aliyun.com/download/doc/latest_version)

下载完成后，将下载的压缩包（.Zip）解压，会看到如下的文件：

![](D:\Si_tech\学习资料\学习笔记\arthas2.png)

+ ##### 运行应用程序

  ###### 1、第一种方式，使用下载包中自带的demo；

  ```java
  Java -jar arthas-demo.jar
  ```

  运行后的结果如下：

  <img src="D:\Si_tech\学习资料\学习笔记\arthas3.jpg" style="zoom:50%;" />

  **NOTE：运行后不要关闭该窗口**

  ###### 2、第二种方式：在IDEA中创建一个应用程序，运行代码，具体实验代码如下:

  ```java
  import java.util.ArrayList;
  import java.util.List;
  import java.util.Random;
  import java.util.concurrent.TimeUnit;
  
  /**
   * @ClassName MathGame
   * @Description TODO
   * @Author hufw
   * @Date 2020/9/10 15:28
   */
  public class MathGame {
      private static Random random = new Random();
  
      public int illegalArgumentCount = 0;
  
      public static void main(String[] args) throws InterruptedException {
          MathGame game = new MathGame();
          while (true) {
              game.run();
              TimeUnit.SECONDS.sleep(1);
          }
      }
  
      public void run() throws InterruptedException {
          try {
              int number = random.nextInt()/10000;
              List<Integer> primeFactors = primeFactors(number);
              print(number, primeFactors);
  
          } catch (Exception e) {
              System.out.println(String.format("illegalArgumentCount:%3d, ", illegalArgumentCount) + e.getMessage());
          }
      }
  
      public static void print(int number, List<Integer> primeFactors) {
          StringBuffer sb = new StringBuffer(number + "=");
          for (int factor : primeFactors) {
              sb.append(factor).append('*');
          }
          if (sb.charAt(sb.length() - 1) == '*') {
              sb.deleteCharAt(sb.length() - 1);
          }
          System.out.println(sb);
      }
  
      public List<Integer> primeFactors(int number) {
          if (number < 2) {
              illegalArgumentCount++;
              throw new IllegalArgumentException("number is: " + number + ", need >= 2");
          }
  
          List<Integer> result = new ArrayList<Integer>();
          int i = 2;
          while (i <= number) {
              if (number % i == 0) {
                  result.add(i);
                  number = number / i;
                  i = 2;
              } else {
                  i++;
              }
          }
  
          return result;
      }
  }
  ```

+ ##### 运行Arthas

  在解压完成的文件夹中，按住Shift键，点击鼠标右键，选择在此处打开PowerShell窗口，输入一下命令：

  ```java
  java -jar arthas-boor.jar
  ```

  运行后会出现以下界面;

  <img src="D:\Si_tech\学习资料\学习笔记\arthas4.jpg" style="zoom:50%;" />

输入对应应用程序前的数字，如现在运行的程序为arthas-demo.jar，输入其前面的数字1即可。

**Note:**

1、当出现终端连续打印的情况时，输入q退出；

2、当想退出Arthas时：

+ quit/exit，只是退出当前的连接，ttach到目标进程上的arthas还会继续运行，端口会保持开放，下次连接时可以直接连接上；
+ stop，完全退出arthas。

#### 功能介绍

+ ##### 查看线程信息、堆栈和系统信息

  ```
  dashboard
  ```

  <img src="D:\Si_tech\学习资料\学习笔记\arthas5.png" style="zoom:50%;" />

  ###### 对应列说明

  + ID：JVM线程ID，请。请注意，此ID与jstack中的nativeID不同
  + NAME：线程名称
  + GROUP：线程组名
  + 优先级：线程优先级，取值范围为1〜10。数字越大，优先级越高。
  + STATE：线程状态
  + CPU％：线程的CPU使用率，每100毫秒采样一次
  + TIME：`minute:second`格式的总运行时间
  + INTERRUPTED：线程中断状态
  + DAEMON：守护程序线程与否

+ ##### 查看具体方法的返回值、入参和异常等

  具体语法为：watch 类名 方法名 表达式；

  ```java
  表达式核心变量列表：
  loader      本次调用类所在的 ClassLoader
  clazz       本次调用类的 Class 引用
  method      本次调用方法反射引用
  target      本次调用类的实例
  params      本次调用参数列表，这是一个数组，如果方法是无参方法则为空数组
  returnObj   本次调用返回的对象。当且仅当 isReturn==true 成立时候有效，表明方法调用是以正常返回的方			   式结束。如果当前方法无返回值 void，则值为 null
  throwExp    本次调用抛出的异常。当且仅当 isThrow==true 成立时有效，表明方法调用是以抛出异常的方式		      结束。
  isBefore    辅助判断标记，当前的通知节点有可能是在方法一开始就通知，此时 isBefore==true 成立，同时 		    isThrow==false 和 isReturn==false，因为在方法刚开始时，还无法确定方法调用将会如何结             束。
  isThrow     辅助判断标记，当前的方法调用以抛异常的形式结束。
  isReturn    辅助判断标记，当前的方法调用以正常返回的形式结束。
  ```

  ```java
  watch demo.MathGame primeFactors returnObj
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas6.png)

+ ##### 查看方法调用路径、耗时

  具体语法为: trace 类名 方法名

  ```
  trace demo.MathGame primeFactors
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas7.png)

+ ##### 反编译代码

  具体语法为：jad 类名

  ```java
  jad demo.MathGame
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas8.png)

+ ##### 查看线程状态，CPU占比等

具体语法为： thread 1 | grep 'main(' 与thread 1作用相同，会打印线程ID 1的栈，通常是main函数的线程；

​						thread -n 1 会同时打印线程CPU占比、线程信息等。

![](D:\Si_tech\学习资料\学习笔记\arthas9.png)

+ ##### 查找JVM中已经加载的类

  具体语法为：sc -d 具体方法

  ​				       sc 具体方法名--->直接返回方法名

  ```java
  sc -d demo.MathGame
  sc demo.MathGame
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas10.jpg)

+ ##### 查看具体方法的调用堆栈

  具体语法为：stack 类名 方法名

  ```java
  stack demo.MathGame primeFactors
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas11.jpg)

+ ##### 类、方法执行监控

  | 参数名称            | 参数说明                                |
  | ------------------- | --------------------------------------- |
  | *class-pattern*     | 类名表达式匹配                          |
  | *method-pattern*    | 方法名表达式匹配                        |
  | *condition-express* | 条件表达式                              |
  | [E]                 | 开启正则表达式匹配，默认为通配符匹配    |
  | `[c:]`              | 统计周期，默认值为120秒                 |
  | [b]                 | 在**方法调用之前**计算condition-express |

  常用语法为：monitor -c 统计周期（s） 类名 方法名

  ```
  monitor -c 5 demo.MathGame primeFactors
  ```

  | 监控项    | 说明                       |
  | --------- | -------------------------- |
  | timestamp | 时间戳                     |
  | class     | Java类                     |
  | method    | 方法（构造方法、普通方法） |
  | total     | 调用次数                   |
  | success   | 成功次数                   |
  | fail      | 失败次数                   |
  | rt        | 平均RT                     |
  | fail-rate | 失败率                     |

  ![](D:\Si_tech\学习资料\学习笔记\arthas12.jpg)

+ ##### 查看类加载器

  了解当前系统中有多少类加载器，以及每个加载器加载的类数量，帮助您判断是否有类加载器泄露。

  具体语法为：classloader

  ```java
  classloader
  ```

  ![](D:\Si_tech\学习资料\学习笔记\arthas13.jpg)

+ #####  Web Console

  具体语法为：java -jar arthas-boot.jar --target-ip 目标系统IP

  在局域网内，其它机器可访问 http://192.168.168.67:8563
  默认情况下，arthas只listen 127.0.0.1，所以如果想从远程连接，则可以使用 --target-ip参数指定listen的IP，更多参考-h的帮助说明。 注意会有安全风险，考虑tunnel server的方案。

+ ##### 生成火焰图（仅支持Linux/Mac）

  什么是火焰图：参见[火焰图官方主页](http://www.brendangregg.com/flamegraphs.html)

  ```
  y 轴表示调用栈，每一层都是一个函数。调用栈越深，火焰就越高，顶部就是正在执行的函数，下方都是它的父函数。
  
  x 轴表示抽样数，如果一个函数在 x 轴占据的宽度越宽，就表示它被抽到的次数多，即执行的时间长。注意，x 轴不代表时间，而是所有的调用栈合并后，按字母顺序排列的。
  
  **火焰图就是看顶层的哪个函数占据的宽度最大。只要有"平顶"（plateaus），就表示该函数可能存在性能问题。**
  
  颜色没有特殊含义，因为火焰图表示的是 CPU 的繁忙程度，所以一般选择暖色调。
  ```

  基本语法为：profiler  要执行的操作

  | 参数名称    | 参数说明                                                     |
  | ----------- | ------------------------------------------------------------ |
  | *action*    | 要执行的操作                                                 |
  | *actionArg* | 属性名模式                                                   |
  | [i:]        | 采样间隔（单位：ns）（默认值：10'000'000，即10 ms）          |
  | [f:]        | 将输出转储到指定路径                                         |
  | [d:]        | 运行评测指定秒                                               |
  | [e:]        | 要跟踪哪个事件（cpu, alloc, lock, cache-misses等），默认是cpu |

  操作流程：

  1、启动profiler

  默认情况下，生成的是cpu的火焰图，即event为`cpu`。可以用`--event`参数来指定。

  ```java
  profiler start
  ```

  2、获取已采集的sample的数量

  ```java
  profiler getSamples
  ```

  3、查看profiler状态

  可以查看当前profiler在采样哪种`event`和采样时间

  ```java
  profiler status
  ```

  4、停止profiler

  + 生成svg格式结果

    ```java
    profiler stop
    ```

    默认情况下，生成的结果保存到应用的`工作目录`下的`arthas-output`目录。可以通过 `--file`参数来指定输出结果路径。

    ```java
    profiler stop --file /tmp/output.svg
    ```

  + 生成html格式结果

    ```java
    profiler stop --format html
    ```

    默认情况下，结果文件是`svg`格式，如果想生成`html`格式，可以用`--format`参数指定；或者在`--file`参数里用文件名指名格式。比如`--file /tmp/result.html`

    ```java
    profiler stop --file /tmp/result.html
    ```

5、在浏览器查看结果

默认情况下，arthas使用3658端口，则可以打开： http://localhost:3658/arthas-output/ 查看到`arthas-output`目录下面的profiler结果：

查看profiler支持的events：

```java
profiler list
```

![](D:\Si_tech\学习资料\学习笔记\arthas14.png)

结果如下:

![](D:\Si_tech\学习资料\学习笔记\arthas15.png)

后续持续添加其它功能。。。