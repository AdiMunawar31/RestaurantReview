package com.example.restaurantreview;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public MainViewModel() {
        findRestaurant();
    }

    private final MutableLiveData<Restaurant>_restaurant = new MutableLiveData<>();
    public LiveData<Restaurant> getRestaurant() {
        return _restaurant;
    }

    private final MutableLiveData<List<CustomerReviewsItem>> _listReview = new MutableLiveData<>();
    public LiveData<List<CustomerReviewsItem>> getListReview() {
        return _listReview;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    private final MutableLiveData<Event<String>> _snackbarText = new MutableLiveData<>();
    public LiveData<Event<String>> snackbarText() {
        return _snackbarText;
    }

    private static final String TAG = "MainViewModel";
    private static final String RESTAURANT_ID = "uewq1zg2zlskfw1e867";

    private void findRestaurant(){
        _isLoading.setValue(true);

        Call<RestaurantResponse> client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID);
        client.enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        _restaurant.setValue(response.body().getRestaurant());
                        _listReview.setValue(response.body().getRestaurant().getCustomerReviews());
                    }
                } else {
                    if (response.body() != null) {
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public final void postReview(String review){
        _isLoading.setValue(true);

        Call<PostReviewResponse> client = ApiConfig.getApiService().posReview(RESTAURANT_ID, "D2Y CODING", review);
        client.enqueue(new Callback<PostReviewResponse>() {
            @Override
            public void onResponse(Call<PostReviewResponse> call, Response<PostReviewResponse> response) {
                _isLoading.setValue(false);

                if(response.isSuccessful()){
                    if (response.body() != null){
                        _listReview.setValue(response.body().getCustomerReviews());
                        _snackbarText.setValue(new Event<>(response.body().getMessage()));
                    } else {
                        if (response.body() != null) {
                            Log.e(TAG, "onFailure: " + response.body().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PostReviewResponse> call, Throwable t) {
                _isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
