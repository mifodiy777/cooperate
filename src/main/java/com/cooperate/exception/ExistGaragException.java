package com.cooperate.exception;

/**
 * Created by Кирилл on 02.04.2017.
 */
public class ExistGaragException extends Exception {

    public ExistGaragException() {
        super("Невозможно создать, так как гараж уже существует!");
    }
}
