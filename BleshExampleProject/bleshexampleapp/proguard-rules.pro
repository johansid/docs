# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/keremgocen/Blesh/Android/sdk/tools/proguard/proguard-android.txt
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

##----Blesh----##
-keep class com.blesh.sdk.** { *; }
-keepattribute InnerClasses
##----OkHttp----##
-dontwarn rx.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
##----Gson----##
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
##----universal-image-loader----##
-keep class com.nostra13.universalimageloader.** { *; }