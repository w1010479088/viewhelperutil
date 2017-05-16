package com.bruceewu;

import java.util.List;

import retrofit2.Call;

/**
 * Created by mac on 2017/5/16.
 */

public class RetTypes {
    public static class Error {
        public static final int CODE_UNKONWN = -1;
        public static final int CODE_UNLOGIN = 10014;
        private int code;
        private String errorMsg;
        private int hashCode;

        public Error() {}

        public Error(int code, String errorMsg, int hashCode) {
            this.code = code;
            this.errorMsg = errorMsg;
            this.hashCode = hashCode;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }


        public void setHashCode(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getCode() {
            return code;
        }

        public boolean needHandle(Call call) {
            return call == null ? false : call.hashCode() == getCallHash();
        }

        public int getCallHash() {
            return hashCode;
        }

    }

    public static class HomeTabs extends Result {
        private List<HomeTab> tabs;

        public List<HomeTab> tabs() {
            return tabs;
        }
    }
}
