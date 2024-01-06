package com.fitsoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.fragment.ProgramExtraFragment;
import com.fitsoo.model.ProfileWorkLogModel;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.utils.FitsooUtils;

import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.fitsoo.utils.FitsooUtils.showMessageAlert;

/**
 * Created by system  on 20/7/17.
 */

public class ProfileWorkLogAdapter extends RecyclerView.Adapter<ProfileWorkLogAdapter.WorkOutHolder> {

    private List<ProfileWorkLogModel> workLogModels;
    private List<ProgramHomeModel> programModels;
    private Context context;
    private LayoutInflater inflater;
    boolean isProgram;

    public ProfileWorkLogAdapter(Context context, boolean isProgram, List<ProfileWorkLogModel> workLogModels, List<ProgramHomeModel> programModels) {
        this.context = context;
        this.workLogModels = workLogModels;
        this.programModels = programModels;
        this.isProgram = isProgram;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WorkOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_workout_log, parent, false);
        return new WorkOutHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkOutHolder holder, final int position) {
        if (isProgram) {
            holder.txtExcercisename.setText(programModels.get(position).getProgramTitle());
            Glide.with(context).load(programModels.get(position).getDemoVideoThumb()).into(holder.imgThumb);
            holder.txtExcerciseDay.setText(programModels.get(position).getTotalpercentage() + "%");
            holder.pgVideo.setVisibility(View.INVISIBLE);
            holder.relParentLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FitsooUtils.isAccess(context)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", programModels.get(position).getProgramId());
                        bundle.putString("pname", programModels.get(position).getProgramTitle());
                        bundle.putString("is_logged", "1");
                        FitsooUtils.ChangeFragment(true, bundle, new ProgramExtraFragment(), (AppCompatActivity) context);
                    } else {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        startActivity(i);

                    }
                }
            });
        } else {
            holder.txtExcercisename.setText(workLogModels.get(position).getName());
            Glide.with(context).load(workLogModels.get(position).getVideo_thumb()).into(holder.imgThumb);
            holder.txtExcerciseDay.setText(FitsooUtils.handleDateInProfile(workLogModels.get(position).getWorkoutdate()));
            holder.imgStatus.setImageResource(FitsooUtils.isVideoCompleted(Long.parseLong(workLogModels.get(position).getVideo_duration()), workLogModels.get(position).getVideourl(), workLogModels.get(position).getProgress(), context) == 1 ? R.mipmap.green_tick_small : R.mipmap.warning);
            holder.pgVideo.setVisibility(View.VISIBLE);
            holder.pgVideo.setMax(workLogModels.get(position).getVideo_duration().isEmpty() ? 0 : Integer.parseInt(workLogModels.get(position).getVideo_duration()));
            holder.pgVideo.setProgress(Integer.parseInt(workLogModels.get(position).getProgress()));
            holder.relParentLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FitsooUtils.isAccess(context)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("vid", workLogModels.get(position).getVid());
                        bundle.putString("vurl", workLogModels.get(position).getVideourl());
                        bundle.putString("progress", workLogModels.get(position).getProgress());
                        bundle.putString("thumb", workLogModels.get(position).getVideo_thumb());
                        bundle.putString("title", workLogModels.get(position).getName());
                        Intent passToVideo = new Intent(context, VideoActivity.class);
                        passToVideo.putExtras(bundle);
                        context.startActivity(passToVideo);
                        //((MainActivity) context).finish();
                    }else {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        startActivity(i);
                    }
                }
            });
            setColorSchemeForHeader(holder.txtHeadIndicator, FitsooUtils.isGreaterThenOtherDate(workLogModels.get(position).getWorkoutdate()));
        }
    }

    private void setColorSchemeForHeader(TextView txtHeadIndicator, int position) {
        switch (position) {
            case 0:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case 1:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                break;
            case 2:
                txtHeadIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (isProgram)
            return programModels.size();
        else
            return workLogModels.size();
    }

    public class WorkOutHolder extends RecyclerView.ViewHolder {
        public TextView txtHeadIndicator;
        public ImageView imgThumb;
        public ProgressBar pgVideo;
        public TextView txtExcercisename;
        public TextView txtExcerciseDay;
        public ImageView imgStatus;
        public RelativeLayout relParentLog;

        public WorkOutHolder(View itemView) {
            super(itemView);
            relParentLog = (RelativeLayout) itemView.findViewById(R.id.rel_parent_log);
            txtExcerciseDay = (TextView) itemView.findViewById(R.id.txtExcerciseDay);
            txtExcercisename = (TextView) itemView.findViewById(R.id.txtExcerciseName);
            pgVideo = (ProgressBar) itemView.findViewById(R.id.progress_workout_adapter);
            imgStatus = (ImageView) itemView.findViewById(R.id.imgStatus);
            imgThumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            txtHeadIndicator = (TextView) itemView.findViewById(R.id.txt_head_indicator);

        }
    }
}
