package diskkiller;

public class PLoginInfo {
    protected String loginUserId;
    protected String loginToken;
    protected String extra;
    protected long firstLoginTime;

    public PLoginInfo(String loginUserId, String loginToken) {
        this(loginUserId, loginToken, (String)null);
    }

    public PLoginInfo(String loginUserId, String loginToken, String extra) {
        this.loginUserId = null;
        this.loginToken = null;
        this.extra = null;
        this.firstLoginTime = 0L;
        this.loginUserId = loginUserId;
        this.loginToken = loginToken;
        this.extra = extra;
    }

    public String getLoginUserId() {
        return this.loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginToken() {
        return this.loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getFirstLoginTime() {
        return this.firstLoginTime;
    }

    public void setFirstLoginTime(long firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public static boolean isFirstLogin(long firstLoginTime) {
        return firstLoginTime <= 0L;
    }
}
