package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest2 {

    /**
     * Some tests for the getStageCount() method.
     */
    @Test
    public void testGetStageCount() {
        DefaultFlowDataset<String> d = new DefaultFlowDataset<>();
        assertEquals(1, d.getStageCount());
        d.setFlow(0, "A", "Z", 11.1);
        assertEquals(1, d.getStageCount());
        // a row of all null values is still counted...
        d.setFlow(1, "Z", "P", 5.0);
        assertEquals(2, d.getStageCount());
    }
}
