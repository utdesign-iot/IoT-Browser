package qrcode;

//Just basically a class with setters and getters to handle the result of the scanned qr code.
public class QResult
{
    private String mContents;
    private QCodeF mQCodeF;

    public void setContents(String contents) {
        mContents = contents;
    }

    public void setBarcodeFormat(QCodeF format) {
        mQCodeF = format;
    }

    public QCodeF getBarcodeFormat() {
        return mQCodeF;
    }

    public String getContents() {
        return mContents;
    }
}