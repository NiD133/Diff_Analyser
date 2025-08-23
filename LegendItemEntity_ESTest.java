package org.jfree.chart.entity;

import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.junit.Test;

/**
 * Readable unit tests for LegendItemEntity.
 * 
 * Focus:
 * - Constructor preconditions
 * - Getters/setters for series key and dataset
 * - Equality semantics (self, clone, different dataset/series key, different type)
 * - Default state (dataset and series key)
 * - toString format
 */
public class LegendItemEntityTest {

    // Utility to create a minimal, valid area for the entity
    private Rectangle2D newArea() {
        return new Rectangle2D.Double(0, 0, 10, 10);
    }

    @Test
    public void constructorRejectsNullArea() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> new LegendItemEntity<String>(null)
        );
        assertEquals("Null 'area' argument.", ex.getMessage());
    }

    @Test
    public void defaultDataset_isNull() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        assertNull(entity.getDataset());
    }

    @Test
    public void defaultSeriesKey_isNull() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        assertNull(entity.getSeriesKey());
    }

    @Test
    public void getSetDataset_returnsSameInstance() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        Dataset dataset = new XIntervalSeriesCollection();
        entity.setDataset(dataset);

        assertSame(dataset, entity.getDataset());
    }

    @Test
    public void getSetSeriesKey_returnsSameValue() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        String key = "Series-A";
        entity.setSeriesKey(key);

        assertEquals(key, entity.getSeriesKey());
    }

    @Test
    public void equals_sameInstance_true() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        assertTrue(entity.equals(entity));
    }

    @Test
    public void equals_withDifferentType_false() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        assertFalse(entity.equals(new Object()));
    }

    @Test
    public void equals_cloneInitially_true() throws Exception {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        Object clone = entity.clone();

        assertTrue(entity.equals(clone));
    }

    @Test
    public void equals_differsAfterDatasetChange_false() throws Exception {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        Object clone = entity.clone();

        // Changing a significant field should break equality with the clone
        entity.setDataset(new XIntervalSeriesCollection());

        assertFalse(entity.equals(clone));
    }

    @Test
    public void equals_withDifferentSeriesKey_false() {
        Rectangle2D area = newArea();
        LegendItemEntity<String> a = new LegendItemEntity<>(area);
        LegendItemEntity<String> b = new LegendItemEntity<>(area);

        a.setSeriesKey("A");
        // b has null series key by default

        assertFalse(a.equals(b));
    }

    @Test
    public void toString_includesKeyAndDataset() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(newArea());
        assertEquals("LegendItemEntity: seriesKey=null, dataset=null", entity.toString());
    }
}