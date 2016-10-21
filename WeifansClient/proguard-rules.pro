# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn
#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class org.iqiyi.video.ui.SNSBindWebview$MyJavaSriptInterface {
    <methods>;
}
-keepclassmembers class org.qiyi.android.video.ui.account.PhoneSNSBind$MyJavaSriptInterface {
    <methods>;
}

-keepclassmembers class org.iqiyi.video.ui.SNSBindForSingleWebview$MyJavaSriptInterface {
    <methods>;
}

-keepclassmembers class org.iqiyi.video.advertising.AdsWebView$MyJavaSriptInterface {
    <methods>;
}

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-dontwarn com.facebook.**
-keep class com.facebook.** {*;}
