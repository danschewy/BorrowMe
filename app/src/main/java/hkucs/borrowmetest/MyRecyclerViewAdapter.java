package hkucs.borrowmetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.ByteBuffer;
import java.util.ArrayList;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<Item> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        private View mView;
        ImageView image, avail_img;
        TextView title, description, price, available;

        public DataObjectHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image);
            title = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_desc);
            price = (TextView) itemView.findViewById(R.id.card_price);
            available = (TextView) itemView.findViewById(R.id.card_available);
            avail_img = (ImageView) itemView.findViewById(R.id.card_avail_img);

            mView = itemView;
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<Item> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.title.setText(mDataset.get(position).title);
        holder.description.setText(mDataset.get(position).description);
        holder.price.setText(String.format("%s%.2f", "$", mDataset.get(position).price));
        boolean isAvailable = true;
        if(mDataset.get(position).isAvailable)
            isAvailable = false;
        holder.available.setText(isAvailable? "Available":"Not Available");
        holder.available.setCompoundDrawablesWithIntrinsicBounds(isAvailable? R.drawable.ic_check_black_24dp : R.drawable.ic_cancel_black_24dp,0, 0, 0);
        holder.avail_img.setColorFilter(isAvailable? R.color.green_500 : R.color.red_900, PorterDuff.Mode.SRC_ATOP);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://borrowmetest-1199c.appspot.com/");
        StorageReference pathRef = storageRef.child(mDataset.get(position).photoUrl);
        Glide.with(holder.mView.getContext())
                .using(new FirebaseImageLoader()).load(pathRef).into(holder.image);
    }

    public void addItem(Item rentItem, int index) {
        mDataset.add(index, rentItem);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public String getItem(int index){
        return mDataset.get(index).itemId;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
    public void swapData(ArrayList<Item> data){
        mDataset = data;
    }
}