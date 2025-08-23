package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFlowDatasetTestTest5 {

    /**
     * Confirm that cloning works.
     * @throws java.lang.CloneNotSupportedException
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultFlowDataset<String> d1 = new DefaultFlowDataset<>();
        d1.setFlow(0, "A", "Z", 1.0);
        DefaultFlowDataset<String> d2 = (DefaultFlowDataset<String>) d1.clone();
        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);
        // check that the clone doesn't share the same underlying arrays.
        d1.setFlow(0, "A", "Y", 8.0);
        assertNotEquals(d1, d2);
        d2.setFlow(0, "A", "Y", 8.0);
        assertEquals(d1, d2);
    }
}
