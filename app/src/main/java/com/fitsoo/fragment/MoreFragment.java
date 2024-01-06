package com.fitsoo.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fitsoo.R;
import com.fitsoo.activity.MainActivity;
import com.fitsoo.activity.SubscribeActivity;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    RelativeLayout relativeContactUs;
    RelativeLayout relativeMyProfile;
    RelativeLayout relativeLogout;
    RelativeLayout relativeFacebookMore;
    RelativeLayout relativeSubscriptionMore;

//    public static Boolean IsFromMoreFragment = false;
    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        IsFromMoreFragment =true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).updateTitle(getString(R.string.more));
        ((MainActivity) getActivity()).BackVisible(false);
        init(view);
    }

    private void init(View view) {
        relativeLogout = view.findViewById(R.id.relativeLogout);
        relativeMyProfile = view.findViewById(R.id.relativeMyProfile);
        relativeContactUs = view.findViewById(R.id.relativeContactUs);
        relativeFacebookMore = view.findViewById(R.id.relativeFacebookMore);
        relativeSubscriptionMore = view.findViewById(R.id.relativeSubscriptionMore);

        relativeLogout.setOnClickListener(this);
        relativeMyProfile.setOnClickListener(this);
        relativeContactUs.setOnClickListener(this);
        relativeFacebookMore.setOnClickListener(this);
        relativeSubscriptionMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeMyProfile:
                ((MainActivity) getActivity()).updateTitle(getString(R.string.nav_profile));
                FitsooUtils.ChangeFragment(true, null, new ProfileFragment(), (AppCompatActivity) getActivity());
                break;
            case R.id.relativeLogout:
                ((MainActivity) getActivity()).performLogOut();
                break;

            case R.id.relativeSubscriptionMore:
                if(FitsooPref.getpackage_bought_from(getActivity()).equals("Android")|| FitsooPref.getpackage_bought_from(getActivity()).equals("")){
                    Intent i = new Intent(getActivity(), SubscribeActivity.class);
                    i.putExtra("from",2);
                    startActivity(i);
                }else {
                    FitsooUtils.showMessageAlert("You can not upgrade/downgrade your existing package from here as you bought your package from "+FitsooPref.getpackage_bought_from(getActivity()) +"\n You can upgrade / downgrade package from the same device in which you bought your existing package", getString(R.string.app_name), getActivity());

                }
                break;
            case R.id.relativeContactUs:
                FitsooUtils.ChangeFragment(true, null, new ContactUsFragment(), (AppCompatActivity) getActivity());
                break;
            case R.id.relativeFacebookMore:
                String url = "https://www.facebook.com/groups/Fitmamachallenge3/";
               Intent  i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }
}
