package com.fitsoo.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.billingclient.api.BillingClient;
import com.bumptech.glide.Glide;
import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.activity.VideoActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramInnerFragment extends BaseFragment implements View.OnClickListener {

    CustomTextView tvDaysMinutes, txt_instruction, textViewHeader, tvDaysUniquecount, tvDaysTotalCount;
    ImageView imageView;
    CustomButton btnBuynow;
    ProgramHomeModel model;
    private BillingClient mBillingClient;
    private String purchaseJSON = "";
    Intent intent;

    public ProgramInnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_inner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FitsooUtils.fragName = getString(R.string.programInner);
        init(view);
        ((MainActivity) getActivity()).BackVisible(true);
        ((MainActivity) getActivity()).updateTitle(getString(R.string.programs));
        if (getArguments() != null) {
            if (getArguments().containsKey("model")) {
                model = getArguments().getParcelable("model");
                setData(model);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setData(ProgramHomeModel model) {
        try {
            if (model.getDemoVideoThumb() != null)
                Glide.with(getActivity()).load(model.getDemoVideoThumb()).placeholder(R.mipmap.ic_launcher).into(imageView);
            tvDaysMinutes.setText((model.getMinute().equals("")) ? "0" : model.getMinute());
            tvDaysTotalCount.setText((model.getNoOfDays().equals("")) ? "0" : model.getNoOfDays());
            tvDaysUniquecount.setText((model.getUniqueVideos().equals("")) ? "0" : model.getUniqueVideos());
            textViewHeader.setText(model.getProgramTitle());
            txt_instruction.setText(model.getDescription());
            if (model.getIsProgramPurchased() == 1 && model.getIsAccess() == 1)
                btnBuynow.setText(R.string.str_enter);
            else if (model.getIsProgramPurchased() == 0 && model.getIsAccess() == 1)
                btnBuynow.setText(R.string.str_enter);
            else if (model.getIsProgramPurchased() == 0 && model.getIsAccess() == 0)
                btnBuynow.setText(R.string.str_enter);
            else if (model.getIsProgramPurchased() == 1 && model.getIsAccess() == 0)
                btnBuynow.setText(R.string.str_enter);
        }catch (Exception e){
            e.printStackTrace();
        }

        /*if (model.getIsProgramPurchased() == 1)
            btnBuynow.setText(getString(R.string.str_enter));*/



    }

    private void init(View view) {
        tvDaysTotalCount = view.findViewById(R.id.tvDaysTotalCount);
        tvDaysMinutes = view.findViewById(R.id.tvDaysMinutes);
        tvDaysUniquecount = view.findViewById(R.id.tvDaysUniquecount);
        textViewHeader = view.findViewById(R.id.textViewHeader);
        imageView = view.findViewById(R.id.imageView);
        btnBuynow = view.findViewById(R.id.btnBuynow);
        txt_instruction = view.findViewById(R.id.txt_instruction);

        btnBuynow.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private void BuyProduct(String response) {
        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.PROGRAM_ID, model.getProgramId());
            loginReques.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(getActivity()));
            loginReques.put("product_response", response);
            loginReques.put(FitsooConstant.REQ_DEVICE_TYPE, "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FitsooUtils.performRequest(getActivity().getApplicationContext(), loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.buyproduct), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                btnBuynow.setText(getString(R.string.str_enter));
                model.setIsProgramPurchased(1);
                model.setIsAccess(1);
            }
        }, getString(R.string.str_please_wait), "buyproduct");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuynow:
                // if (FitsooUtils.isSubscribedFun(getActivity()))
                // if (FitsooUtils.isAccess(getActivity())) {
                if (model.getIsAccess() == 1){

                    EnterToNextScreen();
                }
                else if (model.getIsAccess() == 0) {
                    intent = new Intent(getActivity(), SubscribeActivity.class);
                    startActivity(intent);
                }

                //}
                break;
            case R.id.imageView:

                if (model.getIsAccess() == 1){
                    Bundle bundle = new Bundle();
                    bundle.putString("vid", model.getDemovideoId());
                    bundle.putString("vurl", model.getDemoVideoUrl());
                    bundle.putString("progress", "0");
                    bundle.putString("thumb", model.getDemoVideoThumb());
                    bundle.putString("title", model.getProgramTitle());
                    bundle.putString("programid", "-1");
                    FitsooUtils.programHomeModel = model;
                    Intent passToVideo = new Intent(getActivity(), VideoActivity.class);
                    passToVideo.putExtras(bundle);
                    passToVideo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(passToVideo);

                }
                else if (model.getIsAccess() == 0) {
                    intent = new Intent(getActivity(), SubscribeActivity.class);
                    startActivity(intent);
                }

                // if (FitsooUtils.isSubscribedFun(getActivity()))
                //if (FitsooUtils.isAccess(getActivity())) {
                //}
                break;
        }
    }

    private void EnterToNextScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("pid", model.getProgramId());
        bundle.putString("pname", model.getProgramTitle());
        bundle.putString("is_logged", "0");
        FitsooUtils.ChangeFragment(true, bundle, new ProgramExtraFragment(), (AppCompatActivity) getActivity());
    }

    private void purchasePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.popup_purchase, null, false);
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.show();
        CustomButton btn_Purchase = view.findViewById(R.id.btn_Purchase);
        CustomTextView customTextView3 = view.findViewById(R.id.customTextView3);
        customTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_Purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }
}
