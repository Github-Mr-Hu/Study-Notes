# Windows10安装docker教程

1、Windows家庭版安装Hyper-V

```
pushd "%~dp0"

dir /b %SystemRoot%\servicing\Packages\*Hyper-V*.mum >hyper-v.txt

for /f %%i in ('findstr /i . hyper-v.txt 2^>nul') do dism /online /norestart /add-package:"%SystemRoot%\servicing\Packages\%%i"

del hyper-v.txt

Dism /online /enable-feature /featurename:Microsoft-Hyper-V-All /LimitAccess /ALL
```

使用记事本，保存为Hyper-V.cmd，保存完成后，找到对应文件，在右键菜单中点击：**以管理员身份运行（A）**；在运行结束后输入：Y。







