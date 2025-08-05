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

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CategoryItemEntity} class, focusing on its state,
 * equality, cloning, and serialization.
 */
public class CategoryItemEntityTest {

    // Constants for readable and consistent test data
    private static final String ROW_KEY = "R2";
    private static final String COLUMN_KEY = "C2";
    private static final String TOOLTIP_TEXT = "ToolTip";
    private static final String URL_TEXT = "URL";
    private static final Rectangle2D.Double INITIAL_AREA = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);

    private DefaultCategoryDataset<String, String> dataset;
    private CategoryItemEntity<String, String> entity;

    /**
     * Sets up a common dataset and a CategoryItemEntity instance before each test.
     */
    @BeforeEach
    public void setUp() {
        this.dataset = new DefaultCategoryDataset<>();
        this.dataset.addValue(1.0, "R1", "C1");
        this.dataset.addValue(2.0, "R1", "C2");
        this.dataset.addValue(3.0, "R2", "C1");
        this.dataset.addValue(4.0, "R2", "C2");

        this.entity = new CategoryItemEntity<>(INITIAL_AREA, TOOLTIP_TEXT, URL_TEXT,
                this.dataset, ROW_KEY, COLUMN_KEY);
    }

    /**
     * Verifies that cloning produces an object that is equal to the original
     * but is not the same instance.
     */
    @Test
    @DisplayName("Cloning should produce an equal but not identical instance")
    public void testCloning() throws CloneNotSupportedException {
        // Act
        CategoryItemEntity<String, String> clonedEntity = CloneUtils.clone(this.entity);

        // Assert
        assertNotSame(this.entity, clonedEntity, "Cloned object should not be the same instance.");
        assertEquals(this.entity, clonedEntity, "Cloned object should be equal to the original.");
    }

    /**
     * Verifies that an object's state is preserved after serialization and
     * deserialization.
     */
    @Test
    @DisplayName("Serialization and deserialization should preserve object state")
    public void testSerialization() {
        // Act
        CategoryItemEntity<String, String> deserializedEntity = TestUtils.serialised(this.entity);

        // Assert
        assertEquals(this.entity, deserializedEntity, "Deserialized object should be equal to the original.");
    }

    /**
     * A nested test class for thorough testing of the equals() and hashCode() contract.
     * This improves organization by grouping related tests.
     */
    @Nested
    @DisplayName("Equals and HashCode Contract")
    class EqualsContract {

        private CategoryItemEntity<String, String> identicalEntity;

        @BeforeEach
        void setUp() {
            // Arrange: Create a second, identical entity for comparison
            identicalEntity = new CategoryItemEntity<>(INITIAL_AREA, TOOLTIP_TEXT, URL_TEXT,
                    dataset, ROW_KEY, COLUMN_KEY);
        }

        @Test
        @DisplayName("should be true for two identical objects")
        void equals_whenObjectsAreIdentical_shouldReturnTrue() {
            assertTrue(entity.equals(identicalEntity), "Entities with identical properties should be equal.");
            assertEquals(entity.hashCode(), identicalEntity.hashCode(), "Hash codes should be identical for equal objects.");
        }

        @Test
        @DisplayName("should be false when area is different")
        void equals_whenAreaIsDifferent_shouldReturnFalse() {
            identicalEntity.setArea(new Rectangle2D.Double(9.0, 9.0, 9.0, 9.0));
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when tool tip text is different")
        void equals_whenToolTipTextIsDifferent_shouldReturnFalse() {
            identicalEntity.setToolTipText("Different ToolTip");
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when URL text is different")
        void equals_whenUrlTextIsDifferent_shouldReturnFalse() {
            identicalEntity.setURLText("Different URL");
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when dataset is different")
        void equals_whenDatasetIsDifferent_shouldReturnFalse() {
            identicalEntity.setDataset(new DefaultCategoryDataset<>());
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when row key is different")
        void equals_whenRowKeyIsDifferent_shouldReturnFalse() {
            identicalEntity.setRowKey("R1");
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when column key is different")
        void equals_whenColumnKeyIsDifferent_shouldReturnFalse() {
            identicalEntity.setColumnKey("C1");
            assertNotEquals(entity, identicalEntity);
        }

        @Test
        @DisplayName("should be false when compared to null")
        void equals_whenComparedWithNull_shouldReturnFalse() {
            assertNotEquals(null, entity);
        }

        @Test
        @DisplayName("should be false when compared to an object of a different type")
        void equals_whenComparedWithDifferentClass_shouldReturnFalse() {
            assertNotEquals(entity, new Object());
        }
    }
}