package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DefaultPieDataset with improved readability and maintainability.
 */
public class DefaultPieDatasetTest {

    private static final String KEY_A = "A";
    private static final String KEY_B = "B";

    private static class RecordingChangeListener implements DatasetChangeListener {
        private int eventCount;
        private DatasetChangeEvent lastEvent;

        @Override
        public void datasetChanged(DatasetChangeEvent event) {
            eventCount++;
            lastEvent = event;
        }

        int getEventCount() {
            return eventCount;
        }

        DatasetChangeEvent getLastEvent() {
            return lastEvent;
        }
    }

    @Test
    @DisplayName("clear(): no event when already empty; event when clearing non-empty")
    public void testClear() {
        // given
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        RecordingChangeListener listener = new RecordingChangeListener();
        dataset.addChangeListener(listener);

        // when: clear an already empty dataset
        dataset.clear();

        // then: no event fired
        assertEquals(0, listener.getEventCount(), "No event should fire for clearing an empty dataset");

        // given: add one item
        dataset.setValue(KEY_A, 1.0);
        assertEquals(1, dataset.getItemCount());

        // when: clear non-empty dataset
        dataset.clear();

        // then: an event is fired and dataset is empty
        assertEquals(1, listener.getEventCount(), "One event should fire when clearing non-empty dataset");
        assertNotNull(listener.getLastEvent(), "Last event should be recorded");
        assertEquals(0, dataset.getItemCount());
    }

    @Test
    @DisplayName("getKey(index): returns expected keys and throws for out-of-range indices")
    public void testGetKey() {
        // given
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue(KEY_A, 1.0);
        dataset.setValue(KEY_B, 2.0);

        // then
        assertEquals(KEY_A, dataset.getKey(0));
        assertEquals(KEY_B, dataset.getKey(1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getKey(2));
    }

    @Test
    @DisplayName("getIndex(key): returns index, -1 for unknown key, and throws for null key")
    public void testGetIndex() {
        // given
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue(KEY_A, 1.0);
        dataset.setValue(KEY_B, 2.0);

        // then
        assertEquals(0, dataset.getIndex(KEY_A));
        assertEquals(1, dataset.getIndex(KEY_B));
        assertEquals(-1, dataset.getIndex("XX"));
        assertThrows(IllegalArgumentException.class, () -> dataset.getIndex(null));
    }

    @Test
    @DisplayName("clone(): produces equal but distinct copy")
    public void testCloning() throws CloneNotSupportedException {
        // given
        DefaultPieDataset<String> d1 = new DefaultPieDataset<>();
        d1.setValue("V1", 1);
        d1.setValue("V2", null);
        d1.setValue("V3", 3);

        // when
        DefaultPieDataset<String> d2 = CloneUtils.clone(d1);

        // then
        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);
    }

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    public void testSerialization() {
        // given
        DefaultPieDataset<String> d1 = new DefaultPieDataset<>();
        d1.setValue("C1", 234.2);
        d1.setValue("C2", null);
        d1.setValue("C3", 345.9);
        d1.setValue("C4", 452.7);

        // when
        DefaultPieDataset<String> d2 = TestUtils.serialised(d1);

        // then
        assertEquals(d1, d2);
    }

    @Test
    @DisplayName("getValue(index): bounds checks per issue #212")
    public void testBug212() {
        // given
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // then: out-of-bounds on empty dataset
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(0));

        // when: add first item
        dataset.setValue(KEY_A, 1.0);

        // then: valid for 0, out-of-bounds for 1
        assertEquals(1.0, dataset.getValue(0));
        assertThrows(IndexOutOfBoundsException.class, () -> dataset.getValue(1));
    }
}