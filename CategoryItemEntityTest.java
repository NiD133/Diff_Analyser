/* 
 * JFreeChart : a chart library for the Java(tm) platform
 * =====================================================
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 * Project Info: https://www.jfree.org/jfreechart/index.html
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.
 * 
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 * 
 * ---------------------------
 * CategoryItemEntityTest.java
 * ---------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 * Original Author: David Gilbert;
 * Contributor(s): -;
 */

package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryItemEntity} class.
 */
public class CategoryItemEntityTest {

    private DefaultCategoryDataset<String, String> dataset;
    private CategoryItemEntity<String, String> baseEntity;

    /**
     * Sets up common test objects before each test method.
     */
    @BeforeEach
    public void setUp() {
        dataset = createSampleDataset();
        baseEntity = createSampleEntity(dataset);
    }

    // Helper method to create sample dataset
    private DefaultCategoryDataset<String, String> createSampleDataset() {
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");
        return dataset;
    }

    // Helper method to create sample entity
    private CategoryItemEntity<String, String> createSampleEntity(
            DefaultCategoryDataset<String, String> dataset) {
        return new CategoryItemEntity<>(
            new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
            "ToolTip",
            "URL",
            dataset,
            "R2",
            "C2"
        );
    }

    /**
     * Verifies that two entities with identical field values are equal.
     */
    @Test
    public void identicalEntitiesShouldBeEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        assertEquals(entity1, entity2, "Entities with identical states should be equal");
    }

    /**
     * Verifies that changing the area makes entities unequal.
     */
    @Test
    public void differentAreasShouldMakeEntitiesNotEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        
        entity1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(entity1, entity2, "Entities with different areas should not be equal");
    }

    /**
     * Verifies that setting the same area makes entities equal again.
     */
    @Test
    public void sameAreaAfterChangeShouldMakeEntitiesEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        Rectangle2D newArea = new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);
        
        entity1.setArea(newArea);
        entity2.setArea(newArea);
        assertEquals(entity1, entity2, "Entities with same areas should be equal");
    }

    /**
     * Verifies that changing the tooltip text makes entities unequal.
     */
    @Test
    public void differentTooltipsShouldMakeEntitiesNotEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        
        entity1.setToolTipText("New ToolTip");
        assertNotEquals(entity1, entity2, "Entities with different tooltips should not be equal");
    }

    /**
     * Verifies that setting the same tooltip makes entities equal again.
     */
    @Test
    public void sameTooltipAfterChangeShouldMakeEntitiesEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        String newTooltip = "New ToolTip";
        
        entity1.setToolTipText(newTooltip);
        entity2.setToolTipText(newTooltip);
        assertEquals(entity1, entity2, "Entities with same tooltips should be equal");
    }

    /**
     * Verifies that changing the URL makes entities unequal.
     */
    @Test
    public void differentUrlsShouldMakeEntitiesNotEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        
        entity1.setURLText("New URL");
        assertNotEquals(entity1, entity2, "Entities with different URLs should not be equal");
    }

    /**
     * Verifies that setting the same URL makes entities equal again.
     */
    @Test
    public void sameUrlAfterChangeShouldMakeEntitiesEqual() {
        CategoryItemEntity<String, String> entity1 = createSampleEntity(dataset);
        CategoryItemEntity<String, String> entity2 = createSampleEntity(dataset);
        String newUrl = "New URL";
        
        entity1.setURLText(newUrl);
        entity2.setURLText(newUrl);
        assertEquals(entity1, entity2, "Entities with same URLs should be equal");
    }

    /**
     * Verifies that cloning creates a distinct but equal instance.
     */
    @Test
    public void cloningShouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        CategoryItemEntity<String, String> clone = CloneUtils.clone(baseEntity);
        
        assertNotSame(baseEntity, clone, "Clone should be a different object");
        assertSame(baseEntity.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(baseEntity, clone, "Clone should be equal to original");
    }

    /**
     * Verifies that serialization/deserialization preserves object equality.
     */
    @Test
    public void serializationShouldPreserveEquality() {
        CategoryItemEntity<String, String> deserialized = TestUtils.serialised(baseEntity);
        assertEquals(baseEntity, deserialized, "Deserialized entity should equal original");
    }
}