<img src="screenies/1.gif" width="300" align="right" hspace="20">

# Morphing Material Dialogs
A minimal proof of concept showcasing fab-to-dialog morphing (as in Nick Butcher's [Plaid](https://github.com/nickbutcher/plaid)) with Aidan Follestad's [Material Dialogs](https://github.com/afollestad/material-dialogs).

## How it works

1. Create an [activity](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/blob/master/app/src/main/java/com/aditya/morph/DialogActivity.java) for the dialog with a [skeleton layout](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/blob/master/app/src/main/res/layout/activity_dialog.xml)
2. Pass the dialog parameters (title, content etc.) along with the intent bundle and [start the activity](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/blob/master/app/src/main/java/com/aditya/morph/DialogActivity.java#L118)
3. [Create a MaterialDialog](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/blob/master/app/src/main/java/com/aditya/morph/DialogActivity.java#L40) in the dialog activity, stick it's root view reference [inside our skeleton layout](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/blob/master/app/src/main/java/com/aditya/morph/DialogActivity.java#L52)
4. ~~[Black magic](https://github.com/AdityaAnand1/Morphing-Material-Dialogs/tree/master/app/src/main/java/com/aditya/morph/util).~~ Set up shared elements transitions for morphing to a dialog from a floating action button and vice versa
5. Profit.
