package hkucs.borrowmetest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import org.w3c.dom.Text;

import static android.support.design.widget.Snackbar.*;

public class ProfileActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner rentedItems = (Spinner) findViewById(R.id.itemsForRent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        rentedItems.setAdapter(adapter);

        //eventully replaced with getting user id and lookining up in db

        // Name
        final EditText editName = (EditText) findViewById(R.id.editName);
        final TextView viewName = (TextView) findViewById(R.id.viewName);
        Button editNameBtn = (Button) findViewById(R.id.editNameBtn);
        viewName.setText(user.getFirst_name());
        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (editName.getVisibility() == View.GONE) {
                    editName.setVisibility(View.VISIBLE);
                    viewName.setVisibility(View.GONE);
                } else {
                    viewName.setVisibility(View.VISIBLE);
                    editName.setVisibility(View.GONE);
                }
            }
        });

        // Address
        final EditText editAddress = (EditText) findViewById(R.id.editAddress);
        final TextView viewAddress = (TextView) findViewById(R.id.viewAddress);
        Button editAddressBtn = (Button) findViewById(R.id.editAddressBtn);
        viewAddress.setText(user.getAddress());
        editAddressBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                if (editAddress.getVisibility() == View.GONE){
                    editAddress.setVisibility(View.VISIBLE);
                    viewAddress.setVisibility(View.GONE);
                } else {
                    viewAddress.setVisibility(View.VISIBLE);
                    editAddress.setVisibility(View.GONE);
                }
            }
        });

        // Email
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final TextView viewEmail = (TextView) findViewById(R.id.viewEmail);
        Button editEmailBtn = (Button) findViewById(R.id.editEmailBtn);
        viewEmail.setText(user.getEmail());
        editEmailBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                if (editEmail.getVisibility() == View.GONE){
                    editEmail.setVisibility(View.VISIBLE);
                    viewEmail.setVisibility(View.GONE);
                } else {
                    viewEmail.setVisibility(View.VISIBLE);
                    editEmail.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }
}
