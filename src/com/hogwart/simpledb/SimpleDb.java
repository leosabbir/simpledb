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
        Object currentValue = this.find(key, this.currentTxn);

        if (currentValue != null) {
            if (!currentValue.equals(value)) {
                if (this.counteMap.get(currentValue) == 1) {
                    this.counteMap.remove(currentValue);
                } else {
                    this.counteMap.put(currentValue, this.counteMap.get(currentValue) - 1);
                }
            } else {
                return;
            }
        }

        this.currentTxn.set(key, value);

        if (!this.counteMap.containsKey(value)) {
            this.counteMap.put(value, 1);
        } else {
            this.counteMap.put(value, this.counteMap.get(value) + 1);
        }

    }

    public void unset(String key) {
        Object value = this.find(key, this.currentTxn);
        this.currentTxn.unset(key);
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

    private void rollback() {
        if (this.currentTxn.getParent() == null) {
            System.out.println("NO TRANSACTION");
            return;
        }

        Storage txnCache = this.currentTxn.getCache();
        // rollback counter values
        for(Map.Entry<String, Object> entry : txnCache.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) { // unset command
                Object prevValue = this.find(key, this.currentTxn.getParent());

                if (!this.counteMap.containsKey(prevValue)) {
                    this.counteMap.put(prevValue, 1);
                } else {
                    this.counteMap.put(prevValue, this.counteMap.get(prevValue) + 1);
                }

            } else { //set command
                if (this.counteMap.get(value) == 1) {
                    this.counteMap.remove(value);
                } else {
                    this.counteMap.put(value, this.counteMap.get(value) - 1);
                }

                Object prevValue = this.find(key, this.currentTxn.getParent());

                if (!this.counteMap.containsKey(prevValue)) {
                    this.counteMap.put(prevValue, 1);
                } else {
                    this.counteMap.put(prevValue, this.counteMap.get(prevValue) + 1);
                }
            }
        }


        //change pointer currentTxn
        Transaction temp = this.currentTxn;
        this.currentTxn = this.currentTxn.getParent();
        this.currentTxn.unsetChild();
        temp = null; //free memory block

    }

    private void commit() {
        if (this.currentTxn.getParent() == null) {
            System.out.println("NO TRANSACTION");
            return;
        }

        Transaction child = this.rootTxn.getChild();
        this.currentTxn = this.rootTxn;
        while (child != null) {
            Storage txnCache = child.getCache();

            for(Map.Entry<String, Object> entry : txnCache.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    this.rootTxn.unset(key);
                } else {
                    this.rootTxn.set(key, value);
                }
            }
            child = child.getChild();
        }
        this.currentTxn.unsetChild();
    }

    private Object find(String key, Transaction searchFrom) {
        Transaction txn = searchFrom;
        while (txn != null) {
            if(txn.hasKey(key)) {
                return txn.get(key);
            }
            txn = txn.getParent();
        }
        return null;
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
                System.out.println(this.find(field, this.currentTxn));
                break;

            case NUMEQUALTO:
                System.out.println(this.getNumEqualTo(field));
                break;

            case BEGIN:
                this.currentTxn = new Transaction(this.currentTxn);
                break;

            case ROLLBACK:
                this.rollback();
                break;

            case COMMIT:
                this.commit();
                break;

            default:
                System.err.println("Unsupported command");

        }
    }
}
