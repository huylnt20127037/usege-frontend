package com.group_1.usege.userInfo.services;

import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.UserInfo;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.userInfo.model.UserStatistic;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MasterService {
    @GET("user/{id}")
    Single<Response<UserInfo>> getUserInfo(@Path("id") String id);
    @GET("user/statistic/{id}")
    Single<Response<UserStatistic>> getUserStatistic(@Path("id") String id);
    @GET("user/plan/{id}")
    Single<Response<StoragePlan[]>> getUserPlan(@Path("id") String id);
}
