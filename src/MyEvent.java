public class MyEvent {
    private final String id;
    private final String name;

    public MyEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
