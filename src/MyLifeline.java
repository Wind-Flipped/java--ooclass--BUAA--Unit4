import java.util.ArrayList;
import java.util.List;

public class MyLifeline {
    private final String id;
    private final String name;
    private final boolean isEndPoint;
    private final MyInteraction interaction;
    private final String represent;
    private final List<MyMessage> createMessage;
    private final List<MyMessage> foundMessage;
    private final List<MyMessage> lostMessage;
    private final List<String> attributesName;
    private boolean isDestoryed;

    public MyLifeline(String id, String name, boolean isEndPoint,
                      MyInteraction interaction, String represent) {
        this.id = id;
        this.name = name;
        this.isEndPoint = isEndPoint;
        this.interaction = interaction;
        this.represent = represent;
        isDestoryed = false;
        createMessage = new ArrayList<>();
        foundMessage = new ArrayList<>();
        lostMessage = new ArrayList<>();
        attributesName = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEndPoint() {
        return isEndPoint;
    }

    public void destoryLifeline() {
        isDestoryed = true;
    }

    public boolean isDestoryed() {
        return isDestoryed;
    }

    public void addCreateMessage(MyMessage message) {
        createMessage.add(message);
    }

    public void addFoundMessage(MyMessage message) {
        foundMessage.add(message);
    }

    public void addLostMessage(MyMessage message) {
        lostMessage.add(message);
    }

    public void addAttributeName(String attributeName) {
        attributesName.add(attributeName);
    }

    public int getCreateMessageNum() {
        return createMessage.size();
    }

    public int getFoundMessageNum() {
        return foundMessage.size();
    }

    public int getLostMessageNum() {
        return lostMessage.size();
    }

    public boolean containsAttributeName(String attributeName) {
        return attributesName.contains(attributeName);
    }

    public String getCreator() {
        return createMessage.get(0).getSource().getId();
    }

    public String getRepresent() {
        return represent;
    }
}
