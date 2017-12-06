package hkucs.borrowmetest;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewItem extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView uploadedImage;
    private byte[] image;
    private String userChoosenTask = "";
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CODE_PICKER = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef, itemRef, itemImageRef;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://borrowmetest-1199c.appspot.com/");
        itemRef = storageRef.child( System.currentTimeMillis() + "_item.jpg");
        itemImageRef = storageRef.child("images/" + itemRef.getName());


        uploadedImage = (ImageView) findViewById(R.id.uploaded);

        Spinner spinner = (Spinner) findViewById(R.id.et_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final TextInputEditText title = (TextInputEditText) findViewById(R.id.et_title);
        final TextInputEditText description = (TextInputEditText) findViewById(R.id.et_desc);
        final TextInputEditText price = (TextInputEditText) findViewById(R.id.et_price);
        final TextView path = (TextView) findViewById(R.id.image_path);
        final Spinner category = findViewById(R.id.et_category);
        Button photoButton = (Button) this.findViewById(R.id.cam_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        Button submit = (Button) findViewById(R.id.et_submitbutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title.getText())) {
                    title.setError("Title cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(description.getText())) {
                    title.setError("Description cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(price.getText())) {
                    title.setError("Price cannot be empty");
                    return;
                }
                if (image == null) {
                    Toast.makeText(getApplicationContext(), "Please upload an image", Toast.LENGTH_LONG).show();
                    return;
                }

                UploadTask uploadTask = itemRef.putBytes(image);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Image upload failed, please try again", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Item item = new Item(
                                title.getText().toString(),
                                description.getText().toString(),
                                category.getSelectedItem().toString(),
                                Double.parseDouble(price.getText().toString()),
                                downloadUrl.toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()
                        );
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mypostRef = mDatabaseReference.child("items").push();
                        mypostRef.setValue(item);
                        String itemId = mypostRef.getKey();
                        finish();
                    }
                });


            }
        });

        uploadedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                float scale = width / view.getWidth();
                if (view.getScaleX() == 1) {
                    view.setScaleY(scale);
                    view.setScaleX(scale);
                    view.setElevation(1000);
                } else {
                    view.setScaleY(1);
                    view.setScaleX(1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ArrayList<com.esafirm.imagepicker.model.Image> images = (ArrayList<com.esafirm.imagepicker.model.Image>) ImagePicker.getImages(data);
            Bitmap imageBitmap = BitmapFactory.decodeFile(images.get(0).getPath());
            uploadedImage.setImageBitmap(imageBitmap);
            uploadedImage.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            this.image = baos.toByteArray();
        }
    }

    public void getImage(){
        ImagePicker.create(this)
                .single()// Activity or Fragment
                .imageDirectory("BorrowMe")
                .start(REQUEST_CODE_PICKER);
    }
}
