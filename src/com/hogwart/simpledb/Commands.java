package com.hogwart.simpledb;

/**
 * Created by sabbirmanandhar on 7/20/16.
 */
public enum Commands {
    SET ("SET"),
    UNSET ("UNSET"),
    GET ("GET"),
    NAMEQUALTO ("NAMEEQUAL"),
    END ("END"),
    BEGIN ("BEGIN"),
    ROLLBACK ("ROLLBACK"),
    COMMIT ("COMMIT");

    private String value;

    private Commands(String value) {
        this.value = value;
    }
}
