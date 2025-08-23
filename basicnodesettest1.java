package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

public class BasicNodeSetTestTest1 extends AbstractJXPathTest {

    /**
     * JXPathContext
     */
    protected JXPathContext context;

    /**
     * BasicNodeSet
     */
    protected BasicNodeSet nodeSet;

    /**
     * Add the pointers for the specified path to {@code nodeSet}.
     *
     * @param xpath
     */
    protected void addPointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext(); ) {
            nodeSet.add(iter.next());
        }
        nudge();
    }

    /**
     * Do assertions on DOM element names.
     *
     * @param names    List of expected names
     * @param elements List of DOM elements
     */
    protected void assertElementNames(final List names, final List elements) {
        assertEquals(names.size(), elements.size());
        final Iterator nameIter = names.iterator();
        final Iterator elementIter = elements.iterator();
        while (elementIter.hasNext()) {
            assertEquals(nameIter.next(), ((Element) elementIter.next()).getTagName());
        }
    }

    /**
     * Do assertions on DOM element values.
     *
     * @param values   List of expected values
     * @param elements List of DOM elements
     */
    protected void assertElementValues(final List values, final List elements) {
        assertEquals(values.size(), elements.size());
        final Iterator valueIter = values.iterator();
        final Iterator elementIter = elements.iterator();
        while (elementIter.hasNext()) {
            assertEquals(valueIter.next(), ((Element) elementIter.next()).getFirstChild().getNodeValue());
        }
    }

    /**
     * "Nudge" the nodeSet.
     */
    protected void nudge() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Remove the pointers for the specified path from {@code nodeSet}.
     *
     * @param xpath
     */
    protected void removePointers(final String xpath) {
        for (final Iterator<Pointer> iter = context.iteratePointers(xpath); iter.hasNext(); ) {
            nodeSet.remove(iter.next());
        }
        nudge();
    }

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Test adding pointers.
     */
    @Test
    void testAdd() {
        addPointers("/bean/integers");
        assertEquals(list("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]", "/bean/integers[4]").toString(), nodeSet.getPointers().toString());
        assertEquals(list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), nodeSet.getValues());
        assertEquals(list(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), nodeSet.getNodes());
    }
}
