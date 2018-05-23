# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# * 混淆日志生成目录:app.build.outputs.mapping下,打包时请拷贝此日志,用于错误日志还原分析.
#--------------------------------------------- 公共配置 ----------------------------------------#
-optimizationpasses 5 # 代码混淆压缩比
-dontusemixedcaseclassnames   # 混合时不使用大小写混合，混合后的类名为小写
-dontskipnonpubliclibraryclasses #  指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclassmembers # 指定不去忽略非公共库的类成员
-dontpreverify  # 混淆时不做预校验
-verbose # 使我们的项目混淆后产生映射文件 : 包含有类名->混淆后类名的映射关系
-ignorewarnings # 忽略所有警告
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*# 指定混淆是采用的算法，后面的参数是一个过滤器(谷歌推荐的算法，一般不做更改)

-keepattributes *Annotation*,InnerClasses #保留注解不混淆
-keepattributes Signature # 避免混淆泛型
-keepattributes SourceFile,LineNumberTable# 抛出异常时保留代码行号
#--------------------------------------------- 公共配置 End ----------------------------------------#

#--------------------------------------------- Java ----------------------------------------#
-keepclasseswithmembernames class * { # 保留本地native方法不被混淆
    native <methods>;
}
-keepclassmembers enum * {# 保留枚举类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {# 保留JsonObject不被混淆
   public <init> (org.json.JSONObject);
}
-keep class * implements android.os.Parcelable {# 保留Parcelable序列化类不被混淆
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {# 保留Serializable序列化的类不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#--------------------------------------------- Java End ----------------------------------------#

#--------------------------------------------- Android 系统 ----------------------------------------#
-keep public class * extends android.view.View
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class **.R$* { *; }# 保留R下面的资源
-keep class android.support.** { *; }# 保留support下的所有类及其内部类
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
-keepclassmembers class * extends android.app.Activity{#保留在Activity中的方法参数是view的方法(这样以来我们在layout中写的onClick就不会被影响)
    public void *(android.view.View);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {# 对于带有回调函数的**on*Event、**On*Listener 不被混淆
    void *(**On*Event);
    void *(**On*Listener);
}
#--------------------------------------------- Android 系统 End ----------------------------------------#

#--------------------------------------------- 继承混淆类配置 ----------------------------------------#
-keep interface com.acmenxd.frame.utils.proguard.** { *; }
-keep class * implements com.acmenxd.frame.utils.proguard.IKeepClass
-keepnames class * implements com.acmenxd.frame.utils.proguard.IKeepClassName
-keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepFieldName {
    <fields>;
}
-keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepPublicFieldName {
    public <fields>;
}
-keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepMethodName {
    <methods>;
}
-keepclassmembers class * implements com.acmenxd.frame.utils.proguard.IKeepPublicMethodName {
    public <methods>;
}
#--------------------------------------------- 继承类配置 End ----------------------------------------#

#--------------------------------------------- 第三方依赖库 处理 ----------------------------------------#
#libs

#rxjava & rxandroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#okhttp3
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#FrescoView
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-keepclassmembers class * {
    native <methods>;
}
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** {
    *;
}
#greenDAO 3.2.0
#-keep class * extends org.greenrobot.greendao.AbstractDao{ *; }
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
#eventbus 3.0.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#--------------------------------------------- 项目混淆 处理 ----------------------------------------#
-keep class com.acmenxd.mvpbase.model.**{ *; } # 实体类
-keep class com.acmenxd.mvp.model.**{ *; } # 实体类

#--------------------------------------------- 反射类 处理 ----------------------------------------#


#--------------------------------------------- WebView 处理 ----------------------------------------#


#--------------------------------------------- js交互 处理 ----------------------------------------#

