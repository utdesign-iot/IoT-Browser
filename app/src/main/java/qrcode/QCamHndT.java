package qrcode;


import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

//this is the handler for the camera thread since the camera in and output is handled in the background off of main thread.
public class QCamHndT extends HandlerThread
{
    //vars
    private static final String LOG_TAG = "QCamHndT";
    private QCodeScanV mScannerView;

    //contructor
    public QCamHndT(QCodeScanV scannerView)
    {
        super("QCamHndT");
        mScannerView = scannerView;
        start();
    }

    //when start camera do this.
    public void startCamera(final int cameraId)
    {
        Handler localHandler = new Handler(getLooper());
        localHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                final Camera camera = QCamU.getCameraInstance(cameraId);
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mScannerView.setupCameraPreview(camera);
                    }
                });
            }
        });
    }
}
