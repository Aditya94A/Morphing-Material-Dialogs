<img src="screenies/1.gif" width="300" align="right" hspace="20">

# Morphing Material Dialogs
A library for fab-to-dialog morphing (as in Nick Butcher's [Plaid](https://github.com/nickbutcher/plaid)) with Aidan Follestad's [Material Dialogs](https://github.com/afollestad/material-dialogs).

## Setup Instructions

 Add the following to your root (project) level build.gradle:

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the library to your app's build.gradle

```gradle
	dependencies {
		implementation 'com.github.AdityaAnand1:Morphing-Material-Dialogs:0.0.1-alpha2'
	}
```

Since we'll be morphing a floating action button into a dialog, you should also have the design support library:

```gradle
	dependencies {
    implementation "com.android.support:design:26.01"
	}
```

In your styles.xml, override the MorphDialog.Base.Light theme

```xml
    <style name="MorphDialog.Custom.Light" parent="MorphDialog.Base.Light">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryDark">@color/primary_dark</item>
        <item name="colorAccent">@color/accent</item>
    </style>

    <style name="MorphDialog.Custom.Dark" parent="MorphDialog.Base.Dark">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryDark">@color/primary_dark</item>
        <item name="colorAccent">@color/accent</item>
    </style>
 ```
 
 In your app's manifest file, add the following
 
 ```xml
    <activity
        android:name="in.adityaanand.morphdialog.MorphDialogActivity"
        android:theme="@style/MorphDialog.Custom.Light">
    </activity>
    <activity
        android:name="in.adityaanand.morphdialog.MorphDialogActivityDark"
        android:theme="@style/MorphDialog.Custom.Dark">
    </activity>
 ```
 
 ## Usage instructions
 
 This library mirror's a subset of [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs) API. Currently, you can specify the title, content, positive button text and negative button text (all as CharSequences or string resource IDs).
 
 For example:
 
 ```java
 new MorphDialog.Builder(this, fabView)
                .title("Title")
                .content("This is a sentence. Here is another one.") 
                .positiveText(R.string.ok)
                .useDarkTheme(true) //optional, default is false
                .show();
 ```
 
 
For example, if you have a floating action button in your activity
 
 ```xml
        <android.support.design.widget.FloatingActionButton
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="morph"/>
```

Then your `morph()` might look like:

```java
    public void morph(View view) {
        new MorphDialog.Builder(this, (FloatingActionButton) view)
                .title("Title")
                .content("This is a sentence. Here is another one.")
                .show();
    }
```
