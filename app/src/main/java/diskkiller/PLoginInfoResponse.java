package diskkiller;

public class PLoginInfoResponse {
    protected int code = 0;
    protected long firstLoginTime = 0L;

    public PLoginInfoResponse(int code, long firstLoginTime) {
        this.code = code;
        this.firstLoginTime = firstLoginTime;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getFirstLoginTime() {
        return this.firstLoginTime;
    }

    public void setFirstLoginTime(long firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }
}
