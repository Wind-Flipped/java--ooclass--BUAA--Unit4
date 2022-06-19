import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyOperation {
    public static final ArrayList<String> CORRECT_TYPE = new ArrayList<>(Arrays.asList(
            "byte", "short", "int", "long", "float", "double", "char", "boolean", "String"));
    private final String id;
    private final String name;
    private final Visibility visibility;
    private final ArrayList<UmlParameter> parameters;

    public MyOperation(String id, String name, Visibility visibility) {
        this.id = id;
        this.name = name;
        this.visibility = visibility;
        parameters = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public ArrayList<UmlParameter> getParameters() {
        return parameters;
    }

    public void addParameter(UmlParameter parameter) {
        parameters.add(parameter);
    }

    private boolean isRightType() {
        for (UmlParameter parameter : parameters) {
            if (parameter.getType() instanceof NamedType) {
                if (parameter.getDirection() == Direction.IN) {
                    if (!CORRECT_TYPE.contains(((NamedType) parameter.getType()).getName())) {
                        return false;
                    }
                } else {
                    if (!CORRECT_TYPE.contains(((NamedType) parameter.getType()).getName()) &&
                            !((NamedType) parameter.getType()).getName().equals("void")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Map<String, Integer> getParameterTypeIn() throws MethodWrongTypeException {
        if (!isRightType()) {
            throw new MethodWrongTypeException(null,name);
        }
        Map<String, Integer> map = new HashMap<>();
        for (UmlParameter parameter : parameters) {
            if (!(parameter.getDirection() == Direction.IN)) {
                continue;
            }
            if (parameter.getType() instanceof NamedType) {
                if (map.containsKey(((NamedType) parameter.getType()).getName())) {
                    map.put(((NamedType) parameter.getType()).getName(),
                            map.get(((NamedType) parameter.getType()).getName()) + 1);
                } else {
                    map.put(((NamedType) parameter.getType()).getName(), 1);
                }
            } else {
                if (map.containsKey(((ReferenceType) parameter.getType()).getReferenceId())) {
                    map.put(((ReferenceType) parameter.getType()).getReferenceId(),
                            map.get(((ReferenceType) parameter.getType()).getReferenceId()) + 1);
                } else {
                    map.put(((ReferenceType) parameter.getType()).getReferenceId(), 1);
                }
            }
        }
        return map;
    }

    public Map<String, Integer> getParameterType() {
        Map<String, Integer> map = new HashMap<>();
        for (UmlParameter parameter : parameters) {
            if (parameter.getType() instanceof NamedType) {
                if (map.containsKey(((NamedType) parameter.getType()).getName())) {
                    map.put(((NamedType) parameter.getType()).getName(),
                            map.get(((NamedType) parameter.getType()).getName()) + 1);
                } else {
                    map.put(((NamedType) parameter.getType()).getName(), 1);
                }
            } else {
                if (map.containsKey(((ReferenceType) parameter.getType()).getReferenceId())) {
                    map.put(((ReferenceType) parameter.getType()).getReferenceId(),
                            map.get(((ReferenceType) parameter.getType()).getReferenceId()) + 1);
                } else {
                    map.put(((ReferenceType) parameter.getType()).getReferenceId(), 1);
                }
            }
        }
        return map;
    }

    private boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }

    public boolean findNull() {
        if (isBlankString(name)) {
            return true;
        }
        for (UmlParameter umlParameter : parameters) {
            if (umlParameter.getDirection() != Direction.RETURN) {
                if (isBlankString(umlParameter.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
