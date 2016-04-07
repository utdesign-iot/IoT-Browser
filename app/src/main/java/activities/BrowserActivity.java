package activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.utdesign.iot.baseui.R;

import fragments.DevicesFragment;
import qrcode.QRCSSMainActivity;


public class BrowserActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activty);

        String url = getIntent().getStringExtra(DevicesFragment.URL);

        //THIS GOOGLE ADDRESS IS FOR TESTING PURPOSES ONLY.
        //url = "http://www.google.com";

        //FOR QR CODE.
        if (QRCSSMainActivity.QRWebAddressFlag)
        {
            QRCSSMainActivity.QRWebAddressFlag = false;

            if (savedInstanceState == null)
            {
                Bundle extras = getIntent().getExtras();
                if(extras == null)
                {
                    url= null;
                }
                else
                {
                    url= extras.getString("STRING_I_NEED");
                }
            }
            else
            {
                url= (String) savedInstanceState.getSerializable("STRING_I_NEED");
            }
        }



        WebView webView = (WebView) findViewById(R.id.about_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }
}
