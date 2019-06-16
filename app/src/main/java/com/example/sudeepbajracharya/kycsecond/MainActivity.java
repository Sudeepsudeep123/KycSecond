package com.example.sudeepbajracharya.kycsecond;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sudeepbajracharya.kycsecond.Adapter.KycAdapter;
import com.example.sudeepbajracharya.kycsecond.Adapter.UploadAdapter;
import com.example.sudeepbajracharya.kycsecond.ApiClient.ApiClient;
import com.example.sudeepbajracharya.kycsecond.KycInterface.KycInterface;
import com.example.sudeepbajracharya.kycsecond.KycInterface.OnDataItemClickListener;
import com.example.sudeepbajracharya.kycsecond.Model.DataModel;
import com.example.sudeepbajracharya.kycsecond.Model.KycModel;
import com.example.sudeepbajracharya.kycsecond.Model.UploadModel;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private int counter = 0;
    private int counterUpload = 0;
    KycAdapter kycAdapter;
    UploadAdapter uploadAdapter;
    private ArrayList<KycModel> searchedItem;
    List<KycModel> kycModels;
    private RecyclerView itemRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private File destination = null;
    private InputStream inputStreamImg;
    int p = 0;
    private KycInterface kycInterface;
    private String description = "";
    List<DataModel> dataModelList;
    private Bitmap defaultDraw;
    private Bitmap sampleSignature;
    private ProgressBar progressKYC;
    private Bitmap bitmapSignature;
    private Bitmap bitmapPhotograph;
    private Bitmap bitmapCitizenshipFront;
    private Bitmap bitmapCitizenshipBack;
    private Bitmap bitmapLicense;
    private Bitmap bitmapLicenseBack;
    private Bitmap bitmapPassport;
    private Bitmap bitmapvoterId;

    private String imgSignaturePath = "";
    private String imgPhotoPath = "";
    private String imgCitizenshipPathFront = "";
    private String imgCitizenshipPathBack = "";
    private String imgLicensePath = "";
    private String imgLicenseBackPath = "";
    private String imgPassportPath = "";
    private String imgVoterIdtPath = "";

    String SignatureExtension = "";
    String photoExtension = "";
    String citizenshipFrontExtension = "";
    String citizenshipBackExtension = "";
    String LicenseExtension = "";
    String licenseBackExtension = "";
    String passportExtension = "";
    String voterIdExtension = "";

    private boolean boolSignature = false;
    private boolean boolPhotograph = false;
    private boolean boolCitizenshipFront = false;
    private boolean boolCitizenshipBack = false;

    Button btnSubmitKycForm;

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2,REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        defaultDraw = BitmapFactory.decodeResource(getResources(), R.drawable.defaultcamera);
        sampleSignature = BitmapFactory.decodeResource(getResources(), R.drawable.sample_signature);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        createExampleList();
        buildRecyclerView1();
        getData();
        Log.e("progress","progress");

        btnSubmitKycForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressKYC.setVisibility(View.VISIBLE);
                btnSubmitKycForm.setClickable(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if(imgSignaturePath!="" && imgSignaturePath!=null && !imgSignaturePath.equals("") && !imgSignaturePath.equals(null)) {
                            uploadToServer(SignatureExtension, imgSignaturePath,"client Signature");
                        }
                        if(imgPhotoPath!="" && imgPhotoPath!=null && !imgPhotoPath.equals("") && !imgPhotoPath.equals(null)){
                            uploadToServer(photoExtension, imgPhotoPath,"Photo");

                        }
                        if(imgCitizenshipPathFront!="" && imgCitizenshipPathFront!=null && !imgCitizenshipPathFront.equals("") && !imgCitizenshipPathFront.equals(null)){
                            uploadToServer(citizenshipFrontExtension, imgCitizenshipPathFront,"Citizenship Front");

                        }
                        if(imgCitizenshipPathBack!="" && imgCitizenshipPathBack!=null && !imgCitizenshipPathBack.equals("") && !imgCitizenshipPathBack.equals(null)){
                            uploadToServer(citizenshipBackExtension, imgCitizenshipPathBack,"Citizenship Back");

                        }
                        if(imgLicensePath!="" && imgLicensePath!=null && !imgLicensePath.equals("") && !imgLicensePath.equals(null)){
                            uploadToServer(LicenseExtension, imgLicensePath, "License");
                        }
//                        if(imgLicenseBackPath!="" && imgLicenseBackPath!=null && !imgLicenseBackPath.equals("") && !imgLicenseBackPath.equals(null)){
//                            uploadToServer(licenseBackExtension, imgLicenseBackPath, "License Back");
//
//                        }
                        if(imgPassportPath!="" && imgPassportPath!=null && !imgPassportPath.equals("") && !imgPassportPath.equals(null)){
                            uploadToServer(passportExtension, imgPassportPath, "Passport");

                        }if(imgVoterIdtPath!="" && imgVoterIdtPath!=null && !imgVoterIdtPath.equals("") && !imgVoterIdtPath.equals(null)){
                            uploadToServer(voterIdExtension, imgVoterIdtPath, "Voter's Id");

                        }
                    }
//                    else {
//                        Log.e("here","here");
//                    }
                }
                else {
                    progressKYC.setVisibility(View.VISIBLE);

                    if(imgSignaturePath!="" && imgSignaturePath!=null && !imgSignaturePath.equals("") && !imgSignaturePath.equals(null)) {
                        uploadToServer(SignatureExtension, imgSignaturePath,"client Signature");
                    }
                    if(imgPhotoPath!="" && imgPhotoPath!=null && !imgPhotoPath.equals("") && !imgPhotoPath.equals(null)){
                        uploadToServer(photoExtension, imgPhotoPath,"Photo");

                    }
                    if(imgCitizenshipPathFront!="" && imgCitizenshipPathFront!=null && !imgCitizenshipPathFront.equals("") && !imgCitizenshipPathFront.equals(null)){
                        uploadToServer(citizenshipFrontExtension, imgCitizenshipPathFront,"Citizenship Front");

                    }
                    if(imgCitizenshipPathBack!="" && imgCitizenshipPathBack!=null && !imgCitizenshipPathBack.equals("") && !imgCitizenshipPathBack.equals(null)){
                        uploadToServer(citizenshipBackExtension, imgCitizenshipPathBack,"Citizenship Back");

                    }
                    if(imgLicensePath!="" && imgLicensePath!=null && !imgLicensePath.equals("") && !imgLicensePath.equals(null)){
                        uploadToServer(LicenseExtension, imgLicensePath, "License");

                    }
//                    if(imgLicenseBackPath!="" && imgLicenseBackPath!=null && !imgLicenseBackPath.equals("") && !imgLicenseBackPath.equals(null)){
//                        uploadToServer(licenseBackExtension, imgLicenseBackPath, "License Back");
//
//                    }
                    if(imgPassportPath!="" && imgPassportPath!=null && !imgPassportPath.equals("") && !imgPassportPath.equals(null)){
                        uploadToServer(passportExtension, imgPassportPath, "Passport");

                    }if(imgVoterIdtPath!="" && imgVoterIdtPath!=null && !imgVoterIdtPath.equals("") && !imgVoterIdtPath.equals(null)){
                        uploadToServer(voterIdExtension, imgVoterIdtPath, "Voter's Id");
                    }

                }
            }
        });


    }

    private void getData() {
        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
        Call<List<DataModel>> call = kycInterface.getData();
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                dataModelList = response.body();
                for (DataModel da:dataModelList) {
                    Log.e("Description", da.getDescription() + "    description");
                    description = da.getDescription();

                    if (description == ("client signature") || description.equals("client signature")) {
                            String id = da.getId();
                            kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                            final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                            Log.e("call Image", callImage.request().toString());
                            callImage.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    bitmapSignature = BitmapFactory.decodeStream(response.body().byteStream());
                                    searchedItem.set(0, new KycModel(bitmapSignature, "Signature", "This is the section where owners Post his/her signature."));
                                    buildRecyclerView1();
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("errorInGettingImage", t.getMessage());
                                }
                            });
                        }

                    if (description == ("Photo") || description.equals("Photo")) {
                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapPhotograph = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(1, new KycModel(bitmapPhotograph, "Photo", "This is the section where owners Post his/her Photo."));
                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

                    if (description == ("Citizenship") || description.equals("Citizenship")) {
                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapCitizenshipFront = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(2, new KycModel(bitmapCitizenshipFront, "Citizenship", "This is the section where owners Post his/her Citizenship Front Picture."));
                                progressKYC.setVisibility(View.GONE);

                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

                    if (description == ("Citizenship Back") || description.equals("Citizenship Back")) {
                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapCitizenshipBack = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(3, new KycModel(bitmapCitizenshipBack, "Citizenship Back", "This is the section where owners Post his/her Citizenship Back Picture."));
                                //progressKYC.setVisibility(View.GONE);

                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

                    if (description == ("License") || description.equals("License")) {
                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapLicense = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(4, new KycModel(bitmapLicense, "License", "This is the section where owners Post his/her License."));
                                //progressKYC.setVisibility(View.GONE);

                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

//                    if (description == ("License Back") || description.equals("License Back")) {
//                        String id = da.getId();
//                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
//                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
//                        Log.e("call Image", callImage.request().toString());
//                        callImage.enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                bitmapLicenseBack = BitmapFactory.decodeStream(response.body().byteStream());
//                                searchedItem.set(5, new KycModel(bitmapLicenseBack, "License Back", "his is the section where owners details are available."));
//                                        //progressKYC.setVisibility(View.GONE);
//
//                                buildRecyclerView1();
//                            }
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                Log.e("errorInGettingImage", t.getMessage());
//                            }
//                        });
//                    }

                    if (description == ("Passport") || description.equals("Passport")) {
                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapPassport = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(6, new KycModel(bitmapPassport, "Passport", "This is the section where owners Post his/her Passport."));
                                //progressKYC.setVisibility(View.GONE);

                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

                    if (description == ("Voter's Id") || description.equals("Voter's Id")) {

                        String id = da.getId();
                        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
                        final Call<ResponseBody> callImage = kycInterface.getImage(Integer.parseInt(id));
                        Log.e("call Image", callImage.request().toString());
                        callImage.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                bitmapvoterId = BitmapFactory.decodeStream(response.body().byteStream());
                                searchedItem.set(7, new KycModel(bitmapvoterId, "Voter's Id", "This is the section where owners Post his/her Voter's Id."));
                                //progressKYC.setVisibility(View.GONE);

                                buildRecyclerView1();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("errorInGettingImage", t.getMessage());
                            }
                        });
                    }

                }
                progressKYC.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Log.e("failure", t.getMessage());
            }
        });

    }

    private void uploadToServer(String extension, String filePath, String Description) {
        kycInterface = ApiClient.getApiClient().create(KycInterface.class);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/" + extension), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody fileDescription = RequestBody.create(MediaType.parse("text/plain"), Description);

        Call<UploadModel> fileUpload = kycInterface.uploadImage(fileToUpload, fileName, fileDescription);
        fileUpload.enqueue(new Callback<UploadModel>() {
            @Override
            public void onResponse(Call<UploadModel> call, Response<UploadModel> response) {
                Toast.makeText(MainActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
                counterUpload++;
                if(counterUpload == UploadAdapter.count && counter!=0){
                    //Toast.makeText(MainActivity.this, "Uploaded: " + counterUpload +"=" + counter, Toast.LENGTH_SHORT).show();

                   //Todo
                   //    Redirect after uload is complete
                    progressKYC.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UploadModel> call, Throwable t) {
                Log.e("error",t.getMessage() + "");
            }
        });
    }

    private void buildRecyclerView1() {
        itemRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        kycAdapter = new KycAdapter(searchedItem,getApplicationContext());
        kycAdapter.setOnDataItemClickListener(new OnDataItemClickListener() {
            @Override
            public void onDataClick(KycModel data, int position) {
                if(position == 0 && (bitmapSignature==null)) {

                    p = position;
                    selectImage();
                }
               
               else if(position == 1 && (bitmapPhotograph==null)) {
                    p = position;
                    selectImage();
                } 
                
               else if(position == 2 && (bitmapCitizenshipFront==null)) {
                    p = position;
                    selectImage();
                } 
                
                else if(position == 3 && (bitmapCitizenshipBack==null)) {
                    p = position;
                    selectImage();
                } 
                
               else if(position == 4 && (bitmapLicense==null)) {
                    p = position;
                    selectImage();
                } 
                
//                else if(position == 5 && (bitmapLicenseBack==null)) {
//                    p = position;
//                    selectImage();
//                } 
                
               else if(position == 5 && (bitmapPassport==null)) {
                    p = position;
                    selectImage();
                } 
                
               else if(position == 6 && (bitmapvoterId==null)) {
                    p = position;
                    selectImage();
                }

               else {
                   if(position == 0 && (bitmapSignature!=null)){
                       showImage(bitmapSignature,"Signature");
                   }
                    if(position == 1 && (bitmapPhotograph!=null)) {
                      showImage(bitmapPhotograph,"Passport Size Photo");
                   }

                    if(position == 2 && (bitmapCitizenshipFront!=null)) {
                       showImage(bitmapCitizenshipFront,"Citizenship Front");
                   }

                   if(position == 3 && (bitmapCitizenshipBack!=null)) {
                       showImage(bitmapCitizenshipBack,"Citizenship Back");

                   }

                   if(position == 4 && (bitmapLicense!=null)) {
                       showImage(bitmapLicense,"License");

                   }

//                   if(position == 5 && (bitmapLicenseBack!=null)) {
//                       showImage(bitmapLicenseBack,"License Back");
//                   }

                   if(position == 5 && (bitmapPassport!=null)) {
                       showImage(bitmapPassport,"Passport");

                   }

                   if(position == 6 && (bitmapvoterId!=null)) {

                       showImage(bitmapvoterId,"Voter's Id");
                   }


                   // Toast.makeText(MainActivity.this, "Cannot change picture", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        itemRecyclerView.setLayoutManager(mLayoutManager);
        itemRecyclerView.setAdapter(kycAdapter);
        kycAdapter.notifyDataSetChanged();
    }

    private void selectImage() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_CAMERA);
            PackageManager pm = this.getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, this.getPackageName());

            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "View Sample", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {


                        if (options[item].equals("Take Photo")) {
                            counter++;
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        }

                        else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            counter++;
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                        }

                        else if (options[item].equals("View Sample")) {
                            try {

                                if (p == 0) {
                                    Bitmap bitSignature = BitmapFactory.decodeResource(getResources(), R.drawable.sample_signature);
                                    showImage(bitSignature,"Signature");
                                }
                                if (p == 1) {
                                    Bitmap bitSignature = BitmapFactory.decodeResource(getResources(), R.drawable.pp_photo);
                                    showImage(bitSignature,"Passport Size Photo");
                                }
                                if (p == 2) {
                                    Bitmap citizenfront = BitmapFactory.decodeResource(getResources(), R.drawable.citizenfront);
                                    showImage(citizenfront,"Citizenship Front");
                                }
                                if (p == 3) {
                                    Bitmap citizenback = BitmapFactory.decodeResource(getResources(), R.drawable.citizenback);
                                    showImage(citizenback,"Citizenship Back");
                                }
                                if (p == 4) {
                                    Bitmap liscense = BitmapFactory.decodeResource(getResources(), R.drawable.liscense);
                                    showImage(liscense,"License");
                                }
//                                if (p == 5) {
//                                    Bitmap bitSignature = BitmapFactory.decodeResource(getResources(), R.drawable.citizenshipfront);
//                                    showImage(bitSignature,"License back");
//                                }
                                if (p == 5) {
                                    Bitmap bitpassport = BitmapFactory.decodeResource(getResources(), R.drawable.passport);
                                    showImage(bitpassport,"Passport");
                                }
                                if (p == 6) {
                                    Bitmap voterId = BitmapFactory.decodeResource(getResources(), R.drawable.votersid);
                                    showImage(voterId,"Voter's Id");
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Please choose image first", Toast.LENGTH_SHORT).show();
                            }
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showImage(Bitmap b, String title) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.popup_image, null);
        mBuilder.setTitle(title);
        ImageView imgSignature = mView.findViewById(R.id.imgSignature);
        //imgSignature.setImageResource(R.drawable.sample_signature);
        imgSignature.setImageBitmap(b);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Log.e("Activity", "Pick from Camera::>>> ");

                if (p == 0) {
                    bitmapSignature = (Bitmap) data.getExtras().get("data");
                    bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgSignaturePath = getRealPathFromURI(selectedImage);

                    searchedItem.set(0, new KycModel(bitmapSignature, "Signature", "This is the section where owners Post his/her signature."));
                    buildRecyclerView();
                }

                if (p == 1) {
                    bitmapPhotograph = (Bitmap) data.getExtras().get("data");
                    bitmapPhotograph.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgPhotoPath = getRealPathFromURI(selectedImage);

                    searchedItem.set(1, new KycModel(bitmapPhotograph, "Photograph", "This is the section where owners Post his/her Photograph."));
                    buildRecyclerView();
                }
                if (p == 2) {
                    bitmapCitizenshipBack = (Bitmap) data.getExtras().get("data");

                    bitmapCitizenshipBack.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgCitizenshipPathFront = getRealPathFromURI(selectedImage);

                    searchedItem.set(2, new KycModel(bitmapCitizenshipBack, "Citizenship Front", "This is the section where owners Post his/her Citizenship Front Picture."));
                    buildRecyclerView();
                }
                if (p == 3) {
                    bitmapCitizenshipBack = (Bitmap) data.getExtras().get("data");
                    bitmapCitizenshipBack.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgCitizenshipPathBack = getRealPathFromURI(selectedImage);
                    searchedItem.set(3, new KycModel(bitmapCitizenshipBack, "Citizenship Back", "This is the section where owners Post his/her Citizenship Back Picture"));
                    buildRecyclerView();
                }
                //License
                if (p == 4) {
                    bitmapLicense = (Bitmap) data.getExtras().get("data");
                    bitmapLicense.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgLicensePath = getRealPathFromURI(selectedImage);
                    searchedItem.set(4, new KycModel(bitmapLicense, "License", "This is the section where owners Post his/her License Picture"));
                    buildRecyclerView();
                }
                //license back
//                if (p == 5) {
//                    System.out.println("in p5");
//                    bitmapLicenseBack = (Bitmap) data.getExtras().get("data");
//                    bitmapLicenseBack.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//                    imgLicenseBackPath = getRealPathFromURI(selectedImage);
//                    searchedItem.set(5, new KycModel(bitmapLicenseBack, "License Back", "This is the section where owners Post his/her License Back Picture"));
//                    buildRecyclerView1();
//                }
                //passport
                if (p == 5) {
                    System.out.println("in p6");
                    bitmapPassport = (Bitmap) data.getExtras().get("data");
                    bitmapPassport.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgPassportPath = getRealPathFromURI(selectedImage);
                    searchedItem.set(6, new KycModel(bitmapPassport, "Passport", "This is the section where owners Post his/her Passport Picture"));
                    buildRecyclerView();
                }
                //voter's Id
                if (p == 6) {
                    System.out.println("in p6");
                    bitmapvoterId = (Bitmap) data.getExtras().get("data");
                    bitmapvoterId.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgVoterIdtPath = getRealPathFromURI(selectedImage);
                    searchedItem.set(6, new KycModel(bitmapvoterId, "Voter's Id", "This is the section where owners Post his/her Voter's Id"));
                    buildRecyclerView();
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (boolSignature == true && boolPhotograph == false && boolCitizenshipFront == false && boolCitizenshipBack == false) {
//                    imgSignaturePath = destination.getAbsolutePath();
//                    Log.e("imgSignaturePath", imgSignaturePath + "");
//                }
//                if (boolSignature == false && boolPhotograph == true && boolCitizenshipFront == false && boolCitizenshipBack == false) {
//                    imgPhotoPath = destination.getAbsolutePath();
//                    Log.e("imgPhotoPath", imgPhotoPath + "");
//                }
//                if (boolSignature == false && boolPhotograph == false && boolCitizenshipFront == true && boolCitizenshipBack == false) {
//                    imgCitizenshipPathFront = destination.getAbsolutePath();
//                    Log.e("imgCitizenshipPathFront", imgCitizenshipPathFront + "");
//                }
//                if (boolSignature == false && boolPhotograph == false && boolCitizenshipFront == false && boolCitizenshipBack == true) {
//                    imgCitizenshipPathBack = destination.getAbsolutePath();
//                    Log.e("imgCitizenshipPathBack", imgCitizenshipPathBack + "");
//                }                           
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Log.e("Activity", "Pick from Gallery::>>> ");

                //Signature
                if (p == 0) {
                    bitmapSignature = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgSignaturePath = getRealPathFromURI(selectedImage);
                    SignatureExtension = imgSignaturePath.substring(imgSignaturePath.lastIndexOf("."));
                    searchedItem.set(0, new KycModel(bitmapSignature, "Signature", "This is the section where owners Post his/her signature."));
                    buildRecyclerView();
                }
                //Photograph
                if (p == 1) {
                    bitmapPhotograph = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapPhotograph.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgPhotoPath = getRealPathFromURI(selectedImage);
                    photoExtension = imgPhotoPath.substring(imgPhotoPath.lastIndexOf("."));
                    Log.e("Path of p1", imgPhotoPath);
                    searchedItem.set(1, new KycModel(bitmapPhotograph, "Photograph", "This is the section where owners Post his/her Photograph"));
                    buildRecyclerView();
                }
                //citizenship front
                if (p == 2) {
                    Toast.makeText(this, "here second", Toast.LENGTH_SHORT).show();
                    System.out.println("in p2");
                    bitmapCitizenshipFront = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapCitizenshipFront.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgCitizenshipPathFront = getRealPathFromURI(selectedImage);
                    citizenshipFrontExtension = imgCitizenshipPathFront.substring(imgCitizenshipPathFront.lastIndexOf("."));

                    Log.e("Path of p2", imgCitizenshipPathFront);
                    searchedItem.set(2, new KycModel(bitmapCitizenshipFront, "Citizenship Front", "This is the section where owners Post his/her Citizenship front picture"));
                    buildRecyclerView();
                }
                //citizenship back
                if (p == 3) {
                    System.out.println("in p3");
                    bitmapCitizenshipBack = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapCitizenshipBack.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgCitizenshipPathBack = getRealPathFromURI(selectedImage);
                    citizenshipBackExtension = imgCitizenshipPathBack.substring(imgCitizenshipPathBack.lastIndexOf("."));

                    Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                    Log.e("Path of p3", imgCitizenshipPathBack);
                    searchedItem.set(3, new KycModel(bitmapCitizenshipBack, "Citizenship Back", "This is the section where owners Post his/her Citizenship back picture"));
                    buildRecyclerView();
                }
                //License
                if (p == 4) {
                    Toast.makeText(this, "here second", Toast.LENGTH_SHORT).show();
                    System.out.println("in p4");
                    bitmapLicense = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapLicense.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgLicensePath = getRealPathFromURI(selectedImage);
                    LicenseExtension = imgLicensePath.substring(imgLicensePath.lastIndexOf("."));

                    Log.e("Path of p4", imgLicensePath);
                    searchedItem.set(4, new KycModel(bitmapLicense, "License", "This is the section where owners Post his/her License picture."));
                    buildRecyclerView();
                }
                //license back
//                if (p == 5) {
//                    System.out.println("in p5");
//                    bitmapLicenseBack = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                    bitmapLicenseBack.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//                    imgLicenseBackPath = getRealPathFromURI(selectedImage);
//                    licenseBackExtension = imgLicenseBackPath.substring(imgLicenseBackPath.lastIndexOf("."));
//
//                    Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
//                    Log.e("Path of p5", imgLicenseBackPath);
//                    searchedItem.set(5, new KycModel(bitmapLicenseBack, "License Back", "This is the section where owners Post his/her  License back picture."));
//                    buildRecyclerView1();
//                }
                //passport
                if (p == 5) {
                    System.out.println("in p6");
                    bitmapPassport = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapPassport.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgPassportPath = getRealPathFromURI(selectedImage);
                    passportExtension = imgPassportPath.substring(imgPassportPath.lastIndexOf("."));

                    Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                    Log.e("Path of p6", imgPassportPath);
                    searchedItem.set(6, new KycModel(bitmapPassport, "Passport", "This is the section where owners Post his/her Passport picture."));
                    buildRecyclerView();
                }
                //Voter's ID
                if (p == 6) {
                    System.out.println("in p7");
                    bitmapvoterId = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    bitmapvoterId.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgVoterIdtPath = getRealPathFromURI(selectedImage);
                    voterIdExtension = imgVoterIdtPath.substring(imgVoterIdtPath.lastIndexOf("."));

                    Log.e("Path of p7", imgVoterIdtPath);
                    searchedItem.set(6, new KycModel(bitmapvoterId, "Voter's Id", "This is the section where owners Post his/her Voter's Id"));
                    buildRecyclerView();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buildRecyclerView() {
        itemRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        uploadAdapter = new UploadAdapter(searchedItem,getApplicationContext(),counter);
        uploadAdapter.setOnDataItemClickListener(new OnDataItemClickListener() {
            @Override
            public void onDataClick(KycModel data, int position) {
                if(position == 0 && (bitmapSignature==null)) {

                    p = position;
                    selectImage();
                }

                else if(position == 1 && (bitmapPhotograph==null)) {
                    p = position;
                    selectImage();
                }

                else if(position == 2 && (bitmapCitizenshipFront==null)) {
                    p = position;
                    selectImage();
                }

                else if(position == 3 && (bitmapCitizenshipBack==null)) {
                    p = position;
                    selectImage();
                }

                else if(position == 4 && (bitmapLicense==null)) {
                    p = position;
                    selectImage();
                }

                else if(position == 5 && (bitmapPassport==null)) {
                    p = position;
                    selectImage();
                }

                else if(position == 6 && (bitmapvoterId==null)) {
                    p = position;
                    selectImage();
                }

                else {
                    if(position == 0 && (bitmapSignature!=null)){
                        showImage(bitmapSignature,"Signature");
                    }
                    if(position == 1 && (bitmapPhotograph!=null)) {
                        showImage(bitmapPhotograph,"Passport Size Photo");
                    }

                    if(position == 2 && (bitmapCitizenshipFront!=null)) {
                        showImage(bitmapCitizenshipFront,"Citizenship Front");
                    }

                    if(position == 3 && (bitmapCitizenshipBack!=null)) {
                        showImage(bitmapCitizenshipBack,"Citizenship Back");

                    }

                    if(position == 4 && (bitmapLicense!=null)) {
                        showImage(bitmapLicense,"License");

                    }

//                   if(position == 5 && (bitmapLicenseBack!=null)) {
//                       showImage(bitmapLicenseBack,"License Back");
//                   }

                    if(position == 5 && (bitmapPassport!=null)) {
                        showImage(bitmapPassport,"Passport");

                    }

                    if(position == 6 && (bitmapvoterId!=null)) {

                        showImage(bitmapvoterId,"Voter's Id");
                    }


                    // Toast.makeText(MainActivity.this, "Cannot change picture", Toast.LENGTH_SHORT).show();
                }

            }
        });
        itemRecyclerView.setLayoutManager(mLayoutManager);
        itemRecyclerView.setAdapter(uploadAdapter);
        uploadAdapter.notifyDataSetChanged();
    }

    private void createExampleList() {
        searchedItem.add(new KycModel(defaultDraw, "Signature", "This is the section where owners Post his/her signature."));
        searchedItem.add(new KycModel(defaultDraw, "Photograph", "This is the section where owners Post his/her Photograph"));
        
        searchedItem.add(new KycModel(defaultDraw, "Citizenship Front", "This is the section where owners Post his/her Citizenship front picture"));
        searchedItem.add(new KycModel(defaultDraw, "Citizenship Back", "This is the section where owners Post his/her Citizenship Back picture"));
        
        searchedItem.add(new KycModel(defaultDraw, "License", "This is the section where owners Post his/her License picture."));
//        searchedItem.add(new KycModel(defaultDraw, "License Back", "This is the section where owners Post his/her  License back picture"));
        
        searchedItem.add(new KycModel(defaultDraw, "Passport", "This is the section where owners Post his/her Passport picture."));
        searchedItem.add(new KycModel(defaultDraw, "Voter's Id", "This is the section where owners Post his/her Voter's Id"));
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void initialize() {
        searchedItem = new ArrayList<>();
        itemRecyclerView = findViewById(R.id.rvKYC);
        btnSubmitKycForm = findViewById(R.id.btnSubmitKycForm);
        progressKYC = findViewById(R.id.progressKYC);

    }
}
