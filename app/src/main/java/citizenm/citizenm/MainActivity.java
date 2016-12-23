package citizenm.citizenm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    public WebView webview;
    public TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        webview=(WebView)findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("http://104.236.191.175/citizenm/index.php");
        tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = tabs.getSelectedTabPosition(); // get the position for the current selected tab
                Log.d("test", Integer.toString(selectedTabPosition));

                String p="";
                if(selectedTabPosition==0){p="issues";}
                if(selectedTabPosition==1){p="people";}
                if(selectedTabPosition==2){p="stats";}
                if(selectedTabPosition==3){p="prefs";}

                String u;
                u="http://104.236.191.175/citizenm/index.php?p="+ p;

                webview.loadUrl(u);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void issues(View view) {
        webview=(WebView)findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("http://104.236.191.175/citizenm/index.php?p=issues");
    }

    public void people(View view) {
        webview=(WebView)findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("http://104.236.191.175/citizenm/index.php?p=people");
    }

    public void stats(View view) {
        webview=(WebView)findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("http://104.236.191.175/citizenm/index.php?p=stats");
    }

    public void prefs(View view) {
        WebView webview=(WebView)findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("http://104.236.191.175/citizenm/index.php?p=prefs");
    }

    public void reload(View view) {
        webview.reload();
        tabs = (TabLayout) findViewById(R.id.tabs);
        int selectedTabPosition = tabs.getSelectedTabPosition(); // get the position for the current selected tab
        Log.d("test", Integer.toString(selectedTabPosition));
    }

}
