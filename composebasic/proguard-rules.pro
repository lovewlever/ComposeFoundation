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

#---------------------------------基本指令----------------------------------
 # 设置混淆的压缩比率 0 ~ 7
 -optimizationpasses 5
 # 混淆时不使用大小写混合，混淆后的类名为小写
 -dontusemixedcaseclassnames
 # 指定不去忽略非公共库的类
 -dontskipnonpubliclibraryclasses
 # 指定不去忽略非公共库的成员
 -dontskipnonpubliclibraryclassmembers
 # 混淆时不做预校验
 -dontpreverify
 # 混淆时不记录日志
 -verbose
 # 代码优化
 -dontshrink
 # 不优化输入的类文件
 -dontoptimize
 # 保留注解不混淆
 -keepattributes *Annotation*,InnerClasses
 # 避免混淆泛型
 -keepattributes Signature
 # 保留代码行号，方便异常信息的追踪
 -keepattributes SourceFile,LineNumberTable
 # 混淆采用的算法
 -optimizations !code/simplification/cast,!field/*,!class/merging/*

 # dump.txt文件列出apk包内所有class的内部结构
 -dump class_files.txt
 # seeds.txt文件列出未混淆的类和成员
 -printseeds seeds.txt
 # usage.txt文件列出从apk中删除的代码
 -printusage unused.txt
 # mapping.txt文件列出混淆前后的映射
 -printmapping mapping.txt
#----------------------------------------------------------------------------


#---------------------------------默认保留区，避免混淆Android基本组件---------------------------------
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
#Support
-keep class android.support.** {*;}
-dontwarn android.support.**
-keep interface android.support.** { *; }
#Androidx
-keep class androidx.** {*;}
-keep interface androidx.** {*;}
-keep class * extends androidx.**  { *; }
-dontwarn androidx.**
-keep class com.google.android.material.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

#避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#避免混淆自定义控件类的get/set方法和构造函数
#混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#避免混淆序列化类
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#不混淆Serializable和它的实现子类、其成员变量
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#----------------------------------------------------------------------------

#使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
-keepclassmembers class * {
        public <init>(org.json.JSONObject);
}
#不混淆资源类
-keep class **.R$* {
 *;
}
#避免回调函数 onXXEvent 混淆
-keepclassmembers class * {
    void *(**On*Event);
}

#移除log
-assumenosideeffects class android.util.Log{
    public static int v(...);
    public static int i(...);
    public static int d(...);
    public static int w(...);
    public static int e(...);
}
#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#OKHTTP3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#Glide3
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#Glide4
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#内部----------------------------------------------------------------------------------------------


