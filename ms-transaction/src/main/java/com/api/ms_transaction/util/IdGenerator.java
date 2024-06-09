package com.api.ms_transaction.util;

public class IdGenerator {

    //todo: hacer que este generador funcione independientemente para diferentes entidades como transacciones de diferentes tipos

    private static IdGenerator instance;
    private long currentId;

    private IdGenerator() {
        this.currentId = 0; // Puedes iniciar en cualquier valor que desees
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public synchronized long generateId() {
        return ++currentId;
    }
}

