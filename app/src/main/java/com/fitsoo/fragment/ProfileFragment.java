package com.fitsoo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.fitsoo.R;
import com.fitsoo.activity.ChangePassword;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.adapter.ProfileWorkLogAdapter;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.DateSelectionDialog;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.model.ProfileWorkLogModel;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.model.ViewProfileResponse;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.DatePickerFragment;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.utils.LocationUtil;
import com.fitsoo.utils.MultipartUtility;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomEdittext;
import com.fitsoo.view.CustomTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fitsoo.activity.baseclass.BaseActivity.isopenAppForFistTimeForProfile;

/**
 * Created by system  on 21/7/17.
 */

public class ProfileFragment extends BaseFragment implements OnReceiveResponseListener, View.OnClickListener, DateSelectionDialog {

    private RecyclerView recycleWorkLog;
    private RecyclerView lv_programlog;
    private ProfileWorkLogAdapter adapter;
    private ProfileWorkLogAdapter adapter1;
    private List<ProfileWorkLogModel> workLogModels;
    private List<ProgramHomeModel> programModels;
    private NestedScrollView nested_scroll;
    private TextView txtProfileName;
    LinearLayout linearLanguage, ll_subtype, ll_exp_date;
    private TextView txtDob;
    private TextView txtNoLogAvailable;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;
    private CircularImageView imgProfile;
    private ImageView editprofile;
    private ImageView imgEdit;
    private Uri mCropImageUri;
    RadioButton rbtnEnglish;
    RadioButton rbtnHebrew;
    private RelativeLayout relParent;
    private Bitmap myBitmap;
    private ProgressDialog progressDialog;
    private EditText edtName;
    private String path;
    private boolean isEditable;
    private CustomTextView tvLanguage, tvChangePassword, txt_no_log_mesage_program;
    private boolean isGettingUpdated = false;
    private SwitchCompat switchCompat;
    private ProgressBar progProfile;
    private LocationUtil util;
    private CustomEdittext edt_selectLanguage;
    private RadioGroup radiogroup;


    private boolean isNameUpdated, isProfilePhotoUpdated;
    private String language;
    private TextView txt_subtype, txt_subtype1;
    private TextView txt_exp_date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FitsooUtils.fragName = getString(R.string.nav_profile);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_profile));
        ((MainActivity) getActivity()).BackVisible(true);
        util = LocationUtil.getInstance(getActivity());
        util.startGettingLocation();

        if (util.getLastKnownLocation() != null) {
            FitsooPref.saveLocation(util.getLastKnownLocation().getLatitude(), util.getLastKnownLocation().getLongitude(), getActivity());
        }
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isNameUpdated = false;
        isProfilePhotoUpdated = false;

        ((MainActivity) getActivity()).updateBottomSelection(R.id.action_more);

        txtNoLogAvailable = (TextView) view.findViewById(R.id.txt_no_log_mesage);
        progProfile = (ProgressBar) view.findViewById(R.id.progress_loading);
        progProfile.setVisibility(View.GONE);
        relParent = (RelativeLayout) view.findViewById(R.id.rel_parent);
        switchCompat = (SwitchCompat) view.findViewById(R.id.switch_notification);
        editprofile = (ImageView) view.findViewById(R.id.editprofile);
        imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        imgProfile = (CircularImageView) view.findViewById(R.id.img_profile);
        nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);
        txtProfileName = (TextView) view.findViewById(R.id.txtProfileName);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        txtDob = (TextView) view.findViewById(R.id.txt_dob);
        txt_subtype = (TextView) view.findViewById(R.id.txt_subtype);
        txt_subtype1 = (TextView) view.findViewById(R.id.txt_subtype1);
        txt_exp_date = (TextView) view.findViewById(R.id.txt_exp_date);
        tvLanguage = (CustomTextView) view.findViewById(R.id.tvLanguage);
        tvChangePassword = (CustomTextView) view.findViewById(R.id.tvChangePassword);
        txt_no_log_mesage_program = (CustomTextView) view.findViewById(R.id.txt_no_log_mesage_program);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        linearLanguage = (LinearLayout) view.findViewById(R.id.linearLanguage);
        rbtnHebrew = (RadioButton) view.findViewById(R.id.rbtnHebrew);
        rbtnEnglish = (RadioButton) view.findViewById(R.id.rbtnEnglish);
        ll_exp_date = view.findViewById(R.id.ll_exp_date);
        ll_subtype = view.findViewById(R.id.ll_subtype);

        radiogroup.setOnCheckedChangeListener(null);
        workLogModels = new ArrayList<>();
        programModels = new ArrayList<>();
        recycleWorkLog = (RecyclerView) view.findViewById(R.id.lv_workoutlog);
        lv_programlog = (RecyclerView) view.findViewById(R.id.lv_programlog);
        recycleWorkLog.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        lv_programlog.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        editprofile.setOnClickListener(this);
        txtDob.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        linearLanguage.setOnClickListener(this);
        relParent.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);


        switch (FitsooPref.getSubscribeType(getActivity())) {
//            case 0:
//                break;
            case 1:
                txt_subtype.setText(getActivity().getResources().getString(R.string.monthly));
                txt_subtype1.setText("(Auto renewal every Month)");

                break;
            case 2:
                txt_subtype.setText(getActivity().getResources().getString(R.string.yearly));
                txt_subtype1.setText("(Auto renewal every Year)");
                break;
            case 3:
                txt_subtype.setText(getActivity().getResources().getString(R.string.canceled));
                break;
            default:
                txt_subtype.setText(getActivity().getResources().getString(R.string.not_subscription));
                txt_exp_date.setText(getActivity().getResources().getString(R.string.not_subscription));
                break;
        }

        txt_exp_date.setText(FitsooPref.getValidTill(getActivity()));


        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    makeNameNonEditable();
                    return true;
                }
                return false;
            }
        });


        if (FitsooUtils.isInternetAvailable(getActivity())) {
            JSONObject object = new JSONObject();
            try {
                object.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(getActivity()));
                FitsooUtils.performRequest(getActivity(), object.toString(), getString(R.string.BASE_URL) + getString(R.string.viewdetail), this, getString(R.string.str_please_wait), "getprofile");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "getprofile":
                processProfileResponse(response);
                break;
        }
    }

    private void processProfileResponse(String response) {
        try {
            FitsooUtils.dismissProgressDialog(progressDialog);
            Log.d("Response-fitness111:", response);

            BaseResponse<ViewProfileResponse> profModel = (new Gson()).fromJson(response, new TypeToken<BaseResponse<ViewProfileResponse>>() {
            }.getType());

            if (profModel.getSuccess() == 1) {

                progProfile.setVisibility(View.VISIBLE);
                if (profModel.getData().getProfile_pic() != null && profModel.getData().getProfile_pic().trim().length() > 0) {
                    Glide.with(Objects.requireNonNull(getActivity())).load(profModel.getData().getProfile_pic()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progProfile.setVisibility(View.GONE);
                            imgProfile.setImageResource(R.drawable.ic_profile_avatar);
                            isProfilePhotoUpdated = true;
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progProfile.setVisibility(View.GONE);
                            isProfilePhotoUpdated = true;
                            return false;
                        }
                    }).into(imgProfile);
                   /* Glide.with(getActivity()).load(profModel.getData().getProfile_pic()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progProfile.setVisibility(View.GONE);
                            imgProfile.setImageResource(R.drawable.ic_profile_avatar);
                            isProfilePhotoUpdated = true;
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progProfile.setVisibility(View.GONE);
                            isProfilePhotoUpdated = true;
                            return false;
                        }
                    }).into(imgProfile);*/
                } else {
                    progProfile.setVisibility(View.GONE);
                    imgProfile.setImageResource(R.drawable.ic_profile_avatar);
                    isProfilePhotoUpdated = true;
                }

                switchCompat.setChecked((profModel.getData().getNotification_status().equalsIgnoreCase("0") || profModel.getData().getNotification_status().trim().length() <= 0) ? false : true);
                txtProfileName.setText(profModel.getData().getFirst_name() + " " + profModel.getData().getLast_name());
                edtName.setEnabled(false);
                edtName.setVisibility(View.GONE);
                if (profModel.getData().getDob().contains("0000"))
                    txtDob.setText("-");
                else
                    txtDob.setText(profModel.getData().getDob().replace("-", "/"));

                if (workLogModels.size() > 0) {
                    workLogModels.clear();
                }
                if (programModels.size() > 0) {
                    programModels.clear();
                }
                workLogModels.addAll(profModel.getData().getWorkout_log());
                programModels.addAll(profModel.getData().getProgramInformation());
                if (profModel.getData().getWorkout_log().size() <= 0) {
                    txtNoLogAvailable.setVisibility(View.VISIBLE);
                    recycleWorkLog.setVisibility(View.GONE);
                } else {
                    txtNoLogAvailable.setVisibility(View.GONE);
                    recycleWorkLog.setVisibility(View.VISIBLE);
                }
                if (profModel.getData().getProgramInformation().size() <= 0) {
                    txt_no_log_mesage_program.setVisibility(View.VISIBLE);
                    lv_programlog.setVisibility(View.GONE);
                } else {
                    txt_no_log_mesage_program.setVisibility(View.GONE);
                    lv_programlog.setVisibility(View.VISIBLE);
                }
                if (adapter == null)
                    adapter = new ProfileWorkLogAdapter(getActivity(), false, workLogModels, null);
                recycleWorkLog.setAdapter(adapter);
                recycleWorkLog.setNestedScrollingEnabled(false);

                if (adapter1 == null)
                    adapter1 = new ProfileWorkLogAdapter(getActivity(), true, null, programModels);
                lv_programlog.setAdapter(adapter1);
                lv_programlog.setNestedScrollingEnabled(false);

                nested_scroll.scrollTo(0, 0);
                isNameUpdated = true;

                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isNameUpdated && isProfilePhotoUpdated) {
                            performUpdate("SwitchCalled");
                        }
                    }
                });

                if (Integer.parseInt(profModel.getData().getLanguage()) == 0) {
                    tvLanguage.setText("Language: English");
                    language = "0";
                    radiogroup.check(R.id.rbtnEnglish);
                } else {
                    tvLanguage.setText("Language: Hebrew");
                    radiogroup.check(R.id.rbtnHebrew);
                    language = "1";
                }

                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rbtnEnglish) {
                            language = "0";
                        } else if (checkedId == R.id.rbtnHebrew) {
                            language = "1";
                        }
                        performUpdate("");
                    }
                });

//                if (FitsooPref.getisNewUser(getActivity()).equals("0")) {
//                    if (isopenAppForFistTimeForProfile==0) {
//                        FitsooUtils.showMessageAlert("Hello Users,\n" +
//                                "\n" +
//                                "we have an updates in our subscription packages as well as we have introduced few more attractive features so your subscription will get updated based on new subscription prices.\n" +
//                                "\n" +
//                                "From next renewal, new subscription package price will be applicable.\n" +
//                                "\n" +
//                                "For more information you can send us a message. Use the \"contact us\" button.\n" +
//                                "\n" +
//                                "Thank You.", getActivity().getString(R.string.app_name), getActivity());
//                        isopenAppForFistTimeForProfile=10;
//                    }
//                }
            }
            FitsooUtils.checkUserStatus(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void performProfileAction() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                }
            } else {
                CropImage.startPickImageActivity(getActivity());
            }
        } else {
            CropImage.startPickImageActivity(getActivity());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                Glide.with(getActivity()).asBitmap().load(result.getUri()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imgProfile.setImageBitmap(resource);
                        path = FitsooUtils.saveImage(resource, FitsooConstant.PROFILE_IMAGE_NAME);
                        myBitmap = resource;
                        if (isNameUpdated && isProfilePhotoUpdated) {
                            performUpdate("WhenProfileChanged");
                        }
                    }
                });
                /*Glide.with(getActivity()).load(result.getUri()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgProfile.setImageBitmap(resource);
                        path = FitsooUtils.saveImage(resource, FitsooConstant.PROFILE_IMAGE_NAME);
                        myBitmap = resource;
                        if (isNameUpdated && isProfilePhotoUpdated) {
                            performUpdate("WhenProfileChanged");
                        }
                    }
                });*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(10, 10)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangePassword:
                if (FitsooUtils.isAccess(getActivity())) {
                    Intent passToChangePassword = new Intent(getActivity(), ChangePassword.class);
                    startActivity(passToChangePassword);
                }
                break;
            case R.id.editprofile:
                if (FitsooUtils.isAccess(getActivity())) {
                    performProfileAction();
                }
                break;

            case R.id.txt_dob:
                if (FitsooUtils.isAccess(getActivity()))
                    performDateSelection();
                break;

            case R.id.imgEdit:
                if (FitsooUtils.isAccess(getActivity())) {
                    isEditable = !isEditable;
                    edtName.setText(txtProfileName.getText().toString().trim());
                    performNameEdit();
                }
                break;

            case R.id.rel_parent:
                makeNameNonEditable();
                break;
            case R.id.linearLanguage:
                if (FitsooUtils.isAccess(getActivity())) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_change_language, null, false);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    edt_selectLanguage = view.findViewById(R.id.edt_selectLanguage);
                    CustomButton btn_Save = view.findViewById(R.id.btn_Save);
                    ImageView imageView2 = view.findViewById(R.id.imageView2);
                    edt_selectLanguage.setText(tvLanguage.getText().toString().contains("English") ? getString(R.string.english) : getString(R.string.hebrew));
                    edt_selectLanguage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectLanguage();
                        }
                    });
                    btn_Save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            performUpdate("");
                            dialog.dismiss();
                        }
                    });
                    imageView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }

    private void selectLanguage() {
        final List<String> list = new ArrayList<>();
        list.add(getString(R.string.english));
        list.add(getString(R.string.hebrew));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_language, null, false);
        ListView listview_language = view.findViewById(R.id.listview_language);
        listview_language.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_list, list));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        listview_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvLanguage.setText(getString(R.string.language) + list.get(position));
                edt_selectLanguage.setText(getString(R.string.language) + list.get(position));
                dialog.dismiss();
            }
        });
    }

    private void makeNameNonEditable() {
        if (edtName.getText().toString().trim().length() > 0) {
            FitsooUtils.hideSoftKeyboard(getActivity());
            isEditable = false;
            txtProfileName.setText(edtName.getText().toString().trim());
            edtName.getText().clear();
            performNameEdit();
        }
    }

    private void performNameEdit() {
        txtProfileName.setVisibility(isEditable ? View.INVISIBLE : View.VISIBLE);
        imgEdit.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        edtName.setEnabled(isEditable ? true : false);
        edtName.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        if (!isEditable) {
            if (isNameUpdated && isProfilePhotoUpdated) {
                performUpdate("NameEdit");
            }
        }
    }

    private void performDateSelection() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateListener(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    private void performUpdate(String updateCalled) {
        isGettingUpdated = false;
        if (path == null || path.length() <= 0) {
            if (myBitmap == null) {
                if (imgProfile.getDrawable().getCurrent() instanceof BitmapDrawable) {
                    myBitmap = ((BitmapDrawable) imgProfile.getDrawable().getCurrent()).getBitmap();
                } else {
                    Drawable drawable = imgProfile.getDrawable().getCurrent();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        drawable = (DrawableCompat.wrap(drawable)).mutate();
                    }

                    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                    myBitmap = bitmap;
                }
            }
            path = FitsooUtils.saveImage(myBitmap != null ? myBitmap : BitmapFactory.decodeResource(getResources(), R.mipmap.profile_avtar), FitsooConstant.PROFILE_IMAGE_NAME);
        }
        updateProfile(path.length() > 0 ? new File(path) : null);
    }


    private void updateProfile(final File profilePic) {
        final AsyncTask<Void, String, String> waitForCompletion = new AsyncTask<Void, String, String>() {

            private String email = "";
            private String fName = "";
            private String lName = "";
            private String isCheck = "";
            private String dob = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                BaseResponse<ProfileReponseModel> profileModel = (new Gson()).fromJson(FitsooPref.getUserProfileInfo(getActivity()), new TypeToken<BaseResponse<ProfileReponseModel>>() {
                }.getType());
                email = profileModel.getData().getEmail();
                progressDialog = FitsooUtils.showProgressDialog(getActivity(), getString(R.string.str_please_wait));
                String[] namestr = txtProfileName.getText().toString().trim().split(" ");
                fName = namestr[0];
                for (int x = 1; x < namestr.length; x++) {
                    lName = lName + " " + namestr[x];
                }
                isCheck = switchCompat.isChecked() ? "1" : "0";
                dob = txtDob.getText().toString().trim();
            }

            @Override
            public synchronized String doInBackground(Void... params) {
                String res = "";
                if (isAdded()) {
                    String charset = "UTF-8";
                    String requestURL = getString(R.string.BASE_URL) + getString(R.string.editprofile);
                    try {
                        MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                        multipart.addFormField("email", email);
                        multipart.addFormField("first_name", fName.trim());
                        multipart.addFormField("last_name", lName.trim());
                        multipart.addFormField(FitsooConstant.REQ_LATITUDE, FitsooPref.getLatLong(getActivity())[0]);
                        multipart.addFormField(FitsooConstant.REQ_LONGITUDE, FitsooPref.getLatLong(getActivity())[1]);
                        multipart.addFormField("id", FitsooPref.getUserId(getActivity()) + "");
                        multipart.addFormField("language", language);
                        multipart.addFormField("dob", dob);
                        multipart.addFormField("notification_status", isCheck);
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            if (profilePic != null) {
                                multipart.addFilePart("profile_pic", profilePic);
                            } else {
                                multipart.addFormField("profile_pic", "");
                            }
                        } else {
                            multipart.addFormField("profile_pic", "");
                        }

                        List<String> response = multipart.finish();
                        for (String line : response) {
//                            System.out.println(line);
                            res = line;
                        }
                    } catch (Exception ex) {
                        FitsooUtils.dismissProgressDialog(progressDialog);
                        System.err.println(ex);
                    }
                }
                return res;
            }


            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(String result) {
                FitsooUtils.dismissProgressDialog(progressDialog);
                BaseResponse<ProfileReponseModel> resModel = (new Gson()).fromJson(result, new TypeToken<BaseResponse<ProfileReponseModel>>() {
                }.getType());
                if (resModel != null) {
                    if (resModel.getSuccess() == 1) {
                        FitsooPref.setUserProfileInfo(getActivity(), result);
                    }
                    FitsooUtils.showMessageAlert(resModel.getMessage(), getString(R.string.app_name), getActivity());
                    isGettingUpdated = false;
                    if (language.equals("0"))
                        tvLanguage.setText("Language: English");
                    else
                        tvLanguage.setText("Language: Hebrew");
                }
            }
        };

        if (FitsooUtils.isInternetAvailable(getActivity())) {
            waitForCompletion.execute();
        } else {
            //TODO show internet not available
        }
    }


    @Override
    public void onDateSelected(DatePicker view, int year, int month, int day) {
        txtDob.setText(month + 1 + "/" + day + "/" + year);
        if (!isGettingUpdated) {
            if (isNameUpdated && isProfilePhotoUpdated) {
                performUpdate("Date");
            }
        }
    }
}
