package hkucs.borrowmetest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<RentItem> items = new ArrayList<RentItem>();
    NavigationView navigationView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper db;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter mFAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            //user signed in
        }
        else {
            // no user signed in, redirect to login activity
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //Constructs top toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Construct FAB to add item
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewItem.class);
                startActivity(intent);
            }
        });

        //Contstruct left drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Setup drawer navigation menu (category filters)
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //builds components requiring user data
        buildUI();

        //create categories if need be
        final DatabaseReference categoryReference = mDatabaseReference.child("categories");
        categoryReference.orderByKey();
        final Menu menu = navigationView.getMenu();
        menu.add("All");
        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot c : dataSnapshot.getChildren()){
                    menu.add((String)c.getValue());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        navigationView.invalidate();

        //Build RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Setup RecycleView Adapter
        //Query for all items
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("items");
        setUpFirebaseAdapter(query);
        mFAdapter.startListening();
        mRecyclerView.setAdapter(mFAdapter);

        //Hide FAB on Scroll
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

    //Hide drawer on back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Sign out
        if (id == R.id.action_signout){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //MODIFY filters recyclerview with chosen category
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getTitle().equals("All")){
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("items");
            setUpFirebaseAdapter(query);
            mRecyclerView.swapAdapter(mFAdapter, true);
        }else {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("items").orderByChild("category").equalTo(item.getTitle().toString());
            setUpFirebaseAdapter(query);
            mRecyclerView.swapAdapter(mFAdapter, true);
        }
        mFAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //MODIFY. When coming back to main activity, refreshes recyclerview
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void buildUI(){
        buildNavHeader();
    }

    //setup drawer header with profile information
    public void buildNavHeader() {
        View navheadView = navigationView.getHeaderView(0);
        LinearLayout navhead = navheadView.findViewById(R.id.navheader);
        TextView head_name = navhead.findViewById(R.id.head_name);
        head_name.setText(mAuth.getCurrentUser().getDisplayName());
        TextView head_email = navhead.findViewById(R.id.head_email);
        head_email.setText(mAuth.getCurrentUser().getEmail());
        navhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("uEmail", mAuth.getCurrentUser().getEmail());
                startActivity(intent);
            }
        });
    }

    public void setUpFirebaseAdapter(Query query){
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();

        mFAdapter = new FirebaseRecyclerAdapter<Item, ItemHolder>(options) {

            @Override
            public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view, parent, false);

                return new ItemHolder(view);
            }

            @Override
            protected void onBindViewHolder(ItemHolder holder, final int position, Item model) {
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
                holder.available.setText(model.isAvailable()? "Available":"Not Available");
                holder.available.setCompoundDrawablesWithIntrinsicBounds(model.isAvailable()? R.drawable.ic_check_black_24dp : R.drawable.ic_cancel_black_24dp,0, 0, 0);
                holder.avail_img.setColorFilter(model.isAvailable()? R.color.green_500 : R.color.red_900, PorterDuff.Mode.SRC_ATOP);
                holder.price.setText(Double.toString(model.getPrice()));
                Glide.with(holder.image.getContext()).load(model.getPhotoUrl()).into(holder.image);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, ItemView.class);
                        i.putExtra("item_key", mFAdapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });
            }

        };

    }

    public static class ItemHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        MyClickListener myClickListener;
        View mView;
        ImageView image, avail_img;
        TextView title, description, price, available;

        public ItemHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            title = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_desc);
            price = itemView.findViewById(R.id.card_price);
            available = itemView.findViewById(R.id.card_available);
            avail_img = itemView.findViewById(R.id.card_avail_img);

            Log.i("MAIN ITEMHOLDER", "Adding Listener");
            mView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

        public interface MyClickListener {
            public void onItemClick(int position, View v);
        }

        public void setOnItemClickListener(MyClickListener myClickListener) {
            this.myClickListener = myClickListener;
        }
    }
}
