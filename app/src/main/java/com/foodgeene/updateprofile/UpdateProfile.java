package com.foodgeene.updateprofile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.forgot.ChangePassword;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;
import com.google.android.material.textfield.TextInputEditText;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import network.ConnectivityReceiver;
import network.FileUtils;
import network.FoodGeneeAPI;
import network.MyApplication;
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

public class UpdateProfile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, ConnectivityReceiver.ConnectivityReceiverListener {

    TextInputEditText userName, userEmail, userPassword;
    Button updateButton;
    SessionManager sessionManager;
    String userToken;
    CircleImageView changePic;
    String userIdNew;
    Dialog loadingDialog;
    Uri imageUri;
    ImageView mIvBack;
    LinearLayout mLLChange;
    private static final String TAG = "procheck";
    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final int WRITE_REQUEST_CODE = 400;
    private static final  int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private Uri uri;
    public static String SERVER_PATH = "http://sansdigitals.com/phpdemos/foodgenee/api/";

    String UPLOAD_URL = "http://sansdigitals.com/phpdemos/foodgenee/api/users/user-profilepic.php";
    boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        mIvBack=findViewById(R.id.iv_back);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        isOnline=ConnectivityReceiver.isConnected();
        initViews();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changePic.setOnClickListener(view -> {

            changeProfilePic();
        });
        updateButton.setOnClickListener(view -> {
            if(isOnline)
            updateUserDetails();
            else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

        });
        mLLChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(UpdateProfile.this, ChangePassword.class);
                intent.putExtra("userId", userIdNew);
                startActivity(intent);
            }
        });

        if(isOnline)
        setupProfile();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();



    }




    private void changeProfilePic() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    checkPermission();

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    selectPicture();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


       // selectPicture();

    }

    private void selectPicture() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
    }

    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(UpdateProfile.this,Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                UpdateProfile.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(
                UpdateProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    UpdateProfile.this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    UpdateProfile.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    UpdateProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                UpdateProfile.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                PERMISSIONS_MULTIPLE_REQUEST
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                ActivityCompat.requestPermissions(
                        UpdateProfile.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSIONS_MULTIPLE_REQUEST
                );
            }
        }else {
            // Do something, when permissions are already granted
            //Toast.makeText(UpdateProfile.this,"Permissions already granted",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_MULTIPLE_REQUEST:{
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);

                }else {
                    // Permissions are denied
                    Toast.makeText(UpdateProfile.this,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }


        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UpdateProfile.this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {

            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(true);
            uri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = FileUtils.getPath(UpdateProfile.this, uri);
              // uploadMultipart(filePath);
                uploadMultipart(decodeFile(filePath,600,600));


            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if(requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {


            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                changePic.setImageBitmap(bitmap);
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    String path = FileUtils.getPath(UpdateProfile.this, imageUri);
                    loadingDialog = new Dialog(this);
                    loadingDialog.setContentView(R.layout.loading_dialog_layout);
                    loadingDialog.show();
                    loadingDialog.setCancelable(true);
                    loadingDialog.setCanceledOnTouchOutside(true);
                    uploadMultipart(decodeFile(path,600,600));
                   // uploadMultipart(path);
                } else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

           /* Bitmap photo = (Bitmap) data.getExtras().get("data");

            Uri imageUri = FileUtils.getImageUri(UpdateProfile.this, photo);
            changePic.setImageBitmap(photo);*/





        }


    }
    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

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

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
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
        mLLChange=findViewById(R.id.ll_change);


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

    public void uploadMultipart(String path) {

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {


            try {
                new  MultipartUploadRequest(UpdateProfile.this, "http://sansdigitals.com/phpdemos/foodgenee/api/users/user-profilepic.php")
                        .setMethod("POST")
                        .addFileToUpload(path, "profilepic")
                        .addHeader("Authorization",userToken)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                                Log.e("error","errir");
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                                Log.e("error","errir");
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                Log.d(TAG, "onCompleted: "+serverResponse.getBodyAsString());

                                loadingDialog.dismiss();

                                     try {
                                         JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());

                                         String status = jsonObject.getString("status");
                                         if (status.equals("1")) {
                                             String profilepic = jsonObject.getString("profilepic");

                                             Glide.with(UpdateProfile.this)
                                                     .load(profilepic)
                                                     .into(changePic);
                                         }


                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                            }
                        })
                        .startUpload(); //Starting the upload


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline=isConnected;
    }
}
