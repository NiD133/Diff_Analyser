package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest6 {

    /**
     * Check that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        DefaultFlowDataset<String> d = new DefaultFlowDataset<>();
        assertTrue(d instanceof PublicCloneable);
    }
}
