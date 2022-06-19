import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.List;

public class MyInteraction {
    private final String id;
    private final String name;
    private final String parentId;
    private final List<MyLifeline> lifelines;
    private final List<MyLifeline> endPoints;

    public MyInteraction(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        lifelines = new ArrayList<>();
        endPoints = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }

    public void addEndPoint(UmlEndpoint endpoint) {
        endPoints.add(new MyLifeline(endpoint.getId(), endpoint.getName(),
                true, this, null));
    }

    public void addLifeline(UmlLifeline lifeline) {
        lifelines.add(new MyLifeline(lifeline.getId(),
                lifeline.getName(), false, this, lifeline.getRepresent()));
    }

    public List<MyLifeline> getLifelines() {
        return lifelines;
    }

    public void addMessage(UmlMessage message) throws Exception {
        String sourceId = message.getSource();
        String targetId = message.getTarget();
        MyLifeline source = null;
        MyLifeline target = null;
        for (MyLifeline lifeline : lifelines) {
            if (lifeline.getId().equals(sourceId)) {
                source = lifeline;
            }
            if (lifeline.getId().equals(targetId)) {
                target = lifeline;
            }
        }
        for (MyLifeline endPoint : endPoints) {
            if (endPoint.getId().equals(sourceId)) {
                source = endPoint;
            }
            if (endPoint.getId().equals(targetId)) {
                target = endPoint;
            }
        }
        assert source != null;
        assert target != null;
        MyMessage myMessage = new MyMessage(message.getId(), message.getName(),
                message.getMessageSort(), source, target);
        if (target.isDestoryed()) {
            throw new Exception();
        }
        if (message.getMessageSort() == MessageSort.CREATE_MESSAGE) {
            target.addCreateMessage(myMessage);
        }
        if (message.getMessageSort() == MessageSort.DELETE_MESSAGE) {
            target.destoryLifeline();
        }
        if (source.isEndPoint() && !target.isEndPoint()) {
            target.addFoundMessage(myMessage);
        }
        if (!source.isEndPoint() && target.isEndPoint()) {
            source.addLostMessage(myMessage);
        }
    }

    public int getLifelineNum() {
        return lifelines.size();
    }

    private MyLifeline getTheLifeline(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        boolean isFind = false;
        MyLifeline theLifeline = null;
        for (MyLifeline lifeline : lifelines) {
            if (lifeline.getName() != null) {
                if (lifeline.getName().equals(lifelineName)) {
                    if (!isFind) {
                        isFind = true;
                        theLifeline = lifeline;
                    } else {
                        throw new LifelineDuplicatedException(name, lifelineName);
                    }
                }
            }
        }
        if (isFind) {
            return theLifeline;
        } else {
            throw new LifelineNotFoundException(name, lifelineName);
        }
    }

    public String getCreatorId(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        MyLifeline myLifeline = getTheLifeline(lifelineName);
        int num = myLifeline.getCreateMessageNum();
        if (num == 0) {
            throw new LifelineNeverCreatedException(name, lifelineName);
        } else if (num > 1) {
            throw new LifelineCreatedRepeatedlyException(name, lifelineName);
        } else {
            return myLifeline.getCreator();
        }
    }

    public Pair<Integer, Integer> getFoundLostMessageNum(String lifelineName) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        MyLifeline myLifeline = getTheLifeline(lifelineName);
        return new Pair<>(myLifeline.getFoundMessageNum(), myLifeline.getLostMessageNum());
    }
}
