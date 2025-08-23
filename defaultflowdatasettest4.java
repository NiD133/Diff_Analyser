package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest4 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DefaultFlowDataset<String> d1 = new DefaultFlowDataset<>();
        d1.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> d2 = TestUtils.serialised(d1);
        assertEquals(d1, d2);
    }
}
