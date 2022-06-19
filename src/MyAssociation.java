public class MyAssociation {
    private final String id;
    private final String name;
    private MyClass class1;
    private MyClass class2;
    private String name1;
    private String name2;
    private int num;

    public MyAssociation(String id, String name) {
        this.id = id;
        this.name = name;
        num = 0;
        class1 = null;
        class2 = null;
        name1 = null;
        name2 = null;
    }

    public void addAssociationEnd(MyClass myClass, String name) {
        if (num > 0) {
            class2 = myClass;
            name2 = name;
        } else {
            class1 = myClass;
            name1 = name;
        }
        num++;
    }

    public void applyAssociation() {
        if (!isBlankString(name1)) {
            class2.addAssociationName(name1);
        }
        if (!isBlankString(name2)) {
            class1.addAssociationName(name2);
        }
    }

    public String getId() {
        return id;
    }

    private boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }
}
