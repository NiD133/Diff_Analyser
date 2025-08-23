package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest1 {

    /**
     * Some checks for the getValue() method.
     */
    @Test
    public void testGetFlow() {
        DefaultFlowDataset<String> d = new DefaultFlowDataset<>();
        d.setFlow(0, "A", "Z", 1.5);
        assertEquals(1.5, d.getFlow(0, "A", "Z"));
    }
}
