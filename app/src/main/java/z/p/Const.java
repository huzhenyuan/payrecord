package z.p;

import java.util.HashMap;
import java.util.Map;

public class Const {

    public static final String TAG = "HZY";
    public static final String SERVER = "http://192.168.43.148:8083/";
    //    public static final String SERVER = "http://117.51.137.126:8083/";
    //    public static final String SERVER = "http://117.51.147.243:8083/";
    public static final String CHANNEL_ID = "ID";
    public static final String IntentAction = "xyz.loadnl.Notification";
    public static final String PRIVATE_KEY = "951d6770449ab6d1ca3f6b71d2f00e55ab1af0bb8b967aaab34642cbde6704c7";
    public static final String DB_NAME = "pay";
    public static final String MEMBER_ID = "1254448435801620480";
    public static final String IMEI_KEY = "deviceImei";
    public static final String 充值订单状态_待支付 = "1";
    public static final String 充值订单状态_已支付 = "2";
    public static final String 充值订单状态_客户端停止接单 = "3";
    public static final String 充值订单状态_由于有新订单取消之前订单 = "4";
    private static final String PUBLIC_KEY = "eb55937652a6803176da3734f92b1ad5a1e236fccb13776b712e081d9ccc51f7";

    public final static Map<String, String> BANK_PHONE = new HashMap<String, String>() {{
        put("+8613716431209", "招商银行");
        put("95555", "招商银行");
        put("95566", "中国银行");
        put("95533", "建设银行");
        put("95588", "工商银行");
        put("95558", "中信银行");
        put("95599", "农业银行");
        put("95568", "民生银行");
        put("95595", "光大银行");
        put("95559", "交通银行");
        put("95508", "广发银行");
        put("95528", "浦发银行");
        put("95501", "深发银行");
        put("95577", "华夏银行");
        put("95561", "兴业银行");
    }};

}
