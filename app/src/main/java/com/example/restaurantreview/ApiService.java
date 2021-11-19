package com.example.restaurantreview;

import retrofit2.Call;
import retrofit2.http.*;

interface ApiService {
    @GET("detail/{id}")
    Call<RestaurantResponse> getRestaurant(@Path("id") String id);

    @FormUrlEncoded
    @Headers({"Authorization: token 12345"})
    @POST("review")
    Call<PostReviewResponse> posReview(
            @Field("id") String id,
            @Field("name") String name,
            @Field("review") String review
    );
}
