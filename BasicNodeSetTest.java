package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Unit tests for BasicNodeSet.
 */
public class BasicNodeSetTest extends AbstractJXPathTest {

    private JXPathContext context;
    private BasicNodeSet nodeSet;

    @BeforeEach
    void setUp() {
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Adds pointers for the specified XPath to the node set.
     * 
     * @param xpath the XPath expression
     */
    private void addPointers(String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::add);
        nudgeNodeSet();
    }

    /**
     * Nudges the node set to ensure it's updated.
     */
    private void nudgeNodeSet() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Removes pointers for the specified XPath from the node set.
     * 
     * @param xpath the XPath expression
     */
    private void removePointers(String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::remove);
        nudgeNodeSet();
    }

    /**
     * Verifies that the element names in the list match the expected names.
     * 
     * @param expectedNames the expected names
     * @param elements      the elements to verify
     */
    private void assertElementNames(List<String> expectedNames, List<?> elements) {
        assertEquals(expectedNames.size(), elements.size());
        assertIterableEquals(expectedNames, elements.stream()
                .map(element -> ((Element) element).getTagName())
                .toList());
    }

    /**
     * Verifies that the element values in the list match the expected values.
     * 
     * @param expectedValues the expected values
     * @param elements       the elements to verify
     */
    private void assertElementValues(List<String> expectedValues, List<?> elements) {
        assertEquals(expectedValues.size(), elements.size());
        assertIterableEquals(expectedValues, elements.stream()
                .map(element -> ((Element) element).getFirstChild().getNodeValue())
                .toList());
    }

    @Test
    void testAddingPointers() {
        addPointers("/bean/integers");
        var expectedPointers = List.of("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]", "/bean/integers[4]");
        assertEquals(expectedPointers, nodeSet.getPointers());
        var expectedValues = List.of(1, 2, 3, 4);
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }

    @Test
    void testNodesVsValues() {
        addPointers("/document/vendor/contact");
        var expectedPointers = List.of("/document/vendor[1]/contact[1]", "/document/vendor[1]/contact[2]", "/document/vendor[1]/contact[3]", "/document/vendor[1]/contact[4]");
        assertEquals(expectedPointers, nodeSet.getPointers());
        var expectedValues = List.of("John", "Jack", "Jim", "Jack Black");
        assertEquals(expectedValues, nodeSet.getValues());
        var expectedElementNames = List.of("contact", "contact", "contact", "contact");
        assertElementNames(expectedElementNames, nodeSet.getNodes());
        assertElementValues(expectedValues, nodeSet.getNodes());
    }

    @Test
    void testRemovingPointer() {
        addPointers("/bean/integers");
        removePointers("/bean/integers[4]");
        var expectedPointers = List.of("/bean/integers[1]", "/bean/integers[2]", "/bean/integers[3]");
        assertEquals(expectedPointers, nodeSet.getPointers());
        var expectedValues = List.of(1, 2, 3);
        assertEquals(expectedValues, nodeSet.getValues());
        assertEquals(expectedValues, nodeSet.getNodes());
    }
}