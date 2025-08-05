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
 * -----------------------------------
 * CustomCategoryURLGeneratorTest.java
 * -----------------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.urls;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class.
 */
@DisplayName("CustomCategoryURLGenerator")
class CustomCategoryURLGeneratorTest {

    @Nested
    @DisplayName("equals()")
    class EqualsTests {

        @Test
        @DisplayName("Two new instances should be equal")
        void newInstances_shouldBeEqual() {
            // Arrange
            CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
            CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();

            // Act & Assert
            assertEquals(g1, g2);
        }

        @Test
        @DisplayName("Instances with different URL series should not be equal")
        void instancesWithDifferentContent_shouldNotBeEqual() {
            // Arrange
            CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
            g1.addURLSeries(List.of("URL A1"));
            CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();

            // Act & Assert
            assertNotEquals(g1, g2);
        }

        @Test
        @DisplayName("Instances with the same URL series should be equal")
        void instancesWithSameContent_shouldBeEqual() {
            // Arrange
            CustomCategoryURLGenerator g1 = new CustomCategoryURLGenerator();
            g1.addURLSeries(List.of("URL A1", "URL A2"));
            CustomCategoryURLGenerator g2 = new CustomCategoryURLGenerator();
            g2.addURLSeries(List.of("URL A1", "URL A2"));

            // Act & Assert
            assertEquals(g1, g2);
        }
    }

    @Nested
    @DisplayName("addURLSeries()")
    class AddURLSeriesTests {

        @Test
        @DisplayName("Adding a null list should be treated as an empty list")
        void addURLSeries_withNullList_shouldAddEmptyUrlList() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

            // Act
            generator.addURLSeries(null);

            // Assert
            assertEquals(1, generator.getListCount());
            assertEquals(0, generator.getURLCount(0));
        }

        @Test
        @DisplayName("Adding a valid list should store URLs correctly")
        void addURLSeries_withValidList_shouldStoreUrls() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
            List<String> urls = List.of("URL1", "URL2");

            // Act
            generator.addURLSeries(urls);

            // Assert
            assertEquals(1, generator.getListCount());
            assertEquals(2, generator.getURLCount(0));
            assertEquals("URL1", generator.getURL(0, 0));
            assertEquals("URL2", generator.getURL(0, 1));
        }

        @Test
        @DisplayName("Should create a defensive copy of the input list")
        void addURLSeries_shouldCreateDefensiveCopy() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
            List<String> originalList = new ArrayList<>();
            originalList.add("URL1");
            generator.addURLSeries(originalList);

            // Act: Modify the original list after it has been added
            originalList.clear();
            originalList.add("MODIFIED_URL");

            // Assert: The generator's internal state should be unaffected
            assertEquals(1, generator.getURLCount(0));
            assertEquals("URL1", generator.getURL(0, 0));
        }
    }
    
    @Nested
    @DisplayName("generateURL()")
    class GenerateURLTests {

        @Test
        @DisplayName("Should return the correct URL for valid series and item indices")
        void generateURL_withValidIndices_shouldReturnCorrectUrl() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
            generator.addURLSeries(List.of("S0_URL0", "S0_URL1"));
            generator.addURLSeries(List.of("S1_URL0"));
            
            // Act: The dataset parameter is ignored by this implementation
            String result = generator.generateURL(null, 1, 0);
            
            // Assert
            assertEquals("S1_URL0", result);
        }

        @Test
        @DisplayName("Should return null for an out-of-bounds series index")
        void generateURL_withInvalidSeriesIndex_shouldReturnNull() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
            generator.addURLSeries(List.of("URL"));

            // Act
            String result = generator.generateURL(null, 1, 0);

            // Assert
            assertNull(result);
        }

        @Test
        @DisplayName("Should return null for an out-of-bounds item index")
        void generateURL_withInvalidItemIndex_shouldReturnNull() {
            // Arrange
            CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
            generator.addURLSeries(List.of("URL"));

            // Act
            String result = generator.generateURL(null, 0, 1);

            // Assert
            assertNull(result);
        }
    }

    @Test
    @DisplayName("Cloning should create an independent copy")
    void clone_shouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(List.of("URL A1", "URL A2"));

        // Act
        CustomCategoryURLGenerator clone = CloneUtils.clone(original);

        // Assert: Clone is a separate but equal object
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // Act: Modify the original to test for independence
        original.addURLSeries(List.of("URL B1"));

        // Assert: The clone should not be affected by changes to the original
        assertNotEquals(original, clone);
    }

    @Test
    @DisplayName("Serialization should preserve the generator's state")
    void serialization_shouldPreserveState() {
        // Arrange
        CustomCategoryURLGenerator original = new CustomCategoryURLGenerator();
        original.addURLSeries(List.of("URL A1", "URL A2"));
        original.addURLSeries(List.of("URL B1"));

        // Act
        CustomCategoryURLGenerator deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized);
    }

    @Test
    @DisplayName("Class should implement PublicCloneable")
    void class_shouldImplementPublicCloneable() {
        // Arrange
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Act & Assert
        assertTrue(generator instanceof PublicCloneable);
    }
}