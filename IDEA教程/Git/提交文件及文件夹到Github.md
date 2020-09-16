在上传文件或文件夹至GitHub时，你可能面临这样的问题，在GitHub上创建新仓库时，未选择如下图所示的选项：

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github1.png)

而你又不想使用git命令提交时，可能会面临无法直接在GitHub页面上直接上传文件，当然你可以删除该仓库，重新新建仓库并选择ADD.gitgnore或Add a README file，但是当你是第一次删除仓库时，你又有可能面临一个新的问题，在哪儿才能够删除仓库，首先你需要进入你想删除的仓库，点击页面菜单栏中的Sittings选项，在打开的页面滑至最下面，会出现如下的选项：

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github2.png)

选择Delete this repository按钮，在输入框中输入你的工程名称，如下:

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github3.png)

然后输入你的账号密码就可以删除成功了。当你选择重新添加仓库，并选择相关配置文件后，页面会又如下变化：

![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github4.png)

此时你就可以添加你想添加的文件了，但是这样只能上传文件，而不能添加诸如你本地分类的文件夹。此时我们就可以利用IDEA来帮我们管理和上传文件啦，具体操作步骤如下：

+ 在IDEA中导入你想上传至GitHub的文件夹，如下图所示：

  ![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github5.jpg)

+ 上传至GitHub，步骤如下：

  ![](D:\Si_tech\学习资料\Study-Notes\IDEA教程\Git\github6.png)

输入你的GitHub账户和密码就可以了，其它操作与上传代码一样，Push成功后，就可以在网页登录GitHub或手机端打开GitHub app查看到与你本地文件夹名称相同的仓库啦，后面你在该文件夹中新增的文件或子文件夹，都可以通过IDEA上传至GitHub。