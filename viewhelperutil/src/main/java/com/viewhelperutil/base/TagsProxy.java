package com.viewhelperutil.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac
 */

public class TagsProxy {
    private Map<String, Object> mTags = new HashMap<>();

    public void setTag(String tag, Object obj){
        mTags.put(tag, obj);
    }

    public void setTags(Map<String, Object> tags){
        mTags.putAll(tags);
    }

    public Object tag(String tag){
        return mTags.get(tag);
    }

    public Map<String, Object> tags(){
        return mTags;
    }

    public void clear(){
        mTags.clear();
    }
}
