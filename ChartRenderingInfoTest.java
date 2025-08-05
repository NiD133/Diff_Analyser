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
 * ChartRenderingInfoTest.java
 * ---------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ChartRenderingInfo} class.
 */
@DisplayName("ChartRenderingInfo")
class ChartRenderingInfoTest {

    @Nested
    @DisplayName("equals")
    class Equals {

        @Test
        @DisplayName("should return true for two default instances")
        void equals_shouldReturnTrue_forDefaultInstances() {
            // Arrange
            ChartRenderingInfo info1 = new ChartRenderingInfo();
            ChartRenderingInfo info2 = new ChartRenderingInfo();

            // Act & Assert
            assertEquals(info1, info2);
        }

        @Test
        @DisplayName("should return false when chartArea differs")
        void equals_shouldReturnFalse_whenChartAreaDiffers() {
            // Arrange
            ChartRenderingInfo info1 = new ChartRenderingInfo();
            info1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
            ChartRenderingInfo info2 = new ChartRenderingInfo();

            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should return false when plotInfo differs")
        void equals_shouldReturnFalse_whenPlotInfoDiffers() {
            // Arrange
            ChartRenderingInfo info1 = new ChartRenderingInfo();
            info1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
            ChartRenderingInfo info2 = new ChartRenderingInfo();

            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should return false when entityCollection differs")
        void equals_shouldReturnFalse_whenEntityCollectionDiffers() {
            // Arrange
            ChartRenderingInfo info1 = new ChartRenderingInfo();
            StandardEntityCollection entities = new StandardEntityCollection();
            entities.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
            info1.setEntityCollection(entities);
            ChartRenderingInfo info2 = new ChartRenderingInfo();

            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should return true when all properties are equal")
        void equals_shouldReturnTrue_whenAllPropertiesAreEqual() {
            // Arrange
            ChartRenderingInfo info1 = new ChartRenderingInfo();
            info1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
            info1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
            StandardEntityCollection entities1 = new StandardEntityCollection();
            entities1.add(new ChartEntity(new Rectangle(5, 6, 7, 8)));
            info1.setEntityCollection(entities1);

            ChartRenderingInfo info2 = new ChartRenderingInfo();
            info2.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
            info2.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
            StandardEntityCollection entities2 = new StandardEntityCollection();
            entities2.add(new ChartEntity(new Rectangle(5, 6, 7, 8)));
            info2.setEntityCollection(entities2);

            // Act & Assert
            assertEquals(info1, info2);
        }
    }

    @Test
    @DisplayName("clone should create a deep copy")
    void clone_shouldCreateDeepCopy() throws CloneNotSupportedException {
        // Arrange
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        original.getEntityCollection().add(new ChartEntity(new Rectangle(10, 20, 30, 40)));

        // Act
        ChartRenderingInfo clone = (ChartRenderingInfo) CloneUtils.clone(original);

        // Assert: clone is a separate but equal object
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // Assert: modifying original's chartArea does not affect the clone (deep copy)
        original.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(original.getChartArea(), clone.getChartArea());
        assertNotEquals(original, clone);

        // Assert: modifying original's entity collection does not affect the clone (deep copy)
        original.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        assertNotEquals(original.getEntityCollection(), clone.getEntityCollection());
        assertNotEquals(original, clone);
    }

    @Test
    @DisplayName("serialization should preserve the object's state")
    void serialization_shouldPreserveState() {
        // Arrange
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        originalInfo.getPlotInfo().setDataArea(new Rectangle2D.Double(5.0, 6.0, 7.0, 8.0));

        // Act
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);

        // Assert
        assertEquals(originalInfo, deserializedInfo);
    }

    @Test
    @DisplayName("deserialized PlotInfo should correctly reference its owner")
    void deserialization_shouldRestorePlotInfoOwner() {
        // Arrange
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Act
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);

        // Assert that the PlotRenderingInfo's back-reference to its owner
        // is correctly restored to the new deserialized parent object.
        assertEquals(deserializedInfo, deserializedInfo.getPlotInfo().getOwner());
        assertNotSame(originalInfo, deserializedInfo.getPlotInfo().getOwner());
    }
}