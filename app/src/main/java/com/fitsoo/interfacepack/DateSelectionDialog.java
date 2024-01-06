package com.fitsoo.interfacepack;

import android.widget.DatePicker;

/**
 * Created by xyz on 22/7/17.
 */

public interface DateSelectionDialog {
    void onDateSelected(DatePicker view, int year, int month, int day);
}
