import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyImplementation implements UserApi {
    //private final int size;
    private final UmlElement[] elements;
    private final int size;
    private final MyParser myParser;
    private final ArrayList<MyClass> myClasses;
    private final ArrayList<MyInterface> myInterfaces;
    private final ArrayList<MyOperation> myOperations;
    private final ArrayList<MyStateMachine> myStateMachines;
    private final ArrayList<MyTransition> myTransitions;
    //private final ArrayList<MyState> myStates;
    private final ArrayList<MyInteraction> myInteractions;

    public MyImplementation(UmlElement... elements) {
        MyParser myParser = new MyParser(elements);
        this.elements = myParser.getElements();
        this.size = myParser.getSize();
        myClasses = myParser.getMyClasses();
        myInterfaces = myParser.getMyInterfaces();
        myOperations = myParser.getMyOperations();
        myStateMachines = myParser.getMyStateMachines();
        myTransitions = myParser.getMyTransitions();
        //myStates = new ArrayList<>();
        myInteractions = myParser.getMyInteractions();
        this.myParser = myParser;
    }

    @Override
    public int getClassCount() {
        return myClasses.size();
    }

    private MyClass getMyClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        boolean isFind = false;
        MyClass assignedClass = null;
        for (MyClass myClass : myClasses) {
            if (myClass.getName().equals(className)) {
                if (!isFind) {
                    isFind = true;
                    assignedClass = myClass;
                } else {
                    throw new ClassDuplicatedException(className);
                }
            }
        }
        if (isFind) {
            return assignedClass;
        } else {
            throw new ClassNotFoundException(className);
        }
    }

    private MyStateMachine getStateMachine(String name) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        boolean isFind = false;
        MyStateMachine stateMachine = null;
        for (MyStateMachine stateMachine1 : myStateMachines) {
            if (stateMachine1.getName().equals(name)) {
                if (!isFind) {
                    isFind = true;
                    stateMachine = stateMachine1;
                } else {
                    throw new StateMachineDuplicatedException(name);
                }
            }
        }
        if (isFind) {
            return stateMachine;
        } else {
            throw new StateMachineNotFoundException(name);
        }
    }

    private MyInteraction getInteraction(String name) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        boolean isFind = false;
        MyInteraction theInteraction = null;
        for (MyInteraction interaction : myInteractions) {
            if (interaction.getName().equals(name)) {
                if (!isFind) {
                    isFind = true;
                    theInteraction = interaction;
                } else {
                    throw new InteractionDuplicatedException(name);
                }
            }
        }
        if (isFind) {
            return theInteraction;
        } else {
            throw new InteractionNotFoundException(name);
        }
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return getMyClass(className).getClassSonSize();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return getMyClass(className).getOperationSize();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass theClass = getMyClass(className);
        return theClass.getOperationVisibility(methodName);
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyClass theClass = getMyClass(className);
        try {
            return theClass.getOperationCouplingDegree(methodName);
        } catch (MethodWrongTypeException e) {
            throw new MethodWrongTypeException(className, methodName);
        }
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass theClass = getMyClass(className);
        Set<String> recordReferenceType = new HashSet<>(theClass.getAttributeCouplingDegree());
        while (theClass.hasClassParent()) {
            theClass = theClass.getClassParent();
            recordReferenceType.addAll(theClass.getAttributeCouplingDegree());
        }
        int coupling = recordReferenceType.size();
        if (recordReferenceType.contains(getMyClass(className).getId())) {
            coupling -= 1;
        }
        return coupling;
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass theClass = getMyClass(className);
        Set<String> interfaceSet = new HashSet<>(theClass.getImplementInterfaceList());
        while (theClass.hasClassParent()) {
            theClass = theClass.getClassParent();
            interfaceSet.addAll(theClass.getImplementInterfaceList());
        }
        return new ArrayList<>(interfaceSet);
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass theClass = getMyClass(className);
        int depth = 0;
        while (theClass.hasClassParent()) {
            theClass = theClass.getClassParent();
            depth += 1;
        }
        return depth;
    }

    @Override
    public int getParticipantCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return getInteraction(s).getLifelineNum();
    }

    @Override
    public UmlLifeline getParticipantCreator(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        String lifelineId = getInteraction(s).getCreatorId(s1);
        for (int i = 0; i < size; i += 1) {
            if (elements[i] instanceof UmlLifeline) {
                if (elements[i].getId().equals(lifelineId)) {
                    return (UmlLifeline) elements[i];
                }
            }
        }
        return null;
    }

    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return getInteraction(s).getFoundLostMessageNum(s1);
    }

    @Override
    public int getStateCount(String s) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return getStateMachine(s).getStatesNum();
    }

    @Override
    public boolean getStateIsCriticalPoint(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        boolean isPassed = getStateMachine(s).canPass(null);
        boolean isCrucial = getStateMachine(s).canPass(s1);
        return isPassed && !isCrucial;
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2) throws
            StateMachineNotFoundException, StateMachineDuplicatedException, StateNotFoundException,
            StateDuplicatedException, TransitionNotFoundException {
        MyState source = getStateMachine(s).getState(s1);
        MyState target = getStateMachine(s).getState(s2);
        return source.getEventsName(s, target);
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        for (MyClass myClass : myClasses) {
            if (myClass.findNull()) {
                throw new UmlRule001Exception();
            }
        }
        for (MyInterface myInterface : myInterfaces) {
            if (myInterface.findNull()) {
                throw new UmlRule001Exception();
            }
        }
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        Set<AttributeClassInformation> informationSet = new HashSet<>();
        for (MyClass myClass : myClasses) {
            if (myClass.getDuplicatedAttribute() != null) {
                informationSet.addAll(myClass.getDuplicatedAttribute());
            }
        }
        if (!informationSet.isEmpty()) {
            throw new UmlRule002Exception(informationSet);
        }
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        Set<UmlClassOrInterface> classOrInterfaces = new HashSet<>();
        for (MyClass myClass : myClasses) {
            Set<UmlClassOrInterface> theResult = myClass.getParents();
            if (theResult != null) {
                classOrInterfaces.addAll(theResult);
            }
        }
        for (MyInterface myInterface : myInterfaces) {
            myInterface.getCycles(myInterface);
        }
        if (!MyInterface.CYCLE_INTERFACES.isEmpty()) {
            for (MyInterface myInterface : MyInterface.CYCLE_INTERFACES) {
                classOrInterfaces.add(new UmlClassOrInterface() {
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
                        return myInterface.getName();
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
            }
        }
        if (!classOrInterfaces.isEmpty()) {
            throw new UmlRule003Exception(classOrInterfaces);
        }
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        Set<UmlClassOrInterface> classOrInterfaces = new HashSet<>();
        for (MyInterface myInterface : myInterfaces) {
            if (myInterface.getParentsSet(myInterface)) {
                classOrInterfaces.add(new UmlClassOrInterface() {
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
                        return myInterface.getName();
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
            }
        }
        if (!classOrInterfaces.isEmpty()) {
            throw new UmlRule004Exception(classOrInterfaces);
        }
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        for (MyInterface myInterface : myInterfaces) {
            if (!myInterface.checkPublicAttribute()) {
                throw new UmlRule005Exception();
            }
        }
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        if (!myParser.check006()) {
            throw new UmlRule006Exception();
        }
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        if (!myParser.getCheck007()) {
            throw new UmlRule007Exception();
        }
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        if (!myParser.getCheck008()) {
            throw new UmlRule008Exception();
        }
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        for (MyStateMachine myStateMachine : myStateMachines) {
            if (!myStateMachine.checkAllStates()) {
                throw new UmlRule009Exception();
            }
        }
    }
}
