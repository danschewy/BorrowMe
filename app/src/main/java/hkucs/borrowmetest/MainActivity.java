package hkucs.borrowmetest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper db;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            // user is signed in
        }
        else {
            // no user signed in
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewItem.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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


    View navheadView = navigationView.getHeaderView(0);
    LinearLayout navhead = (LinearLayout) navheadView.findViewById(R.id.navheader);
    mRecyclerView =(RecyclerView)

    findViewById(R.id.recycler_view);
        if(mAuth.getInstance().getCurrentUser() != null) {
            TextView head_name = navhead.findViewById(R.id.head_name);
            head_name.setText(mAuth.getCurrentUser().getDisplayName());
            TextView head_email = navhead.findViewById(R.id.head_email);
            head_email.setText(mAuth.getCurrentUser().getEmail());
        }
        navhead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("uEmail", mAuth.getCurrentUser().getEmail());
                startActivity(intent);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
    mLayoutManager =new

    LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (item.getTitle().equals("All")){
            mAdapter = new MyRecyclerViewAdapter(getDataSet());
            mRecyclerView.swapAdapter(mAdapter, true);
        }else {
            ArrayList<Item> r = getFilteredData((String) item.getTitle());
            mAdapter = new MyRecyclerViewAdapter(r);
            mRecyclerView.swapAdapter(mAdapter, true);
        }
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new
              MyRecyclerViewAdapter.MyClickListener() {
                  @Override
                  public void onItemClick(int position, View v) {
                      Intent intent = new Intent(getApplicationContext(), ItemView.class);
                      String item = ((MyRecyclerViewAdapter) mAdapter).getItem(position);
                      Bundle bundle = new Bundle();
                      bundle.putString("item", item);
                      intent.putExtras(bundle);
                      startActivity(intent);
                  }
              });
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.swapAdapter(mAdapter, true);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<Item> getDataSet() {
        DatabaseReference items = mDatabaseReference.child("items");
        final ArrayList<Item> itemList = new ArrayList<>();
        items.orderByChild("title").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    itemList.add(dataSnapshot.getValue(Item.class));
                    Log.d("LOL","LOL");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return itemList;
    }

    private ArrayList<Item> getFilteredData(String categoryName){
        DatabaseReference items = mDatabaseReference.child("items");
        final ArrayList<Item> filteredItems = new ArrayList<>();
        items.orderByChild("category").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot item : dataSnapshot.getChildren()){
                        String title =  (String) item.child("title").getValue();
                        String description =  (String) item.child("description").getValue();
                        String category =  (String) item.child("category").getValue();
                        String ownerId =  (String) item.child("ownerEmail").getValue();
                        String photoUrl =  (String) item.child("photoUrl").getValue();
                        double price = (Double) item.child("price").getValue();
                        filteredItems.add(new Item(description, title, category, price, photoUrl, ownerId));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return filteredItems;
    }
}
