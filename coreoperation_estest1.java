package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests the string representation of a {@link CoreOperationUnion}.
 * This test focuses on the edge case where the operation is created with no operands.
 */
public class CoreOperationUnionToStringTest {

    @Test
    public void toString_withNoOperands_returnsEmptyString() {
        // Arrange: Create a CoreOperationUnion with an empty array of expressions (operands).
        Expression[] noOperands = new Expression[0];
        CoreOperationUnion unionOperation = new CoreOperationUnion(noOperands);

        // Act: Get the string representation of the operation.
        String result = unionOperation.toString();

        // Assert: The result should be an empty string.
        assertEquals("The string representation of a union with no operands should be empty", "", result);
    }
}