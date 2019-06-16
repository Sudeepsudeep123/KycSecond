package com.example.sudeepbajracharya.kycsecond.KycInterface;

import android.database.Observable;

import com.example.sudeepbajracharya.kycsecond.Model.DataModel;
import com.example.sudeepbajracharya.kycsecond.Model.UploadModel;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface KycInterface {
    @Headers({"Fineract-Platform-TenantId: default", "Authorization: Basic QW5kcm9pZHN1OmFzZGxrZndpeGN2MzM0NQ=="})
    @GET("/fineract-provider/api/v1/clients/1771/documents")
    Call<List<DataModel>> getData();

//TODo
    //make the userId dynamic


    @Headers({"Fineract-Platform-TenantId: default", "Authorization: Basic QW5kcm9pZHN1OmFzZGxrZndpeGN2MzM0NQ=="})
    @GET("/fineract-provider/api/v1/clients/1771/documents/{documentId}/attachment")
    Call<ResponseBody> getImage(@Path("documentId") int id);

    @Multipart
    @Headers({"Fineract-Platform-TenantId: default", "Authorization: Basic QW5kcm9pZHN1OmFzZGxrZndpeGN2MzM0NQ=="})
    @POST("/fineract-provider/api/v1/clients/1771/documents")
    Call<UploadModel> uploadImage(@Part MultipartBody.Part file,
                                  @Part("name") RequestBody requestName,
                                  @Part("description") RequestBody requestDescription);

}
