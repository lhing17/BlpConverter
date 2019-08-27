## [魔兽图标一键制作工具](https://github.com/lhing17/BlpConverter)



可以根据提供图片生成war3需要使用的blp图片，并自动按commandbutton和commandbuttondisabled目录放置，可以直接导入到地图中。


### 运行环境
项目使用Java开发，需要安装JRE1.8+版本。

### 项目构建
1. 项目使用maven进行构建，首先安装maven，并配置环境变量。
2. 在项目路径下执行以下命令，即可生成可执行的jar包
```
mvn clean package
```

### 使用方法
- 设置输入目录和输出目录
- 在输入目录下创建active和passive文件夹
- 将主动技能图片放置在active目录下，被动技能图片放置在passive目录下
- 开始一键生成


### 自定义滤镜
自定义滤镜指只有边框的png图片，除边框外其他部分没有像素，参考red_border.png