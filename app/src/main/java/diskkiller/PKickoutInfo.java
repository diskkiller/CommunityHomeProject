package diskkiller;

public class PKickoutInfo {
    public static final int KICKOUT_FOR_DUPLICATE_LOGIN = 1;
    public static final int KICKOUT_FOR_ADMIN = 2;
    protected int code = -1;
    protected String reason = null;

    public PKickoutInfo(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
