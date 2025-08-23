package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging a grid configured with zero rows results in a calculated
     * size with a height of NaN (Not a Number). This scenario can lead to division-by-zero
     * errors in layout calculations, and NaN is the expected outcome for such an
     * impossible geometric arrangement.
     */
    @Test
    public void arrangeWithZeroRowsShouldReturnSizeWithNaNHeight() {
        // Arrange: Create a grid arrangement with an invalid configuration of 0 rows.
        // The number of columns is negative, further defining this as an edge-case test.
        final int ZERO_ROWS = 0;
        final int NEGATIVE_COLUMNS = -2116;
        GridArrangement arrangement = new GridArrangement(ZERO_ROWS, NEGATIVE_COLUMNS);

        // An empty container to be arranged.
        BlockContainer emptyContainer = new BlockContainer(arrangement);

        // A constraint is needed for the arrangement logic. The specific range values
        // are not critical to this test's outcome.
        RectangleConstraint constraint = new RectangleConstraint(new Range(0.0, 100.0), new Range(0.0, 100.0));

        // Act: Attempt to arrange the container. The Graphics2D context is not used
        // in this particular arrangement path, so it can be null.
        // We are testing the protected 'arrangeFR' method directly, which is called
        // by the public 'arrange' method under certain constraint conditions.
        Size2D arrangedSize = arrangement.arrangeFR(emptyContainer, (Graphics2D) null, constraint);

        // Assert: The resulting height should be NaN, as calculating height for a
        // grid with zero rows is an undefined operation. The width is expected to be 0.0
        // based on the constraint's initial width value.
        assertEquals("Width", 0.0, arrangedSize.getWidth(), 0.01);
        assertTrue("Height should be NaN for a zero-row grid.", Double.isNaN(arrangedSize.getHeight()));
    }
}