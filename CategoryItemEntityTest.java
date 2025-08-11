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
 * ---------------------------
 * CategoryItemEntityTest.java
 * ---------------------------
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryItemEntity} class.
 *
 * These tests verify the core functionality of CategoryItemEntity including:
 * - Equality comparison between entities
 * - Cloning behavior
 * - Serialization/deserialization
 */
public class CategoryItemEntityTest {

    // Test data constants for better maintainability
    private static final String SALES_ROW = "Sales";
    private static final String MARKETING_ROW = "Marketing";
    private static final String Q1_COLUMN = "Q1";
    private static final String Q2_COLUMN = "Q2";

    private static final String ORIGINAL_TOOLTIP = "Original Tooltip";
    private static final String ORIGINAL_URL = "http://example.com/original";
    private static final String UPDATED_TOOLTIP = "Updated Tooltip";
    private static final String UPDATED_URL = "http://example.com/updated";

    private static final Rectangle2D ORIGINAL_AREA = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle2D UPDATED_AREA = new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);

    private DefaultCategoryDataset<String, String> sampleDataset;
    private CategoryItemEntity<String, String> baseEntity;

    @BeforeEach
    public void setUp() {
        sampleDataset = createSampleDataset();
        baseEntity = createSampleEntity();
    }

    /**
     * Creates a sample dataset with quarterly sales data for testing.
     */
    private DefaultCategoryDataset<String, String> createSampleDataset() {
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        dataset.addValue(100.0, SALES_ROW, Q1_COLUMN);
        dataset.addValue(150.0, SALES_ROW, Q2_COLUMN);
        dataset.addValue(80.0, MARKETING_ROW, Q1_COLUMN);
        dataset.addValue(120.0, MARKETING_ROW, Q2_COLUMN);
        return dataset;
    }

    /**
     * Creates a sample CategoryItemEntity for testing purposes.
     */
    private CategoryItemEntity<String, String> createSampleEntity() {
        return new CategoryItemEntity<>(
                ORIGINAL_AREA,
                ORIGINAL_TOOLTIP,
                ORIGINAL_URL,
                sampleDataset,
                MARKETING_ROW,
                Q2_COLUMN
        );
    }

    /**
     * Tests that the equals method correctly identifies identical entities
     * and distinguishes between entities with different properties.
     */
    @Test
    public void testEquals_IdenticalEntities_ShouldBeEqual() {
        CategoryItemEntity<String, String> identicalEntity = new CategoryItemEntity<>(
                ORIGINAL_AREA,
                ORIGINAL_TOOLTIP,
                ORIGINAL_URL,
                sampleDataset,
                MARKETING_ROW,
                Q2_COLUMN
        );

        assertEquals(baseEntity, identicalEntity,
                "Two entities with identical properties should be equal");
    }

    @Test
    public void testEquals_DifferentAreas_ShouldNotBeEqual() {
        CategoryItemEntity<String, String> entityWithDifferentArea = new CategoryItemEntity<>(
                UPDATED_AREA,
                ORIGINAL_TOOLTIP,
                ORIGINAL_URL,
                sampleDataset,
                MARKETING_ROW,
                Q2_COLUMN
        );

        assertNotEquals(baseEntity, entityWithDifferentArea,
                "Entities with different areas should not be equal");
    }

    @Test
    public void testEquals_ModifyingAreaAfterCreation_ShouldAffectEquality() {
        CategoryItemEntity<String, String> modifiableEntity = createSampleEntity();
        assertEquals(baseEntity, modifiableEntity,
                "Entities should be equal before modification");

        modifiableEntity.setArea(UPDATED_AREA);
        assertNotEquals(baseEntity, modifiableEntity,
                "Entities should not be equal after area modification");

        baseEntity.setArea(UPDATED_AREA);
        assertEquals(baseEntity, modifiableEntity,
                "Entities should be equal again after both areas are updated");
    }

    @Test
    public void testEquals_ModifyingTooltipText_ShouldAffectEquality() {
        CategoryItemEntity<String, String> modifiableEntity = createSampleEntity();

        modifiableEntity.setToolTipText(UPDATED_TOOLTIP);
        assertNotEquals(baseEntity, modifiableEntity,
                "Entities with different tooltip text should not be equal");

        baseEntity.setToolTipText(UPDATED_TOOLTIP);
        assertEquals(baseEntity, modifiableEntity,
                "Entities should be equal after both tooltips are updated");
    }

    @Test
    public void testEquals_ModifyingUrlText_ShouldAffectEquality() {
        CategoryItemEntity<String, String> modifiableEntity = createSampleEntity();

        modifiableEntity.setURLText(UPDATED_URL);
        assertNotEquals(baseEntity, modifiableEntity,
                "Entities with different URL text should not be equal");

        baseEntity.setURLText(UPDATED_URL);
        assertEquals(baseEntity, modifiableEntity,
                "Entities should be equal after both URLs are updated");
    }

    /**
     * Tests that cloning creates a separate but equivalent instance.
     */
    @Test
    public void testCloning_ShouldCreateSeparateButEqualInstance() throws CloneNotSupportedException {
        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(baseEntity);

        assertNotSame(baseEntity, clonedEntity,
                "Cloned entity should be a different object instance");
        assertSame(baseEntity.getClass(), clonedEntity.getClass(),
                "Cloned entity should have the same class");
        assertEquals(baseEntity, clonedEntity,
                "Cloned entity should be equal to the original");
    }

    /**
     * Tests that the entity can be serialized and deserialized while maintaining equality.
     */
    @Test
    public void testSerialization_ShouldPreserveEquality() {
        CategoryItemEntity<String, String> deserializedEntity = TestUtils.serialised(baseEntity);

        assertEquals(baseEntity, deserializedEntity,
                "Deserialized entity should be equal to the original");
    }

    /**
     * Additional test to verify entities with different row/column keys are not equal.
     */
    @Test
    public void testEquals_DifferentRowKeys_ShouldNotBeEqual() {
        CategoryItemEntity<String, String> entityWithDifferentRow = new CategoryItemEntity<>(
                ORIGINAL_AREA,
                ORIGINAL_TOOLTIP,
                ORIGINAL_URL,
                sampleDataset,
                SALES_ROW,  // Different row key
                Q2_COLUMN
        );

        assertNotEquals(baseEntity, entityWithDifferentRow,
                "Entities with different row keys should not be equal");
    }

    @Test
    public void testEquals_DifferentColumnKeys_ShouldNotBeEqual() {
        CategoryItemEntity<String, String> entityWithDifferentColumn = new CategoryItemEntity<>(
                ORIGINAL_AREA,
                ORIGINAL_TOOLTIP,
                ORIGINAL_URL,
                sampleDataset,
                MARKETING_ROW,
                Q1_COLUMN  // Different column key
        );

        assertNotEquals(baseEntity, entityWithDifferentColumn,
                "Entities with different column keys should not be equal");
    }
}