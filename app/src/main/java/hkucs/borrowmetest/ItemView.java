package hkucs.borrowmetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ItemView extends AppCompatActivity {

    CarouselView carouselView;
    TextView title;
    TextView description;
    RentItem item;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        carouselView = (CarouselView) findViewById(R.id.item_carousel);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        item = (RentItem) extras.getSerializable("item");



        title = (TextView) findViewById(R.id.item_title);
        title.setText(item.getTitle());
        description = (TextView) findViewById(R.id.item_desc);
        description.setText(item.getDescription());

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

}
