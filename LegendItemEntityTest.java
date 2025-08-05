/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
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
 * -------------------------
 * LegendItemEntityTest.java
 * -------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    // Test data constants for better maintainability
    private static final Rectangle2D INITIAL_AREA = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle2D MODIFIED_AREA = new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);
    private static final String TEST_TOOLTIP = "New ToolTip";
    private static final String TEST_URL = "New URL";
    private static final String TEST_SERIES_KEY = "A";

    /**
     * Tests that the equals method correctly distinguishes between different field values.
     * This test verifies that two LegendItemEntity objects are equal when all their
     * properties match, and not equal when any property differs.
     */
    @Test
    public void testEquals() {
        // Given: Two identical legend item entities
        LegendItemEntity<String> firstEntity = createBasicLegendItemEntity();
        LegendItemEntity<String> secondEntity = createBasicLegendItemEntity();
        
        // Then: They should be equal initially
        assertEquals(firstEntity, secondEntity, "Entities with identical properties should be equal");

        // Test area property affects equality
        testAreaPropertyEquality(firstEntity, secondEntity);
        
        // Test tooltip property affects equality
        testTooltipPropertyEquality(firstEntity, secondEntity);
        
        // Test URL property affects equality
        testUrlPropertyEquality(firstEntity, secondEntity);
        
        // Test dataset property affects equality
        testDatasetPropertyEquality(firstEntity, secondEntity);
        
        // Test series key property affects equality
        testSeriesKeyPropertyEquality(firstEntity, secondEntity);
    }

    /**
     * Tests that cloning creates a separate but equal instance.
     * Verifies that the cloned object is not the same reference but has equal content.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A legend item entity with some properties set
        LegendItemEntity<String> originalEntity = createFullyConfiguredEntity();
        
        // When: Cloning the entity
        LegendItemEntity<String> clonedEntity = CloneUtils.clone(originalEntity);
        
        // Then: The clone should be a different object but with equal content
        assertNotSame(originalEntity, clonedEntity, 
            "Cloned entity should be a different object reference");
        assertSame(originalEntity.getClass(), clonedEntity.getClass(), 
            "Cloned entity should be the same class");
        assertEquals(originalEntity, clonedEntity, 
            "Cloned entity should have equal content to the original");
    }

    /**
     * Tests that serialization and deserialization preserves object equality.
     * This ensures the class properly implements Serializable.
     */
    @Test
    public void testSerialization() {
        // Given: A legend item entity
        LegendItemEntity<String> originalEntity = createFullyConfiguredEntity();
        
        // When: Serializing and deserializing the entity
        LegendItemEntity<String> deserializedEntity = TestUtils.serialised(originalEntity);
        
        // Then: The deserialized entity should equal the original
        assertEquals(originalEntity, deserializedEntity, 
            "Deserialized entity should equal the original entity");
    }

    // Helper methods for better test organization and reusability

    private LegendItemEntity<String> createBasicLegendItemEntity() {
        return new LegendItemEntity<>(INITIAL_AREA);
    }

    private LegendItemEntity<String> createFullyConfiguredEntity() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(INITIAL_AREA);
        entity.setToolTipText(TEST_TOOLTIP);
        entity.setURLText(TEST_URL);
        entity.setDataset(new DefaultCategoryDataset<String, String>());
        entity.setSeriesKey(TEST_SERIES_KEY);
        return entity;
    }

    private void testAreaPropertyEquality(LegendItemEntity<String> first, LegendItemEntity<String> second) {
        // When: Changing area on first entity
        first.setArea(MODIFIED_AREA);
        
        // Then: Entities should not be equal
        assertNotEquals(first, second, "Entities with different areas should not be equal");
        
        // When: Setting same area on second entity
        second.setArea(MODIFIED_AREA);
        
        // Then: Entities should be equal again
        assertEquals(first, second, "Entities with same areas should be equal");
    }

    private void testTooltipPropertyEquality(LegendItemEntity<String> first, LegendItemEntity<String> second) {
        // When: Setting tooltip on first entity
        first.setToolTipText(TEST_TOOLTIP);
        
        // Then: Entities should not be equal
        assertNotEquals(first, second, "Entities with different tooltips should not be equal");
        
        // When: Setting same tooltip on second entity
        second.setToolTipText(TEST_TOOLTIP);
        
        // Then: Entities should be equal again
        assertEquals(first, second, "Entities with same tooltips should be equal");
    }

    private void testUrlPropertyEquality(LegendItemEntity<String> first, LegendItemEntity<String> second) {
        // When: Setting URL on first entity
        first.setURLText(TEST_URL);
        
        // Then: Entities should not be equal
        assertNotEquals(first, second, "Entities with different URLs should not be equal");
        
        // When: Setting same URL on second entity
        second.setURLText(TEST_URL);
        
        // Then: Entities should be equal again
        assertEquals(first, second, "Entities with same URLs should be equal");
    }

    private void testDatasetPropertyEquality(LegendItemEntity<String> first, LegendItemEntity<String> second) {
        // When: Setting dataset on first entity
        first.setDataset(new DefaultCategoryDataset<String, String>());
        
        // Then: Entities should not be equal
        assertNotEquals(first, second, "Entities with different datasets should not be equal");
        
        // When: Setting dataset on second entity
        second.setDataset(new DefaultCategoryDataset<String, String>());
        
        // Then: Entities should be equal again
        assertEquals(first, second, "Entities with same dataset types should be equal");
    }

    private void testSeriesKeyPropertyEquality(LegendItemEntity<String> first, LegendItemEntity<String> second) {
        // When: Setting series key on first entity
        first.setSeriesKey(TEST_SERIES_KEY);
        
        // Then: Entities should not be equal
        assertNotEquals(first, second, "Entities with different series keys should not be equal");
        
        // When: Setting same series key on second entity
        second.setSeriesKey(TEST_SERIES_KEY);
        
        // Then: Entities should be equal again
        assertEquals(first, second, "Entities with same series keys should be equal");
    }
}