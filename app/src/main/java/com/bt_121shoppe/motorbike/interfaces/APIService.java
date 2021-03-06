package com.bt_121shoppe.motorbike.interfaces;

import com.bt_121shoppe.motorbike.models.NotificationSenderViewModel;
import com.bt_121shoppe.motorbike.notifications.MyResponse;
import com.bt_121shoppe.motorbike.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAc-OYK_o:APA91bFUyiHEPdYUjVatqxaVzfLPwVcd090bMY5emPPh-ubQtu76mEDAdmthgR03jYwhClbDqy0lqbSr_HAAvD0vnTqigM16YH4x-Xr1TMb3q_sz9PLtjNLpfnLi6NdCI-v6dyX6-5jB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

    @POST("fcm/send")
    Call<MyResponse> sendNotification1(@Body NotificationSenderViewModel body);
}
