package com.zlk.job;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;


//这里用于改进bug，当更新浏览量时，不需要用户登录，因为更新接口中有需要用户登录的
public class UpdateViewCountFlag{
    private static ThreadLocal<Boolean> updateViewCountFlag = ThreadLocal.withInitial(() -> false);

    public static void setUpdateViewCountFlag(boolean flag) {
        updateViewCountFlag.set(flag);
    }

    public static boolean isUpdateViewCount() {
        return updateViewCountFlag.get();
    }

    public static void clear() {
        updateViewCountFlag.remove();
    }
}
