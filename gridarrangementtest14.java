package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridArrangementTestTest14 {

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
     * The arrangement should be able to handle null blocks in the layout.
     */
    @Test
    public void testNullBlock_NN() {
        BlockContainer c = new BlockContainer(new GridArrangement(1, 1));
        c.add(null);
        Size2D s = c.arrange(null, RectangleConstraint.NONE);
        assertEquals(0.0, s.getWidth(), EPSILON);
        assertEquals(0.0, s.getHeight(), EPSILON);
    }
}
