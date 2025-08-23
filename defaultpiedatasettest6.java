package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultPieDatasetTestTest6 implements DatasetChangeListener {

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
     * A test for bug report https://github.com/jfree/jfreechart/issues/212
     */
    @Test
    public void testBug212() {
        DefaultPieDataset<String> d = new DefaultPieDataset<>();
        assertThrows(IndexOutOfBoundsException.class, () -> d.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> d.getValue(0));
        d.setValue("A", 1.0);
        assertEquals(1.0, d.getValue(0));
        assertThrows(IndexOutOfBoundsException.class, () -> d.getValue(1));
    }
}
