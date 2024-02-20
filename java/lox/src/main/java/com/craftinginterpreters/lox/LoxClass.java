package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

public class LoxClass extends LoxInstance implements LoxCallable{
    final String name;
    private final Map<String, LoxFunction> methods;

    LoxClass(String name, Map<String, LoxFunction> methods) {
        super(null);
        this.name = name;
        this.methods = methods;
        super.klass = this;
    }

    LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        return null;
    }

    @Override
    Object get(Token name)
    {
        Object object = super.get(name);
        if (! (object instanceof LoxFunction))
        {
            throw new RuntimeError(name, "Can't find static class method: '" + name.lexeme + "' from class:" + this.name);
        }
        LoxFunction method = (LoxFunction) object;
        if (! method.isStaticMethod())
        {
            throw new RuntimeError(name, "Can't call non static class method: '" + name.lexeme + "' from class:" + this.name);
        }
        return method;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
