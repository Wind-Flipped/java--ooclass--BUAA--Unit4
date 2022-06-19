import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

public class MyTransition {
    private final String id;
    private final String name;
    private final MyState source;
    private final MyState target;
    private final String guard;
    private final List<MyEvent> events;
    private final Set<String> eventsName;

    public MyTransition(String id, String name,MyState source,MyState target,String guard) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.target = target;
        this.guard = guard;
        events = new ArrayList<>();
        eventsName = new HashSet<>();
    }

    public void addEvent(MyEvent event) {
        events.add(event);
    }

    public MyState getTarget() {
        return target;
    }

    public String getId() {
        return id;
    }

    public List<String> getEventsName() {
        List<String> names = new ArrayList<>();
        for (MyEvent event : events) {
            names.add(event.getName());
        }
        return names;
    }

    public void updateEventsName() {
        for (MyEvent event : events) {
            eventsName.add(event.getName());
        }
    }

    public String getGuard() {
        return guard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyTransition)) {
            return false;
        }
        MyTransition that = (MyTransition) o;
        return guard.equals(that.guard) && eventsName.equals(that.eventsName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guard, eventsName);
    }
}
