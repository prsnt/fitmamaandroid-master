package com.fitsoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.model.ProgramExtras.ProgramExtraVideo;
import com.fitsoo.model.ProgramExtras.ProgramVideo;
import com.fitsoo.model.ProgramExtras.ProgramsExtraModel;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.view.CustomTextView;

import java.util.List;

import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

public class ProgramsExtraAdapter extends RecyclerView.Adapter<ProgramsExtraAdapter.Viewholder> {

    Context context;
    List<ProgramVideo> list;
    List<ProgramExtraVideo> listExtra;
    public int isComefromProfile = 0;
    public static ProgramHomeModel model;
    public static ProgramsExtraModel modelExtra;

    public ProgramsExtraAdapter(Context context, List<ProgramVideo> list, List<ProgramExtraVideo> listExtra) {
        this.list = list;
        this.listExtra = listExtra;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_workout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {

        ViewGroup.LayoutParams params = viewholder.itemView.getLayoutParams();
        params.width = (int) (getScreenWidth() / 3);
        params.height = (int) (getScreenWidth() / 2);

        viewholder.txt_day.setVisibility(View.INVISIBLE);
        viewholder.txt_head_indicator.setVisibility(View.INVISIBLE);
        if (list != null) {
            ProgramVideo model = list.get(i);
            viewholder.progress_workout_adapter.setVisibility(View.VISIBLE);
            viewholder.txtBottomDesc.setText(model.getProgramTitle());
            Glide.with(context).load(model.getVideoThumb()).into(viewholder.img_thumb);
            viewholder.progress_workout_adapter.setMax(model.getVideo_duration().isEmpty() ? 0 : Integer.parseInt(model.getVideo_duration()));
            viewholder.progress_workout_adapter.setProgress(model.getProgress_time().isEmpty() ? 0 : Integer.parseInt(model.getProgress_time()));
        } else {
            viewholder.progress_workout_adapter.setVisibility(View.INVISIBLE);
            ProgramExtraVideo model = listExtra.get(i);
            viewholder.txtBottomDesc.setText(model.getProgramTitle());
            Glide.with(context).load(model.getVideoThumb()).into(viewholder.img_thumb);
        }


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) ((getScreenWidth() / 1000.0) * 320), (int) (((getScreenWidth() / 1000.0) * 320) * 1.64));
        layoutParams.rightMargin = (int) ((getScreenWidth() / 1000.0) * 20);
        viewholder.relWorkoutParent.setLayoutParams(layoutParams);
        viewholder.relWorkoutParent.setGravity(RelativeLayout.CENTER_HORIZONTAL);

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null) {
                    openVideo(list.get(i).getVideoId(),
                            list.get(i).getVideoUrl(),
                            list.get(i).getProgress_time(),
                            list.get(i).getVideoThumb(),
                            list.get(i).getWorkoutType(),
                            list.get(i).getProgramId());
                } else {
                    openVideo(listExtra.get(i).getVideoId(),
                            listExtra.get(i).getVideoUrl(),
                            "0",
                            listExtra.get(i).getVideoThumb(),
                            listExtra.get(i).getWorkoutType(), -1);
                }
            }
        });
    }

    private void openVideo(String vid, String vurl, String progress, String thumb, String title, int pid) {
        Bundle bundle = new Bundle();
        Intent passToVideo = new Intent(context, VideoActivity.class);
        bundle.putString("vid", vid);
        bundle.putString("vurl", vurl);
        bundle.putString("progress", progress);
        bundle.putString("thumb", thumb);
        bundle.putString("title", title);
        bundle.putString("programid", String.valueOf(pid));
        passToVideo.putExtras(bundle);
        context.startActivity(passToVideo);
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return listExtra.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_thumb;
        TextView txtBottomDesc, txt_day;
        CustomTextView txt_head_indicator;
        public RelativeLayout relWorkoutParent;
        ProgressBar progress_workout_adapter;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            relWorkoutParent = (RelativeLayout) itemView.findViewById(R.id.rel_parent_work);
            txt_day = itemView.findViewById(R.id.txt_day);
            txtBottomDesc = itemView.findViewById(R.id.txtBottomDesc);
            img_thumb = itemView.findViewById(R.id.img_thumb);
            progress_workout_adapter = itemView.findViewById(R.id.progress_workout_adapter);
            txt_head_indicator = itemView.findViewById(R.id.txt_head_indicator);
        }
    }
}