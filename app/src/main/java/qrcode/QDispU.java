package qrcode;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


//this is the QR Code Display Utilities that are done automatically for user such as auto focus and reorient the screeen.
public class QDispU
{

    //refocus and screen resolution.
    public static Point getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenResolution = new Point();
        if (android.os.Build.VERSION.SDK_INT >= 13)
        {
            display.getSize(screenResolution);
        }
        else
        {
            screenResolution.set(display.getWidth(), display.getHeight());
        }

        return screenResolution;
    }


    //re orient the screen.
    public static int getScreenOrientation(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(display.getWidth()==display.getHeight())
        {
            orientation = Configuration.ORIENTATION_SQUARE;
        }
        else
        {
            if(display.getWidth() < display.getHeight())
            {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }
            else
            {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

}
