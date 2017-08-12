<img src="screenies/1.gif" width="300" align="right" hspace="20">

# Morphing Material Dialogs
A minimal proof of concept showcasing fab-to-dialog morphing (as in Nick Butcher's [Plaid](https://github.com/nickbutcher/plaid)) with Aidan Follestad's [Material Dialogs](https://github.com/afollestad/material-dialogs).

## How it works

1. Create an activity for the dialog with a skeleton layout xml file
2. Pass the dialog parameters (title, content etc.) along with the intent bundle and start the activity
3. Create a MaterialDialog in the dialog activity, stick it's view reference inside our skeleton layout
4. ~~Black magic.~~ Set up shared elements transitions for morphing to a dialog from a floating action button and vice versa
5. Profit.
