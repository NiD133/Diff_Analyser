package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridArrangementTestTest1 {

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
     * Confirm that the equals() method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        GridArrangement f1 = new GridArrangement(11, 22);
        GridArrangement f2 = new GridArrangement(11, 22);
        assertEquals(f1, f2);
        assertEquals(f2, f1);
        f1 = new GridArrangement(33, 22);
        assertNotEquals(f1, f2);
        f2 = new GridArrangement(33, 22);
        assertEquals(f1, f2);
        f1 = new GridArrangement(33, 44);
        assertNotEquals(f1, f2);
        f2 = new GridArrangement(33, 44);
        assertEquals(f1, f2);
    }
}
