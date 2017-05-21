package com.lousylynx.slik.route.parser.components;

import com.lousylynx.slik.route.parser.Url;
import com.lousylynx.slik.route.parser.UrlParser.Type;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Component<T> {

    @Getter
    private final Component sup;
    @Getter
    private final Type type;
    @Getter
    protected T data;
    @Getter
    protected final List<Component> children = new ArrayList<>();

    public Component(Component sup, Type type, T data) {
        this.sup = sup;
        this.type = type;
        this.data = data;
    }

    public abstract String asRegular();

    public void addChild(Component child) {
        children.add(child);
    }

    public Component getSuper() {
        return sup;
    }

    public Url getSuperMost() {
        if (this instanceof Url) {
            return (Url) this;
        }

        return sup.getSuperMost();
    }

    public void append(String d) throws IllegalStateException {
        if (!(this instanceof InputComponent || this instanceof StringComponent)) {
            throw new IllegalStateException("Attempted to append to a type that does not accept");
        }

        data = (T) (data + d);
    }

    @Override
    public final String toString() {
        return getClass().getName() + "(super=" + (sup == null ? "NULL" : sup.getClass().getName()) + ", type=" + type + ", data=" + (data == null ? "NULL" : data) + ", children=" + children + ")";
    }
}
