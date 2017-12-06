package hkucs.borrowmetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.support.design.widget.Snackbar.*;

public class ProfileActivity extends AppCompatActivity {
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query userQ = database.child("users").orderByChild("email").equalTo(getIntent().getExtras().getString("uEmail"));
        userQ.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(User.class);
                Log.d("fuck", user.getName());
                TextView name, email, address;
                name = findViewById(R.id.name);
                name.setText(user.getName());
                email = findViewById(R.id.email);
                email.setText(user.getEmail());
                address = findViewById(R.id.address);
                address.setText(user.getAddress());
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
        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Request from BorrowMe!");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi! I'd like to rent an item from you.");
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
            }
        });
    }
}
