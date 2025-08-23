package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest3 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultFlowDataset<String> d1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> d2 = new DefaultFlowDataset<>();
        assertEquals(d1, d2);
        d1.setFlow(0, "A", "Z", 1.0);
        assertNotEquals(d1, d2);
        d2.setFlow(0, "A", "Z", 1.0);
        assertEquals(d1, d2);
    }
}
