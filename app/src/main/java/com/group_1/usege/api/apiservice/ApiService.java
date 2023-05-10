package com.group_1.usege.api.apiservice;


import com.group_1.usege.api.googlemaps.GeocodeResponse;
import com.group_1.usege.model.UserFile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("maps/api/geocode/json")
    Call<GeocodeResponse> getLocation(@Query("latlng") String latlng,
                                      @Query("key") String apiKey);

    @Multipart
    @POST("{id}")
    Call<UserFile> uploadFile(@Path("id")String userId,
                                        @Part("info") RequestBody imageDto,
                                        @Part MultipartBody.Part file);

    @PUT("{id}")
    Call<UserFile> updateFile(@Path("id")String userId,
                              @Body UserFile userFile);
}
