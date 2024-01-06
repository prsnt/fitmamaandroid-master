package com.fitsoo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.mediarouter.app.MediaRouteButton;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.fitsoo.R;
import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.preference.FitsooPref;
import com.fitsoo.utils.FitsooUtils;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by system  on 1/8/17.
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener, OnReceiveResponseListener, SeekBar.OnSeekBarChangeListener {

    private RelativeLayout relVideoTopContent;
    private VideoView fitsooVideoview;
    private ImageView imgPlay;
    private ImageView backwardVideo;
    private ImageView forwardVideo;
    private SeekBar progressBar;
    private Runnable runnable;
    private TextView txtProgressValue;
    private TextView txtmaxValue;
    private String urlToPass;
    private String videoId;
    private String vprogress;
    private String thumb;
    private String title;
    private int length;
    private ImageView imgVolume;
    private ImageView imgSmallBack;
    private CountDownTimer countDownTimer;
    private int interval = 0;
    //    private Toolbar toolbar;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mediaRouteMenuItem;
    private RemoteMediaClient remoteMediaClient;
    private boolean isUpdateFromCasting;
    private long prgressOnUpdate = 0;
    private boolean isSeekTracking = false;
    private AudioManager audioManager;
    private boolean isBackCalled;
    ImageView imgBack;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MediaRouteButton mediaRouteButton;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean isProgram;
    private String pid = "";

//    private VerticalSeekBar volumeSeek;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        setContentView(R.layout.video_activity);
        mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mediaRouteButton);

        isBackCalled = false;
        urlToPass = getIntent().getExtras().getString("vurl", "");
        videoId = getIntent().getExtras().getString("vid", "");
        vprogress = getIntent().getExtras().getString("progress", "0");
        thumb = getIntent().getExtras().getString("thumb", "");
        title = getIntent().getExtras().getString("title", "");
        if (getIntent().getExtras().containsKey("programid")) {
            isProgram = true;
            pid = getIntent().getExtras().getString("programid", "");
        } else
            pid = "0";
        isProgram = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (vprogress.trim().length() < 1) {
            vprogress = "0";
        }

        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        //mCastContext.registerLifecycleCallbacksBeforeIceCreamSandwich(this, savedInstanceState);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        if (mCastSession != null && mCastContext.getSessionManager() != null
                && mCastContext.getSessionManager().getCurrentCastSession() != null
                && mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient() != null) {
            mCastContext.getSessionManager().getCurrentCastSession().getRemoteMediaClient().load(buildMediaInfo(), false, Integer.parseInt(vprogress.trim()));
        }

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);

//        volumeSeek = (VerticalSeekBar) findViewById(R.id.volume_seekbar);
        imgSmallBack = (ImageView) findViewById(R.id.img_small_screen);
        imgVolume = (ImageView) findViewById(R.id.img_volume);
        txtProgressValue = (TextView) findViewById(R.id.txt_video_progress_value);
        txtmaxValue = (TextView) findViewById(R.id.txt_video_max_value);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        progressBar = (SeekBar) findViewById(R.id.progress_workout_adapter);
        relVideoTopContent = (RelativeLayout) findViewById(R.id.rel_contents);
        fitsooVideoview = (VideoView) findViewById(R.id.fitsooVideoview);
        imgPlay = (ImageView) findViewById(R.id.img_play_video);
        backwardVideo = (ImageView) findViewById(R.id.img_back_video);
        forwardVideo = (ImageView) findViewById(R.id.img_forward_video);

        Uri uri = Uri.parse(urlToPass.trim());
        Log.d("VideoActivity", "URI: " + uri.toString());
//        fitsooVideoview.setVideoURI(Uri.parse("https://dr7n943fq3qef.cloudfront.net/158647144121723.mp4"));
        fitsooVideoview.setVideoURI(uri);

        imgSmallBack.setOnClickListener(this);
        imgVolume.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        relVideoTopContent.setOnClickListener(this);
        backwardVideo.setOnClickListener(this);
        forwardVideo.setOnClickListener(this);

        if (FitsooUtils.hasValue(vprogress.trim())) {
            fitsooVideoview.seekTo(Integer.parseInt(vprogress));
        }

        fitsooVideoview.setVisibility(View.VISIBLE);
        imgPlay.setImageResource(R.mipmap.pause);

        fitsooVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onPrepared(MediaPlayer mp) {
                fitsooVideoview.setBackground(null);
                if (!isCastingConnected()) {
                    fitsooVideoview.start();
                }
                isUpdateFromCasting = true;
                progressBar.setMax(mp.getDuration());
                txtmaxValue.setText(getMinutes(mp.getDuration()) + ":" + getSeconds(mp.getDuration()));
                countDownTimer = new CountDownTimer(mp.getDuration() * 2, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if ((mCastSession == null) || !mCastSession.isConnected() && !mCastSession.isConnecting()) {
                            progressBar.setProgress(fitsooVideoview.getCurrentPosition());
                            txtProgressValue.setText(getMinutes(fitsooVideoview.getCurrentPosition()) + ":" + getSeconds(fitsooVideoview.getCurrentPosition()));
                            if (!isSeekTracking) {
                                interval = interval + 1;
                                if (interval >= 6) {
                                    hideAllView();
                                }
                            } else {
                                interval = 0;
                            }
                        } else {
                            if (remoteMediaClient != null) {
                                progressBar.setProgress((int) remoteMediaClient.getApproximateStreamPosition());
                                txtProgressValue.setText(getMinutes(remoteMediaClient.getApproximateStreamPosition()) + ":" + getSeconds(remoteMediaClient.getApproximateStreamPosition()));
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        countDownTimer.start();
                    }
                }.start();
            }
        });
        progressBar.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onPause() {
        mCastContext.getSessionManager().removeSessionManagerListener(mSessionManagerListener, CastSession.class);
        if (!pid.equals("-1"))
            updateVideoTime();
        FitsooUtils.dismissProgressDialog(FitsooUtils.progressDialog);
        fitsooVideoview.stopPlayback();
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                if (!pid.equals("-1"))
                    updateVideoTime();
                else
                    onBackPressed();
                break;
            case R.id.img_volume:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                break;

            case R.id.img_back_video:
                interval = 0;
                if (!isCastingConnected()) {
                    fitsooVideoview.seekTo(fitsooVideoview.getCurrentPosition() - ((int) TimeUnit.SECONDS.toMillis(30)));
                } else {
                    remoteMediaClient.seek(remoteMediaClient.getApproximateStreamPosition() - ((int) TimeUnit.SECONDS.toMillis(30)));
                }
                break;

            case R.id.img_forward_video:
                interval = 0;
                if (!isCastingConnected()) {
                    fitsooVideoview.seekTo(fitsooVideoview.getCurrentPosition() + ((int) TimeUnit.SECONDS.toMillis(30)));
                } else {
                    remoteMediaClient.seek(remoteMediaClient.getApproximateStreamPosition() + ((int) TimeUnit.SECONDS.toMillis(30)));
                }
                break;

            case R.id.img_small_screen:
                interval = 0;
                if (!pid.equals("-1"))
                    updateVideoTime();
                break;

            case R.id.img_play_video:
                interval = 0;
                if (!isCastingConnected()) {
                    imgPlay.setImageResource(!fitsooVideoview.isPlaying() ? R.mipmap.pause : R.mipmap.play_triangular_large);
                    if (!fitsooVideoview.isPlaying()) {
                        fitsooVideoview.start();
                        fitsooVideoview.setVisibility(View.VISIBLE);
                    } else {
                        fitsooVideoview.pause();
                    }
                } else {
                    if (isCastingConnected()) {
                        remoteMediaClient = mCastSession.getRemoteMediaClient();
                    }
                    if (remoteMediaClient != null) {
                        imgPlay.setImageResource(!remoteMediaClient.isPlaying() ? R.mipmap.pause : R.mipmap.play_triangular_large);
                        if (!remoteMediaClient.isPlaying()) {
                            remoteMediaClient.play();
                        } else {
                            remoteMediaClient.pause();
                        }
                    }
                }
                break;
            case R.id.rel_contents:
                interval = 0;
                showAllView();
                break;
        }
    }

    private boolean isCastingConnected() {
        if ((mCastSession == null) || !mCastSession.isConnected() && !mCastSession.isConnecting()) {
            return false;
        }
        return true;
    }


    @Override
    public void onResponseReceived(String requestType, String response) {
        switch (requestType) {
            case "workoutlog":
                getWorkoutData();
                break;

            case "workouts":
                FitsooPref.setWorkoutRes(VideoActivity.this, response);
                passToMainScreen();
                break;

            case "programvideoslog":
                onBackPressed();
        }
    }

    private void hideAllView() {
        imgSmallBack.setVisibility(View.GONE);
        txtProgressValue.setVisibility(View.GONE);
        txtmaxValue.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        imgPlay.setVisibility(View.GONE);
        backwardVideo.setVisibility(View.GONE);
        forwardVideo.setVisibility(View.GONE);
        imgVolume.setVisibility(View.GONE);
    }

    private void showAllView() {
        imgVolume.setVisibility(View.VISIBLE);
        imgSmallBack.setVisibility(View.VISIBLE);
        txtProgressValue.setVisibility(View.VISIBLE);
        txtmaxValue.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        imgPlay.setVisibility(View.VISIBLE);
        backwardVideo.setVisibility(View.VISIBLE);
        forwardVideo.setVisibility(View.VISIBLE);
    }

    public void updateVideoTime() {
        if (!isBackCalled) {
            isBackCalled = true;
            JSONObject object = new JSONObject();
            length = fitsooVideoview.getCurrentPosition();

            double progresPercent = ((fitsooVideoview.getCurrentPosition() * 100) / fitsooVideoview.getDuration());
            if (pid.equals("0"))
                try {
                    object.put("user_id", FitsooPref.getUserId(VideoActivity.this));
                    object.put("video_id", videoId);
                    object.put("time_log", fitsooVideoview.getCurrentPosition());
                    object.put("progress_percentage", progresPercent);
                    object.put("progress_type", progresPercent < 30 ? "Lowest" : progresPercent > 80 ? "Highest" : "Normal");
                    FitsooUtils.performRequest(VideoActivity.this, object.toString(), getString(R.string.BASE_URL) + getString(R.string.workoutlog), this, getString(R.string.str_saving_your_progress), "workoutlog");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            else if (!pid.equals("-1")) {
                try {
                    object.put("user_id", FitsooPref.getUserId(VideoActivity.this));
                    object.put("program_video_id", videoId);
                    object.put("time_log", fitsooVideoview.getCurrentPosition());
                    object.put("progress_percentage", progresPercent);
                    object.put("progress_type", progresPercent < 30 ? "Lowest" : progresPercent > 80 ? "Highest" : "Normal");
                    object.put("program_id", pid);
                    FitsooUtils.performRequest(VideoActivity.this, object.toString(), getString(R.string.BASE_URL) + getString(R.string.programvideoslog), this, getString(R.string.str_saving_your_progress), "programvideoslog");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.castmenu, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);

        return true;
    }*/

    @Override
    protected void onResume() {
        mCastContext.getSessionManager().addSessionManagerListener(mSessionManagerListener, CastSession.class);
        if (isCastingConnected() && remoteMediaClient == null && mCastSession.getRemoteMediaClient() != null) {
            remoteMediaClient = mCastSession.getRemoteMediaClient();
            remoteMediaClient.load(buildMediaInfo(), false, Integer.parseInt(vprogress));
            remoteMediaClient.play();
            imgPlay.performClick();
        }
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {

            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                fitsooVideoview.pause();
                loadRemoteMedia(progressBar.getProgress(), true);
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer.start();
                }
                fitsooVideoview.resume();
                invalidateOptionsMenu();
            }
        };
    }


    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
            }

            @Override
            public void onQueueStatusUpdated() {
            }

            @Override
            public void onPreloadStatusUpdated() {
            }

            @Override
            public void onSendingRemoteMediaRequest() {
            }

            @Override
            public void onAdBreakStatusUpdated() {

            }
        });
        remoteMediaClient.load(buildMediaInfo(), autoPlay, position);
        remoteMediaClient.addProgressListener(new RemoteMediaClient.ProgressListener() {
            @Override
            public void onProgressUpdated(final long l, long l1) {
                isUpdateFromCasting = true;
                vprogress = l + "";
                if (prgressOnUpdate < l) {
                    prgressOnUpdate = l;
                }
            }
        }, 1000);
    }


    private MediaInfo buildMediaInfo() {
        try {
            MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, title);
            movieMetadata.putString(MediaMetadata.KEY_TITLE, title);
            movieMetadata.addImage(new WebImage(Uri.parse(thumb)));

            int duration = 0;
            if (fitsooVideoview != null && fitsooVideoview.getDuration() > 0) {
                duration = fitsooVideoview.getDuration();
            }

            return new MediaInfo.Builder(urlToPass)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(movieMetadata)
                    .setStreamDuration(duration * 1000)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void passToMainScreen() {
        Intent passToMain = new Intent(VideoActivity.this, MainActivity.class);
        passToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(passToMain);
        VideoActivity.this.finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!isUpdateFromCasting) {
            if (!isCastingConnected()) {
                fitsooVideoview.seekTo(progress);
                txtProgressValue.setText(getMinutes(fitsooVideoview.getCurrentPosition()) + ":" + getSeconds(fitsooVideoview.getCurrentPosition()));
            } else {
                if (isCastingConnected()) {
                    remoteMediaClient = mCastSession.getRemoteMediaClient();
                }
                if (remoteMediaClient != null) {
                    remoteMediaClient.seek(progress);
                    txtProgressValue.setText(getMinutes(remoteMediaClient.getApproximateStreamPosition()) + ":" + getSeconds(remoteMediaClient.getApproximateStreamPosition()));
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeekTracking = true;
        isUpdateFromCasting = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeekTracking = false;
        isUpdateFromCasting = true;
    }


    private String getMinutes(long milliseconds) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        if (minute > 9) {
            return String.valueOf(minute);
        } else {
            return String.valueOf("0" + minute);
        }
    }

    private String getSeconds(long milliSeconds) {
        long millis = (TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % 60);
        if (millis > 9) {
            return String.valueOf(millis);
        } else {
            return String.valueOf("0" + millis);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //updateVideoTime();
        if (android.os.Build.VERSION.SDK_INT >= 27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void closeTheApplication() {
        FitsooUtils.fragName = "";
        super.onBackPressed();
    }

    public void getWorkoutData() {
        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_ID, FitsooPref.getUserId(VideoActivity.this));
            loginReques.put(FitsooConstant.REQ_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(VideoActivity.this, loginReques.toString(), getString(R.string.BASE_URL) + getString(R.string.workouts), this, getString(R.string.str_empty), "workouts");
    }
}