package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    final Environment enclosing;
    private final Map<String, Integer> values = new HashMap<>();
    private final ArrayList<Object> valuesArray = new ArrayList<>();
    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        valuesArray.add(value);
        values.put(name, valuesArray.size() - 1);
    }

    Object getAt(int distance, int index) {
        return ancestor(distance).valuesArray.get(index);
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i ++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    void assignAt(int distance, int index, Object value) {
        ancestor(distance).valuesArray.set(index, value);
    }


    Object get(Token name) {
        if(values.containsKey(name.lexeme)) {
            return valuesArray.get(values.get(name.lexeme));
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable'" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            valuesArray.set(values.get(name.lexeme), value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}
