package hkucs.borrowmetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ItemView extends AppCompatActivity {

    CarouselView carouselView;
    TextView title, description, price, category;
    Item item;
    ImageListener imageListener;
    Button rentButton;
    Boolean canLoadImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        final String itemKey = getIntent().getExtras().getString("item_key");
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("items").child(itemKey);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    item = dataSnapshot.getValue(Item.class);
                    buildUI();
                }
                else
                    Log.d("ITEMVIEW", itemKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void buildUI() {

        title = findViewById(R.id.item_title);
        title.setText(item.getTitle());

        imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(imageView.getContext()).load(item.getPhotoUrl()).into(imageView);
            }
        };
        carouselView = findViewById(R.id.item_carousel);
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(1);

        description = findViewById(R.id.item_desc);
        description.setText(item.getDescription());

        price = findViewById(R.id.item_price);
        price.setText(String.format("%s%.2f", "$", item.getPrice()));

        category = findViewById(R.id.item_category);
        category.setText(String.format("%s: %s", "Category", item.getCategory()));

        rentButton = findViewById(R.id.rent_button);
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("id", item.getOwnerEmail());
                startActivity(i);
            }
        });
    }

}
