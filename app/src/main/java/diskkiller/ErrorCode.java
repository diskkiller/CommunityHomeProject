package diskkiller;

public class ErrorCode {
    public static int COMMON_CODE_OK = 0;
    public static int COMMON_NO_LOGIN = 1;
    public static int COMMON_UNKNOW_ERROR = 2;
    public static int COMMON_DATA_SEND_FAILD = 3;
    public static int COMMON_INVALID_PROTOCAL = 4;

    public interface ForC {
        public static int BREOKEN_CONNECT_TO_SERVER = 201;
        public static int BAD_CONNECT_TO_SERVER = 202;
        public static int CLIENT_SDK_NO_INITIALED = 203;
        public static int LOCAL_NETWORK_NOT_WORKING = 204;
        public static int TO_SERVER_NET_INFO_NOT_SETUP = 205;
    }

    public interface ForS {
        public static int RESPONSE_FOR_UNLOGIN = 301;
    }
}
