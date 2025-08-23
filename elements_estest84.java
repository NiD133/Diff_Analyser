package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * This test verifies the argument validation of the {@link Elements#eachAttr(String)} method.
 */
public class ElementsEachAttrTest {

    /**
     * Tests that calling eachAttr with a null attribute key throws an IllegalArgumentException.
     * The method should not accept null arguments, as an attribute key cannot be null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void eachAttrShouldThrowExceptionForNullAttributeKey() {
        // Arrange: Create a non-empty Elements collection. The specific content
        // of the elements is irrelevant to this test case.
        Elements elements = new Elements(new Element("p"));

        // Act: Call the method under test with an invalid null argument.
        elements.eachAttr(null);

        // Assert: The test passes if an IllegalArgumentException is thrown,
        // as specified by the 'expected' parameter in the @Test annotation.
    }
}