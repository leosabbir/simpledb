package com.hogwart.simpledb;

/**
 * Created by sabbirmanandhar on 7/23/16.
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
        return this.cache.containsKey(key) && this.cache.get(key) != null;
    }
}
