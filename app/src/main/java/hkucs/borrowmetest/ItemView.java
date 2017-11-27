package hkucs.borrowmetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ItemView extends AppCompatActivity {

    CarouselView carouselView;
    TextView title, description, price;
    RentItem item;
    private DatabaseHelper db;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        db = new DatabaseHelper(getApplicationContext());

        carouselView = (CarouselView) findViewById(R.id.item_carousel);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        item = db.getItemById(extras.getInt("item"));

        title = (TextView) findViewById(R.id.item_title);
        title.setText(item.getTitle());

        description = (TextView) findViewById(R.id.item_desc);
        description.setText(item.getDescription());

        title = (TextView) findViewById(R.id.item_price);
        title.setText(String.format("%s%.2f", "$", item.getPricePerHour()));

        Button rentButton = (Button) findViewById(R.id.rent_button);
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setAvailable(0);
                db.updateItem(item);
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                int u = db.getItemUser(item.getId()).getId();
                i.putExtra("id", u);
                startActivity(i);
            }
        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
