package com.hogwart.simpledb;

import java.util.HashMap;

/**
 * Created by sabbirmanandhar on 7/23/16.
 *
 * It extends HashMap
 * It stores the variables of the current transaction
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

    public Object unset(String key, boolean saveNull) {
        if(!saveNull) {
            return this.remove(key);
        } else {
            Object value = this.get(key);
            this.put(key, null);
            return value;
        }
    }
}
