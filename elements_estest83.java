package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the behavior of the Elements class.
 * Note: The original class name and scaffolding were preserved for context,
 * but in a typical project, they might be simplified to 'public class ElementsTest'.
 */
public class Elements_ESTestTest83 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the hasAttr() method throws an IllegalArgumentException
     * when passed a null attribute name.
     */
    @Test
    public void hasAttrShouldThrowIllegalArgumentExceptionForNullAttributeName() {
        // Arrange: Create an empty Elements object. The content is irrelevant for this test,
        // as the null-check on the attribute name happens before the elements are iterated.
        Elements elements = new Elements();

        // Act & Assert
        try {
            elements.hasAttr(null);
            fail("Expected an IllegalArgumentException to be thrown for a null attribute name.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message from the validation utility.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}