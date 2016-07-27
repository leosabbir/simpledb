package com.hogwart.simpledb;

/**
 * Created by sabbirmanandhar on 7/23/16.
 *
 * Whenever BEGIN command is encountered, a new transaction is represented by this class
 *
 * Transaction class has a cache which is an instance of Storage class. The cache holds holds
 * variables belonging to the current transaction.
 *
 * Transaction also holds pointer to next Transaction or a child transaction which is nested transaction of
 * current transaction.
 * Besides the Transaction also holds point to parent transaction.
 */
public class Transaction {

    //private int txdID;
    private Storage cache;

    private Transaction parent;
    private Transaction child;



    public Transaction(Transaction parent) {
        //this.txdID = id;
        this.cache = new Storage();
        this.parent = parent;
        if (parent != null) {
            parent.child = this;
        }
    }

    public Transaction getParent() {
        return this.parent;
    }

    public Transaction getChild() {
        return this.child;
    }

    public Object get(String key) {
        return this.cache.getValue(key);
    }

    public boolean set(String key, Object value) {
        return this.cache.set(key, value);
    }

    public Object unset(String key) {
        return this.cache.unset(key, this.parent != null);
    }

    public Storage getCache() {
        return this.cache;
    }

    public void unsetChild() {
        this.child = null;
    }

    public boolean hasKey(String key) {
        return this.cache.containsKey(key);
    }
}
