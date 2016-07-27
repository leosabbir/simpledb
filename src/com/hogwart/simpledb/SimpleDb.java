package com.hogwart.simpledb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sabbirmanandhar on 7/20/16.
 *
 * This is the Singleton class representing the main in memory database.
 * It has a root transaction whose cache is the main memory of the database.
 * Subsequent transactions are then represented doubly linked list.
 */
public class SimpleDb {

    private Transaction rootTxn; // root transaction being the main memory holding all variables
    private Transaction currentTxn; // holds pointer to most recent transaction, as nesting grows
    private Map<Object, Integer> counterMap; // Keeps record of count of all variables with given value

    private static SimpleDb INSTANCE;

    private SimpleDb() {
        this.rootTxn = new Transaction(null);
        this.currentTxn = rootTxn;
        counterMap = new HashMap<Object, Integer>();
    }

    public static SimpleDb  GET_INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleDb();
        }
        return INSTANCE;
    }

    /**
     * Saves the variable in current transaction. Before saving the variable,
     * it first checks if the variable already exists in the current or previous
     * transaction. If already present then counterMap has to be updated accordingly.
     *
     * @param key variable to be saved
     * @param value value of the variable
     */
    public void set(String key, Object value) {
        Object currentValue = this.find(key, this.currentTxn);

        if (currentValue != null) {
            if (!currentValue.equals(value)) {
                // update counter Map with new value
                if (this.counterMap.get(currentValue) == 1) {
                    this.counterMap.remove(currentValue);
                } else {
                    this.counterMap.put(currentValue, this.counterMap.get(currentValue) - 1);
                }
            } else {
                return; // nothing to be done if old value is same as new
            }
        }

        this.currentTxn.set(key, value);

        // Update counter Map for old value
        if (!this.counterMap.containsKey(value)) {
            this.counterMap.put(value, 1);
        } else {
            this.counterMap.put(value, this.counterMap.get(value) + 1);
        }

    }

    /**
     *  Removes the variable key from the system from current transaction scrope and update
     *  counterMap accordingly.
     *
     * @param key the variable to be removed from the system
     */
    public void unset(String key) {
        Object value = this.find(key, this.currentTxn);
        this.currentTxn.unset(key);
        if ( value != null ) {
            int currentCount = this.counterMap.get(value);
            if (currentCount == 1) {
                this.counterMap.remove(value);
            } else {
                this.counterMap.put(value, currentCount -  1);
            }
        }
    }

    /**
     * Returns the count of variables with value same as input parameter value
     *
     * @param value
     * @return count of variables
     */
    public int getNumEqualTo(Object value) {
        if (this.counterMap.containsKey(value)) {
            return this.counterMap.get(value);
        }
        return 0;
    }

    /**
     * Undo all the commands basically Set and Unset commands executed in
     * current transaction scope.
     *
     */
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

                if (!this.counterMap.containsKey(prevValue)) {
                    this.counterMap.put(prevValue, 1);
                } else {
                    this.counterMap.put(prevValue, this.counterMap.get(prevValue) + 1);
                }

            } else { //set command
                if (this.counterMap.get(value) == 1) {
                    this.counterMap.remove(value);
                } else {
                    this.counterMap.put(value, this.counterMap.get(value) - 1);
                }

                Object prevValue = this.find(key, this.currentTxn.getParent());

                if (!this.counterMap.containsKey(prevValue)) {
                    this.counterMap.put(prevValue, 1);
                } else {
                    this.counterMap.put(prevValue, this.counterMap.get(prevValue) + 1);
                }
            }
        }


        //change pointer currentTxn
        Transaction temp = this.currentTxn;
        this.currentTxn = this.currentTxn.getParent();
        this.currentTxn.unsetChild();
        temp = null; //free memory block

    }

    /**
     * Applies all the commands of all transactions (Set and Unset) to the root transaction
     */
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

    /**
     * finds the value of the variable in the system. Search starts from the given
     * transaction and propagates towards parent. Value is returned from the most recent one.
     *
     * @param key variable to find
     * @param searchFrom transaction to start the search from
     * @return value if found, null otherwise
     */
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

    /**
     * executes the command
     *
     * @param command
     * @param field
     * @param value
     */
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
        }
    }
}
