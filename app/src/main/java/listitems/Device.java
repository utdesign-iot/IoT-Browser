package listitems;

/**
 * Created by Ray on 3/3/2016.
 */
public class Device {
    /**
     * Created by Ray on 3/1/2016.
     */
    private String deviceName;
    private int imageID;
    private String description;

    public Device(String deviceName, int imageID, String description)
    {
        this.deviceName = deviceName;
        this.description = description;
        this.imageID = imageID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getImageID() {
        return imageID;
    }

    public String getDescription() {
        return description;
    }
}
