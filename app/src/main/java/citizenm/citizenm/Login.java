package citizenm.citizenm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class Login extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public String auth;
    public String fbid;
    SharedPreferencesHelper sph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sph = new SharedPreferencesHelper(this, "myappprefs");
        fbid = sph.getString("fbid");

        if(fbid!=""){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","user_friends","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sph.putString("auth", loginResult.getAccessToken().getToken());
                Log.d(auth,loginResult.getAccessToken().getUserId());
                info.setText(loginResult.getAccessToken().getUserId());


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override

                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("json:", object.toString());

                                try {
                                    JSONObject jsonObj = new JSONObject(object.toString());
                                    String email=jsonObj.getString("email");
                                    String fbid=jsonObj.getString("id");
                                    String name=jsonObj.getString("name");
                                    String pic=jsonObj.getString("picture");

                                    sph.putString("email",email);
                                    sph.putString("name",name);
                                    sph.putString("fbid",fbid);

                                    try {name = URLEncoder.encode(name, "UTF-8");} catch (UnsupportedEncodingException e) {}

                                    String url = "http://104.236.191.175/citizenm/data.php"
                                            + "?email=" + email
                                            + "&name=" + name
                                            + "&fbid=" + fbid;

                                    Log.d("url:",url);

                                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.d("server:","Response: " + response.toString());
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

                                } catch (JSONException e) {
                                    //some exception handler code.
                                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void mp(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
