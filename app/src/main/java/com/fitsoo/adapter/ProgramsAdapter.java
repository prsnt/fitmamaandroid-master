package com.fitsoo.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.fragment.ProgramInnerFragment;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.utils.FitsooUtils;

import java.util.List;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.Viewholder> {

    Context context;
    List<ProgramHomeModel> list;

    public ProgramsAdapter(Context context, List<ProgramHomeModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_programs, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramsAdapter.Viewholder viewholder, final int i) {

        /*ViewGroup.LayoutParams params = viewholder.itemView.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth() / 2.5);
        params.height = (int) (ScreenUtils.getScreenWidth() / 2.5);*/

        ProgramHomeModel model = list.get(i);
        viewholder.tvItemProgram.setText(model.getProgramTitle());
        Glide.with(context).load(model.getDemoVideoThumb()).into(viewholder.imgProgram);
        viewholder.img_play.setVisibility(View.GONE);

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("model", list.get(i));
                    FitsooUtils.ChangeFragment(true, bundle, new ProgramInnerFragment(), (AppCompatActivity) context);
              
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgProgram,img_play;
        TextView tvItemProgram;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvItemProgram = itemView.findViewById(R.id.tvItemProgram);
            imgProgram = itemView.findViewById(R.id.imgProgram);
            img_play = itemView.findViewById(R.id.img_play);
        }
    }
}