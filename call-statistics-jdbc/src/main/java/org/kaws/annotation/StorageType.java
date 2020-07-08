package org.kaws.annotation;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 20:42
 * @Description:
 */
public enum StorageType {

    MYSQL("mysql"),

    MONGO("mongo");

    private final String message;

    StorageType(String message) {
        this.message = message;
    }



}
