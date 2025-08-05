/*
 * ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * A test suite for the CategoryItemEntity class.
 *
 */
package org.jfree.chart.entity;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Before;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Shape;

import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityTest {

    private static final Shape AREA = new Rectangle(10, 20, 30, 40);
    private static final String TOOLTIP = "Test Tooltip";
    private static final String URL = "http://www.jfree.org/jfreechart/";
    private static final String ROW_KEY = "Row 1";
    private static final String COLUMN_KEY = "Column 1";

    private DefaultCategoryDataset<String, String> dataset;
    private CategoryItemEntity<String, String> entity;

    @Before
    public void setUp() {
        this.dataset = new DefaultCategoryDataset<>();
        this.dataset.addValue(1.0, ROW_KEY, COLUMN_KEY);
        this.entity = new CategoryItemEntity<>(AREA, TOOLTIP, URL, this.dataset, ROW_KEY, COLUMN_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullDataset_throwsIllegalArgumentException() {
        new CategoryItemEntity<>(AREA, TOOLTIP, URL, null, ROW_KEY, COLUMN_KEY);
    }

    @Test
    public void getDataset_returnsCorrectDataset() {
        assertSame("The dataset should be the one provided in the constructor.", this.dataset, entity.getDataset());
    }

    @Test
    public void setDataset_updatesDataset() {
        DefaultCategoryDataset<String, String> newDataset = new DefaultCategoryDataset<>();
        entity.setDataset(newDataset);
        assertSame("The dataset should be updated to the new instance.", newDataset, entity.getDataset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDataset_withNullArgument_throwsIllegalArgumentException() {
        entity.setDataset(null);
    }

    @Test
    public void getRowKey_returnsCorrectKey() {
        assertEquals("The row key should be the one provided in the constructor.", ROW_KEY, entity.getRowKey());
    }

    @Test
    public void setRowKey_updatesKey() {
        String newRowKey = "New Row";
        entity.setRowKey(newRowKey);
        assertEquals("The row key should be updated to the new value.", newRowKey, entity.getRowKey());
    }

    @Test
    public void getColumnKey_returnsCorrectKey() {
        assertEquals("The column key should be the one provided in the constructor.", COLUMN_KEY, entity.getColumnKey());
    }

    @Test
    public void setColumnKey_updatesKey() {
        String newColumnKey = "New Column";
        entity.setColumnKey(newColumnKey);
        assertEquals("The column key should be updated to the new value.", newColumnKey, entity.getColumnKey());
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        assertTrue("An entity must be equal to itself.", entity.equals(entity));
    }

    @Test
    public void equals_identicalEntities_returnsTrue() {
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, this.dataset, ROW_KEY, COLUMN_KEY);
        assertTrue("Entities with identical properties should be equal.", entity.equals(entity2));
        assertEquals("Hashcodes of equal entities should be the same.", entity.hashCode(), entity2.hashCode());
    }

    @Test
    public void equals_differentObjectType_returnsFalse() {
        assertFalse("An entity should not be equal to an object of a different type.", entity.equals("Some String"));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        assertFalse("An entity should not be equal to null.", entity.equals(null));
    }

    @Test
    public void equals_differentRowKey_returnsFalse() {
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, this.dataset, "Different Row", COLUMN_KEY);
        assertFalse("Entities with different row keys should not be equal.", entity.equals(entity2));
    }

    @Test
    public void equals_differentColumnKey_returnsFalse() {
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, this.dataset, ROW_KEY, "Different Column");
        assertFalse("Entities with different column keys should not be equal.", entity.equals(entity2));
    }

    @Test
    public void equals_differentDataset_returnsFalse() {
        DefaultCategoryDataset<String, String> otherDataset = new DefaultCategoryDataset<>();
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, otherDataset, ROW_KEY, COLUMN_KEY);
        assertFalse("Entities with different datasets should not be equal.", entity.equals(entity2));
    }

    @Test
    public void clone_createsIndependentCopy() throws CloneNotSupportedException {
        CategoryItemEntity<String, String> clonedEntity = (CategoryItemEntity<String, String>) entity.clone();

        assertEquals("Cloned entity should be equal to the original.", entity, clonedEntity);
        assertNotSame("Cloned entity should be a different instance from the original.", entity, clonedEntity);
    }

    @Test
    public void toString_returnsMeaningfulRepresentation() {
        String str = entity.toString();
        assertTrue("toString() should contain the class name.", str.startsWith("CategoryItemEntity:"));
        assertTrue("toString() should contain the row key.", str.contains("rowKey=" + ROW_KEY));
        assertTrue("toString() should contain the column key.", str.contains("columnKey=" + COLUMN_KEY));
    }

    /**
     * This test demonstrates a bug in the equals() method, which does not handle
     * null keys and throws a NullPointerException. A robust implementation
     * should use Objects.equals() for comparison.
     */
    @Test(expected = NullPointerException.class)
    public void equals_withNullRowKey_throwsNullPointerException() {
        CategoryItemEntity<String, String> entity1 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, dataset, null, COLUMN_KEY);
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, dataset, null, COLUMN_KEY);
        entity1.equals(entity2); // This call is expected to throw
    }

    /**
     * This test demonstrates a bug in the equals() method, which does not handle
     * null keys and throws a NullPointerException. A robust implementation
     * should use Objects.equals() for comparison.
     */
    @Test(expected = NullPointerException.class)
    public void equals_withNullColumnKey_throwsNullPointerException() {
        CategoryItemEntity<String, String> entity1 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, dataset, ROW_KEY, null);
        CategoryItemEntity<String, String> entity2 = new CategoryItemEntity<>(AREA, TOOLTIP, URL, dataset, ROW_KEY, null);
        entity1.equals(entity2); // This call is expected to throw
    }
}