package com.hogwart.simpledb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sabbirmanandhar on 7/20/16.
 */
public class SimpleDb {

    private Map<String, Object> storage;

    public SimpleDb() {
        this.storage = new HashMap<String, Object>();
    }

    public void execute (Commands command, String field, Object value) {

        switch (command) {
            case SET:
                this.storage.put(field, value);
                break;

            case UNSET:
                this.storage.remove(field);
                break;

            case GET:
                System.out.println(this.storage.get(field));
                break;

            case NAMEQUALTO:
                int counter = 0;
                for(Object dbValue : this.storage.values()) {
                    if (dbValue.equals(value)) {
                        counter++;
                    }
                }
                System.out.println(counter);
                break;
        }
    }
}
