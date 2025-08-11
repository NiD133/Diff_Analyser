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
 * -----------------------------------------
 * IntervalCategoryToolTipGeneratorTest.java
 * -----------------------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
@DisplayName("IntervalCategoryToolTipGenerator")
class IntervalCategoryToolTipGeneratorTest {

    private static final String CUSTOM_FORMAT_STRING = "{3} - {4}";

    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCode {

        @Test
        @DisplayName("should be equal to another instance created with the default constructor")
        void equals_shouldBeTrue_forDefaultInstances() {
            // Arrange
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
            IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();

            // Assert
            assertEquals(g1, g2);
        }

        @Test
        @DisplayName("should be equal to another instance with the same number format")
        void equals_shouldBeTrue_forSameNumberFormat() {
            // Arrange
            NumberFormat numberFormat = new DecimalFormat("0.000");
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, numberFormat);
            IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, numberFormat);

            // Assert
            assertEquals(g1, g2);
        }

        @Test
        @DisplayName("should be equal to another instance with the same date format")
        void equals_shouldBeTrue_forSameDateFormat() {
            // Arrange
            DateFormat dateFormat = new SimpleDateFormat("d-MMM");
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, dateFormat);
            IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, dateFormat);

            // Assert
            assertEquals(g1, g2);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different format")
        void equals_shouldBeFalse_forDifferentFormat() {
            // Arrange
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator(); // Default format
            IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, new DecimalFormat("0.000"));

            // Assert
            assertNotEquals(g1, g2);
        }

        @Test
        @DisplayName("should not be equal to an instance of its superclass")
        void equals_shouldBeFalse_whenComparedToSuperclassInstance() {
            // Arrange
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
            StandardCategoryToolTipGenerator g2 = new StandardCategoryToolTipGenerator(
                    IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,
                    NumberFormat.getInstance());

            // Assert
            assertNotEquals(g1, g2);
        }

        @Test
        @DisplayName("should have the same hash code for two equal instances")
        void hashCode_shouldBeSame_forEqualInstances() {
            // Arrange
            IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
            IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();

            // Assert
            assertEquals(g1.hashCode(), g2.hashCode());
            assertEquals(g1, g2);
        }
    }

    @Nested
    @DisplayName("Cloning and Serialization")
    class CloningAndSerialization {

        @Test
        @DisplayName("should be cloneable")
        void class_shouldImplement_PublicCloneable() {
            assertTrue(new IntervalCategoryToolTipGenerator() instanceof PublicCloneable);
        }

        @Test
        @DisplayName("clone() should produce an equal and independent instance")
        void clone_shouldReturnEqualButNotSameInstance() throws CloneNotSupportedException {
            // Arrange
            IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator();

            // Act
            IntervalCategoryToolTipGenerator cloned = CloneUtils.clone(original);

            // Assert
            assertNotSame(original, cloned, "Cloned object should not be the same instance as the original.");
            assertEquals(original, cloned, "Cloned object should be equal to the original.");
        }

        @Test
        @DisplayName("should be equal to the original after serialization and deserialization")
        void serialization_shouldPreserveEquality() {
            // Arrange
            IntervalCategoryToolTipGenerator original = new IntervalCategoryToolTipGenerator(CUSTOM_FORMAT_STRING, DateFormat.getInstance());

            // Act
            IntervalCategoryToolTipGenerator deserialized = TestUtils.serialised(original);

            // Assert
            assertEquals(original, deserialized);
        }
    }
}