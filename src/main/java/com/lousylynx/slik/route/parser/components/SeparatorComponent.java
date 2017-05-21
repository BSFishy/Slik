package com.lousylynx.slik.route.parser.components;

import static com.lousylynx.slik.route.parser.UrlParser.Type.SEPARATOR;

public class SeparatorComponent extends Component<String> {

    public SeparatorComponent(Component sup) {
        super(sup, SEPARATOR, "/");
    }

    @Override
    public String asRegular() {
        return "\\/";
    }
}
