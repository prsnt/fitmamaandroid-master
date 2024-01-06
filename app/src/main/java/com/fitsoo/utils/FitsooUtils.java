package com.fitsoo.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.facebook.login.LoginManager;
import com.fitsoo.R;
import com.fitsoo.activity.LoginActivity;
import com.fitsoo.constants.FitsooConstant;
import com.fitsoo.interfacepack.CustomDialogOneButtonListener;
import com.fitsoo.interfacepack.CustomDialogThreeButtonListener;
import com.fitsoo.interfacepack.CustomDialogTwoButtonListener;
import com.fitsoo.interfacepack.OnReceiveResponseListener;
import com.fitsoo.model.BaseResponse;
import com.fitsoo.model.ProgramHomeModel;
import com.fitsoo.model.UserStatus;
import com.fitsoo.preference.FitsooPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FitsooUtils {

    public static final String FRAGMENT_TRANSITION_TAG = "FragTrans";
    private static final String TAG = "FitsooUtils";
    public static boolean isInFront;
    public static String fragName = "";
    public static int vidPosition = 3;
    public static ProgramHomeModel programHomeModel;
    public static String pid, pname;
    public static boolean isAppInFront = true;
    public static boolean APPInBack = false;
    public static int SERVICE_CALL_TIME = 10000;
    public static ProgressDialog progressDialog;
    private static Context appContext;


    //private static AlertDialogListner dialoglistner;
    private static Context appContextR;
    private static String reqTagR = "";
    private static String reqParamsR = "";
    private static String pMessageR = "";
    private static String flagTypeR = "";
    private static OnReceiveResponseListener onReceiveResponseReceiver;
    private static String reqTag = "";
    private static String reqParams = "";
    private static String pMessage = "";
    private static String flagType = "";
    private static OnReceiveResponseListener onReceiveResponse;
    private static String response;
    private static String responsereceiver;
    private static boolean isPermissiondisplayed = false;

    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
//            return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            return ResourcesCompat.getFont(context, R.font.roboto_regular);
        } else if (tag == 101) {
//            return Typeface.createFromAsset(context.getAssets(), "fonts/ROBOTO-BOLD.ttf");
            return ResourcesCompat.getFont(context, R.font.roboto_bold);
        } else if (tag == 102) {
//            return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            return ResourcesCompat.getFont(context, R.font.roboto_medium);
        } else if (tag == 103) {
//            return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            return ResourcesCompat.getFont(context, R.font.roboto_light);
        }
        return ResourcesCompat.getFont(context, R.font.roboto_regular);
    }

    /**
     * Method checks null for Object , String , Collection type only
     *
     * @param obj Object
     * @return true if there is some value in the given object else false
     */

    public static boolean hasValue(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof EditText) {
            return ((EditText) obj).getText().toString().trim().length() > 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size() > 0;
        } else if (obj instanceof String) {
            return ((String) obj).length() > 0;
        }
        return true;
    }

    /**
     * @param context the Context of the activity where the internet check is needed to be done.
     * @method isInternetAvailable will be used to check if there is any Internet available
     * in the device or not.
     */

    public static boolean isInternetAvailable(Context context) {

        if (context != null) {
            //Instance of Connectivity Manager to perform Internet Check
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //Fetching the information available related to the active Network type
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            // Returns false if active network is null or if not connected to internet, else returns true
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static boolean isSubscribedFun(Context context) {
        if (FitsooPref.getAccess(context) == 1002) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the email is valid or not
     *
     * @param text Text to check if is in valid email format
     * @return true if email is in a valid format else returns false
     */
    public static boolean isValidEmail(CharSequence text) {
        return text != null && text.toString().trim().length() != 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * Checks if the text is of Minimum Length
     *
     * @param textToCheck   The text for which the length is to be checked
     * @param minCharLength the Length that the @param textToCheck has to be atleast
     * @return returns true if the length is greater then minCharLength
     */
    public static boolean isOfMinimumLength(String textToCheck, int minCharLength) {
        return textToCheck != null && textToCheck.length() >= minCharLength;
    }

    public static boolean isExpressionCorrect(String string, String regexExpression) {
        Pattern pattern = Pattern.compile(regexExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * Will show the message with the Text and title, should be used when no action is needed to be
     * performed when dialog is dismissed
     *
     * @param message Message to be included in the alert dialog
     * @param title   Title of the alert dialog
     */
    public static void showMessageAlert(String message, String title, Context context) {
        try {


            AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);
            messageAlert.setTitle(title);
            messageAlert.setMessage(message);
            messageAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            messageAlert.create().show();
        } catch (Exception e) {
        }
    }

    public static void showMessageAlertRed(String title, Context context) {
        AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//        textView.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
//        textView.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
//        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 40, 20, 10);
        textView.setTextSize(20F);
//        textView.setfon
        textView.setText(title);
        textView.setTextColor(Color.RED);
//        String a = "Hello Users,\n\nwe have an updates in our subscription packages as well we have introduce few more attractive features, so please cancel your current subscription before it will get auto renewed and subscribe for the new packages to obtain new features \n\n";
//        String b = "Hello Users,\n\nwe have an updates in our subscription packages as well we have introduce few more attractive features, so please cancel your current subscription before it will get auto renewed and subscribe for the new packages to obtain new features \n\nNotes:\n";
//        String c = "Hello Users,\n\nwe have an updates in our subscription packages as well we have introduce few more attractive features, so please cancel your current subscription before it will get auto renewed and subscribe for the new packages to obtain new features \n\n" + Html.fromHtml("<font color='#FFFFFF'>Notes:</font>") + "\nIf you will not cancel your existing/old subscription package before it gets auto renew, then we will not be responsible of the access blockage and loss of money.";
//        SpannableStringBuilder MessageBuilder = new SpannableStringBuilder(c);

//        MessageBuilder.setSpan(
//                new ForegroundColorSpan(context.getResources().getColor(R.color.red)),
//                a.length(),
//                b.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        );

        messageAlert.setCustomTitle(textView);
//        messageAlert.setTitle(title);
//        messageAlert.setMessage(MessageBuilder);
        messageAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        messageAlert.create().show();
    }

    /**
     * Will show Alert dialog with implemented custom listener if user wants to listen to one action taken
     *
     * @param message        The message for the alert dialog
     * @param title          Title to be displayed in the Alert Dialog
     * @param context        Context of the Activity or the Parent activity of the Fragment where this dialog is needed to be diaplayed
     * @param dialogListener Custom listener to check any action taken in dialog
     */
    public static void showMessageAlert(String message, String title, Context context, final CustomDialogOneButtonListener dialogListener) {
        AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);
        messageAlert.setTitle(title);
        messageAlert.setMessage(message);
        messageAlert.setCancelable(false);
        messageAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogPositiveButtonClick(dialog, which);
            }
        });
        messageAlert.create().show();
    }

    /**
     * Will show Alert dialog with implemented custom listener if user wants to listen to two action taken
     *
     * @param message        The message for the alert dialog
     * @param title          Title to be displayed in the Alert Dialog
     * @param context        Context of the Activity or the Parent activity of the Fragment where this dialog is needed to be diaplayed
     * @param dialogListener Custom listener to check any action taken in dialog
     */
    public static void showMessageAlert(String message, String title, Context context, final CustomDialogTwoButtonListener dialogListener, String positiveText, String negativeText) {
        AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);
        messageAlert.setTitle(title);
        messageAlert.setMessage(message);
        messageAlert.setPositiveButton(FitsooUtils.hasValue(positiveText) ? positiveText : "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogPositiveButtonClick(dialog, which);
            }
        });

        messageAlert.setNegativeButton(FitsooUtils.hasValue(negativeText) ? negativeText : "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogNegativeButtonClick(dialog, which);
            }
        });

        messageAlert.create().show();
    }

    /**
     * Will show Alert dialog with implemented custom listener if user wants to listen to three action buttons
     *
     * @param message        The message for the alert dialog
     * @param title          Title to be displayed in the Alert Dialog
     * @param context        Context of the Activity or the Parent activity of the Fragment where this dialog is needed to be diaplayed
     * @param dialogListener Custom listener to check any action taken in dialog
     */
    public static void showMessageAlert(String message, String title, Context context, final CustomDialogThreeButtonListener dialogListener) {
        AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);
        messageAlert.setTitle(title);
        messageAlert.setMessage(message);
        messageAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogPositiveButtonClick(dialog, which);
            }
        });

        messageAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogNegativeButtonClick(dialog, which);
            }
        });

        messageAlert.setNeutralButton("Neutral", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogNeutralButtonClick(dialog, which);
            }
        });
        messageAlert.create().show();
    }

    /**
     * This method should be used whereever we want to call Intent to Load a different Screen
     * IMPORTANT , method should not be used when passing Bundle.
     *
     * @param context context of the Screen from where this method is called
     * @param cls     Class which is needed to be loaded
     */
    public static void passToScreen(Context context, Class<?> cls, Bundle bundle) {
        Intent passToNeededScreen = new Intent(context, cls);
        if (bundle != null) {
            passToNeededScreen.putExtras(bundle);
        }
        context.startActivity(passToNeededScreen);
    }

    public static void performRequestReceiver(Context context, String params, String requestUrlTag, OnReceiveResponseListener onReceiveResponseListener, String progressMessage, String flag) {
        if (FitsooUtils.isInternetAvailable(context)) {
            appContextR = context;
            reqTagR = requestUrlTag;
            reqParamsR = params;
            flagTypeR = flag;
            pMessageR = progressMessage;
            onReceiveResponseReceiver = onReceiveResponseListener;
            new CreateRequestReceiver().execute(reqParamsR);
        } else {
            FitsooUtils.showMessageAlert("Please check your internet connection.", context.getString(R.string.app_name), context);
        }
    }

    public static void performRequest(Context context, String params, String requestUrlTag, OnReceiveResponseListener onReceiveResponseListener, String progressMessage, String flag) {
        if (FitsooUtils.isInternetAvailable(context)) {
            appContext = context;
            reqTag = requestUrlTag;
            reqParams = params;
            flagType = flag;
            pMessage = progressMessage;
            onReceiveResponse = onReceiveResponseListener;
            new CreateRequest().execute(reqParams);

        } else {
            FitsooUtils.showMessageAlert("Please check your internet connection.", context.getString(R.string.app_name), context);
        }
    }

    /**
     * The method will be called whenever we want to create a Post Request
     *
     * @param context context of the Activity from where the request is being made
     * @param json2   The Json that will be used to create the Request
     * @param url     url of the Query to be fired
     * @return Returns the String response from the server
     * <p>
     * TODO Change the parameters of username and password from static to dynamic later
     */
    public static String makePostRequest(Context context, String json2, String url) {
        String responseStr = "{\"success\":\"0\",\"message\":\"There was some error getting data, Please try again later.\"}";
        String responsecon = "{\"success\":\"1\",\"message\":\"Challenges video list.\",\"data\":[{\"vid\":\"139\",\"workfocus_name\":\"Core & Abs\",\"worktype_name\":\"Six Pack\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151633893319332.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516339059six pack 5.jpg\"},{\"vid\":\"137\",\"workfocus_name\":\"Lower Body\",\"worktype_name\":\"Booty Boost\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151616639656538.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516166504Booty Boost 2.JPG\"},{\"vid\":\"136\",\"workfocus_name\":\"Interval\",\"worktype_name\":\"Tabata\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151616359120268.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516163644tabata 1.jpg\"},{\"vid\":\"121\",\"workfocus_name\":\"Full Body\",\"worktype_name\":\"MamaDance\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151399076779648.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1514047514Mamadance 2.JPG\"},{\"vid\":\"95\",\"workfocus_name\":\"Interval\",\"worktype_name\":\"Speedy\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151276054945517.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1512791654Speedy 3.JPG\"},{\"vid\":\"71\",\"workfocus_name\":\"Full Body\",\"worktype_name\":\"Bottle Fit\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151016747558274.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1510169374Bottle Fit 2.JPG\"}]}\n";
        try {
            URL urls;

            urls = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) urls.openConnection();

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty(FitsooConstant.REQ_HEADER_TOKEN, context.getString(R.string.APPLICATION_TOKEN));

            connection.setReadTimeout(60000);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            // connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(json2);
            wr.flush();
            wr.close();

            if (connection.getResponseCode() == 200) {
                if (connection.getInputStream() != null) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    responseStr = rd.readLine();
                    if ((responseStr == null) || responseStr.length() == 0) {
                        responseStr = "{\"success\":\"0\",\"message\":\"There was some error getting data, Please try again later.\"}";
                        //FitsooUtils.dismissProgressDialog(progressDialog);
                    }
                    // else if(responseStr.contains("data":""))
                  /*  else if (responseStr.equals("{\"data\":\"\",\"success\":\"1\",\"message\":\"Success\"}"))
                    {
                        FitsooUtils.dismissProgressDialog(progressDialog);
                    }*/
                }
            }
        } catch (IOException e) {
            FitsooUtils.dismissProgressDialog(progressDialog);
            e.printStackTrace();
        } catch (Exception e) {
            FitsooUtils.dismissProgressDialog(progressDialog);
            e.printStackTrace();
        }
        //  Log.d("Response-i:", responseStr);

        return responseStr;
    }

    public static boolean isAccess(Context context) {


        if (FitsooPref.getAccess(context) == 1) {
            return true;
        } else if (FitsooPref.getSubscribeType(context) == 1) {
            return true;
        } else {
//            showMessageAlert(FitsooPref.getAccessmsg(context), context.getString(R.string.app_name), context);
            return false;
        }
    }

    public static String makePostRequestReceiver(Context context, String json2, String url) {
        String responseStr = "{\"success\":\"0\",\"message\":\"There was some error getting data, Please try again later.\"}";
        String responsecon = "{\"success\":\"1\",\"message\":\"Challenges video list.\",\"data\":[{\"vid\":\"139\",\"workfocus_name\":\"Core & Abs\",\"worktype_name\":\"Six Pack\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151633893319332.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516339059six pack 5.jpg\"},{\"vid\":\"137\",\"workfocus_name\":\"Lower Body\",\"worktype_name\":\"Booty Boost\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151616639656538.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516166504Booty Boost 2.JPG\"},{\"vid\":\"136\",\"workfocus_name\":\"Interval\",\"worktype_name\":\"Tabata\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151616359120268.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1516163644tabata 1.jpg\"},{\"vid\":\"121\",\"workfocus_name\":\"Full Body\",\"worktype_name\":\"MamaDance\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151399076779648.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1514047514Mamadance 2.JPG\"},{\"vid\":\"95\",\"workfocus_name\":\"Interval\",\"worktype_name\":\"Speedy\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151276054945517.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1512791654Speedy 3.JPG\"},{\"vid\":\"71\",\"workfocus_name\":\"Full Body\",\"worktype_name\":\"Bottle Fit\",\"videourl\":\"https:\\/\\/dr7n943fq3qef.cloudfront.net\\/151016747558274.mp4\",\"video_thumb\":\"http:\\/\\/dashboard.fitsoo.com\\/uploads\\/workout_videos\\/thumb\\/1510169374Bottle Fit 2.JPG\"}]}\n";
        try {
            URL urls;

            urls = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) urls.openConnection();

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty(FitsooConstant.REQ_HEADER_TOKEN, context.getString(R.string.APPLICATION_TOKEN));

            connection.setReadTimeout(60000);
            connection.setConnectTimeout(60000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            // connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(json2);
            wr.flush();
            wr.close();

            if (connection.getResponseCode() == 200) {
                if (connection.getInputStream() != null) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    responseStr = rd.readLine();
                    if ((responseStr == null) || responseStr.length() == 0) {
                        responseStr = "{\"success\":\"0\",\"message\":\"There was some error getting data, Please try again later.\"}";
                        //FitsooUtils.dismissProgressDialog(progressDialog);
                    }
                    // else if(responseStr.contains("data":""))
                  /*  else if (responseStr.equals("{\"data\":\"\",\"success\":\"1\",\"message\":\"Success\"}"))
                    {
                        FitsooUtils.dismissProgressDialog(progressDialog);
                    }*/
                }
            }
        } catch (IOException e) {
            FitsooUtils.dismissProgressDialog(progressDialog);
            e.printStackTrace();
        } catch (Exception e) {
            FitsooUtils.dismissProgressDialog(progressDialog);
            e.printStackTrace();
        }
        //  Log.d("Response-i:", responseStr);

        return responseStr;
    }

    /**
     * The method converts the bitmap into Base64 String
     *
     * @param bitmap bitmap that is needed to be converted
     * @return the converted String
     */
    public static String getBase64StringFromImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    /**
     * Will hide the keyboard
     *
     * @param context
     */
    public static void hideSoftKeyboard(Context context) {
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Returns the Pixels from DP
     *
     * @param dp      the Density that is needed to be converted
     * @param context Contect of the activity or Fragment from where this method will be called
     * @return returns the Pixels in float format
     */
    public static int getPixelFromDp(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {

        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();


        } catch (Exception e) {

        }
        return progressDialog;
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public static String saveImage(Bitmap finalBitmap, String imagename) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        File file = new File(myDir, imagename);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static boolean isWriteStoragePermssionGranted(Context context) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                isPermissiondisplayed = false;
            } else {
                ActivityCompat.requestPermissions((AppCompatActivity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                isPermissiondisplayed = true;
            }
        }

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) && !isPermissiondisplayed) {
            if (FitsooPref.getShouldShowDialog(context)) {
                showSettingsAlert(context, "Write External Storage permission is needed to save and retrieve your profile Image.\nPlease go to settings and update the permission..", "External Storage permission!");
            } else {
                return true;
            }
        }
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void showSettingsAlert(final Context context, String message, String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);

//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                context.startActivity(intent);
            }
        });
        alertDialog.setNeutralButton("Don't show again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FitsooPref.setShouldShowSettingDialog(context, false);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Returns the date format in form of a String
     *
     * @param addremove Moves date forward or backward depending on the passed parameter
     * @return Date in form of String
     */
    public static String getDate(int addremove) {
        Date currDatea = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDatea);
        cal.add(Calendar.DATE, addremove);

        Date date = cal.getTime();
        DateFormat outputFormatter1 = new SimpleDateFormat("MM-dd");
        return outputFormatter1.format(date);
    }

    public static String handleDateInProfile(String dateParam) {
        DateFormat currDate = new SimpleDateFormat("MM-dd-yyyy");
        Date datePassed = null;
        try {
            datePassed = new SimpleDateFormat("MM-dd-yyyy").parse(dateParam);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currDate.format(datePassed).equalsIgnoreCase(getToday())) {
            return "Today";
        } else if (currDate.format(datePassed).equalsIgnoreCase(getTomorrow())) {
            return "Tomorrow";
        } else if (currDate.format(datePassed).equalsIgnoreCase(getYesterday())) {
            return "Yesterday";
        } else {
            return currDate.format(datePassed);
        }
    }

    public static int isGreaterThenOtherDate(String dateParam) {
        Date datePassed = null;
        Date dateToday = null;
        try {
            datePassed = new SimpleDateFormat("MM-dd-yyyy").parse(dateParam);
            dateToday = new SimpleDateFormat("MM-dd-yyyy").parse(getToday());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (datePassed.compareTo(dateToday) > 0) {
            System.out.println("datePassed is after dateToday");
            return 2;
        } else if (datePassed.compareTo(dateToday) < 0) {
            return 0;
        } else if (datePassed.compareTo(dateToday) == 0) {
            return 1;
        } else {
            return 1;
        }
    }

    public static int isVideoCompleted(long duration, String videoUrl, String progress, Context cont) {
        long totDur = duration;
        long progDur = Long.parseLong(progress) * 100;
        int percent;
        if (totDur == 0) {
            percent = 0;
        } else {
            percent = (int) (progDur / totDur);
            Log.d(TAG, "isVideoCompleted: Percent" + totDur + "\n" + progDur);
        }
        if (percent > 80) {
            return 1;
        }
        return 0;
    }

    private static String getToday() {
        Date currDatea = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDatea);
        cal.add(Calendar.DATE, 0);

        Date date = cal.getTime();
        DateFormat outputFormatter1 = new SimpleDateFormat("MM-dd-yyyy");
        return outputFormatter1.format(date);
    }

    private static String getYesterday() {
        Date currDatea = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDatea);
        cal.add(Calendar.DATE, -1);

        Date date = cal.getTime();
        DateFormat outputFormatter1 = new SimpleDateFormat("MM-dd-yyyy");
        return outputFormatter1.format(date);
    }

    private static String getTomorrow() {
        Date currDatea = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDatea);
        cal.add(Calendar.DATE, 1);

        Date date = cal.getTime();
        DateFormat outputFormatter1 = new SimpleDateFormat("MM-dd-yyyy");
        return outputFormatter1.format(date);
    }

    public static void checkUserStatus(final Context context) {
        JSONObject loginReques = new JSONObject();
        try {
            loginReques.put(FitsooConstant.REQ_USER_ID, String.valueOf(FitsooPref.getUserId(context)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FitsooUtils.performRequest(context, loginReques.toString(), context.getString(R.string.BASE_URL) + context.getString(R.string.checkuserstatus), new OnReceiveResponseListener() {
            @Override
            public void onResponseReceived(String requestType, String response) {
                if (response.contains("isLocked") && response.contains("isDelete")) {
                    BaseResponse<UserStatus> userStatus = (new Gson()).fromJson(response, new TypeToken<BaseResponse<UserStatus>>() {
                    }.getType());
                    if (userStatus != null && userStatus.getSuccess() == 1) {
                        if (userStatus.getData() != null && userStatus.getData().getIsDelete().equals("0")) {
                            if (userStatus.getData().getIsLocked() != null && userStatus.getData().getIsLocked().equalsIgnoreCase("0")) {
                                FitsooUtils.showMessageAlert(context.getResources().getString(R.string.error_user_locked), context.getString(R.string.app_name), context, new CustomDialogOneButtonListener() {
                                    @Override
                                    public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
                                        FitsooUtils.fragName = "";
                                        LoginManager.getInstance().logOut();
                                        FitsooPref.clearSharePreference(context);
                                        Intent passToLogin = new Intent(context, LoginActivity.class);
                                        context.startActivity(passToLogin);
                                        ((AppCompatActivity) context).finish();
                                    }
                                });
                            }
                        } else {
                            //TODO User has been deleted Alert
                            if (userStatus.getData() != null) {
                                FitsooUtils.showMessageAlert(context.getString(R.string.error_account_deleted), context.getString(R.string.app_name), context, new CustomDialogOneButtonListener() {
                                    @Override
                                    public void onDialogPositiveButtonClick(DialogInterface dialog, int which) {
                                        FitsooUtils.fragName = "";
                                        LoginManager.getInstance().logOut();
                                        FitsooPref.clearSharePreference(context);
                                        Intent passToLogin = new Intent(context, LoginActivity.class);
                                        context.startActivity(passToLogin);
                                        ((AppCompatActivity) context).finish();

                                    }
                                });
                            }
                        }
                    } else {
                        //TODO confirm what is needed to be done
                        if (userStatus != null) {
                            FitsooUtils.showMessageAlert(userStatus.getMessage(), context.getString(R.string.app_name), context);
                        }
                    }
                }
            }
        }, "", "");
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void ChangeFragment(boolean isBackstack, Bundle bundle, Fragment fragment, AppCompatActivity context) {
        if (bundle != null)
            fragment.setArguments(bundle);
        if (isBackstack) {
            context.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.content_frame, fragment, FitsooUtils.FRAGMENT_TRANSITION_TAG).addToBackStack(null).commit();
        } else {
            context.getSupportFragmentManager().popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            context.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.content_frame, fragment, FitsooUtils.FRAGMENT_TRANSITION_TAG).commit();
            context.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, FitsooUtils.FRAGMENT_TRANSITION_TAG).commit();
        }
    }

    public static void Addfragment(boolean isBackstack, Bundle bundle, Fragment
            fragment, AppCompatActivity context) {
        if (bundle != null)
            fragment.setArguments(bundle);
        if (isBackstack)
            context.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).add(R.id.content_frame, fragment).addToBackStack(null).commit();
        else
            context.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).add(R.id.content_frame, fragment).commit();
    }

    public static class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.Viewholder> {
        int count;
        Context context;
        int layout;
        boolean isoffer;

        public CommonAdapter(int count, Context context, int layout, boolean isoffer) {
            this.count = count;
            this.context = context;
            this.layout = layout;
            this.isoffer = isoffer;
        }

        @NonNull
        @Override
        public CommonAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommonAdapter.Viewholder viewholder, int i) {

            ViewGroup.LayoutParams params = viewholder.itemView.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth() / 2;
            params.height = ScreenUtils.getScreenWidth() / 2;
        }

        @Override
        public int getItemCount() {
            return count;
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            RecyclerView recyclerviewTopOffer;
            RecyclerView recyclerviewBottomOffer;

            public Viewholder(@NonNull View itemView) {
                super(itemView);

            }
        }
    }

    private static class CreateRequest extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pMessage != null && pMessage.trim().length() > 0 && !pMessage.equals("")) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    dismissProgressDialog(progressDialog);
                    //progressDialog.setCancelable(false);
                }
                showProgressDialog(appContext, pMessage);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            response = FitsooUtils.makePostRequest(appContext, params[0], reqTag);
            Log.d("Response-fitness:", response);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dismissProgressDialog(progressDialog);
            onReceiveResponse.onResponseReceived(flagType, response);
        }
    }

    private static class CreateRequestReceiver extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pMessageR != null && pMessageR.trim().length() > 0 && !pMessageR.equals("")) {
                progressDialog = FitsooUtils.showProgressDialog(appContextR, pMessageR);
                progressDialog.setCancelable(false);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            responsereceiver = FitsooUtils.makePostRequestReceiver(appContextR, params[0], reqTagR);
            Log.d("ResponseR-fitness:", responsereceiver);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // progressDialog.dismiss();

            if (responsereceiver.equals("") && responsereceiver.equals(null)) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } else {

                FitsooUtils.dismissProgressDialog(progressDialog);
                onReceiveResponseReceiver.onResponseReceived(flagTypeR, responsereceiver);

            }

        }

    }

}
