package com.rizkiduwinanto.kriptografi2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class ComposeEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;

    private Button buttonSend;
    private String mEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mEmail = mAuth.getCurrentUser().getEmail();
    }

    private void SendEmail() {
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String password = sharedPref.getString("Password", "Password Not Found");

        Log.d("Password", password);
        Log.d("Email", mEmail);

        SenderMail senderMail = new SenderMail(this, mEmail, password, email, subject, message);

        senderMail.execute();

        startActivity(new Intent(getApplicationContext(), InboxActivity.class));
    }

    @Override
    public void onClick(View v) {
        SendEmail();
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
            case R.id.sent_mail:
                startActivity(new Intent(getApplicationContext(), SentEmailActivity.class));
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
