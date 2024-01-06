package com.fitsoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.fragment.ChallengeFragment;
import com.fitsoo.model.ChallengeModel;
import com.fitsoo.utils.FitsooUtils;

import java.util.List;

/**
 * Created by system  on 21/7/17.
 */

public class ChallengeAdapter extends BaseAdapter {

    public List<ChallengeModel> challengeModels;
    public Context context;
    public LayoutInflater inflater;

    public ChallengeAdapter(Context context, List<ChallengeModel> challengeModels) {
        this.challengeModels = challengeModels;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return challengeModels.size();
    }

    @Override
    public Object getItem(int position) {
        return challengeModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return challengeModels.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChallengeHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_challenge, parent, false);
            holder = new ChallengeHolder();
            holder.relThumb = (RelativeLayout) convertView.findViewById(R.id.rel_thumbparent);
            holder.imgHolder = (ImageView) convertView.findViewById(R.id.img_thumb);
            holder.txtBottomDesc = (TextView) convertView.findViewById(R.id.txtBottomDesc);
            convertView.setTag(holder);
        } else {
            holder = (ChallengeHolder) convertView.getTag();
        }

        holder.txtBottomDesc.setText(challengeModels.get(position).getWorkfocus_name());
        Glide.with(context).load(challengeModels.get(position).getVideo_thumb()).into(holder.imgHolder);
        holder.relThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (FitsooUtils.isSubscribedFun(context)){
//                    if (FitsooUtils.isAccess(context)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("vid", challengeModels.get(position).getVid());
                        bundle.putString("vurl", challengeModels.get(position).getVideourl());
                        bundle.putString("progress", "0");
                        bundle.putString("thumb", challengeModels.get(position).getVideo_thumb());
                        bundle.putString("title", challengeModels.get(position).getWorktype_name());
                        bundle.putString("programid", "-1");
                        Intent passToVideo = new Intent(context, VideoActivity.class);
                        passToVideo.putExtras(bundle);
                        context.startActivity(passToVideo);
                        //((MainActivity) context).finish();
//                    }
            }
        });

        return convertView;
    }

    public class ChallengeHolder {
        public ImageView imgHolder;
        public TextView txtBottomDesc;
        public RelativeLayout relThumb;
    }

}
