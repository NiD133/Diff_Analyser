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
 * -------------------------------------------
 * IntervalCategoryItemLabelGeneratorTest.java
 * -------------------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.data.category.IntervalCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
@DisplayName("IntervalCategoryItemLabelGenerator")
class IntervalCategoryItemLabelGeneratorTest {

    @Nested
    @DisplayName("Label Generation")
    class LabelGenerationTests {

        private IntervalCategoryDataset mockNumericDataset;
        private IntervalCategoryDataset mockDateDataset;

        @BeforeEach
        void setUp() {
            // Arrange: Create a mock dataset for numeric values
            mockNumericDataset = mock(IntervalCategoryDataset.class);
            when(mockNumericDataset.getSeriesKey(0)).thenReturn("Series 1");
            when(mockNumericDataset.getColumnKey(0)).thenReturn("Category A");
            when(mockNumericDataset.getStartValue(0, 0)).thenReturn(95.12);
            when(mockNumericDataset.getEndValue(0, 0)).thenReturn(105.78);

            // Arrange: Create a mock dataset for date values
            mockDateDataset = mock(IntervalCategoryDataset.class);
            long startTime = 1672531200000L; // 2023-01-01 00:00:00 GMT
            long endTime = 1672617600000L;   // 2023-01-02 00:00:00 GMT
            when(mockDateDataset.getSeriesKey(1)).thenReturn("Series 2");
            when(mockDateDataset.getColumnKey(1)).thenReturn("Category B");
            when(mockDateDataset.getStartValue(1, 1)).thenReturn(new Date(startTime));
            when(mockDateDataset.getEndValue(1, 1)).thenReturn(new Date(endTime));
        }

        @Test
        @DisplayName("should generate correct label using a number formatter")
        void generateLabel_withNumberFormat_shouldProduceCorrectlyFormattedString() {
            // Arrange
            String labelFormat = "{0}: {3} to {4}";
            DecimalFormat numberFormat = new DecimalFormat("0.0");
            var generator = new IntervalCategoryItemLabelGenerator(labelFormat, numberFormat);

            // Act
            String label = generator.generateLabel(mockNumericDataset, 0, 0);

            // Assert
            assertEquals("Series 1: 95.1 to 105.8", label);
        }

        @Test
        @DisplayName("should generate correct label using a date formatter")
        void generateLabel_withDateFormat_shouldProduceCorrectlyFormattedString() {
            // Arrange
            String labelFormat = "Range: {3} -> {4}";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            var generator = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);

            // Act
            String label = generator.generateLabel(mockDateDataset, 1, 1);

            // Assert
            assertEquals("Range: 2023-01-01 -> 2023-01-02", label);
        }
    }

    @Nested
    @DisplayName("Object Contract")
    class ObjectContractTests {

        @Test
        @DisplayName("should be equal for two default instances")
        void equals_withDefaultConstructors_shouldBeEqual() {
            // Arrange
            var generatorA = new IntervalCategoryItemLabelGenerator();
            var generatorB = new IntervalCategoryItemLabelGenerator();

            // Act & Assert
            assertEquals(generatorA, generatorB);
        }

        @Test
        @DisplayName("should be equal when constructed with the same number format")
        void equals_withSameNumberFormat_shouldBeEqual() {
            // Arrange
            String labelFormat = "{3} - {4}";
            var numberFormat = new DecimalFormat("0.000");
            var generatorA = new IntervalCategoryItemLabelGenerator(labelFormat, numberFormat);
            var generatorB = new IntervalCategoryItemLabelGenerator(labelFormat, numberFormat);

            // Act & Assert
            assertEquals(generatorA, generatorB);
        }

        @Test
        @DisplayName("should be equal when constructed with the same date format")
        void equals_withSameDateFormat_shouldBeEqual() {
            // Arrange
            String labelFormat = "{3} - {4}";
            var dateFormat = new SimpleDateFormat("d-MMM");
            var generatorA = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);
            var generatorB = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);

            // Act & Assert
            assertEquals(generatorA, generatorB);
        }

        @Test
        @DisplayName("should not be equal for different formatters")
        void equals_withDifferentFormatters_shouldNotBeEqual() {
            // Arrange
            var generatorA = new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
            var generatorB = new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));

            // Act & Assert
            assertNotEquals(generatorA, generatorB);
        }

        @Test
        @DisplayName("should have hash code consistent with equals")
        void hashCode_shouldBeConsistentWithEquals() {
            // Arrange
            var generatorA = new IntervalCategoryItemLabelGenerator();
            var generatorB = new IntervalCategoryItemLabelGenerator();

            // Act & Assert
            assertEquals(generatorA, generatorB);
            assertEquals(generatorA.hashCode(), generatorB.hashCode());
        }
    }

    @Test
    @DisplayName("should be cloneable")
    void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        var original = new IntervalCategoryItemLabelGenerator();

        // Act
        var clone = (IntervalCategoryItemLabelGenerator) original.clone();

        // Assert
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    @Test
    @DisplayName("should implement PublicCloneable")
    void class_shouldImplementPublicCloneable() {
        // Arrange
        var generator = new IntervalCategoryItemLabelGenerator();

        // Act & Assert
        assertTrue(generator instanceof PublicCloneable);
    }

    @Test
    @DisplayName("should be serializable")
    void serialization_shouldPreserveState() {
        // Arrange
        var originalGenerator = new IntervalCategoryItemLabelGenerator("{3} - {4}", DateFormat.getInstance());

        // Act
        var deserializedGenerator = TestUtils.serialised(originalGenerator);

        // Assert
        assertEquals(originalGenerator, deserializedGenerator);
    }
}