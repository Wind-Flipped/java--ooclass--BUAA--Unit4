import com.oocourse.uml3.models.common.MessageSort;

public class MyMessage {
    private final String id;
    private final String name;
    private final MessageSort messageSort;
    private final MyLifeline source;
    private final MyLifeline target;

    public MyMessage(String id, String name, MessageSort messageSort,
                     MyLifeline source, MyLifeline target) {
        this.id = id;
        this.name = name;
        this.messageSort = messageSort;
        this.source = source;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MessageSort getMessageSort() {
        return messageSort;
    }

    public MyLifeline getSource() {
        return source;
    }

    public MyLifeline getTarget() {
        return target;
    }
}
