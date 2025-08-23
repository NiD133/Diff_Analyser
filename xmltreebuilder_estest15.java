package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains an improved version of a test for the XmlTreeBuilder.
 * The original test was auto-generated and lacked clarity.
 */
public class XmlTreeBuilder_ESTestTest15 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that processStartTag correctly creates a new element
     * and pushes it onto the builder's stack.
     */
    @Test
    public void processStartTag_whenInvoked_pushesNewElementOntoStack() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // To process tags, the builder needs to be initialized. The original test
        // used a complex setup; a direct initialization is clearer and achieves
        // the same prerequisite state.
        Parser parser = new Parser(builder);
        builder.initialiseParse(new StringReader(""), "http://example.com", parser);

        // The original test also established a parsing context with an element on the stack.
        // We simulate this by pushing a dummy context element. The specific nature of this
        // element is not relevant to the behavior under test.
        builder.push(new Element("context"));

        // The original test used a URL as a tag name. We retain this to ensure
        // the test continues to cover this specific edge case.
        String newTagName = "http://www.w3.org/2000/svg";

        // Act
        // The processStartTag method should create a new element and add it to the stack.
        boolean wasProcessed = builder.processStartTag(newTagName);

        // Assert
        assertTrue("processStartTag should return true upon successful processing.", wasProcessed);

        // Verify that the new element was indeed pushed to the stack.
        Element pushedElement = builder.pop();
        assertEquals("The popped element should have the specified tag name.", newTagName, pushedElement.tagName());

        // This assertion, also from the original test, confirms the builder is
        // correctly configured for XML parsing.
        assertEquals("The default namespace should be the standard XML namespace.",
            "http://www.w3.org/XML/1998/namespace", builder.defaultNamespace());
    }
}