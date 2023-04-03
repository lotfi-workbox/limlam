# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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

#-dontobfuscate

-keep class androidx.drawerlayout.widget.DrawerLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.RoundImageView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedAppBarLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedButton {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedCardView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedCheckBox {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedCollapsingToolbarLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedConstraintLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedCoordinatorLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedDrawerLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedEditText {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedFrameLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageButton {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedImageView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedNavigationView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedProgressBar {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedRecyclerView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedScrollView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedSearchView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedSeekBar {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedTabLayout {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedTextView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedToolbar {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedView {*;}
-keep class com.saeedlotfi.limlam.userInterface.layouts._commom.customViews.ThemedViewPager {*;}





