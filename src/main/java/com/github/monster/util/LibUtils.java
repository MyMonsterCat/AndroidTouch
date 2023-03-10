package com.github.monster.util;

import com.github.monster.constant.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LibUtils {

    public static String getMinitouchBin(String abi, int sdk) {
        if (sdk < Constants.MAX_API_LEVEL_WITHOUT_PIE) {
            return Constants.DIR_STF_ROOT + '/' + abi + '/' + Constants.BIN_MINITOUCH_NOPIE;
        }
        return Constants.DIR_STF_ROOT + '/' + abi + '/' + Constants.BIN_MINITOUCH;
    }
}
