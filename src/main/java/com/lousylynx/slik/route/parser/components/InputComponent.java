package com.lousylynx.slik.route.parser.components;

import static com.lousylynx.slik.route.parser.UrlParser.Type.INPUT;

public class InputComponent extends Component<String> {

    public InputComponent(Component sup) {
        super(sup, INPUT, "");
    }

    @Override
    public String asRegular() {
        getSuperMost().add(getData());

        return "([^.\\/]+)";
    }
}
