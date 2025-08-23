package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link Elements} class.
 * Note: The original test class name 'Elements_ESTestTest163' and its extension
 * 'Elements_ESTest_scaffolding' are preserved as changing them is beyond the scope
 * of refactoring a single test case.
 */
public class Elements_ESTestTest163 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling the traverse() method with a null NodeVisitor
     * throws an IllegalArgumentException, as the visitor argument is mandatory.
     */
    @Test
    public void traverseWithNullVisitorThrowsIllegalArgumentException() {
        // Arrange: Create an empty Elements collection. The behavior should be
        // consistent regardless of whether the collection contains elements or not.
        Elements elements = new Elements();

        // Act & Assert: Attempt to traverse with a null visitor and verify the exception.
        try {
            elements.traverse(null);
            fail("Expected an IllegalArgumentException to be thrown when the visitor is null.");
        } catch (IllegalArgumentException e) {
            // The method should validate its input and reject nulls.
            // The "Object must not be null" message comes from the Validate helper class.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}