package io.mobitech.content.services.api;

import io.mobitech.content.model.mobitech.IpResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpifyAPI {

    String BASE_URL = "https://api.ipify.org";

    @GET("/?format=json")
    Call<IpResponse> resolveIp();
}
