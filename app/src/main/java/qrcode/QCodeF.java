package qrcode;

import net.sourceforge.zbar.Symbol;

import java.util.List;
import java.util.ArrayList;


public class QCodeF {
    private int mId;
    private String mName;

    public static final QCodeF NONE = new QCodeF(Symbol.NONE, "NONE");
    public static final QCodeF PARTIAL = new QCodeF(Symbol.PARTIAL, "PARTIAL");
    public static final QCodeF EAN8 = new QCodeF(Symbol.EAN8, "EAN8");
    public static final QCodeF UPCE = new QCodeF(Symbol.UPCE, "UPCE");
    public static final QCodeF ISBN10 = new QCodeF(Symbol.ISBN10, "ISBN10");
    public static final QCodeF UPCA = new QCodeF(Symbol.UPCA, "UPCA");
    public static final QCodeF EAN13 = new QCodeF(Symbol.EAN13, "EAN13");
    public static final QCodeF ISBN13 = new QCodeF(Symbol.ISBN13, "ISBN13");
    public static final QCodeF I25 = new QCodeF(Symbol.I25, "I25");
    public static final QCodeF DATABAR = new QCodeF(Symbol.DATABAR, "DATABAR");
    public static final QCodeF DATABAR_EXP = new QCodeF(Symbol.DATABAR_EXP, "DATABAR_EXP");
    public static final QCodeF CODABAR = new QCodeF(Symbol.CODABAR, "CODABAR");
    public static final QCodeF CODE39 = new QCodeF(Symbol.CODE39, "CODE39");
    public static final QCodeF PDF417 = new QCodeF(Symbol.PDF417, "PDF417");
    public static final QCodeF QRCODE = new QCodeF(Symbol.QRCODE, "QRCODE");
    public static final QCodeF CODE93 = new QCodeF(Symbol.CODE93, "CODE93");
    public static final QCodeF CODE128 = new QCodeF(Symbol.CODE128, "CODE128");

    public static final List<QCodeF> ALL_FORMATS = new ArrayList<QCodeF>();

    static {
        ALL_FORMATS.add(QCodeF.PARTIAL);
        ALL_FORMATS.add(QCodeF.EAN8);
        ALL_FORMATS.add(QCodeF.UPCE);
        ALL_FORMATS.add(QCodeF.ISBN10);
        ALL_FORMATS.add(QCodeF.UPCA);
        ALL_FORMATS.add(QCodeF.EAN13);
        ALL_FORMATS.add(QCodeF.ISBN13);
        ALL_FORMATS.add(QCodeF.I25);
        ALL_FORMATS.add(QCodeF.DATABAR);
        ALL_FORMATS.add(QCodeF.DATABAR_EXP);
        ALL_FORMATS.add(QCodeF.CODABAR);
        ALL_FORMATS.add(QCodeF.CODE39);
        ALL_FORMATS.add(QCodeF.PDF417);
        ALL_FORMATS.add(QCodeF.QRCODE);
        ALL_FORMATS.add(QCodeF.CODE93);
        ALL_FORMATS.add(QCodeF.CODE128);
    }

    public QCodeF(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public static QCodeF getFormatById(int id) {
        for(QCodeF format : ALL_FORMATS) {
            if(format.getId() == id) {
                return format;
            }
        }
        return QCodeF.NONE;
    }
}