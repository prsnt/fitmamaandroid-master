package com.fitsoo.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.adapter.ProgramsAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ChallengeModel;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.model.ProgramResponse;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsFragment extends Fragment implements OnReceiveResponseListener {

    RecyclerView recyclerPrograms;
    private List<ProgramHomeModel> programModels;
    private ProgramsAdapter adapter;

    public ProgramsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        programModels = new ArrayList<>();
        FitsooUtils.fragName = getString(R.string.programs);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_challenge));
        ((MainActivity)getActivity()).updateTitle(getString(R.string.programs));
        ((MainActivity) getActivity()).BackVisible(false);
        ((MainActivity)getActivity()).updateBottomSelection(R.id.action_programs);
        adapter = new ProgramsAdapter(getActivity(), programModels);
        return inflater.inflate(R.layout.fragment_programs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerPrograms.setAdapter(adapter);
        recyclerPrograms.setLayoutManager(gridLayoutManager);

        getProgramsData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(View view) {
        recyclerPrograms = view.findViewById(R.id.recyclerPrograms);
    }

    public void getProgramsData() {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FitsooUtils.performRequest(getActivity(), loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.programlist), this, getString(R.string.str_please_wait), "programlist");
    }

    private void processProgramsAdapter(String response) {
        try {
            Gson gson = new     Gson();
            Log.d("Response-Frag:", response);

            final ProgramResponse workOutRes = (gson).fromJson(response, new TypeToken<ProgramResponse>() {
            }.getType());

            if (workOutRes.getSuccess()==1) {
                if (programModels.size() > 0) {
                    programModels.clear();
                }
                if (workOutRes.getProgramInformation() != null && workOutRes.getProgramInformation().size() > 0) {
                    FitsooPref.setPrograms(getActivity(), response);
                    programModels.addAll(workOutRes.getProgramInformation());
                } else {
                    BaseResponse<List<ProgramHomeModel>> savedRes = (new Gson()).fromJson(FitsooPref.getPrograms(getActivity()), new TypeToken<BaseResponse<List<ChallengeModel>>>() {
                    }.getType());
                    if (workOutRes.getSuccess()==1 && savedRes.getData() != null && savedRes.getData().size() > 0) {
                        programModels.addAll(savedRes.getData());
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                if (FitsooPref.getPrograms(getActivity()).trim().length() > 0) {
                    BaseResponse<List<ProgramHomeModel>> savedRes = (new Gson()).fromJson(FitsooPref.getChallengeRes(getActivity()), new TypeToken<BaseResponse<List<ChallengeModel>>>() {
                    }.getType());
                    if (workOutRes.getSuccess()==1 && savedRes.getData() != null && savedRes.getData().size() > 0) {
                        programModels.addAll(savedRes.getData());
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

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "programlist":
                processProgramsAdapter(response);
                break;
        }
    }
}
