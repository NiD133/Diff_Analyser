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
 * --------------------------
 * PlotRenderingInfoTest.java
 * --------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   (Refactored for clarity by a professional software engineer);
 *
 */

package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PlotRenderingInfo} class, focusing on equality,
 * cloning, and serialization.
 */
@DisplayName("PlotRenderingInfo")
class PlotRenderingInfoTest {

    private PlotRenderingInfo info1;
    private PlotRenderingInfo info2;

    @BeforeEach
    void setUp() {
        info1 = new PlotRenderingInfo(new ChartRenderingInfo());
        info2 = new PlotRenderingInfo(new ChartRenderingInfo());
    }

    @Nested
    @DisplayName("equals() method")
    class Equals {

        @Test
        @DisplayName("should be true for two newly created instances")
        void equals_whenNewlyCreated_isTrue() {
            assertEquals(info1, info2);
            assertEquals(info1.hashCode(), info2.hashCode());
        }

        @Test
        @DisplayName("should be true for the same instance")
        void equals_whenSameInstance_isTrue() {
            assertEquals(info1, info1);
        }

        @Test
        @DisplayName("should be false when compared to null")
        void equals_whenComparedToNull_isFalse() {
            assertNotEquals(null, info1);
        }

        @Test
        @DisplayName("should be false when compared to a different class")
        void equals_whenComparedToDifferentClass_isFalse() {
            assertNotEquals("String", info1);
        }

        @Test
        @DisplayName("should be false if plot areas differ")
        void equals_whenPlotAreaDiffers_isFalse() {
            // Arrange
            info1.setPlotArea(new Rectangle(1, 2, 3, 4));
            
            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should be false if data areas differ")
        void equals_whenDataAreaDiffers_isFalse() {
            // Arrange
            info1.setDataArea(new Rectangle(1, 2, 3, 4));

            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should be false if subplot lists differ")
        void equals_whenSubplotListsDiffer_isFalse() {
            // Arrange
            info1.addSubplotInfo(new PlotRenderingInfo(null));

            // Act & Assert
            assertNotEquals(info1, info2);
        }

        @Test
        @DisplayName("should be false if subplot content differs")
        void equals_whenSubplotContentDiffers_isFalse() {
            // Arrange: Make both infos have one identical subplot
            info1.addSubplotInfo(new PlotRenderingInfo(null));
            info2.addSubplotInfo(new PlotRenderingInfo(null));
            assertEquals(info1, info2, "Infos should be equal after adding identical subplots.");

            // Act: Modify the subplot in one info
            info1.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));

            // Assert: The parent infos should no longer be equal
            assertNotEquals(info1, info2);
        }
    }

    @Nested
    @DisplayName("clone() method")
    class Cloning {

        @Test
        @DisplayName("should produce an equal but not identical object")
        void clone_producesEqualObject() throws CloneNotSupportedException {
            // Arrange
            info1.setPlotArea(new Rectangle2D.Double(1, 2, 3, 4));
            info1.setDataArea(new Rectangle2D.Double(5, 6, 7, 8));

            // Act
            PlotRenderingInfo clone = CloneUtils.clone(info1);

            // Assert
            assertNotSame(info1, clone, "Clone should be a different instance.");
            assertEquals(info1, clone, "Clone should be equal to the original.");
        }

        @Test
        @DisplayName("should create a deep copy independent of the original")
        void clone_isDeepAndIndependent() throws CloneNotSupportedException {
            // Arrange: Set up original object with mutable state
            info1.setPlotArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
            info1.addSubplotInfo(new PlotRenderingInfo(null));
            PlotRenderingInfo clone = CloneUtils.clone(info1);
            assertEquals(info1, clone);

            // Act: Modify a mutable field in the original object
            info1.getPlotArea().setRect(10, 20, 30, 40);

            // Assert: The clone should not be affected
            assertNotEquals(info1, clone, "Modifying original's plotArea should not affect the clone.");
        }
    }

    @Nested
    @DisplayName("Serialization")
    class Serialization {

        @Test
        @DisplayName("should preserve the state of a default instance")
        void serialization_ofDefaultInstance() {
            // Arrange
            PlotRenderingInfo original = new PlotRenderingInfo(new ChartRenderingInfo());

            // Act
            PlotRenderingInfo restored = TestUtils.serialised(original);

            // Assert
            assertEquals(original, restored);
        }

        @Test
        @DisplayName("should preserve the state of a populated instance")
        void serialization_ofPopulatedInstance() {
            // Arrange
            PlotRenderingInfo original = new PlotRenderingInfo(new ChartRenderingInfo());
            original.setPlotArea(new Rectangle2D.Double(1, 2, 3, 4));
            original.setDataArea(new Rectangle2D.Double(5, 6, 7, 8));
            original.addSubplotInfo(new PlotRenderingInfo(null));
            original.getSubplotInfo(0).setDataArea(new Rectangle(10, 20, 30, 40));

            // Act
            PlotRenderingInfo restored = TestUtils.serialised(original);

            // Assert
            assertEquals(original, restored);
        }
    }
}