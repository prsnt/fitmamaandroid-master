package com.fitsoo.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.adapter.ChallengeAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ChallengeModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by system  on 21/7/17.
 */

public class ChallengeFragment extends BaseFragment implements OnReceiveResponseListener {

    private List<ChallengeModel> challengeModels;
    private ChallengeAdapter adapter;
    private GridView gridChallenges;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        challengeModels = new ArrayList<>();
        FitsooUtils.fragName = getString(R.string.nav_challenge);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_challenge));
        ((MainActivity) getActivity()).BackVisible(false);
        ((MainActivity) getActivity()).updateTitle(getString(R.string.free_challange));
        ((MainActivity) getActivity()).updateBottomSelection(R.id.action_Challange);
        adapter = new ChallengeAdapter(getActivity(), challengeModels);
        return inflater.inflate(R.layout.challenge_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridChallenges = (GridView) view.findViewById(R.id.grid_challenges);
        gridChallenges.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWorkoutData();
            }
        }, 400);
        super.onResume();
    }

    public void getWorkoutData() {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FitsooUtils.performRequest(getActivity(), loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.chanlleges_api), this, getString(R.string.str_please_wait), "chanlleges");
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "chanlleges":
                processChallengeAdapter(response);
                break;
        }
    }

    private void processChallengeAdapter(String response) {
        try {
            Gson gson = new Gson();
            Log.d("Response-Frag:", response);

            final BaseResponse<List<ChallengeModel>> workOutRes = (gson).fromJson(response, new TypeToken<BaseResponse<List<ChallengeModel>>>() {
            }.getType());

            if (workOutRes.getSuccess() == 1) {
                if (challengeModels.size() > 0) {
                    challengeModels.clear();
                }
                if (workOutRes.getData() != null && workOutRes.getData().size() > 0) {
                    FitsooPref.setChallengeRes(getActivity(), response);
                    challengeModels.addAll(workOutRes.getData());
                } else {
                    BaseResponse<List<ChallengeModel>> savedRes = (new Gson()).fromJson(FitsooPref.getChallengeRes(getActivity()), new TypeToken<BaseResponse<List<ChallengeModel>>>() {
                    }.getType());
                    if (savedRes.getSuccess() == 1 && savedRes.getData() != null && savedRes.getData().size() > 0) {
                        challengeModels.addAll(savedRes.getData());
                    }
                }

                adapter.notifyDataSetChanged();
            } else {
                if (FitsooPref.getChallengeRes(getActivity()).trim().length() > 0) {
                    BaseResponse<List<ChallengeModel>> savedRes = (new Gson()).fromJson(FitsooPref.getChallengeRes(getActivity()), new TypeToken<BaseResponse<List<ChallengeModel>>>() {
                    }.getType());
                    if (savedRes.getSuccess() == 1 && savedRes.getData() != null && savedRes.getData().size() > 0) {
                        challengeModels.addAll(savedRes.getData());
                    }
                } else {
                    FitsooUtils.showMessageAlert(workOutRes.getMessage(), getString(R.string.app_name), getActivity());
                }
            }
            FitsooUtils.checkUserStatus(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
         /*   public void showAlertDialog (final Context context, String title, String msg){

                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context)
                        .setTitle(title).setMessage(msg).setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    if (context != null) {
                                        getWorkoutData();
                                    }
                                } catch (ClassCastException e) {

                                }

                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }*/

}

