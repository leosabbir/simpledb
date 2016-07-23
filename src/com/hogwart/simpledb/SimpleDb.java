package com.hogwart.simpledb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sabbirmanandhar on 7/20/16.
 */
public class SimpleDb {

    private Transaction rootTxn;
    private Transaction currentTxn;
    private Map<Object, Integer> counteMap;

    private static SimpleDb INSTANCE;

    private SimpleDb() {
        this.rootTxn = new Transaction(null);
        this.currentTxn = rootTxn;
        counteMap = new HashMap<Object, Integer>();
    }

    public static SimpleDb  GET_INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleDb();
        }
        return INSTANCE;
    }

    public Object get(String key) {
        return this.currentTxn.get(key);
    }

    public void set(String key, Object value) {
        if (this.currentTxn.set(key, value)) {
            if (!this.counteMap.containsKey(value)) {
                this.counteMap.put(value, 1);
            } else {
            this.counteMap.put(value, this.counteMap.get(value) + 1);
            }
        }
    }

    public void unset(String key) {
        Object value = this.currentTxn.unset(key);
        if ( value != null ) {
            int currentCount = this.counteMap.get(value);
            if (currentCount == 1) {
                this.counteMap.remove(value);
            } else {
                this.counteMap.put(value, currentCount -  1);
            }
        }
    }

    public int getNumEqualTo(Object value) {
        if (this.counteMap.containsKey(value)) {
            return this.counteMap.get(value);
        }
        return 0;
    }

    public void execute (Commands command, String field, Object value) {

        switch (command) {
            case SET:
                this.set(field, value);
                break;

            case UNSET:
                this.unset(field);
                break;

            case GET:
                System.out.println(this.get(field));
                break;

            case NUMEQUALTO:
                System.out.println(this.getNumEqualTo(field));
                break;
        }
    }
}
