package com.lousylynx.slik.route.parser.components;

import java.util.ArrayList;
import java.util.List;

import static com.lousylynx.slik.route.parser.UrlParser.Type.OPTIONAL;

public class OptionalComponent extends Component<List<Component>> {

    public OptionalComponent(Component sup) {
        super(sup, OPTIONAL, new ArrayList<>());
    }

    @Override
    public String asRegular() {
        String result = "(";

        for(Component child : children) {
            result += child.asRegular();
        }

        result += ")?";

        return result;
    }
}
