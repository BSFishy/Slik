package com.lousylynx.slik.route.parser;

import com.lousylynx.slik.route.parser.components.Component;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Url extends Component<List<Component>> {

    @Getter
    private String regular;

    public Url() {
        super(null, UrlParser.Type.SUPER, new ArrayList<>());
    }

    public void addComponent(Component c) {
        data.add(c);
    }

    @Override
    public String asRegular() {
        String result = "";

        for (Component child : children) {
            result += child.asRegular();
        }

        return result;
    }

    public void setRegular() {
        regular = asRegular();
    }
}
