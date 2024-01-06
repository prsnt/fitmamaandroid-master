package com.fitsoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.activity.LoginActivity;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.fragment.HomeFragment;
import com.fitsoo.fragment.ProgramExtraFragment;
import com.fitsoo.fragment.ProgramInnerFragment;
import com.fitsoo.fragment.WorkOutFragment;
import com.fitsoo.model.Extravideo;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.model.WorkoutModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomTextView;

import java.util.List;

/**
 * Created by system  on 20/7/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.WorkOutHolder> {

    private List<WorkoutModel> workoutModels;
    private List<Extravideo> ExtraVideosList;
    private List<ProgramHomeModel> ProgramVideosList;
    private Context context;
    private LayoutInflater inflater;
    private HomeFragment homeFragment;
    int from;

    public HomeAdapter(Context context, Object workoutModels, HomeFragment homeFragment, int from) {
        this.context = context;
        this.from = from;
        if (from == 1)
            this.workoutModels = (List<WorkoutModel>) workoutModels;
        if (from == 2)
            this.ProgramVideosList = (List<ProgramHomeModel>) workoutModels;
        if (from == 3)
            this.ExtraVideosList = (List<Extravideo>) workoutModels;
        inflater = LayoutInflater.from(context);
        this.homeFragment = homeFragment;
    }

    @Override
    public WorkOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_workout, parent, false);
        return new WorkOutHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkOutHolder holder, final int position) {

        if (from == 1) {
            setColorSchemeForHeader(holder.txtDay, holder.txtHeadIndicator, position);
            holder.txtBottomDesc.setText(workoutModels.get(position).getWorktype_name());
            holder.progressVideo.setMax(workoutModels.get(position).getVideo_duration().isEmpty() ? 0 : Integer.parseInt(workoutModels.get(position).getVideo_duration()));
            if (FitsooUtils.hasValue(workoutModels.get(position).getProgress_time())) {
                holder.progressVideo.setProgress(Integer.parseInt(workoutModels.get(position).getProgress_time()));
            }
            holder.relThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!FitsooUtils.isAccess(context)) {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        context.startActivity(i);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("position", "" + position);
                        FitsooUtils.ChangeFragment(false, bundle, new WorkOutFragment(), (AppCompatActivity) context);
                    }
                }
            });
            Glide.with(context).load(workoutModels.get(position).getVideo_thumb()).into(holder.imgThumb);

            homeFragment.setProgressScroll(1);
        } else if (from == 2) {
            setColorSchemeForHeader(holder.txtDay, holder.txtHeadIndicator, position);
            holder.txtBottomDesc.setText(ProgramVideosList.get(position).getProgramTitle());
            holder.progressVideo.setVisibility(View.GONE);

            holder.txtDay.setVisibility(View.INVISIBLE);
            holder.txtHeadIndicator.setVisibility(View.INVISIBLE);
            holder.relThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("model", ProgramVideosList.get(position));
                    FitsooUtils.ChangeFragment(true, bundle, new ProgramInnerFragment(), (AppCompatActivity) context);
                    ((MainActivity) context).updateBottomSelection(R.id.action_programs);
                }
            });
            Glide.with(context).load(ProgramVideosList.get(position).getDemoVideoThumb()).into(holder.imgThumb);

            homeFragment.setProgressScroll(2);
        } else {
            holder.txtDay.setVisibility(View.INVISIBLE);
            holder.txtBottomDesc.setText(ExtraVideosList.get(position).getTitle());
            holder.progressVideo.setVisibility(View.INVISIBLE);
            holder.txtHeadIndicator.setVisibility(View.INVISIBLE);

            holder.relThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FitsooUtils.isSubscribedFun(context))
                        if (FitsooUtils.isAccess(context)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("vid", ExtraVideosList.get(position).getVid());
                            bundle.putString("vurl", ExtraVideosList.get(position).getVideourl());
                            bundle.putString("progress", ExtraVideosList.get(position).getProgressTime());
                            bundle.putString("thumb", ExtraVideosList.get(position).getVideoThumb());
                            bundle.putString("title", ExtraVideosList.get(position).getTitle());
                            bundle.putString("programid", "-1");
                            Intent passToVideo = new Intent(context, VideoActivity.class);
                            passToVideo.putExtras(bundle);
                            context.startActivity(passToVideo);

                            //homeFragment.startPurchaseProcess(position);
                        } else{

                            Intent i = new Intent(context, SubscribeActivity.class);
                            context.startActivity(i);

                        }
                }
            });
            Glide.with(context).load(ExtraVideosList.get(position).getVideoThumb()).into(holder.imgThumb);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) ((getScreenWidth() / 1000.0) * 320), (int) (((getScreenWidth() / 1000.0) * 320) * 1.64));
        layoutParams.rightMargin = (int) ((getScreenWidth() / 1000.0) * 20);
        holder.relWorkoutParent.setLayoutParams(layoutParams);
        holder.relWorkoutParent.setGravity(RelativeLayout.CENTER_HORIZONTAL);
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (displayMetrics.widthPixels - (context.getResources().getDimensionPixelSize(R.dimen._15dp) * 2));
    }

    private void setColorSchemeForHeader(CustomTextView txtDay, TextView txtHeadIndicator, int position) {
        switch (position) {
            case 0:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                txtDay.setText(FitsooUtils.getDate(-3));
                break;
            case 1:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                txtDay.setText(FitsooUtils.getDate(-2));
                break;
            case 2:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                txtDay.setText("Yesterday");
                break;
            case 3:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                txtDay.setText("Today");
                break;
            case 4:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                txtDay.setText("Tomorrow");
                break;
            case 5:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                txtDay.setText(FitsooUtils.getDate(2));
                break;
            case 6:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                txtDay.setText(FitsooUtils.getDate(3));
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (from == 1)
            return workoutModels.size();
        else if (from == 2)
            return ProgramVideosList.size();
        else
            return ExtraVideosList.size();
    }

    public class WorkOutHolder extends RecyclerView.ViewHolder {
        public TextView txtHeadIndicator;
        public ImageView imgThumb;
        public ProgressBar progressVideo;
        public CustomTextView txtBottomDesc;
        public CustomTextView txtDay;
        public RelativeLayout relThumb;
        public RelativeLayout relWorkoutParent;

        public WorkOutHolder(View itemView) {
            super(itemView);

            relWorkoutParent = (RelativeLayout) itemView.findViewById(R.id.rel_parent_work);
            relThumb = (RelativeLayout) itemView.findViewById(R.id.rel_thumbparent);
            txtDay = (CustomTextView) itemView.findViewById(R.id.txt_day);
            txtBottomDesc = (CustomTextView) itemView.findViewById(R.id.txtBottomDesc);
            progressVideo = (ProgressBar) itemView.findViewById(R.id.progress_workout_adapter);
            imgThumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            txtHeadIndicator = (TextView) itemView.findViewById(R.id.txt_head_indicator);
        }
    }
}
