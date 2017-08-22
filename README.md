<img src="screenies/1.gif" width="300" align="right" hspace="20">

# Morphing Material Dialogs
[![Release](https://jitpack.io/v/AdityaAnand1/Morphing-Material-Dialogs.svg)](https://jitpack.io/#AdityaAnand1/Morphing-Material-Dialogs)
[![Build Status](https://travis-ci.org/AdityaAnand1/Morphing-Material-Dialogs.svg)](https://travis-ci.org/AdityaAnand1/Morphing-Material-Dialogs)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/AdityaAnand1/Morphing-Material-Dialog/blob/master/LICENSE.txt)



A library for fab-to-dialog morphing (as in Nick Butcher's [Plaid](https://github.com/nickbutcher/plaid)) with Aidan Follestad's [Material Dialogs](https://github.com/afollestad/material-dialogs).

# Table of Contents
1. [Setup Instructions](https://github.com/AdityaAnand1/Morphing-Material-Dialogs#setup-instructions)
2. [Usage Instructions](https://github.com/AdityaAnand1/Morphing-Material-Dialogs#usage-instructions)
2. [Misc](https://github.com/AdityaAnand1/Morphing-Material-Dialogs#misc)

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

In your styles.xml, override the MorphDialog.Base themes (at least one, both if you wish to support light and dark themes for your app)

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
 
 In your app's manifest file, add the following (at least one, both if you wish to support light and dark themes for your app)
 
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

## Misc

P.S: We're at v0.0.1-alpha2 pre-release. Expect things to change and break and evolve :)

#### What happens below API 21 (<Lollipop)?

Nothing. Since this library uses activity transitions which are properly supported only for Lollipop and up, the dialog pops up normally without any morphing animation below Lollipop.

#### Why does the library not support all of Material Dialogs features?

Doing so will mean that this library would have to duplicate the entire API of MaterialDialogs. I'm currently looking for a cleaner and leaner alternative to accomplishing this ([Suggestions welcome](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/issues/new))

#### I want to morph **from something other than a fab**/ I want to morph **to something other than MorphDialog**

Currently, this library does not support something-other-than-a-fab-to-something-other-than-a-material-dialog transition. If you'd like for it to work in another setting, say a custom view, head over to the [standalone](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/tree/master/standalone) and check out the minimal implementation that will point you in the direction of a custom solution. Although, I'm certainly open to all ideas, including turning this into a generic morph-anything-to-anything library but doing so may or may not even be possible. [Suggestions welcome](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/issues/new)
