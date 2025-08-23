package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BasicNodeSet} class, focusing on modification operations like add/remove.
 */
public class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Tests that removing a pointer correctly updates the node set's internal state.
     */
    @Test
    void removingPointerUpdatesTheNodeSet() {
        // Arrange: Populate the node set with pointers to all integers in the bean.
        addPointers("/bean/integers");

        // Act: Remove the pointer to the fourth integer.
        removePointers("/bean/integers[4]");

        // Assert: The node set should now only contain the first three integers and their corresponding pointers.

        // 1. Verify that the list of pointers is correct.
        List<String> expectedPointerPaths = list(
            "/bean/integers[1]",
            "/bean/integers[2]",
            "/bean/integers[3]"
        );
        List<String> actualPointerPaths = nodeSet.getPointers().stream()
                .map(Pointer::asPath)
                .collect(Collectors.toList());
        assertEquals(expectedPointerPaths, actualPointerPaths, "Pointers should be updated after removal");

        // 2. Verify that the lists of values and nodes are correct.
        List<Integer> expectedValues = list(1, 2, 3);
        assertEquals(expectedValues, nodeSet.getValues(), "Values should be updated after removal");
        assertEquals(expectedValues, nodeSet.getNodes(), "Nodes should be updated after removal");
    }

    /**
     * Adds all pointers matching the given XPath to the node set.
     *
     * @param xpath The XPath expression to find pointers for.
     */
    private void addPointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext(); ) {
            nodeSet.add(iter.next());
        }
        forceCacheUpdate();
    }

    /**
     * Removes all pointers matching the given XPath from the node set.
     *
     * @param xpath The XPath expression to find pointers for.
     */
    private void removePointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext(); ) {
            nodeSet.remove(iter.next());
        }
        forceCacheUpdate();
    }

    /**
     * Forces the BasicNodeSet to re-compute its internal cached lists.
     * <p>
     * The BasicNodeSet lazily computes its lists of pointers, values, and nodes.
     * This method forces an immediate update, which is necessary for making assertions
     * after the set has been modified via {@code add()} or {@code remove()}.
     */
    private void forceCacheUpdate() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }
}