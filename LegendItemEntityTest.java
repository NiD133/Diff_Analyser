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

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Tests for the {@link LegendItemEntity} class, focusing on equality,
 * cloning, and serialization.
 */
@DisplayName("LegendItemEntity")
class LegendItemEntityTest {

    private LegendItemEntity<String> entity;

    /**
     * Creates a default, fully-populated entity for use in tests.
     *
     * @return A new LegendItemEntity instance.
     */
    private static LegendItemEntity<String> createDefaultEntity() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        entity.setToolTipText("Test ToolTip");
        entity.setURLText("http://www.jfree.org");
        entity.setDataset(new DefaultCategoryDataset<>());
        entity.setSeriesKey("Test Series");
        return entity;
    }

    @BeforeEach
    void setUp() {
        entity = createDefaultEntity();
    }

    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be true for two identical entities")
        void twoIdenticalEntitiesShouldBeEqual() {
            // Arrange
            LegendItemEntity<String> entity2 = createDefaultEntity();

            // Assert
            assertEquals(entity, entity2, "Entities with the same attributes should be equal.");
            assertEquals(entity.hashCode(), entity2.hashCode(), "Hash codes should be equal for equal objects.");
        }

        @Test
        @DisplayName("should be true for the same instance")
        void sameInstanceShouldBeEqual() {
            assertEquals(entity, entity, "An entity should be equal to itself.");
        }

        @Test
        @DisplayName("should be false when compared to null")
        void comparingToNullShouldBeFalse() {
            assertNotEquals(null, entity, "An entity should not be equal to null.");
        }

        @Test
        @DisplayName("should be false when compared to an object of a different type")
        void comparingToDifferentTypeShouldBeFalse() {
            assertNotEquals("Some String", entity, "An entity should not be equal to an object of a different class.");
        }

        /**
         * Provides a stream of functions, each modifying a single attribute of a LegendItemEntity.
         * This is used to test that each attribute is correctly checked in the equals() method.
         *
         * @return A stream of Arguments, each containing a name and a modifying function.
         */
        static Stream<Arguments> attributeModifiers() {
            return Stream.of(
                arguments("area", (Consumer<LegendItemEntity<String>>) e -> e.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0))),
                arguments("toolTipText", (Consumer<LegendItemEntity<String>>) e -> e.setToolTipText("Different ToolTip")),
                arguments("urlText", (Consumer<LegendItemEntity<String>>) e -> e.setURLText("http://different.url")),
                arguments("dataset", (Consumer<LegendItemEntity<String>>) e -> e.setDataset(new DefaultCategoryDataset<>())),
                arguments("seriesKey", (Consumer<LegendItemEntity<String>>) e -> e.setSeriesKey("Different Series"))
            );
        }

        @ParameterizedTest(name = "should be false when {0} is different")
        @MethodSource("attributeModifiers")
        void equalsShouldBeFalseWhenAttributeIsDifferent(String attributeName, Consumer<LegendItemEntity<String>> modifier) {
            // Arrange
            LegendItemEntity<String> entity2 = createDefaultEntity();
            
            // Act: Modify a single attribute of the second entity
            modifier.accept(entity2);

            // Assert
            assertNotEquals(entity, entity2, "Entities with a different " + attributeName + " should not be equal.");
        }
    }

    @Test
    @DisplayName("cloning should produce an equal but not identical instance")
    void cloningShouldResultInEqualButNotSameInstance() throws CloneNotSupportedException {
        // Act
        LegendItemEntity<String> clone = CloneUtils.clone(entity);

        // Assert
        assertNotSame(entity, clone, "Clone should be a different object instance.");
        assertEquals(entity, clone, "Clone should be equal to the original object.");
    }

    @Test
    @DisplayName("serialization and deserialization should preserve equality")
    void serializationShouldPreserveEquality() {
        // Act
        LegendItemEntity<String> deserializedEntity = TestUtils.serialised(entity);

        // Assert
        assertEquals(entity, deserializedEntity, "Deserialized entity should be equal to the original.");
    }
}