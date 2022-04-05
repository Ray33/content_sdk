/*
 * Copyright (c) 2016. http://mobitech.io. All rights reserved.
 * Code is not permitted for commercial use w/o permission of Mobitech.io - support@mobitech.io .
 * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package io.mobitech.content.services;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.mobitech.content.BuildConfig;
import io.mobitech.content.R;
import io.mobitech.content.model.mobitech.ContentResponse;
import io.mobitech.content.model.mobitech.ContentType;
import io.mobitech.content.model.mobitech.Document;
import io.mobitech.content.model.mobitech.IpResponse;
import io.mobitech.content.services.api.IpifyAPI;
import io.mobitech.content.services.api.MobitechContentAPI;
import io.mobitech.content.services.api.callbacks.ContentCallback;
import io.mobitech.content.utils.GetCountryUtil;
import io.mobitech.content.utils.LowEndDeviceDetectionUtil;
import io.mobitech.content.utils.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created on 19.11.2017
 *
 * @author Sergey Pogoryelov
 */
public class RecommendationService {
    private static final String TAG = RecommendationService.class.getSimpleName();

    private static RecommendationService instance;

    private static final long YEAR_IN_MILLISECONDS = 365L * 24 * 60 * 60 * 1000;

    private static final int DOCUMENTS_DEFAULT_LIMIT = 15;

    public String contentServerBaseUrl;

    private static final int MAX_REQUEST_RETRIES_NUMBER = 3;

////////////////////        Uncomment and use it for debug purposes        ////////////////////
//
//    static OkHttpClient httpClient;
//
//    static {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////

    //builder
    private Retrofit retrofitMobitechContentBuilder;
    private Retrofit retrofitIpifyBuilder;

    //api
    private MobitechContentAPI mobitechContentAPI;
    private IpifyAPI ipifyAPI;

    private Context context;
    private String publisherKey;
    private String userId;
    private String userAgent;
    private String country;
    private String locale;
    private String userIp;
    private boolean isLowEndDevice;

    private LowEndDeviceDetectionUtil lowEndDeviceDetectionUtil;

    /**
     * Build or get existing recommendation service
     *
     * @param context       android context
     * @param publisherKey  publisher key
     * @param userId        user id (unique value for each user)
     * @return
     */
    public static RecommendationService build(@NonNull Context context,
                                                   @NonNull String publisherKey, @NonNull String userId) {
        return build(context, publisherKey, userId, null);
    }

    /**
     * Build or get existing recommendation service
     *
     * @param context       android context
     * @param publisherKey  publisher key
     * @param userId        user id (unique value for each user)
     * @param userAgent     user agent (optional)
     * @return
     */
    public static RecommendationService build(@NonNull Context context,
                                                   @NonNull String publisherKey,
                                                   @NonNull String userId, @Nullable String userAgent) {
        return build(context, publisherKey, userId, userAgent, GetCountryUtil.getUserCountryByCellularNetwork(context));
    }

    /**
     * Build or get existing recommendation service
     *
     * @param context       android context
     * @param publisherKey  publisher key
     * @param userId        user id (unique value for each user)
     * @param userAgent     (optional) user agent
     * @param country       (optional) user country
     * @return
     */
    public static RecommendationService build(@NonNull Context context,
                                                   @NonNull String publisherKey, @NonNull String userId,
                                                   @Nullable String userAgent, @Nullable String country) {
        return build(context, publisherKey, userId, userAgent, country, null);
    }

    /**
     * Build or get existing recommendation service
     *
     * @param context       android context
     * @param publisherKey  publisher key
     * @param userId        user id (unique value for each user)
     * @param userAgent     (optional) user agent
     * @param country       (optional) user country
     * @param userIp        (optional) user IP
     * @return
     */
    public static RecommendationService build(@NonNull Context context,
                                                   @NonNull String publisherKey, @NonNull String userId,
                                                   @Nullable String userAgent, @Nullable String country,
                                                   @Nullable String userIp) {
        return build(context, publisherKey, userId, userAgent, country, null, userIp);
    }

    /**
     * Build or get existing recommendation service
     *
     * @param context       android context
     * @param publisherKey  publisher key
     * @param userId        user id (unique value for each user)
     * @param userAgent     (optional) user agent
     * @param country       (optional) user country
     * @param userIp        (optional) user IP
     * @param locale        (optional) user's preferred language
     * @return
     */
    public static RecommendationService build(@NonNull Context context,
                                                   @NonNull String publisherKey, @NonNull String userId,
                                                   @Nullable String userAgent, @Nullable String country,
                                                   @Nullable String userIp, @Nullable String locale) {

        instance = new RecommendationService(context, publisherKey, userId, userAgent, country,
                locale, userIp);
        printVersion();


        return instance;
    }

    private RecommendationService(Context context, String publisherKey, String userId,
                                  String userAgent, @Nullable String country, String locale,
                                  String userIp) {
        this.context = context;
        this.publisherKey = publisherKey;
        this.userId = userId;
        this.userAgent = userAgent;

        contentServerBaseUrl = context.getString(R.string.CONTENT_BASE_URL);

        retrofitMobitechContentBuilder = initializeRetrofitMobitechContentBuilder();
        retrofitIpifyBuilder = initializeRetrofitIpifyBuilder();

        mobitechContentAPI = retrofitMobitechContentBuilder.create(MobitechContentAPI.class);
        ipifyAPI = retrofitIpifyBuilder.create(IpifyAPI.class);

        if (TextUtils.isEmpty(country)) {
            country = GetCountryUtil.getUserCountryByCellularNetwork(context);
        }
        this.country = country;

        if (TextUtils.isEmpty(locale)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                locale = Locale.getDefault().toLanguageTag();
            }else{
                locale = Locale.getDefault().toString();
                if (locale.length() <= 2){
                    locale += "_" + country;
                }
            }
        }
        this.locale = locale;


        this.lowEndDeviceDetectionUtil = new LowEndDeviceDetectionUtil(context);
        this.isLowEndDevice = lowEndDeviceDetectionUtil.isLowEndDevice();

        if (!TextUtils.isEmpty(userIp)) {
            this.userIp = userIp;
        } else {
            resolveIp();
        }
    }

    /**
     * Get organic content recommendation from Mobitech content server
     *
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getOrganicContent(@Nullable String categories, @Nullable Integer limit,
                                  @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.ORGANIC, null, null, null,
                null, categories, limit, contentCallback, 0);
    }

    /**
     * Get organic content recommendation from Mobitech content server
     *
     * @param channelId       (optional) channel id
     * @param imgWidth        (optional) image width
     * @param imgHeight       (optional) image height
     * @param vendorFilter    (optional) unwanted content provider, comma-separated (contentProvider,contentProvider2)
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getOrganicContent(@Nullable String channelId, @Nullable Integer imgWidth,
                                  @Nullable Integer imgHeight, @Nullable String vendorFilter,
                                  @Nullable String categories, @Nullable Integer limit,
                                  @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.ORGANIC, channelId, imgWidth, imgHeight,
                vendorFilter, categories, limit, contentCallback, 0);
    }

    /**
     * Get promoted content recommendation from Mobitech content server
     *
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getPromotedContent(@Nullable String categories, @Nullable Integer limit,
                                   @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.PROMOTED, null, null, null,
                null, categories, limit, contentCallback, 0);
    }

    /**
     * Get promoted content recommendation from Mobitech content server
     *
     * @param channelId       (optional) channel id
     * @param imgWidth        (optional) image width
     * @param imgHeight       (optional) image height
     * @param vendorFilter    (optional) unwanted content provider comma-separated (contentProvider;contentProvider2)
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getPromotedContent(@Nullable String channelId, @Nullable Integer imgWidth,
                                   @Nullable Integer imgHeight, @Nullable String vendorFilter,
                                   @Nullable String categories, @Nullable Integer limit,
                                   @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.PROMOTED, channelId, imgWidth, imgHeight,
                vendorFilter, categories, limit, contentCallback, 0);
    }

    /**
     * Get video content recommendation from Mobitech content server
     *
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getVideoContent(@Nullable String categories, @Nullable Integer limit,
                                @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.VIEDO, null, null, null,
                null, categories, limit, contentCallback, 0);
    }

    /**
     * Get video content recommendation from Mobitech content server
     *
     * @param channelId       (optional) channel id
     * @param imgWidth        (optional) image width
     * @param imgHeight       (optional) image height
     * @param vendorFilter    (optional) unwanted content provider comma-separated (contentProvider;contentProvider2)
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getVideoContent(@Nullable String channelId, @Nullable Integer imgWidth,
                                @Nullable Integer imgHeight, @Nullable String vendorFilter,
                                @Nullable String categories, @Nullable Integer limit,
                                @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.VIEDO, channelId, imgWidth, imgHeight,
                vendorFilter, categories, limit, contentCallback, 0);
    }

    /**
     * Get mixed content recommendation (organic + promoted) from Mobitech content server
     *
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getMixedContent(@Nullable String categories, @Nullable Integer limit,
                                @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.MIX, null, null, null,
                null, categories, limit, contentCallback, 0);
    }

    /**
     * Get mixed content recommendation (organic + promoted) from Mobitech content server
     *
     * @param channelId       (optional) channel id
     * @param imgWidth        (optional) image width
     * @param imgHeight       (optional) image height
     * @param vendorFilter    (optional) unwanted content provider comma-separated (contentProvider;contentProvider2)
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getMixedContent(@Nullable String channelId, @Nullable Integer imgWidth,
                                @Nullable Integer imgHeight, @Nullable String vendorFilter,
                                @Nullable String categories, @Nullable Integer limit,
                                @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.MIX, channelId, imgWidth, imgHeight,
                vendorFilter, categories, limit, contentCallback, 0);
    }

    /**
     * Get mixed + video content recommendation (organic + promoted + video) from Mobitech content server
     *
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getMixedVideoContent(@Nullable String categories, @Nullable Integer limit,
                                     @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.MIX_VIDEO, null, null, null,
                null, categories, limit, contentCallback, 0);
    }

    /**
     * Get mixed + video content recommendation (organic + promoted + video) from Mobitech content server
     *
     * @param channelId       (optional) channel id
     * @param imgWidth        (optional) image width
     * @param imgHeight       (optional) image height
     * @param vendorFilter    (optional) unwanted content provider comma-separated (contentProvider;contentProvider2)
     * @param categories      (optional) categories string, comma-separated (category1,category2)
     * @param limit           (optional) results limit
     * @param contentCallback results callback
     */
    public void getMixedVideoContent(@Nullable String channelId, @Nullable Integer imgWidth,
                                     @Nullable Integer imgHeight, @Nullable String vendorFilter,
                                     @Nullable String categories, @Nullable Integer limit,
                                     @NonNull final ContentCallback<List<Document>> contentCallback) {
        getContent(ContentType.MIX_VIDEO, channelId, imgWidth, imgHeight,
                vendorFilter, categories, limit, contentCallback, 0);
    }

    /**
     * Get content recommendations
     * @param contentType       content type (enum value)
     * @param channelId         (optional) channel id
     * @param imgWidth          (optional) image height
     * @param imgHeight         (optional) image width
     * @param vendorFilter      (optional) unwanted content provider comma-separated (contentProvider;contentProvider2)
     * @param categories        (optional) categories
     * @param limit             (optional) limit
     * @param contentCallback   (optional) results callback
     */
    private void getContent(@NonNull final ContentType contentType, @Nullable String channelId,
                            @Nullable final Integer imgWidth, @Nullable final Integer imgHeight,
                            @Nullable final String vendorFilter, @Nullable final String categories,
                            @Nullable Integer limit,
                            @NonNull final ContentCallback<List<Document>> contentCallback, final int retryNumber) {
        //validation & defaults
        if (TextUtils.isEmpty(channelId)) {
            channelId = "default";
        }

        final String channelIdFinal = channelId;


        if (limit == null) {
            limit = DOCUMENTS_DEFAULT_LIMIT;
        }

        final Integer limitFinal = limit;

        Call<ContentResponse> call = mobitechContentAPI.getDocuments(publisherKey,
                categories, limitFinal.toString(), userId, userIp,
                country, channelIdFinal, imgWidth, imgHeight,
                isLowEndDevice, locale, vendorFilter, contentType.getValue(), userAgent);

        call.enqueue(new Callback<ContentResponse>() {
            @Override
            public void onResponse(@NonNull Call<ContentResponse> call, @NonNull Response<ContentResponse> response) {
                List<Document> documents = new ArrayList<>();

                long oneYearAgo = System.currentTimeMillis() - YEAR_IN_MILLISECONDS;

                if (response.code() == 200) {
                    ContentResponse body = response.body();
                    if (body != null) {
                        documents = body.getDocuments();
                        for (Document document : documents) {
                            try {
                                long publishedTime = Long.parseLong(document.getPublishedTime());
                                if (publishedTime < oneYearAgo) {
                                    document.setPublishedTime(Long.toString(System.currentTimeMillis()));
                                }
                            } catch (NumberFormatException e) {
                                // Do nothing
                            }
                        }
                    }
                } else {
                    Log.w(TAG, response.code() + ": " + response.message());
                }

                contentCallback.processResult(documents, context);
            }

            @Override
            public void onFailure(@NonNull Call<ContentResponse> call, @NonNull Throwable t) {
                Log.w(TAG, "Failed to get documents from content server: " + t.getMessage() +
                        " retry number: " + retryNumber);

                if (retryNumber < MAX_REQUEST_RETRIES_NUMBER) {
                    getContent(contentType, channelIdFinal, imgWidth, imgHeight, vendorFilter,
                            categories, limitFinal, contentCallback, retryNumber + 1);
                } else {
                    Log.w(TAG, "failed to get any documents from the content server after all hard tries :(");
                    contentCallback.processResult(new ArrayList<Document>(), context);
                }
            }
        });
    }

    /**
     * Asynchronously resolves user ip via ipify.org
     * @return user ip
     */
    private void resolveIp() {
        Call<IpResponse> ipCall = ipifyAPI.resolveIp();
        ipCall.enqueue(new Callback<IpResponse>() {
            @Override
            public void onResponse(@NonNull Call<IpResponse> call, @NonNull Response<IpResponse> response) {
                if (response.code() == 200) {
                    IpResponse body = response.body();
                    if (body != null) {
                        userIp = body.getIp();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<IpResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to resolve user ip: " + t.getMessage());
            }
        });
    }

    @NonNull
    private Retrofit initializeRetrofitMobitechContentBuilder() {
        return new Retrofit.Builder()
                .baseUrl(contentServerBaseUrl)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .client(RetrofitUtil.initHttpClient(true))
                .build();
    }

    @NonNull
    private Retrofit initializeRetrofitIpifyBuilder() {
        return new Retrofit.Builder()
                .baseUrl(IpifyAPI.BASE_URL)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .client(RetrofitUtil.initHttpClient(true))
                .build();
    }

    private static void printVersion() {
        Log.i(TAG, " #     #                                            ");
        Log.i(TAG, " ##   ##  ####  #####  # ##### ######  ####  #    # ");
        Log.i(TAG, " # # # # #    # #    # #   #   #      #    # #    # ");
        Log.i(TAG, " #  #  # #    # #####  #   #   #####  #      ###### ");
        Log.i(TAG, " #     # #    # #    # #   #   #      #      #    # ");
        Log.i(TAG, " #     # #    # #    # #   #   #      #    # #    # ");
        Log.i(TAG, " #     #  ####  #####  #   #   ######  ####  #    # ");
        Log.i(TAG, ">>>>>>>>>>>>>>>>>>> content SDK ver. " + BuildConfig.ARTIFACT_VERSION_STR + " <<<<<<<<<<<<<<<<<<<");
    }
}
