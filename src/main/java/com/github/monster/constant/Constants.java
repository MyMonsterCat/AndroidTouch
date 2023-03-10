package com.github.monster.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String DIR_LIBS_ROOT = "libs";

    public static final String DIR_STF_ROOT = DIR_LIBS_ROOT + "/stf";

    public static final String DIR_DEVICE_TEMP = "/data/local/tmp";

    /**
     * not support pie
     */
    public static final int MAX_API_LEVEL_WITHOUT_PIE = 16;

    /**
     * minitouch constants
     */
    public static final String BIN_MINITOUCH = "minitouch";

    public static final String BIN_MINITOUCH_NOPIE = "minitouch-nopie";

    public static final String REMOTE_PATH_MINITOUCH = Constants.DIR_DEVICE_TEMP + '/' + Constants.BIN_MINITOUCH;

    public static final String CMD_MINITOUCH = REMOTE_PATH_MINITOUCH;

    public static final String SOCKET_NAME_MINITOUCH = "minitouch";

    /**
     * minitouch no support Android 10 and up, use minitouchagent instead
     */
    public static final int MIN_API_LEVEL_TOUCH_AGENT = 29;

    /**
     * STFService constants
     */
    public static final String APK_STF_SERVICE = DIR_STF_ROOT + "/STFService.apk";

    public static final String PKG_STF_SERVICE = "jp.co.cyberagent.stf";

    public static final String CLS_STF_AGENT = PKG_STF_SERVICE + ".Agent";

    public static final String SOCKET_NAME_MINITOUCH_AGENT = "minitouchagent";

}
