package hkucs.borrowmetest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import static android.support.design.widget.Snackbar.*;

public class ProfileActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //eventully replaced with getting user id and lookining up in db
        getUser(0);

    }

    public void getUser(int id){
        user = new User ("Hasan", "Asim","6 Sassoon Road","hasan.asim134@gmail.com");
    }
}
