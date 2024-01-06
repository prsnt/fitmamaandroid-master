package com.fitsoo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.adapter.HomeAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.CustomDialogOneButtonListener;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.Extravideo;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.model.WorkoutModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.utils.RecyclerHelperClass.GravitySnapHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by system  on 20/7/17.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CustomDialogOneButtonListener {

    private TextView txtChallengeDesc;
    private String strChallengeText = "Challenge yourself and try these<br/> <b>FREE 6 FitMama workout videos</b>";
    private Spanned result;
    private Button btnEnterChallenge;
    private RecyclerView recycleWorkout, recycle_Programs;
    private HomeAdapter adapter;
    private HomeAdapter adapter_Program;
    private List<WorkoutModel> modelList;
    private List<ProgramHomeModel> ProgramVideosList;
    private List<Extravideo> ExtraVideosList;
    private Button enterWorkOut, btn_enter_Programs;
    private SeekBar seekWorkOut, seekbar_Programs;
    private int progressUpdateValue = 0;
    private int progressUpdateValueP = 0;
    private SnapHelper snapHelper;
    private boolean isFromRecycle = true;
    private CountDownTimer countDownTimer;
    private int currPos = 99;
    private static boolean isFirstTime = true;
    private static boolean isFirstTime_Program = true;
    private int initialProgress = 0;
    private int initialProgress_Program = 0;
    private GridLayoutManager gridLayoutManager;
    private BaseResponse<List<WorkoutModel>> workOutRes;
    private boolean isFromRecycle_P = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        FitsooUtils.fragName = getString(R.string.nav_home);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_home));
        ((MainActivity) getActivity()).BackVisible(false);
        ((MainActivity) getActivity()).updateBottomSelection(R.id.action_Home);
        snapHelper = new GravitySnapHelper(Gravity.START);
        seekWorkOut = (SeekBar) view.findViewById(R.id.seekbar_workout);
        txtChallengeDesc = (TextView) view.findViewById(R.id.txtChallengeDesc);
        btnEnterChallenge = (Button) view.findViewById(R.id.btn_enter_challenge);
        enterWorkOut = (Button) view.findViewById(R.id.btn_enter);
        recycleWorkout = (RecyclerView) view.findViewById(R.id.recycle_workout);
        recycle_Programs = (RecyclerView) view.findViewById(R.id.recycle_Programs);
        seekbar_Programs = (SeekBar) view.findViewById(R.id.seekbar_Programs);
        btn_enter_Programs = (Button) view.findViewById(R.id.btn_enter_Programs);
        modelList = new ArrayList<>();
        ProgramVideosList = new ArrayList<>();
        ExtraVideosList = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), modelList, HomeFragment.this, 1);
        adapter_Program = new HomeAdapter(getActivity(), ProgramVideosList, HomeFragment.this, 2);
//        recycleWorkout.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recycleWorkout.setLayoutManager(gridLayoutManager);
        recycleWorkout.setAdapter(adapter);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recycle_Programs.setLayoutManager(gridLayoutManager);
        recycle_Programs.setAdapter(adapter_Program);

        ((MainActivity) getActivity()).updateTitle(getString(R.string.home));

        snapHelper.attachToRecyclerView(recycleWorkout);

        recycleWorkout.setOnFlingListener(snapHelper);

        enterWorkOut.setOnClickListener(this);
        btnEnterChallenge.setOnClickListener(this);
        btn_enter_Programs.setOnClickListener(this);
        isFirstTime = true;
        isFirstTime_Program = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(strChallengeText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(strChallengeText);
        }

        txtChallengeDesc.setText(result);
        getWorkoutData();
        seekWorkOut.setOnSeekBarChangeListener(this);
        seekbar_Programs.setOnSeekBarChangeListener(this);

        recycleWorkout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isFromRecycle) {
                    seekWorkOut.setProgress(recycleWorkout.computeHorizontalScrollOffset());
                }
            }
        });

        recycle_Programs.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (isFromRecycle_P) {
                    seekbar_Programs.setProgress(recycle_Programs.computeHorizontalScrollOffset());
                }
            }
        });

        seekbar_Programs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromRecycle_P = false;
                return false;
            }
        });

        seekWorkOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromRecycle = false;
                return false;
            }
        });

        recycle_Programs.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                isFromRecycle_P = true;
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {
                isFromRecycle_P = true;
            }
        });

        recycleWorkout.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                isFromRecycle = true;
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                isFromRecycle = true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                isFromRecycle = true;
            }
        });

        countDownTimer = new CountDownTimer(1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (progressUpdateValue < seekWorkOut.getProgress()) {
                    seekWorkOut.setProgress(seekWorkOut.getProgress() - 10);
                } else if (progressUpdateValue > seekWorkOut.getProgress()) {
                    seekWorkOut.setProgress(seekWorkOut.getProgress() + 10);
                }
            }

            @Override
            public void onFinish() {
                seekWorkOut.setProgress(progressUpdateValue);
            }
        };

        // FitsooUtils.startPurchaseProcess(getActivity());
        return view;
    }

    public void setProgressScroll(int from) {

        if (from == 1)
            if (isFirstTime || seekWorkOut.getMax() == 0) {
                seekWorkOut.setMax((int) (recycleWorkout.computeHorizontalScrollRange() / 1.71));
                seekWorkOut.setProgress(((int) (recycleWorkout.computeHorizontalScrollRange() / 1.71)) / 2);
                initialProgress = ((int) (recycleWorkout.computeHorizontalScrollRange() / 1.71)) / 2;
                isFirstTime = false;
                // isFromRecycle=true;
            } else if (isFirstTime_Program || seekbar_Programs.getMax() == 0) {
                seekbar_Programs.setMax((int) (recycle_Programs.computeHorizontalScrollRange() / 1.71));
                seekbar_Programs.setProgress(((int) (recycle_Programs.computeHorizontalScrollRange() / 1.71)) / 2);
                initialProgress_Program = ((int) (recycle_Programs.computeHorizontalScrollRange() / 1.71)) / 2;
                isFirstTime_Program = false;
                // isFromRecycle=true;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter:
                if (!(FitsooPref.getWorkOutRes(getActivity()).contains("isLocked") && FitsooPref.getWorkOutRes(getActivity()).contains("isDelete"))) {
                    if (!FitsooUtils.isAccess(getActivity())) {

                        Intent i = new Intent(getActivity(), SubscribeActivity.class);
                        getActivity().startActivity(i);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("position", "" + 1);
                        FitsooUtils.ChangeFragment(false, bundle, new WorkOutFragment(), (AppCompatActivity) getActivity());
                    }
                }
                break;

            case R.id.btn_enter_challenge:
                loadChallenge();
                break;

            case R.id.btn_enter_Programs:
                ((MainActivity) getActivity()).updateTitle(getString(R.string.programs));
                Fragment challFragment = new ProgramsFragment();
                FragmentManager challFrag = getActivity().getSupportFragmentManager();
                challFrag.beginTransaction().replace(R.id.content_frame, challFragment, FitsooUtils.FRAGMENT_TRANSITION_TAG).commit();
                break;
        }
    }

    private void loadWorkOut() {
        String position = modelList.size() > 3 ? "3" : String.valueOf(modelList.size() - 1);
        if (modelList.size() > 0) {
            ((MainActivity) getActivity()).updateTitle(getString(R.string.nav_workout));
            Bundle bundle = new Bundle();
            bundle.putString("position", position);
            FitsooUtils.ChangeFragment(false, bundle, new WorkOutFragment(), (AppCompatActivity) getActivity());
        } else {
            FitsooUtils.showMessageAlert(workOutRes.getMessage(), getString(R.string.app_name), getActivity());
        }
    }

    private void loadChallenge() {
        ((MainActivity) getActivity()).updateTitle(getString(R.string.nav_challenge));
        FitsooUtils.ChangeFragment(true, null, new ChallengeFragment(), (AppCompatActivity) getActivity());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seekWorkOut)
            if (!isFromRecycle) {
                if (progress == 0) {
                    recycleWorkout.smoothScrollToPosition(progress);
                }
                recycleWorkout.scrollBy(progress - progressUpdateValue, 0);
                progressUpdateValue = progress;
            }

        if (seekBar == seekbar_Programs)
            if (!isFromRecycle_P) {
                if (progress == 0) {
                    recycle_Programs.smoothScrollToPosition(progress);
                }
                recycle_Programs.scrollBy(progress - progressUpdateValueP, 0);
                progressUpdateValueP = progress;
            }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public void getWorkoutData() {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(getActivity()));
            loginReques.put(FitsooConstant.REQ_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(getActivity(), loginReques.toString(), getActivity().getResources().getString(R.string.BASE_URL) + getString(R.string.workouts), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                processWorkoutAdapter(response);
            }
        }, getString(R.string.str_please_wait), "workouts");

    }

    private void processWorkoutAdapter(String response) {
        FitsooPref.setWorkoutRes(getActivity(), response);
        Log.d("LOGTEST", response);
        if (!response.contains("instructions") && !response.contains("workfocus_name") && !response.contains("worktype_name")) {
            getWorkoutData();
        } else {
            Log.d("STATUS_RES", "Response Test WorkoutAdapter>>>>  " + response);
            workOutRes = (new Gson()).fromJson(response, new TypeToken<BaseResponse<List<WorkoutModel>>>() {
            }.getType());
            if (FitsooUtils.hasValue(workOutRes.getService_call_time())) {
                FitsooUtils.SERVICE_CALL_TIME = Integer.parseInt(workOutRes.getService_call_time());
            }
            if (workOutRes.getSuccess() == 1) {
//            FitsooPref.setWorkoutRes(getActivity(), response);
                if (workOutRes.getData() != null)
                    modelList.addAll(workOutRes.getData());
                if (workOutRes.getProgramvideos() != null)
                    ProgramVideosList.addAll(workOutRes.getProgramvideos());
                if (workOutRes.getExtravideos() != null)
                    ExtraVideosList.addAll(workOutRes.getExtravideos());

                adapter.notifyDataSetChanged();
                adapter_Program.notifyDataSetChanged();
                recycleWorkout.scrollToPosition(2);
            } else {
                FitsooUtils.showMessageAlert(workOutRes.getMessage(), getString(R.string.app_name), getActivity());
            }

            FitsooUtils.checkUserStatus(getActivity());
            isFirstTime = true;
            isFirstTime_Program = true;
        }
    }

    @Override
    public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {

    }
}
