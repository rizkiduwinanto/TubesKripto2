package com.rizkiduwinanto.kriptografi2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class SentEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox:
                startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                return true;
            case R.id.compose:
                startActivity(new Intent(getApplicationContext(), ComposeEmailActivity.class));
                return true;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPrefs = getSharedPreferences("Prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.remove("Email");
                editor.remove("Password");
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
