package com.lousylynx.slik.route.parser.components;

import java.util.regex.Pattern;

import static com.lousylynx.slik.route.parser.UrlParser.Type.REGULAR;

public class StringComponent extends Component<String> {

    public StringComponent(Component sup, String data) {
        super(sup, REGULAR, data);
    }

    @Override
    public String asRegular() {
        return Pattern.quote(data);
    }
}
