package qrcode;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
//this class controls the barcode that was scanned
public abstract class QCodeScanV extends FrameLayout implements Camera.PreviewCallback
{
    //variables
    private Camera mCamera;
    private QCamP mPreview;
    private QIVwF mViewFinderView;
    private Rect mFramingRectInPreview;
    private QCamHndT mQCamHndT;
    private Boolean mFlashState;
    private boolean mAutofocusState = true;
    public QCodeScanV(Context context) {
        super(context);
    }
    public QCodeScanV(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    //sets up the layout seen by the vuewer from the camera.
    public final void setupLayout(Camera camera)
    {
        removeAllViews();

        mPreview = new QCamP(getContext(), camera, this);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.setBackgroundColor(Color.BLACK);
        relativeLayout.addView(mPreview);
        addView(relativeLayout);

        mViewFinderView = createViewFinderView(getContext());
        if (mViewFinderView instanceof View)
        {
            addView((View) mViewFinderView);
        }
        else
        {
            throw new IllegalArgumentException("QIVwF object returned by " +
                    "'createViewFinderView()' should be instance of android.view.View");
        }
    }

    /**
     * <p>Method that creates view that represents visual appearance of a barcode scanner</p>
     * <p>Override it to provide your own view for visual appearance of a barcode scanner</p>
     *
     * @param context {@link Context}
     * @return {@link android.view.View} that implements {@link QVwFV}
     */
    protected QIVwF createViewFinderView(Context context) {
        return new QVwFV(context);
    }

    //start the camera
    public void startCamera(int cameraId)
    {
        if(mQCamHndT == null)
        {
            mQCamHndT = new QCamHndT(this);
        }
        mQCamHndT.startCamera(cameraId);
    }


    //setup the camera.
    public void setupCameraPreview(Camera camera)
    {
        mCamera = camera;
        if(mCamera != null)
        {
            setupLayout(mCamera);
            mViewFinderView.setupViewFinder();
            if(mFlashState != null)
            {
                setFlash(mFlashState);
            }
            setAutoFocus(mAutofocusState);
        }
    }

    //on start camera
    public void startCamera() {
        startCamera(-1);
    }

    //on stopping the camera do this.
    public void stopCamera()
    {
        if(mCamera != null)
        {
            mPreview.stopCameraPreview();
            mPreview.setCamera(null, null);
            mCamera.release();
            mCamera = null;
        }
        if(mQCamHndT != null)
        {
            mQCamHndT.quit();
            mQCamHndT = null;
        }
    }

    //do this when stop camera.
    public void stopCameraPreview()
    {
        if(mPreview != null)
        {
            mPreview.stopCameraPreview();
        }
    }

    //do this when resuming camera
    protected void resumeCameraPreview()
    {
        if(mPreview != null)
        {
            mPreview.showCameraPreview();
        }
    }

    //this is to calibrate the viewing rectangle for the qr code reader.
    public synchronized Rect getFramingRectInPreview(int previewWidth, int previewHeight)
    {
        if (mFramingRectInPreview == null)
        {
            Rect framingRect = mViewFinderView.getFramingRect();
            int viewFinderViewWidth = mViewFinderView.getWidth();
            int viewFinderViewHeight = mViewFinderView.getHeight();
            if (framingRect == null || viewFinderViewWidth == 0 || viewFinderViewHeight == 0)
            {
                return null;
            }

            Rect rect = new Rect(framingRect);
            rect.left = rect.left * previewWidth / viewFinderViewWidth;
            rect.right = rect.right * previewWidth / viewFinderViewWidth;
            rect.top = rect.top * previewHeight / viewFinderViewHeight;
            rect.bottom = rect.bottom * previewHeight / viewFinderViewHeight;

            mFramingRectInPreview = rect;
        }
        return mFramingRectInPreview;
    }


    //for future when implement flashlight capability for dark situations.
    public void setFlash(boolean flag)
    {
        mFlashState = flag;
        if(mCamera != null && QCamU.isFlashSupported(mCamera))
        {

            Camera.Parameters parameters = mCamera.getParameters();
            if(flag) {
                if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
                {
                    return;
                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF))
                {
                    return;
                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            mCamera.setParameters(parameters);
        }
    }


    //for future when implement flashlight capability for dark situations.
    public boolean getFlash()
    {
        if(mCamera != null && QCamU.isFlashSupported(mCamera))
        {
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }


    //for future when implement flashlight capability for dark situations.
    public void toggleFlash()
    {
        if(mCamera != null && QCamU.isFlashSupported(mCamera))
        {
            Camera.Parameters parameters = mCamera.getParameters();
            if(parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH))
            {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            else
            {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCamera.setParameters(parameters);
        }
    }


    //camera autofocus helper.
    public void setAutoFocus(boolean state)
    {
        mAutofocusState = state;
        if(mPreview != null)
        {
            mPreview.setAutoFocus(state);
        }
    }
}
