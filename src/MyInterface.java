import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyInterface {
    private final String id;
    private final String name;
    private final Visibility visibility;
    private final ArrayList<MyInterface> interfaces;
    private final ArrayList<UmlAttribute> attributes;
    private final Set<String> interfaceString;
    private final Set<MyInterface> parentInterfaces;
    public static final Set<MyInterface> CYCLE_INTERFACES = new HashSet<>();
    private final Set<MyInterface> cycleInterfaces;
    private int sign;

    public MyInterface(String id, String name, Visibility visibility) {
        this.id = id;
        this.name = name;
        this.visibility = visibility;
        interfaces = new ArrayList<>();
        attributes = new ArrayList<>();
        interfaceString = new HashSet<>();
        interfaceString.add(name);
        parentInterfaces = new HashSet<>();
        //parentInterfaces.addAll(interfaces);
        cycleInterfaces = new HashSet<>();
        sign = 0;
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
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

    public ArrayList<MyInterface> getInterfaces() {
        return interfaces;
    }

    public void addInterface(MyInterface myInterface) {
        interfaces.add(myInterface);
    }

    public Set<String> getInterfaceList() {
        if (!interfaces.isEmpty()) {
            for (MyInterface myInterface : interfaces) {
                interfaceString.addAll(myInterface.getInterfaceList());
            }
        }
        return interfaceString;
    }

    private boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }

    public boolean findNull() {
        if (isBlankString(name)) {
            return true;
        }
        for (UmlAttribute umlAttribute : attributes) {
            if (isBlankString(umlAttribute.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean getParentsSet(MyInterface theInterface) {
        if (parentInterfaces.contains(theInterface)) {
            return true;
        }
        parentInterfaces.add(theInterface);
        if (!theInterface.interfaces.isEmpty()) {
            for (MyInterface myInterface : theInterface.interfaces) {
                if (getParentsSet(myInterface)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkPublicAttribute() {
        for (UmlAttribute umlAttribute : attributes) {
            if (!(umlAttribute.getVisibility() == Visibility.PUBLIC)) {
                return false;
            }
        }
        return true;
    }

    public boolean getCycles(MyInterface theInterface) {
        if (cycleInterfaces.contains(theInterface)) {
            return false;
        }
        if (sign == 0) {
            sign = 1;
        } else {
            cycleInterfaces.add(theInterface);
        }
        if (cycleInterfaces.contains(this)) {
            return true;
        }
        for (MyInterface myInterface : theInterface.interfaces) {
            if (getCycles(myInterface)) {
                CYCLE_INTERFACES.addAll(cycleInterfaces);
                return true;
            }
        }
        cycleInterfaces.remove(theInterface);
        return false;
    }
}
