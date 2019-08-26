## 魔兽图标一键制作工具

可以根据提供图片生成war3需要使用的blp图片，并自动按commandbutton和commandbuttondisabled目录放置，可以直接导入到地图中。

使用方法：
- 设置输入目录和输出目录
- 在输入目录下创建active和passive文件夹
- 将主动技能图片放置在active目录下，被动技能图片放置在passive目录下
- 开始一键生成

注意开发环境需要安装blpIIO.jar，如：
```
mvn install:install-file -Dfile="G:\1\blpIIO.jar" -DgroupId=com.hiveworkshop -DartifactId=blpIIO -Dversion=1.0 -Dpackaging=jar
```
上面的文件路径修改为自己的路径