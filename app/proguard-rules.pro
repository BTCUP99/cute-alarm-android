# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/bitme/Library/Android/sdk/tools/proguard/proguard-android.txt

# Keep Room entities
-keep class com.cutealarm.android.data.database.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }
