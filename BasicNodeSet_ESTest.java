import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the BasicNodeSet class.
 */
public class BasicNodeSetTest {

    @Test(timeout = 4000)
    public void testAddingNullPointerShouldThrowNullPointerExceptionWhenGettingValues() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        basicNodeSet.add((Pointer) null);
        assertThrows(NullPointerException.class, basicNodeSet::getValues);
    }

    @Test(timeout = 4000)
    public void testAddingNullPointerShouldThrowNullPointerExceptionWhenGettingNodes() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        basicNodeSet.add((Pointer) null);
        assertThrows(NullPointerException.class, basicNodeSet::getNodes);
    }

    @Test(timeout = 4000)
    public void testAddingPointerWithNullQNameShouldThrowRuntimeExceptionWhenGettingValues() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        VariablePointer variablePointer = new VariablePointer((QName) null);
        basicNodeSet.add(variablePointer);
        assertThrows(RuntimeException.class, basicNodeSet::getValues);
    }

    @Test(timeout = 4000)
    public void testAddingPointerWithNullQNameShouldThrowRuntimeExceptionWhenGettingNodes() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        VariablePointer variablePointer = new VariablePointer((QName) null);
        basicNodeSet.add(variablePointer);
        assertThrows(RuntimeException.class, basicNodeSet::getNodes);
    }

    @Test(timeout = 4000)
    public void testAddingPointerWithEmptyQNameShouldThrowIllegalArgumentExceptionWhenGettingValues() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        BasicVariables basicVariables = new BasicVariables();
        QName qName = new QName("");
        VariablePointer variablePointer = new VariablePointer(basicVariables, qName);
        basicNodeSet.add(variablePointer);
        assertThrows(IllegalArgumentException.class, basicNodeSet::getValues);
    }

    @Test(timeout = 4000)
    public void testAddingPointerWithEmptyQNameShouldThrowIllegalArgumentExceptionWhenGettingNodes() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        BasicVariables basicVariables = new BasicVariables();
        QName qName = new QName("");
        VariablePointer variablePointer = new VariablePointer(basicVariables, qName);
        basicNodeSet.add(variablePointer);
        assertThrows(IllegalArgumentException.class, basicNodeSet::getNodes);
    }

    @Test(timeout = 4000)
    public void testAddingNullNodeSetShouldThrowNullPointerException() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        assertThrows(NullPointerException.class, () -> basicNodeSet.add((NodeSet) null));
    }

    @Test(timeout = 4000)
    public void testRemovingPointerFromEmptySetShouldNotThrowException() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        basicNodeSet.remove((Pointer) null);
    }

    @Test(timeout = 4000)
    public void testGettingValuesFromEmptySetShouldReturnEmptyList() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        assertTrue(basicNodeSet.getValues().isEmpty());
    }

    @Test(timeout = 4000)
    public void testGettingPointersShouldReturnSameList() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        List<Pointer> pointers1 = basicNodeSet.getPointers();
        List<Pointer> pointers2 = basicNodeSet.getPointers();
        assertSame(pointers1, pointers2);
    }

    @Test(timeout = 4000)
    public void testGettingNodesShouldReturnSameList() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        List nodes1 = basicNodeSet.getNodes();
        List nodes2 = basicNodeSet.getNodes();
        assertSame(nodes1, nodes2);
    }

    @Test(timeout = 4000)
    public void testAddingSelfToSetShouldNotThrowException() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        basicNodeSet.add((NodeSet) basicNodeSet);
    }

    @Test(timeout = 4000)
    public void testToStringShouldReturnEmptyListString() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        assertEquals("[]", basicNodeSet.toString());
    }

    @Test(timeout = 4000)
    public void testAddingNullPointerAndSelfToSetShouldNotThrowException() {
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        basicNodeSet.add((Pointer) null);
        basicNodeSet.add((NodeSet) basicNodeSet);
    }
}