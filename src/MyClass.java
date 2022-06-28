import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class MyClass {
    private final String id;
    private final String name;
    private final Visibility visibility;
    private final ArrayList<MyClass> classSon;
    private final ArrayList<MyInterface> interfaceParent;
    private final ArrayList<MyOperation> operations;
    private final ArrayList<UmlAttribute> attributes;
    private MyClass classParent;
    private final Set<String> associationName;
    private final Set<String> duplicatedName;
    private static final Set<String> CYCLE_ID = new HashSet<>();

    public MyClass(String id, String name, Visibility visibility) {
        this.id = id;
        this.name = name;
        this.visibility = visibility;
        classSon = new ArrayList<>();
        interfaceParent = new ArrayList<>();
        operations = new ArrayList<>();
        classParent = null;
        attributes = new ArrayList<>();
        associationName = new HashSet<>();
        duplicatedName = new HashSet<>();
    }

    public void addClassSon(MyClass myClass) {
        classSon.add(myClass);
    }

    public void addInterface(MyInterface myInterface) {
        interfaceParent.add(myInterface);
    }

    public void addOperation(MyOperation myOperation) {
        operations.add(myOperation);
    }

    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
        addAssociationName(attribute.getName());
    }

    public void addAssociationName(String assName) {
        if (associationName.contains(assName)) {
            duplicatedName.add(assName);
        } else {
            associationName.add(assName);
        }
    }

    public int getClassSonSize() {
        return classSon.size();
    }

    public int getOperationSize() {
        return operations.size();
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

    public MyClass getClassParent() {
        return classParent;
    }

    public void setClassParent(MyClass classParent) {
        this.classParent = classParent;
    }

    public ArrayList<MyClass> getClassSon() {
        return classSon;
    }

    public ArrayList<MyInterface> getInterfaceParent() {
        return interfaceParent;
    }

    public ArrayList<MyOperation> getOperations() {
        return operations;
    }

    public boolean hasClassParent() {
        return classParent != null;
    }

    private boolean hasInterfaceParent() {
        return !interfaceParent.isEmpty();
    }

    public Map<Visibility, Integer> getOperationVisibility(String methodName) {
        Map<Visibility, Integer> map = new HashMap<>();
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        for (MyOperation myOperation : operations) {
            if (myOperation.getName().equals(methodName)) {
                map.put(myOperation.getVisibility(), map.get(myOperation.getVisibility()) + 1);
            }
        }
        return map;
    }

    public List<Integer> getOperationCouplingDegree(String methodName)
            throws MethodWrongTypeException, MethodDuplicatedException {
        ArrayList<Map<String, Integer>> arrayList = new ArrayList<>();
        boolean isDuplicate = false;
        for (MyOperation myOperation : operations) {
            if (myOperation.getName().equals(methodName)) {
                Map<String, Integer> map = myOperation.getParameterTypeIn();
                if (arrayList.contains(map)) {
                    isDuplicate = true;
                } else {
                    arrayList.add(map);
                }
            }
        }
        if (isDuplicate) {
            throw new MethodDuplicatedException(name, methodName);
        }
        arrayList = new ArrayList<>();
        for (MyOperation myOperation : operations) {
            if (myOperation.getName().equals(methodName)) {
                Map<String, Integer> map = myOperation.getParameterType();
                arrayList.add(map);
            }
        }
        List<Integer> integerList = new ArrayList<>();
        for (Map<String, Integer> map : arrayList) {
            int coupling = 0;
            for (String type : map.keySet()) {
                if (!MyOperation.CORRECT_TYPE.contains(type) &&
                        !type.equals(id) && !type.equals("void")) {
                    coupling += 1;
                }
            }
            integerList.add(coupling);
        }
        return integerList;
    }

    public Set<String> getAttributeCouplingDegree() {
        Set<String> recordType = new HashSet<>();
        for (UmlAttribute attribute : attributes) {
            if (attribute.getType() instanceof ReferenceType) {
                recordType.add(((ReferenceType) attribute.getType()).getReferenceId());
            }
        }
        return recordType;
    }

    public Set<String> getImplementInterfaceList() {
        Set<String> interfaceRealized = new HashSet<>();
        if (hasInterfaceParent()) {
            for (MyInterface myInterface : interfaceParent) {
                interfaceRealized.addAll(myInterface.getInterfaceList());
            }
        }
        return interfaceRealized;
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
        for (MyOperation myOperation : operations) {
            if (myOperation.findNull()) {
                return true;
            }
        }
        return false;
    }

    public Set<AttributeClassInformation> getDuplicatedAttribute() {
        if (duplicatedName.isEmpty()) {
            return null;
        }
        Set<AttributeClassInformation> informationSet = new HashSet<>();
        for (String string : duplicatedName) {
            informationSet.add(new AttributeClassInformation(string, name));
        }
        return informationSet;
    }

    public Set<UmlClassOrInterface> getParents() {
        Set<UmlClassOrInterface> classes = new HashSet<>();
        MyClass theClass = classParent;
        Set<String> cycleId = new HashSet<>();
        while (theClass != null) {
            MyClass finalTheClass = theClass;
            if (!CYCLE_ID.contains(theClass.getId())) {
                cycleId.add(theClass.getId());
                classes.add(new UmlClassOrInterface() {
                    @Override
                    public Visibility getVisibility() {
                        return null;
                    }

                    @Override
                    public ElementType getElementType() {
                        return null;
                    }

                    @Override
                    public String getId() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return finalTheClass.getName();
                    }

                    @Override
                    public String getParentId() {
                        return null;
                    }

                    @Override
                    public Map<String, Object> toJson() {
                        return null;
                    }
                });
            } else { return null; }
            if (theClass.getId().equals(id)) {
                if (!cycleId.isEmpty()) {
                    CYCLE_ID.addAll(cycleId);
                }
                return classes;
            }
            theClass = theClass.getClassParent();
        }
        return null;
    }
}
