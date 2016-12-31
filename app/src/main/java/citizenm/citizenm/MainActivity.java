package citizenm.citizenm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<issue> issueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private issueAdaptor iAdapter;
    public TabLayout tabs;
    SharedPreferencesHelper sph;
    public String p="issues";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sph = new SharedPreferencesHelper(this, "myappprefs");
        String name = sph.getString("name");
        Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tabs.getSelectedTabPosition(); // get the position for the current selected tab
                Log.d("test", Integer.toString(selectedTabPosition));

                if (selectedTabPosition == 0) {
                    p = "issues";
                }
                if (selectedTabPosition == 1) {
                    p = "people";
                }
                if (selectedTabPosition == 2) {
                    p = "stats";
                }
                if (selectedTabPosition == 3) {
                    p = "prefs";
                }

                refreshlist(p);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        refreshlist(p);

    }

    public void refreshlist(String p){

        String url = "http://104.236.191.175/citizenm/issues.php?p="+p;

        iAdapter = new issueAdaptor(issueList);
        RecyclerView.LayoutManager iLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(iLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("data:","Response: " + response.toString());
                        showlist(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("error", error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsArrayRequest);
        Log.d("test2","test2");

    }

    private void showlist(JSONArray response){

        issue issue;

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);
                String url = jsonobject.getString("image");
                String headline = jsonobject.getString("articleheader");
                String fulltext = jsonobject.getString("articlebrief");
                String age = "2d";
                issue = new issue(url, headline, fulltext, age);
                issueList.add(issue);
            }
            catch(JSONException e){
                Log.e("error",e.toString());
            }

        }

        iAdapter.notifyDataSetChanged();
        //prepareIssueData();
    }

    public void fb(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}