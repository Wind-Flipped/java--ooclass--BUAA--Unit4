import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyState {
    private final String id;
    private final String name;
    private final boolean isFinal;
    private final List<MyTransition> transitions;

    public MyState(String id, String name, boolean isFinal) {
        this.id = id;
        this.name = name;
        this.isFinal = isFinal;
        transitions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addLink(MyTransition transition) throws Exception {
        if (isFinal) {
            throw new Exception();
        }
        transitions.add(transition);
    }

    public List<String> getEventsName(String s, MyState target) throws TransitionNotFoundException {
        List<String> names = new ArrayList<>();
        for (MyTransition transition : transitions) {
            if (transition.getTarget() == target) {
                names.addAll(transition.getEventsName());
            }
        }
        if (names.isEmpty()) {
            throw new TransitionNotFoundException(s, name, target.getName());
        }
        return names;
    }

    public boolean goToFinal(List<MyState> usedStates) {
        for (MyTransition transition : transitions) {
            if (transition.getTarget().isFinal) {
                return true;
            } else if (usedStates.contains(transition.getTarget())) {
                continue;
            } else {
                usedStates.add(transition.getTarget());
                if (transition.getTarget().goToFinal(usedStates)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDuplicatedTransition() {
        for (MyTransition myTransition : transitions) {
            myTransition.updateEventsName();
        }
        List<MyTransition> newTransitions = new ArrayList<>(transitions);
        while (!newTransitions.isEmpty()) {
            MyTransition oneTransition = newTransitions.get(0);
            newTransitions.remove(0);
            Set<String> eventsName1 = new HashSet<>(oneTransition.getEventsName());
            for (MyTransition theTransition : newTransitions) {
                Set<String> eventsName2 = new HashSet<>(theTransition.getEventsName());
                for (String eventName : eventsName1) { if (eventsName2.contains(eventName))
                    { if (isBlankString(oneTransition.getGuard()) || isBlankString(
                            theTransition.getGuard()) || oneTransition.getGuard().equals(
                                    theTransition.getGuard())) { return false; } }
                }
            }
        }
        return true;
    }

    private boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }
}
