# AppFrame

基于目前一些主流技术,整理的模块化开放框架

如要了解功能实现,请运行app程序查看控制台日志和源代码!
* 源代码 : <a href="https://github.com/AcmenXD/AppFrame">AcmenXD/AppFrame</a>
* apk下载路径 : <a href="https://github.com/AcmenXD/Resource/blob/master/apks/AppFrame.apk">AppFrame.apk</a>

### 功能
---
- 多项目共用一份frame框架
- 多业务可分多Module开发,便于协作
- 默认debug&release都开启 -> <a href="http://blog.csdn.net/wxd_beijing/article/details/70140536">混淆</a> | zipalign优化 | 移除无用的resource文件
- 框架已对<a href="https://github.com/AcmenXD">com.github.AcmenXD</a>:<a href="https://github.com/AcmenXD/Toaster">Toaster</a> | <a href="https://github.com/AcmenXD/SpTool">SpTool</a> | <a href="https://github.com/AcmenXD/Retrofit">Retrofit</a> | <a href="https://github.com/AcmenXD/Marketer">Marketer</a> | <a href="https://github.com/AcmenXD/Glide">Glide</a> | <a href="https://github.com/AcmenXD/FrescoView">FrescoView</a> | <a href="https://github.com/AcmenXD/Logger">Logger</a> | <a href="https://github.com/AcmenXD/RecyclerView">RecyclerView</a>做好配置支持
- 框架已集成并添加<a href="https://github.com/ReactiveX/RxJava">RxJava</a> | <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a> | <a href="https://github.com/greenrobot/greenDAO">greenDAO</a> | <a href="https://github.com/greenrobot/EventBus">EventBus</a>支持
- 特别说明 -> 框架支持库请移步对应的github查看使用方法及源码

### frame 框架Module
---
**configs**
```java
-> FrameConfig    : 基础配置信息,项目配置必须继承此类,从而拓展其他配置!  例如:AppFrameConfig/OtherConfit(每个项目都有一份单独的配置清单)
-> ConfigBuilder : 框架进行初始化操作类,包括初始化第三方组件/类库
```
---
**basis**
```java
-> impl包               : 框架层所需的接口基类
-> mvp包                : 实现MVP的接口基类
-> FrameApplication     : 框架层Application, 项目需继承此类做相关拓展!
-> FrameActivity        : 框架层Activity,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> FrameFragment        : 框架层Fragment,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> FrameService         : 框架层Service,实现Subscription | Presenter支持,网络状态监控,Net支持,以及销毁等
-> FramePresenter       : 框架层Presenter,实现Subscription支持,网络状态监控,Net支持,以及销毁等
-> FrameModel           : 框架层Model,实现Subscription支持,网络状态监控,Net支持,以及销毁等
-> ActivityStackManager : Activity堆栈管理器,提供exit | restartApp支持
```
---
**utils**
```java
-> code     : 编码加解密相关类
-> net      : 网络状态监控实现,以及网络状态工具类等
-> proguard : 混淆基类,各个混淆配置可直接继承对应接口,实现混淆配置
-> ...      : 各种工具类,具体功能请查看源码
```
---
**logger** / **marketer** / **recyclerview** / **retrofit** / **sptool** / **toaster**
```java
-> 功能组件,详情请移步对应的github查看使用方法及源码
```
---
### 结构说明 -> testbase 模拟项目的基准库
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
-> EventBusHelper  : EventBus帮助类
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
### 结构说明 -> test 模拟项目的主Module
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
### 结构说明 -> test_demo 模拟项目的业务Module
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