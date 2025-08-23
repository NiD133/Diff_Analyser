package org.jfree.chart.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the {@link GridArrangement} class, focusing on its contract and behavior.
 */
class GridArrangementTest {

    /**
     * The GridArrangement class is designed to be immutable. This test verifies
     * that it does not implement the Cloneable interface, as cloning is
     * unnecessary and discouraged for immutable objects.
     */
    @Test
    @DisplayName("GridArrangement should not be cloneable")
    void isNotCloneable() {
        // Arrange: Create an instance of the class under test.
        GridArrangement arrangement = new GridArrangement(1, 2);

        // Assert: Verify that the instance is not cloneable.
        assertFalse(arrangement instanceof Cloneable,
                "GridArrangement is immutable and should not implement Cloneable.");
    }
}