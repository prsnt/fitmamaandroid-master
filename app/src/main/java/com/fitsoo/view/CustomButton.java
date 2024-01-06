package com.fitsoo.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;


import com.fitsoo.utils.FitsooUtils;


/**
 * Created by System on 3/7/2016.
 */
public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
        this.setTypeface(FitsooUtils.getFont(context,
                Integer.parseInt(this.getTag().toString())));
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(FitsooUtils.getFont(context,
                Integer.parseInt(this.getTag().toString())));
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(FitsooUtils.getFont(context,
                Integer.parseInt(this.getTag().toString())));
    }
}