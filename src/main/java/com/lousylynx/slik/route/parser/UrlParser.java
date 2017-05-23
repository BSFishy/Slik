package com.lousylynx.slik.route.parser;

import com.lousylynx.slik.route.parser.components.*;

import static com.lousylynx.slik.route.parser.UrlParser.Type.*;

public class UrlParser {

    // Example: /download/{name}.{ext}[(/{name}.{ext})]
    //
    // Rules:
    // { } - Required input - Nothing except normal characters can be inside
    // [ ] - Optional
    // ( ) - Loop as much as needed - NO
    //  $  - Match all
    //  *  - Anything
    public static Url parse(String url) throws IllegalStateException {
        Component sup = new Url();
        Component currentComponent = null;
        Type currentType = SUPER;

        if(url.startsWith("$")) {
            sup.getSuperMost().setMatchAll(true);
            url = url.substring(1);
        }

        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            String s = String.valueOf(c);
            int subChar = i + 1;

            if (c == '/') { // A seperator - /
                if (currentType == INPUT) {
                    throw new IllegalStateException("Tried to put a \"/\" inside of a input block");
                } else if (currentType == REGULAR) {
                    sup.addChild(currentComponent);
                    url = url.substring(subChar);
                    currentComponent = null;
                    i = -1;
                    subChar = i + 1;
                }

                sup.addChild(new SeparatorComponent(sup));
                url = url.substring(subChar);
                currentType = Type.valueOf(sup);
                i = -1;
            } else if (c == '{') { // An input block - { }
                if (currentType == REGULAR) {
                    sup.addChild(currentComponent);
                    url = url.substring(subChar);
                    i = -1;
                }

                currentComponent = new InputComponent(sup);
                currentType = INPUT;
            } else if (c == '}') { // Closing input block - { }
                sup.addChild(currentComponent);
                url = url.substring(subChar);
                currentType = Type.valueOf(sup);
                currentComponent = null;
                i = -1;
            } else if (c == '[') { // An optional block - [ ]
                if (currentType == INPUT) {
                    throw new IllegalStateException("Tried to put a \"/\" inside of a input block");
                } else if (currentType == REGULAR) {
                    sup.addChild(currentComponent);
                    url = url.substring(subChar);
                    currentComponent = null;
                    i = -1;
                    subChar = i + 1;
                }

                sup = new OptionalComponent(sup);
                url = url.substring(subChar);
                currentType = OPTIONAL;
                i = -1;
            } else if (c == ']') { // Closing optional block - [ ]
                if(currentType == REGULAR) {
                    sup.addChild(currentComponent);
                    currentComponent = null;
                }

                sup.getSup().addChild(sup);
                sup = sup.getSup();
                url = url.substring(subChar);
                currentType = Type.valueOf(sup);
                i = -1;
//            } else if (c == '(') { // A loop block - ( )
//                if (currentType == INPUT) {
//                    throw new IllegalStateException("Tried to put a \"/\" inside of a input block");
//                } else if (currentType == REGULAR) {
//                    sup.addChild(currentComponent);
//                    url = url.substring(subChar);
//                    currentComponent = null;
//                    i = -1;
//                    subChar = i + 1;
//                }
//
//                sup = new LoopComponent(sup);
//                url = url.substring(subChar);
//                currentType = LOOP;
//                i = -1;
//            } else if (c == ')') { // Closing loop block - ( )
//                if(currentType == REGULAR) {
//                    sup.addChild(currentComponent);
//                    currentComponent = null;
//                }
//
//                sup.getSup().addChild(sup);
//                sup = sup.getSup();
//                url = url.substring(subChar);
//                currentType = Type.valueOf(sup);
//                i = -1;
            } else { // Regular text
                if (currentType == INPUT || currentType == REGULAR) {
                    currentComponent.append(s);
                } else {
                    currentComponent = new StringComponent(sup, s);
                    currentType = REGULAR;
                }
            }
        }

        if(currentType == REGULAR) {
            sup.addChild(currentComponent);
        }

        Url superMost = sup.getSuperMost();
        superMost.setRegular();

        return superMost;
    }

    public static boolean matches(Url haystack, String needle) {

        return false;
    }

    public enum Type {
        REGULAR(StringComponent.class),
        OPTIONAL(OptionalComponent.class),
        INPUT(InputComponent.class),
        LOOP(LoopComponent.class),
        SEPARATOR(SeparatorComponent.class),
        SUPER(Url.class);

        private final Class<? extends Component> clazz;

        Type(Class<? extends Component> clazz) {
            this.clazz = clazz;
        }

        public static Type valueOf(Component c) {
            for (Type t : values()) {
                if (t.clazz == c.getClass()) {
                    return t;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
