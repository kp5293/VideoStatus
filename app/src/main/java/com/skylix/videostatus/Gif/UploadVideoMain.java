package com.skylix.videostatus.Gif;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.skylix.videostatus.R;
import com.skylix.videostatus.tools.Config;
import com.skylix.videostatus.tools.Connectivity;
import com.skylix.videostatus.tools.Functions;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import ir.hamsaa.RtlMaterialSpinner;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Destiny on 10-Jul-17.
 */

public class UploadVideoMain extends Fragment {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0;

    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient = null;

    private LinearLayout signInView, userUploadView;
    private View FragmentView;
    Button btnChooseVideo, btnUploadVideo;
    EditText editEmail, editTitle;
    private static final int SELECT_VIDEO = 3;
    Bitmap bitmap;
    ImageView ivVideoThumbnail;
    String catAlias, selectedFilePath, videoTitle;
    VideoView video_gif;
    RtlMaterialSpinner categorySpinner, languageSpinner;
    String catSpinnerItem, lanSpinnerItem, date, userEmail, selectedImagePath;
    Uri selectedVideoUri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.activity_upload_video, null);

        init(FragmentView);
        return FragmentView;

    }

    private void init(View view) {

        categorySpinner = (RtlMaterialSpinner) view.findViewById(R.id.categorySpinner);
        languageSpinner = (RtlMaterialSpinner) view.findViewById(R.id.languageSpinner);

        signInView = (LinearLayout) view.findViewById(R.id.SignInView);
        userUploadView = (LinearLayout) view.findViewById(R.id.userUploadView);
        btnChooseVideo = (Button) view.findViewById(R.id.btnChooseVideo);
        btnUploadVideo = (Button) view.findViewById(R.id.btnUploadVideo);
        signInButton = (com.google.android.gms.common.SignInButton) view.findViewById(R.id.sign_in_button);
        video_gif = (VideoView) view.findViewById(R.id.video_gif);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editTitle = (EditText) view.findViewById(R.id.editTitle);
        ivVideoThumbnail = (ImageView) view.findViewById(R.id.ivVideoThumbnail);

        Functions.GetCategorySpinnerData(getActivity(), categorySpinner);

        Functions.GetLanguageSpinnerData(getActivity(), languageSpinner);

        btnChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVideoChooser();
            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categorySpinner.getSelectedItemPosition() > 0) {
//                    Toast.makeText(getActivity(), "Category selected....", Toast.LENGTH_SHORT).show();
                    if (languageSpinner.getSelectedItemPosition() > 0) {
//                        Toast.makeText(getActivity(), "Language selected....", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(editEmail.getText().toString())) {
//                            Toast.makeText(getActivity(), "Email Not Empty....", Toast.LENGTH_SHORT).show();
                            if (!TextUtils.isEmpty(editTitle.getText().toString())) {
//                                Toast.makeText(getActivity(), "Title Not Empty....", Toast.LENGTH_SHORT).show();
                                if (selectedVideoUri != null) {
                                    File file = new File(selectedFilePath);
                                    float length = file.length();
                                    length = length / 1024;
                                    length = length / 1024;

                                    Log.e("RESULT", "Original Length:--" + file.length());
                                    Log.e("RESULT", "Video size:--" + length + "MB");

                                    if (length < 5.0) {
                                        userUploadGif();

                                    } else {
                                        Toast.makeText(getActivity(), "Select File less then 5 MB..", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getActivity(), "Please Select Video..", Toast.LENGTH_SHORT).show();                                }
                            } else {
                                editTitle.setError("Enter Title of Video");
                                editTitle.requestFocus();
                            }
                        } else {
                            editEmail.setError("Enter Emaill Address");
                            editEmail.requestFocus();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Select Language....", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Category....", Toast.LENGTH_SHORT).show();
                }


            }
        });

        categorySpinner.setHint("Select Category...");
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    position--;
                    catSpinnerItem = Functions.CategorySpinnerArrayList.get(position).getCat_type();
                    Toast.makeText(getActivity(), catSpinnerItem + "--" + position, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Select Value...", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        languageSpinner.setHint("Select Language...");
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    position--;
//                            Toast.makeText(getActivity(), "Boolean Value" + isSpinnerTouched, Toast.LENGTH_LONG).show();
//                    spinner_item = Functions.DpSpinnerArrayList.get(position).getDp_sub_cat();
//                    Toast.makeText(getActivity(), spinner_item, Toast.LENGTH_LONG).show();
                    lanSpinnerItem = Functions.LanguageSpinnerArrayList.get(position).getLanguage();
                    Toast.makeText(getActivity(), lanSpinnerItem + "--" + position, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Select Value...", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        InitializeGSO();

        FirebaseAuthListener();
        signInButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (Connectivity.getInstance(getActivity()).isOnline()) {
                    signIn();
                } else {

                }

            }
        });
    }

    private void InitializeGSO() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void FirebaseAuthListener() {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (user.getDisplayName() != null) {

                        Log.d(TAG, "USER NAME:-" + user.getDisplayName());
                        Log.d(TAG, "USER Email:-" + user.getEmail());
                        userEmail = user.getEmail();
                        signInView.setVisibility(View.GONE);
//                        btnViewUserUpload.setVisibility(View.VISIBLE);
                        userUploadView.setVisibility(View.VISIBLE);
                        editEmail.setText(user.getEmail());

                    } else {
                        signInView.setVisibility(View.VISIBLE);
//                        btnViewUserUpload.setVisibility(View.GONE);
                        userUploadView.setVisibility(View.GONE);
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Log.d(TAG, "USER NAME" + account.getDisplayName());
                signInView.setVisibility(View.GONE);
                userUploadView.setVisibility(View.VISIBLE);

            } else {
                signInView.setVisibility(View.VISIBLE);
                userUploadView.setVisibility(View.GONE);

            }
        }

        if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {

             selectedVideoUri = data.getData();
            selectedFilePath = Config.getVideoPath(selectedVideoUri, getActivity());

            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = video_gif.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            video_gif.setLayoutParams(params);

            video_gif.setMediaController(null);
            video_gif.setVideoURI(selectedVideoUri);
            video_gif.start();
            video_gif.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0, 0);
                }
            });
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(selectedFilePath,
                    MediaStore.Video.Thumbnails.MINI_KIND);
//            ivVideoThumbnail.setImageBitmap(thumbnail);
            Uri thumbUri = getImageUri(getActivity(), thumbnail);
            Log.d("RESULT", "Selected Path:--" + thumbUri);
            selectedImagePath = Config.getPath(thumbUri, getActivity());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), thumbUri);
                ivVideoThumbnail.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void showVideoChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select  Video "), SELECT_VIDEO);
    }

    public void userUploadGif() {

        videoTitle = editTitle.getText().toString().trim();
        catAlias = Config.createAlias(catSpinnerItem);
        String videoName = Config.renameTitle(videoTitle);
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Config.URL_USER_UPLOAD)
                    .addParameter(Config.KEY_CATEGORY_TYPE, catSpinnerItem)
                    .addParameter(Config.KEY_CATEGORY_ALIAS, catAlias)
                    .addParameter(Config.KEY_TITLE, videoTitle)
                    .addParameter(Config.KEY_LANGUAGE, lanSpinnerItem)
                    .addParameter(Config.KEY_APPROVED, "0")
                    .addParameter(Config.KEY_FAV_COUNTER, "0")
                    .addParameter(Config.KEY_DATE, date)
                    .addFileToUpload(selectedFilePath, "myFile") //Adding file
                    .addParameter("name", videoName)
                    .addFileToUpload(selectedImagePath, "pic") //Adding file
                    .addParameter(Config.KEY_EMAIL, editEmail.getText().toString())
                    .addParameter(Config.KEY_LIKES, "0")
                    .addParameter(Config.KEY_DISLIKES, "0")
                    .setNotificationConfig(new UploadNotificationConfig().setIcon(R.drawable.logo_512)
                            .setErrorMessage("Video Cannot be Upload.."))
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Log.d("RESULT", "--" + exc.getMessage());

            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
