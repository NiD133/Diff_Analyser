package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridArrangementTestTest5 {

    private static final double EPSILON = 0.000000001;

    private BlockContainer createTestContainer1() {
        Block b1 = new EmptyBlock(10, 11);
        Block b2 = new EmptyBlock(20, 22);
        Block b3 = new EmptyBlock(30, 33);
        BlockContainer result = new BlockContainer(new GridArrangement(1, 3));
        result.add(b1);
        result.add(b2);
        result.add(b3);
        return result;
    }

    /**
     * Test arrangement with a fixed width and no height constraint.
     */
    @Test
    public void testFN() {
        BlockContainer c = createTestContainer1();
        RectangleConstraint constraint = new RectangleConstraint(100.0, null, LengthConstraintType.FIXED, 0.0, null, LengthConstraintType.NONE);
        Size2D s = c.arrange(null, constraint);
        assertEquals(100.0, s.width, EPSILON);
        assertEquals(33.0, s.height, EPSILON);
    }
}
