package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultKeyedValueDatasetTestTest3 {

    /**
     * Confirm that the clone is independent of the original.
     * @throws java.lang.CloneNotSupportedException
     */
    @Test
    public void testCloneIndependence() throws CloneNotSupportedException {
        DefaultKeyedValueDataset d1 = new DefaultKeyedValueDataset("Key", 10.0);
        DefaultKeyedValueDataset d2 = CloneUtils.clone(d1);
        assertEquals(d1, d2);
        d2.updateValue(99.9);
        assertNotEquals(d1, d2);
        d2.updateValue(10.0);
        assertEquals(d1, d2);
    }
}
