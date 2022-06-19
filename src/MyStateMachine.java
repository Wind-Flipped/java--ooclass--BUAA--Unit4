import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;

public class MyStateMachine {
    private final String id;
    private final String name;
    private String regionId;
    private MyState initialState;
    private final ArrayList<MyState> states;

    public MyStateMachine(String id, String name) {
        this.id = id;
        this.name = name;
        regionId = null;
        initialState = null;
        states = new ArrayList<>();
    }

    public void setRegionId(String id) {
        regionId = id;
    }

    public void setInitialState(MyState initialState) {
        this.initialState = initialState;
    }

    public void addState(MyState state) {
        states.add(state);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegionId() {
        return regionId;
    }

    public int getStatesNum() {
        return states.size() + 1;
    }

    public MyState getState(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        boolean isFind = false;
        MyState theState = null;
        for (MyState myState : states) {
            if (myState.getName() != null) {
                if (myState.getName().equals(stateName)) {
                    if (!isFind) {
                        isFind = true;
                        theState = myState;
                    } else {
                        throw new StateDuplicatedException(name, stateName);
                    }
                }
            }
        }
        if (isFind) {
            return theState;
        } else {
            throw new StateNotFoundException(name, stateName);
        }
    }

    public MyTransition addLinks(UmlTransition transition) throws Exception {
        String sourceId = transition.getSource();
        String targetId = transition.getTarget();
        String guard = transition.getGuard();
        MyState source = null;
        MyState target = null;
        if (initialState.getId().equals(sourceId)) {
            source = initialState;
        }
        for (MyState state : states) {
            if (state.getId().equals(sourceId)) {
                source = state;
            }
            if (state.getId().equals(targetId)) {
                target = state;
            }
        }
        MyTransition transition1 = new MyTransition(transition.getId(), transition.getName(),
                source, target, guard);
        assert source != null;
        source.addLink(transition1);
        return transition1;
    }

    public boolean canPass(String stateName) throws
            StateNotFoundException, StateDuplicatedException {
        MyState theState = null;
        if (stateName != null) {
            boolean isFind = false;
            for (MyState state : states) {
                if (state.getName() != null) {
                    if (state.getName().equals(stateName)) {
                        if (!isFind) {
                            isFind = true;
                            theState = state;
                        } else {
                            throw new StateDuplicatedException(name, stateName);
                        }
                    }
                }
            }
            if (!isFind) {
                throw new StateNotFoundException(name,stateName);
            }
            ArrayList<MyState> usedStates = new ArrayList<>();
            usedStates.add(theState);
            return initialState.goToFinal(usedStates);
        } else {
            return initialState.goToFinal(new ArrayList<>());
        }
    }

    public boolean checkAllStates() {
        for (MyState myState : states) {
            if (!myState.checkDuplicatedTransition()) {
                return false;
            }
        }
        return true;
    }
}
