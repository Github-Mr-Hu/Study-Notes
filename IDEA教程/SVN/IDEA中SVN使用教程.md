1、安装SVN，从https://tortoisesvn.net/下载，在本地电脑上安装，安装成功后，在桌面点击右键会出现SVN菜单；

NOTE：在安装过程中默认下选项会有一个默认不安装，请手动选择安装，否则不会生成svn.exe；

2、IDEA中安装SVN，File--->Settings--->Subversion，其中路径选择至svn.exe，其它项默认选择。

![SVN安装教程1](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程1.png)

3、从SVN下载代码，在IDEA导航菜单栏中选择VCS--->Get from Version Control...--->在弹出的窗口中选择Version Control为Subversion--->在窗口中点击“+”号--->在URL中输入SVN地址，点击OK--->点击Check Out。

NOTE：在从SVN导出代码时，可能会遇到不成功的情况，重复Check Out几次后便会成功。

![SVN安装教程2](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程2.png)![SVN安装教程3](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程3.png)![SVN安装教程4](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程4.png)

4、上传代码至SVN，选择新建代码或修改代码，点击鼠标右键--->在弹出菜单选择Subversion--->选择Add to VCS（这一步主要是完成将代码添加至要上传的目录中，待上传的代码为橙色时选择Add to VCS 会变成绿色，表明已加入上传目录中。NOTE：不添加将导致在上传时找不到文件）；

![SVN安装教程5](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程5.png)

--->选择导航栏中的Update priject按钮，从SVN更新代码（必须操作步骤，保证上传前的代码是最新）--->点击Commit按钮提交代码；

![image-20200819205041081](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程6.png)

--->在弹出的窗口左上角选择要上传的文件（默认选择改动过和新建文件）-->下半部分分别展示本地代码和SVN中的代码，其中左边为SVN代码，右边为本地代码  --->对比完成后，再中间部分填入本次提交内容（格式：系统：对应开发接口）--->点击右下方Commit，提交代码，没有报错就完成了代码的提交，有错重复前面的步骤。

![image-20200819205902167](D:\Si_tech\学习资料\Study-Notes\IDEA教程\SVN\SVN安装教程7.png)

NOTE：

本地工程上传至SVN没有SVN选项的解决办法？

再IDEA中打开本地工程--->在打开的工程中根据上述的从SVN下载代码步骤，从SVN下载对应工程的代码，将现有工程覆盖（改动代码不会被覆盖）--->修改完成后，提交代码。





SVN查看提交记录：

右键工程--->show history

