package hkucs.borrowmetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import static android.support.design.widget.Snackbar.*;

public class ProfileActivity extends AppCompatActivity {
    User user;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = new DatabaseHelper(getApplicationContext());

        Intent i = getIntent();
        user = db.getUserById(i.getExtras().getInt("id"));

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

        TextView name, email, address;
        name = findViewById(R.id.name);
        name.setText(user.getFirst_name() + " " + user.getLast_name());
        email = findViewById(R.id.email);
        email.setText(user.getEmail());
        address = findViewById(R.id.address);
        address.setText(user.getAddress());
    }
}
