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
 * ---------------------------------
 * DefaultKeyedValueDatasetTest.java
 * ---------------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.data.general;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultKeyedValueDataset} class.
 */
@DisplayName("A DefaultKeyedValueDataset")
class DefaultKeyedValueDatasetTest {

    @Nested
    @DisplayName("equals contract")
    class EqualsContract {

        @Test
        @DisplayName("is true for two instances with the same key and value")
        void equals_withSameState_shouldBeTrue() {
            // Arrange
            DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
            DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test", 45.5);

            // Assert
            assertTrue(dataset1.equals(dataset2), "Datasets with the same state should be equal.");
            assertEquals(dataset1.hashCode(), dataset2.hashCode(), "Hashcodes of equal objects must be equal.");
        }

        @Test
        @DisplayName("is false for two instances with different keys")
        void equals_withDifferentKeys_shouldBeFalse() {
            // Arrange
            DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test 1", 45.5);
            DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test 2", 45.5);

            // Assert
            assertFalse(dataset1.equals(dataset2));
        }

        @Test
        @DisplayName("is false for two instances with different values")
        void equals_withDifferentValues_shouldBeFalse() {
            // Arrange
            DefaultKeyedValueDataset dataset1 = new DefaultKeyedValueDataset("Test", 45.5);
            DefaultKeyedValueDataset dataset2 = new DefaultKeyedValueDataset("Test", 45.6);

            // Assert
            assertFalse(dataset1.equals(dataset2));
        }
    }

    @Nested
    @DisplayName("cloning")
    class Cloning {

        @Test
        @DisplayName("produces a separate but equal instance")
        void clone_shouldReturnDifferentButEqualInstance() throws CloneNotSupportedException {
            // Arrange
            DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Test", 45.5);

            // Act
            DefaultKeyedValueDataset cloned = (DefaultKeyedValueDataset) original.clone();

            // Assert
            assertNotSame(original, cloned, "A clone must be a different object instance.");
            assertEquals(original, cloned, "A clone should be equal to the original object.");
        }

        @Test
        @DisplayName("produces an independent copy")
        void clone_shouldBeIndependentOfOriginal() throws CloneNotSupportedException {
            // Arrange
            DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Key", 10.0);
            DefaultKeyedValueDataset cloned = (DefaultKeyedValueDataset) original.clone();

            // Act: Modify the state of the clone
            cloned.updateValue(99.9);

            // Assert: The original remains unchanged
            assertNotEquals(original, cloned, "A modified clone should not be equal to the original.");
            assertEquals(10.0, original.getValue(), "Modifying the clone should not change the original's value.");
        }
    }

    @Nested
    @DisplayName("serialization")
    class Serialization {

        @Test
        @DisplayName("preserves the object's state")
        void serialization_shouldPreserveDatasetState() {
            // Arrange
            DefaultKeyedValueDataset original = new DefaultKeyedValueDataset("Test", 25.3);

            // Act
            DefaultKeyedValueDataset deserialized = TestUtils.serialised(original);

            // Assert
            assertEquals(original, deserialized, "The deserialized dataset should be equal to the original.");
        }
    }
}