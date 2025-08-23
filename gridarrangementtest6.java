package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridArrangementTestTest6 {

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
     * Test arrangement with a fixed height and no width constraint.
     */
    @Test
    public void testNF() {
        BlockContainer c = createTestContainer1();
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedHeight(100.0);
        Size2D s = c.arrange(null, constraint);
        assertEquals(90.0, s.width, EPSILON);
        assertEquals(100.0, s.height, EPSILON);
    }
}
