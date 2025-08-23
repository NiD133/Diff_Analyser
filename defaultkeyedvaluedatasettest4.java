package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultKeyedValueDatasetTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DefaultKeyedValueDataset d1 = new DefaultKeyedValueDataset("Test", 25.3);
        DefaultKeyedValueDataset d2 = TestUtils.serialised(d1);
        assertEquals(d1, d2);
    }
}
