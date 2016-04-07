package qrcode;


public class QResult {
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