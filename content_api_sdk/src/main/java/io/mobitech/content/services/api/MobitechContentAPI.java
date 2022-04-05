package io.mobitech.content.services.api;

import io.mobitech.content.model.mobitech.ContentResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MobitechContentAPI {
    String LIMIT = "limit";
    String CATEGORIES_REQUEST_PARAM = "categories";
    String USER_ID_REQUEST_PARAM = "user_id";
    String CONTENT_USERIP_REQUEST_PARAM = "user_ip";
    String COUNTRY_USER_REQUEST_PARAM = "c";
    String PUBLISHER_CHANNEL_ID_REQUEST_PARAM = "p_id";
    String IMAGE_WIDTH_PARAM = "img_width";
    String IMAGE_HEIGHT_PARAM = "img_height";
    String OLD = "is_device_low";
    String LOCALE = "locale";
    String VENDOR_FILTER = "vendorFilter";
    String TYPE = "type";
    String USER_AGENT = "ua";

    @GET("/v1.1/{mobitechId}/document/get")
    Call<ContentResponse> getDocuments(
            @Path("mobitechId") String mobitechId,
            @Query(value = CATEGORIES_REQUEST_PARAM) String categoriesStr,
            @Query(value = LIMIT) String limitStr,
            @Query(value = USER_ID_REQUEST_PARAM) String userId,
            @Query(value = CONTENT_USERIP_REQUEST_PARAM) String userIp,
            @Query(value = COUNTRY_USER_REQUEST_PARAM) String iso2country,
            @Query(value = PUBLISHER_CHANNEL_ID_REQUEST_PARAM) String channelId,
            @Query(value = IMAGE_WIDTH_PARAM) Integer imgWidth,
            @Query(value = IMAGE_HEIGHT_PARAM) Integer imgHeight,
            @Query(value = OLD) Boolean isOldDevice,
            @Query(value = LOCALE) String locale,
            @Query(value = VENDOR_FILTER) String vendorFilter,
            @Query(value = TYPE) String type,
            @Query(value = USER_AGENT) String userAgent);
}
