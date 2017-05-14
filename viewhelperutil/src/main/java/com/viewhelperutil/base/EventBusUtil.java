package com.viewhelperutil.base;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus工具类
 */
public class EventBusUtil {
    private volatile static EventBus mInstance;

    public static EventBus getEventBus(){
        if(mInstance == null){
            synchronized (EventBusUtil.class){
                if(mInstance == null){
                    mInstance = EventBus.builder().eventInheritance(false).build();
                }
            }
        }
        return mInstance;
    }

    public static void register_bus(Object obj) {
        if (!EventBusUtil.getEventBus().isRegistered(obj)){
            EventBusUtil.getEventBus().register(obj);
        }
    }

    public static void unregister_bus(Object obj) {
        if (EventBusUtil.getEventBus().isRegistered(obj)) {
            EventBusUtil.getEventBus().unregister(obj);
        }
    }

    public static void post_event(Object data) {
        EventBusUtil.getEventBus().post(data);
    }
}
