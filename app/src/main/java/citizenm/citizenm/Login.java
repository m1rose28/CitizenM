package citizenm.citizenm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.viralypatel.sharedpreferenceshelper.lib.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private TextView ziptext;
    private EditText editZipText;
    private Button zipbutton;
    private CallbackManager callbackManager;
    public String auth;
    public String fbid;
    public String email;
    public String name;
    public String zip;
    public String state;
    public JSONObject jsonObj;
    SharedPreferencesHelper sph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sph = new SharedPreferencesHelper(this, "myappprefs");
        fbid = sph.getString("fbid");

        if(fbid!=""){
            mp();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        ziptext = (TextView)findViewById(R.id.ziptext);
        zipbutton = (Button)findViewById(R.id.zipbutton);
        editZipText = (EditText)findViewById(R.id.editZipText);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","user_friends","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sph.putString("auth", loginResult.getAccessToken().getToken());
                Log.d(auth,loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override

                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("citizen:", "step 1 - got fb:"+ object.toString());
                                parsefbdata(object);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,name,picture,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



    }

    public void parsefbdata(JSONObject object){

        try {
            jsonObj = new JSONObject(object.toString());
            email=jsonObj.getString("email");
            fbid=jsonObj.getString("id");
            name=jsonObj.getString("name");

            sph.putString("email",email);
            sph.putString("name",name);
            sph.putString("fbid",fbid);

            Log.d("citizen:", "step 2 - parsed fb data into share prefs");

            getzip();

        } catch (JSONException e) {
            //some exception handler code.
        }

    }

    public void getzip(){
        loginButton.setVisibility(View.GONE);
        editZipText.setVisibility(View.VISIBLE);
        ziptext.setVisibility(View.VISIBLE);
        zipbutton.setVisibility(View.VISIBLE);
        Log.d("citizen:", "step 3 - paint zip code screen");
    }

    public void checkzip(View view){

        zip=editZipText.getText().toString();
        Log.d("citizen:", "step 4 - checking zip code"+zip);

        try {name = URLEncoder.encode(name, "UTF-8");} catch (UnsupportedEncodingException e) {}

        String url = "http://104.236.191.175/citizenm/mycongress.php?"
                + "&zipcode=" + zip;

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("data:","Response: " + response.toString());

                        if(response.toString().equals("[{\"result\":\"invalid\"}]")) {
                            Toast.makeText(getApplicationContext(), "hm, try another zip code?", Toast.LENGTH_LONG).show();
                            Log.d("citizen","invalid zip - go fish");
                            return;
                        }

                        sph.putString("congresslist",response.toString());

                        checkzip2(response);

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

    }

    public void checkzip2(JSONArray response){

        Log.d("citizen:", "step 5 - parsing the new json data from my congress and store it locally as a query string");

        String congresslistquery = null;

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);
                congresslistquery=congresslistquery+jsonobject.getString("bioguide")+":";
            }
            catch(JSONException e){
                Log.e("error",e.toString());
            }

        }

        sph.putString("congresslist",congresslistquery);

        Log.d("citizen:", congresslistquery);

        datatosever();

    }

    public void datatosever(){
        try {name = URLEncoder.encode(name, "UTF-8");} catch (UnsupportedEncodingException e) {}

        String url = "http://104.236.191.175/citizenm/data.php"
                + "?email=" + email
                + "&name=" + name
                + "&fbid=" + fbid
                + "&zip=" + zip;

        Log.d("url:",url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("server:","Step 6: sent dato to server:" + response.toString());
                        mp();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("server:","yikes volley error");

                    }


                });

        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void mp(){
        Log.d("citizen:", "woo hoo-sending off to main page");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
