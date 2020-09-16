Master/Slave相当于Server（服务）和agent（代理）的概念。Master提供web接口让用户来管理job和slave，job可以运行在master本机或者被分配到slave上运行。一个master可以关联多个slave用来为不同的job或相同的job的不同配置来服务。

![](D:\Si_tech\学习资料\Study-Notes\fortress\Master-Slave架构.png)