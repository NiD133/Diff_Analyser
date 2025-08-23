package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that the hashCode() method adheres to its contract.
     *
     * The Java contract for hashCode() states that if two objects are equal
     * (according to their equals() method), they must have the same hash code.
     * For the StandardXYBarPainter class, all instances are considered equal.
     * This test confirms that two different instances produce the same hash code.
     */
    @Test
    public void testHashCodeContract() {
        // Arrange: Create two separate instances of the painter.
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Assert: Verify that the two objects are considered equal, and then
        // confirm that their hash codes are also equal, fulfilling the contract.
        assertEquals("All instances of StandardXYBarPainter should be equal.", painter1, painter2);
        assertEquals("Equal objects must have equal hash codes.", painter1.hashCode(), painter2.hashCode());
    }
}