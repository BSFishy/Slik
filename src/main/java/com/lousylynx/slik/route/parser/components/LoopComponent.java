package com.lousylynx.slik.route.parser.components;

import java.util.ArrayList;
import java.util.List;

import static com.lousylynx.slik.route.parser.UrlParser.Type.LOOP;

public class LoopComponent extends Component<List<Component>> {

    public LoopComponent(Component sup) {
        super(sup, LOOP, new ArrayList<>());
    }

    @Override
    public String asRegular() {
        String result = "(";

        for(Component child : children) {
            result += child.asRegular();
        }

        result += ")+";

        return result;
    }
}
