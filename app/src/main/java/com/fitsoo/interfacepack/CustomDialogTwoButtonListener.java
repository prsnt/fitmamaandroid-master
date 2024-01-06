package com.fitsoo.interfacepack;

import android.content.DialogInterface;

/**
 * Created by system  on 7/4/17.
 *
 * The purpose of this listener is to pass on the actions taken in the Dialog to the Screens needed.
 * Dialog is being shown from the common util class and so there was a need to provide an interface
 * using which the actions can be passed on to the needed screen
 */

public interface CustomDialogTwoButtonListener {

    // Called when positive button of dialog is being clicked
    void onDialogPositiveButtonClick(DialogInterface dialog, int which);

    // Called when Negative button of the Dialog is being clicked
    void onDialogNegativeButtonClick(DialogInterface dialog, int which);

}
