package com.hogwart.simpledb;

/**
 * Created by sabbirmanandhar on 7/20/16.
 *
 *
 * enum representation of String commands.
 *
 * All the commands are represented by an enum
 */
public enum Commands {
    SET ("SET"),
    UNSET ("UNSET"),
    GET ("GET"),
    NUMEQUALTO ("NUMEQUALTO"),
    END ("END"),
    BEGIN ("BEGIN"),
    ROLLBACK ("ROLLBACK"),
    COMMIT ("COMMIT");

    private String value;

    private Commands(String value) {
        this.value = value;
    }
}
