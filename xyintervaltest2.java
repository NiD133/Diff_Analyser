package org.jfree.data.xy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the {@link XYInterval} class, focusing on its immutability contract.
 */
@DisplayName("XYInterval")
class XYIntervalTest {

    @Test
    @DisplayName("Should not be cloneable as it is an immutable class")
    void isNotCloneableBecauseItIsImmutable() {
        // Arrange: Create an instance of the immutable XYInterval class.
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);

        // Act & Assert: Verify that the class does not implement the Cloneable interface.
        // This is a common practice for immutable objects to prevent mutable copies.
        assertFalse(interval instanceof Cloneable,
                "XYInterval is designed to be immutable and should not implement Cloneable.");
    }
}