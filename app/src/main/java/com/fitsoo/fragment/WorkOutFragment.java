package com.fitsoo.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.adapter.HomeAdapter;
import com.fitsoo.adapter.WorkoutPagerAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.Extravideo;
import com.fitsoo.model.WorkoutModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by system  on 21/7/17.
 */

public class WorkOutFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private List<WorkoutModel> workoutModels;
    private List<Extravideo> ExtraVideosList;
    private WorkoutModel currModel;
    private TextView txtTrainingType;
    private TextView txtPresenterName;
    private TextView txtWorkoutFocus;
    private TextView txtInstruction;
    private TextView txtYesterdayDay;
    private TextView txtTomorrowDay;
    private TextView txtToday;
    private int position;
    private SeekBar seekBarWorkout;
    private boolean isSeekDragged = false;
    private ViewPager workOutPager;
    RecyclerView recycle_Extras;
    private PagerAdapter adapter;
    private HomeAdapter adapterExtra;
    private BaseResponse<List<WorkoutModel>> workoutBaseRes;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FitsooUtils.fragName = getString(R.string.nav_workout);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_workout));
        ((MainActivity) getActivity()).updateTitle(getString(R.string.workout));
        ((MainActivity) getActivity()).updateBottomSelection(R.id.action_Workout);
        return inflater.inflate(R.layout.workout_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).updateTitle(getString(R.string.nav_workout));
        ((MainActivity) getActivity()).BackVisible(false);
        //assert getArguments() != null;
        //position = Integer.parseInt(getArguments().getString("position", String.valueOf(FitsooUtils.vidPosition)));
        // FitsooUtils.vidPosition = position;
        workoutModels = new ArrayList<>();
        ExtraVideosList = new ArrayList<>();

        workOutPager = (ViewPager) view.findViewById(R.id.workout_pager);
        seekBarWorkout = (SeekBar) view.findViewById(R.id.seekbar_workout);
        txtToday = (TextView) view.findViewById(R.id.txtCurrDay);
        txtYesterdayDay = (TextView) view.findViewById(R.id.txtYesterdayDay);
        txtTomorrowDay = (TextView) view.findViewById(R.id.txtTomorrowDay);
        txtInstruction = (TextView) view.findViewById(R.id.txt_instruction);
        txtWorkoutFocus = (TextView) view.findViewById(R.id.txtWorkoutFocusName);
        txtPresenterName = (TextView) view.findViewById(R.id.txtPresenterName);
        txtTrainingType = (TextView) view.findViewById(R.id.txtCircuitTraining);
        recycle_Extras = (RecyclerView) view.findViewById(R.id.recycle_Extras);

        seekBarWorkout.setMax(6);


        if (!FitsooPref.getWorkOutRes(getActivity()).contains("instructions") && !FitsooPref.getWorkOutRes(getActivity()).contains("workfocus_name") && !FitsooPref.getWorkOutRes(getActivity()).contains("worktype_name")) {
            getWorkoutData();
        } else {
            mainScreenActivityOnObtainingData();
        }
    }

    private void mainScreenActivityOnObtainingData() {
        workoutBaseRes = (new Gson()).fromJson(FitsooPref.getWorkOutRes(getActivity()), new TypeToken<BaseResponse<List<WorkoutModel>>>() {
        }.getType());
        Log.d("WorkOutRes>>>", "Frag >>> " + FitsooPref.getWorkOutRes(getActivity()));
        if (workoutModels.size() > 0) {
            workoutModels.clear();
        }
        if (ExtraVideosList.size() > 0) {
            ExtraVideosList.clear();
        }

        if (workoutBaseRes != null) {
            workoutModels.addAll(workoutBaseRes.getData());
            ExtraVideosList.addAll(workoutBaseRes.getExtravideos());
        }

        adapter = new WorkoutPagerAdapter(getActivity(), workoutModels);
        adapterExtra = new HomeAdapter(getActivity(), ExtraVideosList, null, 3);
        workOutPager.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recycle_Extras.setLayoutManager(gridLayoutManager);
        recycle_Extras.setAdapter(adapterExtra);

        if (workoutBaseRes.getData().size() > 0 && workoutBaseRes.getData().size() - 1 >= position) {
            position = 3;
            workOutPager.setCurrentItem(position);
            seekBarWorkout.setProgress(position);
            seekBarDrag();
            currModel = workoutModels.get(position);
            setDataForView();
        } else {
            if (workoutBaseRes.getData().size() > 0) {
                position = workoutBaseRes.getData().size() - 1;
                workOutPager.setCurrentItem(position);
            }
        }
        //workOutPager.setCurrentItem(3);
        txtYesterdayDay.setOnClickListener(this);
        txtTomorrowDay.setOnClickListener(this);

        seekBarWorkout.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isSeekDragged) {
                    position = progress;
                    seekBarDrag();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekDragged = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isSeekDragged) {
                    position = seekBar.getProgress();
                    seekBarDrag();
                }
                isSeekDragged = false;
            }
        });
        workOutPager.addOnPageChangeListener(this);
        FitsooUtils.checkUserStatus(getActivity());
    }

    public void getWorkoutData() {

        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(getActivity()));
            loginReques.put(FitsooConstant.REQ_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(getActivity(), loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.workouts), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                if (!response.contains("instructions") && !response.contains("workfocus_name") && !response.contains("worktype_name")) {
                    getWorkoutData();
                } else {
                    FitsooPref.setWorkoutRes(getActivity(), response);
                    mainScreenActivityOnObtainingData();
                }
            }
        }, getString(R.string.str_please_wait), "workouts");

    }


    private void setDataForView() {
        if (workoutBaseRes.getData().size() > 0) {
            txtTrainingType.setText(currModel.getWorktype_name());
            txtPresenterName.setText(currModel.getInstructor_name());
            txtWorkoutFocus.setText(currModel.getWorkfocus_name());
            txtInstruction.setText(currModel.getInstructions());

            setTopData();
        }
    }


    private void setTopData() {

        switch (position) {
            case 0:
                txtYesterdayDay.setText("");
                txtToday.setText(FitsooUtils.getDate(-3));
                txtTomorrowDay.setText(FitsooUtils.getDate(-2));
                break;

            case 1:
                txtYesterdayDay.setText(FitsooUtils.getDate(-3));
                txtToday.setText(FitsooUtils.getDate(-2));
                txtTomorrowDay.setText("Yesterday");
                break;

            case 2:
                txtYesterdayDay.setText(FitsooUtils.getDate(-2));
                txtToday.setText("Yesterday");
                txtTomorrowDay.setText("Today");
                break;

            case 3:
                txtYesterdayDay.setText("Yesterday");
                txtToday.setText("Today");
                txtTomorrowDay.setText("Tomorrow");
                break;

            case 4:
                txtYesterdayDay.setText("Today");
                txtToday.setText("Tomorrow");
                txtTomorrowDay.setText(FitsooUtils.getDate(2));
                break;

            case 5:
                txtYesterdayDay.setText("Tomorrow");
                txtToday.setText(FitsooUtils.getDate(2));
                txtTomorrowDay.setText(FitsooUtils.getDate(3));
                break;

            case 6:
                txtYesterdayDay.setText(FitsooUtils.getDate(2));
                txtToday.setText(FitsooUtils.getDate(3));
                txtTomorrowDay.setText("");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtYesterdayDay:
                addyesterdayLogic();
                break;

            case R.id.txtTomorrowDay:
                addTomorrowLogic();
                break;
        }
    }

    private void addTomorrowLogic() {
        if (workoutBaseRes.getData().size() > 0) {
            if (workoutModels.size() - 1 > position) {
                position++;
                currModel = workoutModels.get(position);
                seekBarWorkout.setProgress(position);
            }
            workOutPager.setCurrentItem(workOutPager.getCurrentItem() + 1);
            setDataForView();
        }
    }

    private void seekBarDrag() {
        if (workoutBaseRes.getData().size() > 0) {
            if (workoutModels.size() - 1 > position) {
                currModel = workoutModels.get(position);
            }
            workOutPager.setCurrentItem(seekBarWorkout.getProgress());
            setDataForView();
        }
    }

    private void addyesterdayLogic() {
        if (workoutBaseRes.getData().size() > 0) {
            if (position > 0) {
                position--;
                currModel = workoutModels.get(position);
                seekBarWorkout.setProgress(position);
            }
            workOutPager.setCurrentItem(workOutPager.getCurrentItem() - 1);
            setDataForView();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (workoutBaseRes.getData().size() > 0) {
            currModel = workoutModels.get(i);
            position = i;
            seekBarWorkout.setProgress(i);
            setDataForView();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
