package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultKeyedValueDatasetTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultKeyedValueDataset d1 = new DefaultKeyedValueDataset("Test", 45.5);
        DefaultKeyedValueDataset d2 = new DefaultKeyedValueDataset("Test", 45.5);
        assertEquals(d1, d2);
        assertEquals(d2, d1);
        d1 = new DefaultKeyedValueDataset("Test 1", 45.5);
        d2 = new DefaultKeyedValueDataset("Test 2", 45.5);
        assertNotEquals(d1, d2);
        d1 = new DefaultKeyedValueDataset("Test", 45.5);
        d2 = new DefaultKeyedValueDataset("Test", 45.6);
        assertNotEquals(d1, d2);
    }
}
