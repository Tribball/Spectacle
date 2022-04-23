package com.mvye.spectacle.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
<<<<<<< Updated upstream
=======
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.LoginActivity;
>>>>>>> Stashed changes
=======
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
>>>>>>> main
import com.mvye.spectacle.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

<<<<<<< HEAD
<<<<<<< Updated upstream
=======
=======
>>>>>>> main
    public static final String TAG = "ProfileFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 63;
    public static final int PICK_PHOTO_CODE = 85;
    private Button btnTakeImage;
    private Button btnOpenGallery;
<<<<<<< HEAD
    private Button btnLogout;
=======
>>>>>>> main
    private ImageView ivProfile;
    private TextView tvUsername;
    private File photoFile;
    public String photoFileName = "photo.jpg";


<<<<<<< HEAD
>>>>>>> Stashed changes
=======
>>>>>>> main
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
<<<<<<< HEAD
<<<<<<< Updated upstream
=======
=======
>>>>>>> main

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTakeImage = view.findViewById(R.id.btnTakeImage);
        btnOpenGallery = view.findViewById(R.id.btnGallery);
<<<<<<< HEAD
        btnLogout = view.findViewById(R.id.btnLogout);
=======
>>>>>>> main
        ivProfile = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvUsername);

        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { pickPhoto(); }
        });
        setCurrentProfilePicture();
<<<<<<< HEAD
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });
=======
>>>>>>> main
    }

    private void setCurrentProfilePicture() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = (ParseFile) user.get("profilePicture");
        Glide.with(requireContext()).load(file.getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(ivProfile);
    }

    private void launchCamera() {
        Log.d(TAG, "Opening camera");
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileproviderr", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfile.setImageBitmap(takenImage);
                setProfilePicture(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setProfilePicture(Bitmap takenImage) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilePicture", saveProfilePicture(takenImage));
        user.saveInBackground();
        Toast.makeText(getContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
    }

    private ParseFile saveProfilePicture(Bitmap takenImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        takenImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapBytes = stream.toByteArray();
        ParseFile newProfilePicture = new ParseFile("profilePicture.png", bitmapBytes);
        try {
            newProfilePicture.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newProfilePicture;
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void pickPhoto(){
        // Create intent for picking a photo from the gallery
        Log.d(TAG, "Entered pickPhoto method");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            Log.d(TAG, "Result is not null :)");
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Log.d(TAG, "Entering loadFromUri method");
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

<<<<<<< HEAD
    private void logOutUser() {
        ParseUser.logOut();
        goToMainActivity();
        Toast.makeText(getContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);

    }

>>>>>>> Stashed changes
=======
>>>>>>> main
}