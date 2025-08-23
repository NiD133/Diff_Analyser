package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CoreOperation} class, focusing on its string representation.
 */
public class CoreOperationTest {

    /**
     * Tests the toString() method for a nested CoreOperation.
     *
     * <p>This test verifies that when a symmetric operation (like '=') contains
     * another operation of the same precedence, unnecessary parentheses are not added
     * to the string output.</p>
     */
    @Test
    public void toStringForNestedSymmetricOperationShouldGenerateCorrectString() {
        // Arrange: Create a nested operation structure.
        // The NameAttributeTest class represents the '=' operation, which is a
        // symmetric CoreOperation.
        // The inner operation will represent "null = null".
        CoreOperation innerOperation = new NameAttributeTest(null, null);

        // The outer operation nests the inner one, representing "(null = null) = (null = null)".
        CoreOperation outerOperation = new NameAttributeTest(innerOperation, innerOperation);

        // Act: Generate the string representation of the entire nested operation.
        String stringRepresentation = outerOperation.toString();

        // Assert: Verify the string is formatted as expected. Because the '=' operation
        // is symmetric and has the same precedence at both levels, no parentheses
        // should be added.
        assertEquals("null = null = null = null", stringRepresentation);
    }
}