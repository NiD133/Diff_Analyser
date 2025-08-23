package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Tests for the BasicNodeSet focusing on its handling of different data models,
 * like XML documents.
 *
 * Note: The original class name 'BasicNodeSetTestTest2' was renamed to 'BasicNodeSetTest'
 * to remove redundancy.
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
     * Populates the nodeSet with pointers matching the given XPath expression.
     *
     * @param xpath the XPath expression to find pointers for
     */
    private void addPointers(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::add);
        forceInternalCacheEvaluation();
    }

    /**
     * Removes pointers matching the given XPath expression from the nodeSet.
     *
     * @param xpath the XPath expression to find pointers for
     */
    private void removePointers(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(nodeSet::remove);
        forceInternalCacheEvaluation();
    }



    /**
     * Forces the evaluation of the nodeSet's internal caches.
     * <p>
     * The BasicNodeSet implementation lazily initializes its internal lists of
     * pointers, nodes, and values. Calling the getter methods triggers this
     * initialization, which is necessary for the test assertions to work correctly.
     */
    private void forceInternalCacheEvaluation() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    @Test
    @DisplayName("For XML models, getNodes() should return DOM elements while getValues() returns their text content")
    void nodesAndValuesShouldDifferForXmlModel() {
        // ARRANGE: Define expected data and populate the node set.
        final String xpath = "/document/vendor/contact";
        final List<String> expectedPointerPaths = list(
                "/document/vendor[1]/contact[1]",
                "/document/vendor[1]/contact[2]",
                "/document/vendor[1]/contact[3]",
                "/document/vendor[1]/contact[4]");
        final List<String> expectedValues = list("John", "Jack", "Jim", "Jack Black");

        // ACT: Add pointers from the specified XPath to the node set.
        addPointers(xpath);

        // ASSERT: Verify the contents of the node set.

        // 1. Verify the pointers are correctly identified and stored.
        final List<String> actualPointerPaths = nodeSet.getPointers()
                .stream()
                .map(Pointer::asPath)
                .collect(Collectors.toList());
        assertEquals(expectedPointerPaths, actualPointerPaths, "Pointers should match the expected paths");

        // 2. Verify that getValues() returns the text content of the elements.
        assertEquals(expectedValues, nodeSet.getValues(), "Values should be the text content of the nodes");

        // 3. Verify that getNodes() returns the actual DOM Element objects.
        @SuppressWarnings("unchecked")
        final List<Element> actualNodes = (List<Element>) nodeSet.getNodes();

        // 3a. Check that the elements have the correct tag name.
        final List<String> expectedNodeTags = list("contact", "contact", "contact", "contact");
        final List<String> actualNodeTags = actualNodes.stream()
                .map(Element::getTagName)
                .collect(Collectors.toList());
        assertEquals(expectedNodeTags, actualNodeTags, "All nodes should be 'contact' elements");

        // 3b. Check that the text content of the DOM elements matches the expected values.
        final List<String> actualNodeTextContent = actualNodes.stream()
                .map(element -> element.getFirstChild().getNodeValue())
                .collect(Collectors.toList());
        assertEquals(expectedValues, actualNodeTextContent, "Text content of node elements should match the values");
    }
}