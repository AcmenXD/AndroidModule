#AndroidProject
基于 MVP 以及一些 主流技术,整理的一个Android框架.

### 功能
---
- 可视化打包工具配置渠道
- debug|release版本控制debug开关
- 签名信息在local.properties配置
- 默认debug&release都开启 -> 混淆|zipalign优化|移除无用的resource文件
- 默认生成包名为:项目名_v版本_当前时间_debug|release.apk
- 框架已对com.github.AcmenXD:Toaster | SpTool | Retrofit | Marketer | FrescoView | Logger | RecyclerView做好配置支持
- 框架已集成并添加rxandroid | greendao | eventbus支持
- 特别说明 -> 框架支持库请移步对应的github查看使用方法及源码

### 结构说明
---
**configs**
```java
-> AppConfig  : App相关配置,程序启动时装载 版本号/包名/渠道/IMEI等信息
-> BaseConfig : 基础配置,debug开关/默认渠道号/数据库名称/Log开关/Net等诸多初始化配置,都在此类中设置
```
---
**base**
```java
-> BaseApplication      : 继承Application, 做初始化等配置
-> BaseActivity         : 顶级Activity,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> BaseFragment         : 顶级Fragment,实现Subscription | Presenter支持,内容 | 加载 | 错误视图,网络状态监控,Net支持,以及销毁等
-> BasePresenter        : 顶级Presenter,实现Subscription支持,网络状态监控,Net支持,以及销毁等
-> ActivityStackManager : Activity堆栈管理器,提供exit | restartApp支持
-> IActivityFragment    : BaseActivity & BaseFragment 提供的公共函数接口
-> IBView               : MVP V层的实现接口类
-> IBPresenter          : MVP P层的实现接口类
```
---
**utils**
```java
-> code     : Base64编码配置
-> net      : 网络状态监控实现,以及网络状态工具类等
-> proguard : 混淆基类,各个混淆配置可直接继承对应接口,实现混淆配置
-> ...      : 各种工具类,具体功能请查看源码
```
---
**widget**
```java
-> frame          : 框架所需要的一些布局文件
-> CircleProgress : 圆形进度条
-> ...            : 各种自定义组件 & 自定义View 等
```
---
**db**
```java
-> dao      : GreenDao生成一些文件,数据库配置及表管理等
-> helper   : 数据库操作帮助类,数据库表升级基类
-> migrator : 数据库表升级支持
-> ...      : 数据库表操作相关类
```
---
**net**
```java
-> IRequestDoc      : Retrofit注解的使用文档
-> IAllRequest      : 默认所有服务器接口的定义
-> IUploadRequest   : 默认上传接口的定义
-> IDownloadRequest : 默认下载接口的定义
-> NetCode          : 服务器状态码统一处理类
```
---
**model**
```java
-> request  : 服务器请求实体类定义
-> response : 服务器响应实体类定义
-> db       : 数据库实体类定义
```
---
**view**
```java
-> SplashActivity : 启动页
-> MainActivity   : 主页
-> test           : 测试的Activity
```
---
**presenter**
```java
-> ILogin         : 模拟登录模块mvp接口定义
-> LoginPresenter : 模拟登录模块p层实现

```
---
有问题请于作者联系AcmenXD@163.com ^_^!
---
### 打个小广告^_^
**gitHub** : https://github.com/AcmenXD   如对您有帮助,欢迎点Star支持,谢谢~

**技术博客** : http://blog.csdn.net/wxd_beijing
# END