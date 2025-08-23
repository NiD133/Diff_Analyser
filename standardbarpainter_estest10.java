package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that two separate instances of StandardBarPainter are considered equal.
     * This is the expected behavior as the class is stateless.
     */
    @Test
    public void twoDefaultInstancesShouldBeEqual() {
        // Arrange: Create two separate instances of the painter.
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Act & Assert: The two instances should be equal to each other.
        // assertEquals internally calls the .equals() method and provides a
        // more informative failure message than assertTrue.
        assertEquals(painter1, painter2);
    }
}