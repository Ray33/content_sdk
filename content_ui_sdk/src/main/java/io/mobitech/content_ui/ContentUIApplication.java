package io.mobitech.content_ui;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.UUID;

import io.mobitech.content.services.RecommendationService;
import io.mobitech.content_ui.interfaces.OnLoadCompleteListener;

/**
 * Created by Viacheslav Titov on 15.09.2016.
 */

public class ContentUIApplication extends Application {

    private static final String TAG = ContentUIApplication.class.getPackage() + "." + ContentUIApplication.class.getSimpleName();

    private String userID = "";

    private static ContentUIApplication sInstance;

    private RecommendationService recommendationService;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete() {
                // Do nothing
            }
        });
    }

    public static ContentUIApplication getInstance() {
        return sInstance;
    }

    public void init(final OnLoadCompleteListener onLoadCompleteListener) {
        //callback for user id:
        //Either takes the user's advertiserId or creates a unique ID, if
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ContentUIApplication.this);
                    if (!adInfo.isLimitAdTrackingEnabled()) {//if user hasn't opt-out
                        return adInfo.getId();
                    } else {
                        //Settings.Secure.ANDROID_ID: A 64-bit number (as a hex string) that is randomly
                        // generated when the user first sets up the device and should remain
                        // constant for the lifetime of the user's device.
                        String android_id = Settings.Secure.getString(ContentUIApplication.this.getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                        //if ANDROID_ID is unavailable - generate a random ID
                        return android_id == null ? new UUID(System.currentTimeMillis(), System.currentTimeMillis() * 2).toString() : android_id;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                //default return is empty, in order to catch issues with user id generation
                return "";
            }

            @Override
            protected void onPostExecute(String advertId) {
                userID = advertId;
                //init Mobitech's content SDK
                recommendationService = RecommendationService.build(getApplicationContext(), getBaseContext().getString(R.string.MOBITECH_CONTENT_PUBLISHER_API_KEY), advertId);
                onLoadCompleteListener.onLoadComplete();
            }

        };
        task.execute();
    }

    public String getUserID() {
        return userID;
    }

    public RecommendationService getRecommendationService() {
        return recommendationService;
    }
}
