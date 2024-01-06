package com.fitsoo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.fragment.baseclass.BaseFragment;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProfileReponseModel;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.fitsoo.view.CustomButton;
import com.fitsoo.view.CustomTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by system  on 19/7/17.
 */

public class ContactUsFragment extends BaseFragment implements OnReceiveResponseListener{

    private CustomTextView txtName;
    private EditText edtSubject;
    private EditText edtmessage;
    private CustomButton btnSendMessage;
    private BaseResponse<ProfileReponseModel> resModel;
    private int status = 0;
    private CustomTextView txtwebsite;
    private CustomTextView txtEmail;
    private RelativeLayout relParent;
    AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_us_fragment, container, false);
        FitsooUtils.fragName = getString(R.string.nav_contact);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).createLogInAnalytics(getString(R.string.nav_contact));
        ((MainActivity) getActivity()).updateTitle(getString(R.string.nav_contact));
        ((MainActivity) getActivity()).BackVisible(true);
        resModel = (new Gson()).fromJson(FitsooPref.getUserProfileInfo(getActivity()), new TypeToken<BaseResponse<ProfileReponseModel>>() {
        }.getType());
        relParent = (RelativeLayout) view.findViewById(R.id.rel_parent);
        edtSubject = (EditText) view.findViewById(R.id.edt_subject);
        txtEmail = (CustomTextView) view.findViewById(R.id.txt_email);
        txtwebsite = (CustomTextView) view.findViewById(R.id.txtwebsite);
        btnSendMessage = (CustomButton) view.findViewById(R.id.btn_send_message);
        edtmessage = (EditText) view.findViewById(R.id.edt_message);
        txtName = (CustomTextView) view.findViewById(R.id.txt_name);
        txtName.setText(resModel.getData().getFirst_name() + " " + resModel.getData().getLast_name());
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllFieldsValid()) {
                    doSendMessage();
                }
            }
        });

        relParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitsooUtils.hideSoftKeyboard(getActivity());
            }
        });

        txtwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri urlUrl = Uri.parse(txtwebsite.getText().toString().trim());
                if (!txtwebsite.getText().toString().trim().startsWith("http://") && !txtwebsite.getText().toString().trim().startsWith("https://")) {
                    urlUrl = Uri.parse("http://" + txtwebsite.getText().toString().trim());
                }
                Intent i = new Intent(Intent.ACTION_VIEW, urlUrl);
                startActivity(i);
            }
        });
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+txtEmail.getText().toString().trim()));
                intent.putExtra("subject", "");
                intent.putExtra("body", "");
                startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.putExtra(Intent.EXTRA_EMAIL, txtEmail.getText().toString().trim());
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                intent.putExtra(Intent.EXTRA_TEXT, "");
//                intent.setType("text/plain");
//                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        FitsooUtils.checkUserStatus(getActivity());
    }

    private boolean isAllFieldsValid() {
        if (!FitsooUtils.hasValue(edtSubject.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_enter_subject), getString(R.string.app_name), getActivity());
            return false;
        } else if (!FitsooUtils.hasValue(edtmessage.getText().toString().trim())) {
            FitsooUtils.showMessageAlert(getString(R.string.str_enter_message), getString(R.string.app_name), getActivity());
            return false;
        }
        return true;
    }


    private void doSendMessage() {

        FitsooUtils.hideSoftKeyboard(getActivity());
        JSONObject messageReq = new JSONObject();
        try {
            messageReq.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(getActivity()));
            messageReq.put("subject", edtSubject.getText().toString().trim());
            messageReq.put("message", edtmessage.getText().toString().trim());
            messageReq.put("email", resModel.getData().getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(getActivity(), messageReq.toString(), getString(R.string.BASE_URL) + getString(R.string.contactus), this, getString(R.string.str_please_wait), "contactus");
    }

    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "contactus":
                processResponse(response);
                break;
        }
    }

    private void processResponse(String response) {
        try {
            BaseResponse res = (new Gson()).fromJson(response, new TypeToken<BaseResponse>() {
            }.getType());
            status = res.getSuccess();
            if (status == 1) {
                showSuccessDialog();
            } else {
                FitsooUtils.showMessageAlert(res.getMessage(), getString(R.string.app_name), getActivity());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showSuccessDialog() {

        View view = View.inflate(getActivity(), R.layout.dialog_success_contact, null);
        ImageView img = (ImageView) view.findViewById(R.id.imgcross);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtmessage.getText().clear();
                edtSubject.getText().clear();
                dialog.dismiss();
            }
        });
        AlertDialog.Builder redeemDialog = new AlertDialog.Builder(getActivity(), R.style.FullScreenDialog);
        redeemDialog.setView(view);
        dialog = redeemDialog.create();
        dialog.show();
    }

}
