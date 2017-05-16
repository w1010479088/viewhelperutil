package com.bruceewu;

import java.util.HashMap;

/**
 * Created by mac
 */

public class Requestor {

    public static class Home {
        public static void home_tabs() {
            HashMap<String, String> params = ProtocolType.HOME_TABS.params();
            RequestUtil.get(params, RetTypes.HomeTabs.class, ProtocolType.HOME_TABS);
        }
    }
}
