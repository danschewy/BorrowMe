package hkucs.borrowmetest;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class NewItem extends AppCompatActivity {

    private DatabaseHelper db;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView uploadedImage;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        db = new DatabaseHelper(getApplicationContext());

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
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        Button submit = (Button) findViewById(R.id.et_submitbutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(title.getText())){
                    title.setError("Title cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(description.getText())){
                    title.setError("Description cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(price.getText())){
                    title.setError("Price cannot be empty");
                    return;
                }
                if(image==null){
                    Toast.makeText(getApplicationContext(), "Please upload an image", Toast.LENGTH_LONG).show();
                }
                RentItem item = new RentItem();
                item.setTitle(title.getText().toString());
                item.setDescription(description.getText().toString());
                item.setPricePerHour(Double.parseDouble(price.getText().toString()));
                item.setImage(image); //set image to byte array
                item.setAvailable(1);

                long item_id = db.createItem(item);
                db.createItemCategory((int) item_id, db.getCategoryByName(category.getSelectedItem().toString()).getId());
                finish();
            }
        });

        uploadedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                float scale =  width / view.getWidth();
                if(view.getScaleX() == 1) {
                    view.setScaleY(scale);
                    view.setScaleX(scale);
                    view.setElevation(1000);
                }else{
                    view.setScaleY(1);
                    view.setScaleX(1);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            uploadedImage.setImageBitmap(photo);
            uploadedImage.setVisibility(View.VISIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            this.image = bArray;
        }
        else{
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA);
        }
    }
}
