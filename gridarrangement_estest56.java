package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeNF method throws a NullPointerException
     * when the constraint argument is null. The 'NF' in the method name
     * suggests 'No constraint' for width and 'Fixed' for height, but the
     * implementation still requires a non-null constraint object to
     * access height information.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeNFShouldThrowNullPointerExceptionWhenConstraintIsNull() {
        // Arrange
        GridArrangement arrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer(arrangement);
        Graphics2D g2 = null; // This argument is not used before the exception is thrown.

        // Act: Call the method with a null constraint, which is expected to cause the exception.
        // Assert: The @Test(expected) annotation handles the exception verification.
        arrangement.arrangeNF(container, g2, null);
    }
}