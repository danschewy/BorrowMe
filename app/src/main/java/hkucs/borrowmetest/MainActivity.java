package hkucs.borrowmetest;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<RentItem> items = new ArrayList<RentItem>();
    NavigationView navigationView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        //create categories
        db.deleteAllCategories();
        String[] catArr = getResources().getStringArray(R.array.category_array);
        for (String title : catArr) {
            db.createCategory(new Category(title));
        }
        ArrayList<Category> categories = db.getAllCategories();
        Menu menu = navigationView.getMenu();
        menu.add("All");
        for (Category cat : categories) {
            menu.add(cat.getTitle());
        }

        navigationView.invalidate();


    View navheadView = navigationView.getHeaderView(0);
    LinearLayout navhead = (LinearLayout) navheadView.findViewById(R.id.navheader);
        navhead.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
    });
    mRecyclerView =(RecyclerView)

    findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
    mLayoutManager =new

    LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter =new MyRecyclerViewAdapter(getDataSet());
    mRecyclerView.setAdapter(mAdapter);

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
        if (id == R.id.action_settings) {
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
            mRecyclerView.swapAdapter(new MyRecyclerViewAdapter(getDataSet()),true);
        }else {
            mRecyclerView.swapAdapter(new MyRecyclerViewAdapter(getFilteredData((String) item.getTitle())),false);
        }
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
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
                                                                                  RentItem item = ((MyRecyclerViewAdapter) mAdapter).getItem(position);
                                                                                  Bundle bundle = new Bundle();
                                                                                  bundle.putSerializable("item", item);
                                                                                  intent.putExtras(bundle);
                                                                                  startActivity(intent);
                                                                              }
                                                                          });
    }

    private ArrayList<RentItem> getDataSet() {
        ArrayList<RentItem> results;
        results = db.getAllItems();
        return results;
    }

    private ArrayList<RentItem> getFilteredData(String categoryName){
        int categoryId = db.getCategoryByName(categoryName).getId();
        ArrayList<RentItem> filteredItems = db.getItemsByCategory(categoryId);
        return filteredItems;
    }

    private void addMenuItems() {

    }
}
