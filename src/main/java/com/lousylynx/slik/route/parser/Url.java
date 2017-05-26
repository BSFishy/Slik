package com.lousylynx.slik.route.parser;

import com.lousylynx.slik.Slik;
import com.lousylynx.slik.route.parser.components.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Url extends Component<List<Component>> {

    @Getter
    private String regular;
    private final Map<String, List<Integer>> inputs = new HashMap<>();
    private int position = 0;

    @Getter
    @Setter
    private boolean matchAll = false;

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

    public void add(String name) {
        List<Integer> value = (inputs.containsKey(name) ? inputs.get(name) : new ArrayList<>());
        value.add(position);
        inputs.put(name, value);

        positionAdd();
    }

    public void positionAdd() {
        position++;
    }

    public String getName(int index) {
        for(String key : inputs.keySet()) {
            List<Integer> value = inputs.get(key);
            if(value.contains(index))
                return key;
        }

        return "";
    }

    public String substitute(Map<String, String> values) {
        return recursiveSubstitute(children, values, false);
    }

    private String recursiveSubstitute(List<Component> components, Map<String, String> values, boolean optional) {
        String result = "";

        for(Component child : components) {
            if(child instanceof StringComponent) {
                result += ((StringComponent) child).getData();
            } else if(child instanceof SeparatorComponent) {
                result += "/";
            } else if(child instanceof OptionalComponent) {
                String optResult = recursiveSubstitute(child.getChildren(), values, true);

                if(!optResult.isEmpty()) {
                    result += optResult;
                }
            } else if(child instanceof InputComponent) {
                if(!values.containsKey(((InputComponent) child).getData()) && optional) {
                    return "";
                } else if(values.containsKey(((InputComponent) child).getData())) {
                    result += values.get(((InputComponent) child).getData());
                } else {
                    Slik.getLOG().warn("There was an issue getting the following input: \"" + ((InputComponent) child).getData() + "\". Ignoring");
                }
            }
        }

        return result;
    }
}
