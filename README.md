# AndroidModule

基于<a href="https://github.com/AcmenXD/AndroidFrame">AcmenXD/AndroidFrame</a>,整理的组件化框架

如要了解功能实现,请运行app程序查看控制台日志和源代码!
* 源代码 : <a href="https://github.com/AcmenXD/AndroidModule">AcmenXD/AndroidModule</a>
* apk下载路径 : <a href="https://github.com/AcmenXD/Resource/blob/master/apks/AndroidModule.apk">AndroidModule.apk</a>
* 参考 : <a href="https://blog.csdn.net/guiying712/article/details/55213884#t4">Android组件化方案</a>
* 路由 : <a href="https://github.com/alibaba/ARouter">ARouter</a>

### 功能
---
- 多业务可分多Module开发,便于协作
- 默认debug&release都开启 -> <a href="http://blog.csdn.net/wxd_beijing/article/details/70140536">混淆</a> | zipalign优化 | 移除无用的resource文件
- 特别说明 -> 框架支持库请移步对应的github查看使用方法及源码

### 结构说明 -> core 核心库
---
**base**
```java
-> impl包          : base层所需的接口基类
-> AppConfig       : debug开关,config配置及项目用到的其他参数等
-> AppFrameConfig  : 继承自FrameConfig,修改一些基础配置
-> BaseApplication : 继承自FrameApplication,拓展项目配置初始化等
-> BaseActivity    : 继承自FrameActivity,拓展项目功能
-> BaseFragment    : 继承自FrameFragment,拓展项目功能
-> BaseService     : 继承自FrameService,拓展项目功能
-> BasePresenter   : 继承自FramePresenter,拓展项目功能
-> BaseModel       : 继承自FrameModel,拓展项目功能
```
---
**db**
```java
-> core     : 数据库操作帮助类,数据库表升级基类
-> dao      : GreenDao生成一些文件,数据库配置及表管理等
-> migrator : 数据库表升级支持
-> ...      : 数据库表操作相关类
```
---
**http**
```java
-> IRequestDoc      : Retrofit注解的使用文档
-> IRequest         : 默认所有服务器接口的定义
-> RequestCallback  : 服务器状态码统一处理类
```
---
**widget**
```java
-> CircleProgress : 圆形进度条
-> ...            : 各种自定义组件 & 自定义View 等
```
---
**model**
```java
-> db       : 数据库实体类定义
-> request  : 服务器请求实体类定义
-> response : 服务器响应实体类定义
```
---
### 结构说明 -> app 壳Module
### 结构说明 -> main 业务主Module
**view**
```java
-> SplashActivity : 启动页
-> MainActivity   : 主页
```
---
**mvp**
```java
-> ITest         : 模拟登录模块mvp接口定义
-> TestModel     : 模拟登录模块m层实现
-> TestPresenter : 模拟登录模块p层实现
```
---
### 结构说明 -> demo 业务Module
**view**
```java
-> 各功能模块的测试Activity
```
---
有问题请与作者联系AcmenXD@163.com ^_^!
---
**gitHub** : https://github.com/AcmenXD   如对您有帮助,欢迎点Star支持,谢谢~

**技术博客** : http://blog.csdn.net/wxd_beijing
# END