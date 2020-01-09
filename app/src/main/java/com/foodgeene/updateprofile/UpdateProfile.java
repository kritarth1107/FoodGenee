package com.foodgeene.updateprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import network.FoodGeneeAPI;
import network.RetrofitClient;
import network.ScalarClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    TextInputEditText userName, userEmail, userPassword;
    Button updateButton;
    SessionManager sessionManager;
    String userToken;
    CircleImageView changePic;
    String userIdNew;
    Dialog loadingDialog;
    private static final String TAG = "procheck";
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    public static String SERVER_PATH = "http://sansdigitals.com/phpdemos/foodgenee/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);



        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        initViews();
        changePic.setOnClickListener(view -> {

            changeProfilePic();
        });
        updateButton.setOnClickListener(view -> {
            updateUserDetails();

        });
        setupProfile();


    }

    private void changeProfilePic() {

        selectPicture();

    }

    private void selectPicture() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UpdateProfile.this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){

            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            uri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, UpdateProfile.this);
                File file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                String action = "updateprofilepic";
                action.trim();
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profilepic", file.getName(), mFile);

                FoodGeneeAPI uploadImage = ScalarClient.getApiClient().create(FoodGeneeAPI.class);
                Call<ResponseBody> fileUpload = uploadImage.updatePic(fileToUpload, userIdNew, "updateprofilepic");
                fileUpload.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Toast.makeText(UpdateProfile.this, "Done", Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "Error " + t.getMessage());

                        Toast.makeText(UpdateProfile.this, "Not Done", Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }
                });
            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }


    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void setupProfile() {


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI
                .class);

        Call<UserModel> call = foodGeneeAPI.userDetails("users",userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                try {
                    UserModel retrievedModel = response.body();
                    Users retrievedModelUsers = retrievedModel.getUsers();
                    userIdNew = retrievedModel.getUsers().getId();
                    userName.setText(retrievedModelUsers.getName());
                    userEmail.setText(retrievedModelUsers.getEmail());
                    userPassword.setText(retrievedModelUsers.getMobile());
                    Glide.with(UpdateProfile.this)
                            .load(retrievedModel.getUsers().getProfilepic())
                            .into(changePic);

                }
                catch (Exception e){

                    Toast.makeText(UpdateProfile.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private void updateUserDetails() {

        String newname = userName.getText().toString().trim();
        String newemail = userEmail.getText().toString().trim();
        String newPass = userPassword.getText().toString().trim();

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);

        Call<UpdateModel> call = foodGeneeAPI.updateUser("updation", newname, newemail, newPass,  userToken,"application/x-www-form-urlencoded");

        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                UpdateModel reModel = response.body();
                assert reModel != null;
                if(reModel.getStatus()==1){

                    Toast.makeText(UpdateProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        }
                else if(reModel.getStatus()==0){

                    Toast.makeText(UpdateProfile.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(UpdateProfile.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {

            }
        });

    }

    private void initViews() {
        userName = findViewById(R.id.changeName);
        userEmail = findViewById(R.id.changeEmail);
        userPassword = findViewById(R.id.changePassword);
        userPassword.setEnabled(false);
        updateButton = findViewById(R.id.updateButton);
        changePic = findViewById(R.id.userPic);


    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if(uri != null){
            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            String filePath = getRealPathFromURIPath(uri, UpdateProfile.this);
            File file = new File(filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profilepic", file.getName(), mFile);
            String action = "com/foodgeene/updateprofile";
            action.trim();

            FoodGeneeAPI uploadImage = ScalarClient.getApiClient().create(FoodGeneeAPI.class);
            Call<ResponseBody> fileUpload = uploadImage.updatePic(fileToUpload, userIdNew, "com/foodgeene/updateprofile");
            fileUpload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Toast.makeText(UpdateProfile.this, "Done", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Error " + t.getMessage());
                    Toast.makeText(UpdateProfile.this, "NotDone", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
