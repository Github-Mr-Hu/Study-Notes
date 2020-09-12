## 前言

### 1、了解Git 与 SVN 区别

Git 不仅仅是个版本控制系统，它也是个内容管理系统(CMS)，工作管理系统等。

如果你是一个具有使用 SVN 背景的人，你需要做一定的思想转换，来适应 Git 提供的一些概念和特征。

Git 与 SVN 区别点：

- **1、Git 是分布式的，SVN 不是**：这是 Git 和其它非分布式的版本控制系统，例如 SVN，CVS 等，最核心的区别。
- **2、Git 把内容按元数据方式存储，而 SVN 是按文件：**所有的资源控制系统都是把文件的元信息隐藏在一个类似 .svn、.cvs 等的文件夹里。
- **3、Git 分支和 SVN 的分支不同：**分支在 SVN 中一点都不特别，其实它就是版本库中的另外一个目录。
- **4、Git 没有一个全局的版本号，而 SVN 有：**目前为止这是跟 SVN 相比 Git 缺少的最大的一个特征。
- **5、Git 的内容完整性要优于 SVN：**Git 的内容存储使用的是 SHA-1 哈希算法。这能确保代码内容的完整性，确保在遇到磁盘故障和网络问题时降低对版本库的破坏。

## 2、安装GIT

从https://git-scm.com/downloads下载GIT，安装全部默认选项，点击下一步即可。安装完成后，再cmd输入git，出现下面内容，表明git安装成功。

![image-20200826184454601](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\git1.png)

## 3、初始化Git仓储/（仓库）

- 这个仓库会存放，git对我们项目代码进行备份的文件
- 在项目目录右键打开 git bash 
- 命令： 'git init'

## 4、配置个人信息

- 就是在GIT中设置当前使用用户是谁

- 每一次备份都会把当前备份者 的信息储存起来

- 命令 

  + 配置用户名 ：git config --global user.name "xiaoming"

  + 配置邮箱： git config --global user.email "xiaoming@sina.com"

## 5、把代码存储到仓库中

- 两步把代码存储到.git仓储中   

  + 1、把代码放到仓储的门口
    +  git add ./文件名 提交指定文件
    + git add ./    提交当前目录中的所有文件

  + 2、把仓储中的代码放到里面的房间中
    + git commit -m "这是对存储东西的一些说明"

- 一次性把代码存储到.git仓储中（版本库）
  + git commit --all -m "这是对存储东西的一些说明"（一步完成存储）
  + --all 表示把所有修改的文件提交到版本库

## 6、查看当前状态

- 可以用俩查看当前代码有没有被放到仓储中去

+ 命令：git status
  + 绿色---已放到门口（暂存区）
  + 红色---文件修改还未放到暂存区
  + 白色---已放到版本库

## 7、查询提交记录

- 命令：git log  或 git log --oneline
  + 包含提交内容、提交人、时间和说明

## 8、提交代码到GitHub

- git push [地址] master(分支名)

  + 示例：git push https://github.com/huoqishi/test112.git master

  + 会把当前分支的内容上传到远程的master分支上

## 9、从GitHub获取代码

- git pull [地址] master

  + 示例：git pull https://github.com/huoqishi/test112.git master

  + 会把远程分支的数据得到：*注意本机要初始化一个仓储*
  + 多次执行不会覆盖，会进行文件合并处理

- git clone [地址]
  + 会得到远程仓储相同的数据，如果多次执行会覆盖本地内容。

## 10、SSH上传代码（不用输入用户名和密码）

- 公钥 私钥，两者是有关联的
- 生成公钥和私钥
	+ ssh-keygen -t rsa -C "xiaoming@sina.com"
	+ 默认生成的文件在C盘用户目录中
		+ .pub 公钥
		+ .rsa 私钥  复制里面的内容添加到GitHub的 Setting->SSH and GPG keys的key中
	+ 创建新的仓库，复制SSH路径
	+ git push [SSH路径] master

## 11、IDEA中GIT使用教程

+ IDEA版本：2020.2

+ 在IDEA中指定GIT的路径，File--->Setting--->Version Control--->Git--->Path to Git executable；

  **NOTE：**2020.2版本IDEA，本地成功安装Git后，IDEA会自动选择本地的git.exe。

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\git2.png)

+ 设置GitHub账号，File--->Setting--->Version Control--->GitHub--->Add Account--->Log in with Token；

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\git3.png)**NOTE:**点击Generate后，会跳转到GitHub网站，如下图所示：

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\git4.png)

默认选择，拉到最下面，点击Generate token，即可生成IDEA对应的令牌，复制到IDEA中的Token中，就完成了GitHub账户的设置。

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\git5.png)

+ IDEA 创建的本地项目push到GitHub上
  +  在GitHub上新建一个仓库；
  + 仓库创建成功后，拷贝仓库地址；
  + 在本地创建一个项目；
  + 在本地创建一个Git仓库，Flie-->Setting--->VCS--->import into Version Control--->Create Git Repository--->选择本地仓库位置；
  + 把项目加入到本地仓库的stage区暂存，右键项目--->Git--->Add
  + 将暂存的项目提交到本地仓库然后提交到远程仓库(IDEA里将这两步骤简化为一步 即Commit and Push)；操作与SVN类似。

+ 用IDEA从github上pull一个现成的项目到本地
  
  + 与SVN操作类似。
  
  
