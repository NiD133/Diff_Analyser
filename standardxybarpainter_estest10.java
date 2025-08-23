package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 * This focuses on the behavior of the equals() method.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that two distinct instances of StandardXYBarPainter are considered equal.
     * This is the expected behavior because the class is stateless.
     */
    @Test
    public void equals_withTwoDefaultInstances_shouldReturnTrue() {
        // Arrange: Create two separate instances of the painter.
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Act & Assert: The two instances should be equal to each other.
        // The assertEquals method internally calls the .equals() method and provides
        // a more informative failure message than assertTrue(painter1.equals(painter2)).
        assertEquals(painter1, painter2);
    }
}