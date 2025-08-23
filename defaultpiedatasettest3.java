package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultPieDatasetTestTest3 implements DatasetChangeListener {

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
     * Some checks for the getIndex() method.
     */
    @Test
    public void testGetIndex() {
        DefaultPieDataset<String> d = new DefaultPieDataset<>();
        d.setValue("A", 1.0);
        d.setValue("B", 2.0);
        assertEquals(0, d.getIndex("A"));
        assertEquals(1, d.getIndex("B"));
        assertEquals(-1, d.getIndex("XX"));
        boolean pass = false;
        try {
            d.getIndex(null);
        } catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }
}
