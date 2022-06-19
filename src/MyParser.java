import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;

import java.util.ArrayList;
import java.util.List;

public class MyParser {
    private final UmlElement[] elements;
    private final int size;
    private final ArrayList<MyClass> myClasses;
    private final ArrayList<MyInterface> myInterfaces;
    private final ArrayList<MyOperation> myOperations;
    private final ArrayList<MyStateMachine> myStateMachines;
    private final ArrayList<MyTransition> myTransitions;
    //private final ArrayList<MyState> myStates;
    private final ArrayList<MyInteraction> myInteractions;
    private final ArrayList<MyAssociation> myAssociations;
    private final ArrayList<UmlAttribute> myAttributes;
    private boolean check007;
    private boolean check008;

    public MyParser(UmlElement... elements) {
        //this.elements = new UmlElement[elements.length];
        //size = elements.length;
        this.elements = elements;
        this.size = elements.length;
        check007 = true;
        check008 = true;
        myClasses = new ArrayList<>();
        myInterfaces = new ArrayList<>();
        myOperations = new ArrayList<>();
        myStateMachines = new ArrayList<>();
        myTransitions = new ArrayList<>();
        //myStates = new ArrayList<>();
        myInteractions = new ArrayList<>();
        myAssociations = new ArrayList<>();
        myAttributes = new ArrayList<>();
        firstLoop();
        secondLoop();
        thirdLoop();
        fourthLoop();
        fifthLoop();
        for (MyAssociation myAssociation : myAssociations) {
            myAssociation.applyAssociation();
        }
    }

    private void firstLoop() {
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlClass) {
                myClasses.add(new MyClass(elements[i].getId(),
                        elements[i].getName(), ((UmlClass) elements[i]).getVisibility()));
            } else if (elements[i] instanceof UmlInterface) {
                myInterfaces.add(new MyInterface(elements[i].getId(),
                        elements[i].getName(), ((UmlInterface) elements[i]).getVisibility()));
            } else if (elements[i] instanceof UmlStateMachine) {
                myStateMachines.add(new MyStateMachine(elements[i].getId(),
                        elements[i].getName()));
            } else if (elements[i] instanceof UmlInteraction) {
                myInteractions.add(new MyInteraction(elements[i].getId(),
                        elements[i].getName(), elements[i].getParentId()));
            }
        }
    }

    private void secondLoop() {
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlGeneralization) {
                String sourceId = ((UmlGeneralization) elements[i]).getSource();
                String targetId = ((UmlGeneralization) elements[i]).getTarget();
                MyClass sourceClass = null;
                MyClass targetClass = null;
                for (MyClass myClass : myClasses) {
                    if (myClass.getId().equals(sourceId)) {
                        sourceClass = myClass;
                        break;
                    }
                }
                for (MyClass myClass : myClasses) {
                    if (myClass.getId().equals(targetId)) {
                        targetClass = myClass;
                        break;
                    }
                }
                if (sourceClass != null && targetClass != null) {
                    sourceClass.setClassParent(targetClass);
                    targetClass.addClassSon(sourceClass);
                } else {
                    MyInterface sourceInterface = null;
                    MyInterface targetInterface = null;
                    for (MyInterface myInterface : myInterfaces) {
                        if (myInterface.getId().equals(sourceId)) {
                            sourceInterface = myInterface;
                            break;
                        }
                    }
                    for (MyInterface myInterface : myInterfaces) {
                        if (myInterface.getId().equals(targetId)) {
                            targetInterface = myInterface;
                            break;
                        }
                    }
                    sourceInterface.addInterface(targetInterface);
                }
            } else if (elements[i] instanceof UmlOperation) {
                String parentId = elements[i].getParentId();
                MyOperation operation = new MyOperation(elements[i].getId(),
                        elements[i].getName(), ((UmlOperation) elements[i]).getVisibility());
                myOperations.add(operation);
                for (MyClass myClass : myClasses) {
                    if (myClass.getId().equals(parentId)) {
                        myClass.addOperation(operation);
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlRegion) {
                for (MyStateMachine stateMachine : myStateMachines) {
                    if (stateMachine.getId().equals(elements[i].getParentId())) {
                        stateMachine.setRegionId(elements[i].getId());
                        break;
                    }
                }
            }
        }
    }

    private void thirdLoop() {
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlParameter) {
                String parentId = elements[i].getParentId();
                for (MyOperation myOperation : myOperations) {
                    if (myOperation.getId().equals(parentId)) {
                        myOperation.addParameter((UmlParameter) elements[i]);
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlAttribute) {
                String parentId = elements[i].getParentId();
                myAttributes.add((UmlAttribute) elements[i]);
                for (MyClass myClass : myClasses) {
                    if (myClass.getId().equals(parentId)) {
                        myClass.addAttribute((UmlAttribute) elements[i]);
                        break;
                    }
                }
                for (MyInterface myInterface : myInterfaces) {
                    if (myInterface.getId().equals(parentId)) {
                        myInterface.addAttribute((UmlAttribute) elements[i]);
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlState) {
                for (MyStateMachine stateMachine : myStateMachines) {
                    if (stateMachine.getRegionId().equals(elements[i].getParentId())) {
                        stateMachine.addState(new MyState(elements[i].getId(),
                                elements[i].getName(), false));
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlPseudostate) {
                for (MyStateMachine stateMachine : myStateMachines) {
                    if (stateMachine.getRegionId().equals(elements[i].getParentId())) {
                        stateMachine.setInitialState(new MyState(elements[i].getId(),
                                elements[i].getName(), false));
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlFinalState) {
                for (MyStateMachine stateMachine : myStateMachines) {
                    if (stateMachine.getRegionId().equals(elements[i].getParentId())) {
                        stateMachine.addState(new MyState(elements[i].getId(),
                                elements[i].getName(), true));
                        break;
                    }
                }
            }
        }
    }

    private void fourthLoop() {
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlTransition) {
                for (MyStateMachine stateMachine : myStateMachines) {
                    if (stateMachine.getRegionId().equals(elements[i].getParentId())) {
                        try {
                            myTransitions.add(stateMachine.addLinks((UmlTransition) elements[i]));
                        } catch (Exception e) {
                            check008 = false;
                        }
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlInterfaceRealization) {
                String sourceId = ((UmlInterfaceRealization) elements[i]).getSource();
                String targetId = ((UmlInterfaceRealization) elements[i]).getTarget();
                MyClass sourceClass = null;
                MyInterface targetInterface = null;
                for (MyClass myClass : myClasses) {
                    if (myClass.getId().equals(sourceId)) {
                        sourceClass = myClass;
                        break;
                    }
                }
                for (MyInterface myInterface : myInterfaces) {
                    if (myInterface.getId().equals(targetId)) {
                        targetInterface = myInterface;
                        break;
                    }
                }
                sourceClass.addInterface(targetInterface);
            } else if (elements[i] instanceof UmlEndpoint) {
                for (MyInteraction interaction : myInteractions) {
                    if (interaction.getId().equals(elements[i].getParentId())) {
                        interaction.addEndPoint((UmlEndpoint) elements[i]);
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlLifeline) {
                for (MyInteraction interaction : myInteractions) {
                    if (interaction.getId().equals(elements[i].getParentId())) {
                        interaction.addLifeline((UmlLifeline) elements[i]);
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlAssociation) {
                myAssociations.add(new MyAssociation(elements[i].getId(), elements[i].getName()));
            }
        }
    }

    private void fifthLoop() {
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlEvent) {
                for (MyTransition myTransition : myTransitions) {
                    if (myTransition.getId().equals(elements[i].getParentId())) {
                        myTransition.addEvent(new MyEvent(elements[i].getId(),
                                elements[i].getName()));
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlMessage) {
                for (MyInteraction interaction : myInteractions) {
                    if (interaction.getId().equals(elements[i].getParentId())) {
                        try {
                            interaction.addMessage((UmlMessage) elements[i]);
                        } catch (Exception e) {
                            check007 = false;
                        }
                        break;
                    }
                }
            } else if (elements[i] instanceof UmlAssociationEnd) {
                for (MyAssociation myAssociation : myAssociations) {
                    if (myAssociation.getId().equals(elements[i].getParentId())) {
                        MyClass theClass = null;
                        for (MyClass myClass : myClasses) {
                            if (myClass.getId().equals(
                                    ((UmlAssociationEnd) elements[i]).getReference())) {
                                theClass = myClass;
                                break;
                            }
                        }
                        myAssociation.addAssociationEnd(theClass, elements[i].getName());
                    }
                }
            }
        }
    }

    public UmlElement[] getElements() {
        return elements;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<MyClass> getMyClasses() {
        return myClasses;
    }

    public ArrayList<MyInterface> getMyInterfaces() {
        return myInterfaces;
    }

    public ArrayList<MyOperation> getMyOperations() {
        return myOperations;
    }

    public ArrayList<MyStateMachine> getMyStateMachines() {
        return myStateMachines;
    }

    public ArrayList<MyTransition> getMyTransitions() {
        return myTransitions;
    }

    public ArrayList<MyInteraction> getMyInteractions() {
        return myInteractions;
    }

    public boolean getCheck007() {
        return check007;
    }

    public boolean getCheck008() {
        return check008;
    }

    public boolean check006() {
        for (MyInteraction myInteraction : myInteractions) {
            String parentId = myInteraction.getParentId();
            List<MyLifeline> lifelines = myInteraction.getLifelines();
            for (MyLifeline myLifeline : lifelines) {
                String represent = myLifeline.getRepresent();
                boolean match = false;
                for (UmlAttribute umlAttribute : myAttributes) {
                    if (represent != null && represent.equals(umlAttribute.getId())
                            && umlAttribute.getParentId().equals(parentId)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    return false;
                }
            }
        }
        return true;
    }
}
