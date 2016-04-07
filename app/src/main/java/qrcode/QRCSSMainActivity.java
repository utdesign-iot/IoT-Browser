package qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.utdesign.iot.baseui.R;

import activities.BrowserActivity;


public class QRCSSMainActivity extends QRCBSActivity implements QScanV.ResultHandler {
    public static Boolean QRWebAddressFlag = false;
    private QScanV mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.simple_scanner_content_frame);
        mScannerView = new QScanV(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(QResult rawQResult) {

        //TESTING PURPOSES ONLY, WILL LAUNCH DEVICE WEB BROWSER.
        //Toast.makeText(this, "Contents = " + rawQResult.getContents() + ", Format = " + rawQResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rawQResult.getContents()));
        //startActivity(browserIntent);


        //ATTEMPT TO LAUNCH THE BrowserActivity.java
        QRWebAddressFlag = true;
        Intent i = new Intent(QRCSSMainActivity.this, BrowserActivity.class);
        String strName = null;
        strName = rawQResult.getContents();
        i.putExtra("STRING_I_NEED", strName);

        //Toast to let user new where they are going.
        Toast.makeText(this, "Web Address Opening: " + strName, Toast.LENGTH_LONG).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(QRCSSMainActivity.this);
            }
        }, 2000);


        //LAUNCHING THE WEB BROWSER ACTIVITY FROM QR CODE. NOTE: THIS STATEMENT NEEDS TO BE AT END OF METHOD.
        startActivity(i);

    }//end of handleResult method.


}
