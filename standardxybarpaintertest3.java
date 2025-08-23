package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 */
@DisplayName("StandardXYBarPainter")
class StandardXYBarPainterTest {

    /**
     * Verifies that StandardXYBarPainter does not support cloning.
     * This is the desired behavior because the class is immutable,
     * so a single instance can be safely shared without needing to be copied.
     */
    @Test
    @DisplayName("should not be cloneable because it is immutable")
    void shouldNotBeCloneable() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();

        // Assert
        assertFalse(painter instanceof Cloneable,
                "Instances are immutable and should not implement Cloneable.");
        assertFalse(painter instanceof PublicCloneable,
                "Instances are immutable and should not implement PublicCloneable.");
    }
}