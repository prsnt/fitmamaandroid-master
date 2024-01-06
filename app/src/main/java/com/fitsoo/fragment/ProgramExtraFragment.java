package com.fitsoo.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.adapter.ProgramsExtraAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.ProgramExtras.ProgramExtraVideo;
import com.fitsoo.model.ProgramExtras.ProgramVideo;
import com.fitsoo.model.ProgramExtras.ProgramsExtraModel;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramExtraFragment extends BaseFragment implements OnReceiveResponseListener {

    private List<ProgramVideo> programModels;
    private List<ProgramExtraVideo> programExtraModels;
    private ProgramsExtraAdapter adapter;
    private ProgramsExtraAdapter adapterExtra;
    RecyclerView recycle_workout, recycle_Extra;
    String program_id, program_name;
    SeekBar seekbar_workout;
    ProgressBar progressBar2;
    CustomTextView txtPageTitle, tvPercentage;
    private boolean isFromRecycle = true;
    private int progressUpdateValue = 0;
    CustomTextView tvDownloadPDF;
    String pdfString;
    TextView tvNoDataFound;

    public int isComefromProfile;

    public ProgramExtraFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        programModels = new ArrayList<>();
        programExtraModels = new ArrayList<>();
        FitsooUtils.fragName = getString(R.string.programExtra);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_challenge));
        ((MainActivity) getActivity()).BackVisible(true);
        ((MainActivity) getActivity()).updateTitle(getString(R.string.program));

        return inflater.inflate(R.layout.fragment_program_extra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycle_Extra = view.findViewById(R.id.recycle_Extra);
        recycle_workout = view.findViewById(R.id.recycle_workout);
        seekbar_workout = view.findViewById(R.id.seekbar_workout);
        txtPageTitle = view.findViewById(R.id.txtPageTitle);
        tvPercentage = view.findViewById(R.id.tvPercentage);
        progressBar2 = view.findViewById(R.id.progressBar2);
        tvDownloadPDF = view.findViewById(R.id.tvDownloadPDF);
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
        tvDownloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnPDFClick(v);
            }
        });

        adapter = new ProgramsExtraAdapter(getActivity(), programModels, null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recycle_workout.setLayoutManager(gridLayoutManager);
        recycle_workout.setAdapter(adapter);

        progressBar2.setScaleY(3f);

        adapterExtra = new ProgramsExtraAdapter(getActivity(), null, programExtraModels);

        Log.e("extraVideos", "onViewCreated: "+programExtraModels);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recycle_Extra.setLayoutManager(gridLayoutManager);
        recycle_Extra.setAdapter(adapterExtra);
        tvNoDataFound.setVisibility(View.VISIBLE);


        recycle_workout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isFromRecycle) {
                    seekbar_workout.setProgress(recycle_workout.computeHorizontalScrollOffset());
                }
            }
        });

        seekbar_workout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromRecycle = false;
                return false;
            }
        });

        seekbar_workout.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!isFromRecycle) {
                    if (progress == 0) {
                        recycle_workout.smoothScrollToPosition(progress);
                    }
                    recycle_workout.scrollBy(progress - progressUpdateValue, 0);
                    progressUpdateValue = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setDataComefromProfile(ProgramHomeModel model) {

        pdfString = model.getDownloadPDF();
        tvPercentage.setText(model.getTotalpercentage() + "%");
        progressBar2.setProgress(Integer.parseInt(model.getTotalpercentage()));
        if (model.getProgramInformation() != null && model.getProgramInformation().size() > 0) {
            //FitsooPref.setProgramsE(getActivity(), response);
            programModels.addAll(model.getProgramInformation());
            adapter.model = model;
            adapter.notifyDataSetChanged();
        }
        if (model.getExtraVidoes() != null && model.getExtraVidoes().size() > 0) {
            //FitsooPref.setProgramsE(getActivity(), response);
            programExtraModels.addAll(model.getExtraVidoes());
            adapterExtra.model = model;
            adapterExtra.notifyDataSetChanged();
        }
    }

    public void OnPDFClick(View v) {
        String url = pdfString;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (program_id == null) {
            if (getArguments() != null)
                if (getArguments().containsKey("pid")) {
                    program_id = getArguments().getString("pid");
                    program_name = getArguments().getString("pname");
                    isComefromProfile = Integer.parseInt(getArguments().getString("is_logged"));
                    txtPageTitle.setText(program_name);
                    //tvDownloadPDF.setText(program_name + " " + getString(R.string.program_manual));
                    getProgramsData();
                }
        } else
            getProgramsData();
    }

    public void getProgramsData() {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.PROGRAM_ID, program_id);
            loginReques.put("is_logged", isComefromProfile);
            loginReques.put(FitsooConstant.REQ_USER_ID, FitsooPref.getUserId(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FitsooUtils.performRequest(getActivity(), loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.programdetail), this, getString(R.string.str_please_wait), "programdetail");
    }

    private void processProgramsAdapter(String response, ProgramsExtraModel model) {
        try {
            Gson gson = new Gson();
            Log.d("Response-Frag:", response);
            final ProgramsExtraModel workOutRes;
            if (model != null)
                workOutRes = model;
            else
                workOutRes = (gson).fromJson(response, new TypeToken<ProgramsExtraModel>() {
                }.getType());

            if (workOutRes.getSuccess().equals("1")) {
                pdfString = workOutRes.getDownloadPDF();
                tvDownloadPDF.setText(workOutRes.getDownloadPDFName());
                if (programModels.size() > 0) {
                    programModels.clear();
                }
                if (programExtraModels.size() > 0) {
                    programExtraModels.clear();
                }
                if (workOutRes.getProgramVideos() != null && workOutRes.getProgramVideos().size() > 0) {
                    //FitsooPref.setProgramsE(getActivity(), response);
                    programModels.addAll(workOutRes.getProgramVideos());
                    adapter.modelExtra = workOutRes;
                    adapter.notifyDataSetChanged();
                }

                if (workOutRes.getProgramExtraVideos() != null && workOutRes.getProgramExtraVideos().size() > 0) {
                    //FitsooPref.setProgramsE(getActivity(), response);
                    tvNoDataFound.setVisibility(View.GONE);
                    programExtraModels.addAll(workOutRes.getProgramExtraVideos());
                    adapterExtra.modelExtra = workOutRes;
                    adapterExtra.notifyDataSetChanged();
                }
                tvPercentage.setText(workOutRes.getProgramPercentage() + "%");
                progressBar2.setProgress((int) workOutRes.getProgramPercentage());

            } /*else {
                if (FitsooPref.getProgramsE(getActivity()).trim().length() > 0) {
                    ProgramsExtraModel savedRes = (new Gson()).fromJson(FitsooPref.getProgramsE(getActivity()), new TypeToken<ProgramsExtraModel>() {
                    }.getType());
                    if (workOutRes.getSuccess().equals("1") && savedRes.getProgramVideos() != null && savedRes.getProgramVideos().size() > 0) {
                        programModels.addAll(savedRes.getProgramVideos());
                    }
                    if (workOutRes.getSuccess().equals("1") && savedRes.getProgramExtraVideos() != null && savedRes.getProgramExtraVideos().size() > 0) {
                        programExtraModels.addAll(savedRes.getProgramExtraVideos());
                    }
                } else {
                    FitsooUtils.showMessageAlert(workOutRes.getMessage(), getString(R.string.app_name), getActivity());
                }
            }*/
            FitsooUtils.checkUserStatus(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "programdetail":
                processProgramsAdapter(response, null);
                break;
        }
    }
}
