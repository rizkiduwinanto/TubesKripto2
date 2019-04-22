package com.rizkiduwinanto.kriptografi2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {
    private static final String URL = "http://androidcodefinder.com/RecyclerViewJson.json";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<InboxModel> inboxModelList;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.inbox_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InboxActivity.this, ComposeEmailActivity.class);
                startActivity(intent);
            }
        });

        inboxModelList = new ArrayList<>();

        this.loadData();
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                InboxModel inboxModel = new InboxModel(
                                        object.getString("head").substring(0, 1),
                                        object.getString("head"),
                                        object.getString("subject"),
                                        object.getString("description"),
                                        object.getString("date")
                                );
                                inboxModelList.add(inboxModel);
                            }


                            mAdapter = new InboxAdapter(inboxModelList, getApplicationContext());
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
            case R.id.compose:
                startActivity(new Intent(getApplicationContext(), ComposeEmailActivity.class));
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
