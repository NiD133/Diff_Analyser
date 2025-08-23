package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultPieDatasetTestTest5 implements DatasetChangeListener {

    private DatasetChangeEvent lastEvent;

    /**
     * Records the last event.
     *
     * @param event  the last event.
     */
    @Override
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastEvent = event;
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DefaultPieDataset<String> d1 = new DefaultPieDataset<>();
        d1.setValue("C1", 234.2);
        d1.setValue("C2", null);
        d1.setValue("C3", 345.9);
        d1.setValue("C4", 452.7);
        DefaultPieDataset<String> d2 = TestUtils.serialised(d1);
        assertEquals(d1, d2);
    }
}
