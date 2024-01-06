package com.fitsoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.model.WorkoutModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;

import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by system  on 17/8/17.
 */

public class WorkoutPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<WorkoutModel> workModel;

    public WorkoutPagerAdapter(Context context, List<WorkoutModel> workModel) {
        mContext = context;
        this.workModel = workModel;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.adapter_workout_screen, collection, false);
        TextView txtHeadindicator = (TextView) view.findViewById(R.id.txt_head_indicator);
        ImageView imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress_workout_adapter);
        RelativeLayout relParent = (RelativeLayout) view.findViewById(R.id.rel_thumbparent);

        Glide.with(mContext).load(workModel.get(position).getVideo_thumb()).into(imgThumb);

        progress.setMax(workModel.get(position).getVideo_duration().isEmpty() ? 0 : Integer.parseInt(workModel.get(position).getVideo_duration()));
        if (workModel.get(position).getProgress_time().trim().length() > 0) {
            progress.setProgress(Integer.parseInt(workModel.get(position).getProgress_time()));
        } else {
            progress.setProgress(0);
        }

        setTopData(position, txtHeadindicator);
        relParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((FitsooPref.getSubscribeType(mContext) != 0)||FitsooUtils.isAccess(mContext)){
                    if (FitsooUtils.isAccess(mContext)) {
                        FitsooUtils.vidPosition = position;
                        Bundle bundle = new Bundle();
                        Intent passToVideo = new Intent(mContext, VideoActivity.class);
                        bundle.putString("vid", workModel.get(position).getVid());
                        bundle.putString("vurl", workModel.get(position).getVideourl());
                        bundle.putString("progress", workModel.get(position).getProgress_time());
                        bundle.putString("thumb", workModel.get(position).getVideo_thumb());
                        bundle.putString("title", workModel.get(position).getWorktype_name());
                        passToVideo.putExtras(bundle);
                        mContext.startActivity(passToVideo);
                    }}else {
                    startActivity(new Intent(mContext, SubscribeActivity.class));
//                    ((MainActivity) mContext).finish();
                }
            }
        });
        collection.addView(view);
        return view;
    }


    private void setTopData(int position, TextView txtHeadindicator) {

        switch (position) {
            case 0:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
                break;

            case 1:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
                break;

            case 2:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
                break;

            case 3:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                break;

            case 4:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                break;

            case 5:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                break;

            case 6:
                txtHeadindicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                break;
        }

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return workModel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
