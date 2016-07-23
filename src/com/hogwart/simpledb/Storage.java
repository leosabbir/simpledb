package com.hogwart.simpledb;

import java.util.HashMap;

/**
 * Created by sabbirmanandhar on 7/23/16.
 */
public class Storage extends HashMap<String, Object> {

    public Object getValue(String key) {
        if (this.containsKey(key)) {
            return this.get(key);
        }

        return null;
    }

    public boolean set(String key, Object value) {
        if (this.containsKey(key) && value.equals(this.getValue(key))) {
                return false; // Nothing to be done, <key> already has same value
        }
        this.put(key, value);
        return true;
    }

    public Object unset(String key) {
        return this.remove(key);
    }
}
