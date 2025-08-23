package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultPieDatasetTestTest2 implements DatasetChangeListener {

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
     * Some checks for the getKey(int) method.
     */
    @Test
    public void testGetKey() {
        DefaultPieDataset<String> d = new DefaultPieDataset<>();
        d.setValue("A", 1.0);
        d.setValue("B", 2.0);
        assertEquals("A", d.getKey(0));
        assertEquals("B", d.getKey(1));
        boolean pass = false;
        try {
            d.getKey(-1);
        } catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
        pass = false;
        try {
            d.getKey(2);
        } catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }
}
