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
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    // Test data constants for better readability and maintainability
    private static final Rectangle PLOT_AREA_1 = new Rectangle(2, 3, 4, 5);
    private static final Rectangle PLOT_AREA_2 = new Rectangle(2, 3, 4, 5);
    private static final Rectangle DATA_AREA_1 = new Rectangle(2, 4, 6, 8);
    private static final Rectangle DATA_AREA_2 = new Rectangle(2, 4, 6, 8);
    private static final Rectangle SUBPLOT_DATA_AREA = new Rectangle(1, 2, 3, 4);

    /**
     * Creates a basic PlotRenderingInfo instance for testing.
     */
    private PlotRenderingInfo createBasicPlotRenderingInfo() {
        return new PlotRenderingInfo(new ChartRenderingInfo());
    }

    /**
     * Test the equals() method to ensure proper equality comparison.
     * This test verifies that two PlotRenderingInfo objects are equal when they have
     * the same plot area, data area, and subplot information.
     */
    @Test
    public void testEquals() {
        // Given: Two identical PlotRenderingInfo instances
        PlotRenderingInfo firstInfo = createBasicPlotRenderingInfo();
        PlotRenderingInfo secondInfo = createBasicPlotRenderingInfo();
        
        // Then: They should be equal initially
        assertEquals(firstInfo, secondInfo, "Two basic PlotRenderingInfo instances should be equal");
        assertEquals(secondInfo, firstInfo, "Equality should be symmetric");

        // When: Setting different plot areas
        firstInfo.setPlotArea(PLOT_AREA_1);
        // Then: They should not be equal
        assertNotEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with different plot areas should not be equal");
        
        // When: Setting the same plot area on both
        secondInfo.setPlotArea(PLOT_AREA_2);
        // Then: They should be equal again
        assertEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with same plot areas should be equal");

        // When: Setting different data areas
        firstInfo.setDataArea(DATA_AREA_1);
        // Then: They should not be equal
        assertNotEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with different data areas should not be equal");
        
        // When: Setting the same data area on both
        secondInfo.setDataArea(DATA_AREA_2);
        // Then: They should be equal again
        assertEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with same data areas should be equal");

        // When: Adding subplot info to only one instance
        firstInfo.addSubplotInfo(new PlotRenderingInfo(null));
        // Then: They should not be equal
        assertNotEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with different subplot counts should not be equal");
        
        // When: Adding the same subplot info to both
        secondInfo.addSubplotInfo(new PlotRenderingInfo(null));
        // Then: They should be equal again
        assertEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with same subplot info should be equal");

        // When: Modifying subplot data area in only one instance
        firstInfo.getSubplotInfo(0).setDataArea(SUBPLOT_DATA_AREA);
        // Then: They should not be equal
        assertNotEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with different subplot data areas should not be equal");
        
        // When: Setting the same subplot data area on both
        secondInfo.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        // Then: They should be equal again
        assertEquals(firstInfo, secondInfo, "PlotRenderingInfo instances with same subplot data areas should be equal");
    }

    /**
     * Test cloning functionality to ensure deep copying works correctly.
     * This test verifies that cloned objects are independent of their originals.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A PlotRenderingInfo with a plot area
        PlotRenderingInfo originalInfo = createBasicPlotRenderingInfo();
        originalInfo.setPlotArea(new Rectangle2D.Double());
        
        // When: Cloning the object
        PlotRenderingInfo clonedInfo = CloneUtils.clone(originalInfo);
        
        // Then: The clone should be a different object but equal in content
        assertNotSame(originalInfo, clonedInfo, "Cloned object should be a different instance");
        assertSame(originalInfo.getClass(), clonedInfo.getClass(), "Cloned object should be of the same class");
        assertEquals(originalInfo, clonedInfo, "Cloned object should be equal to original");

        // Test independence: Modifying plot area
        // When: Changing the original's plot area
        originalInfo.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        // Then: The clone should remain unchanged
        assertNotEquals(originalInfo, clonedInfo, "Original and clone should be independent after modifying plot area");
        
        // When: Applying the same change to the clone
        clonedInfo.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        // Then: They should be equal again
        assertEquals(originalInfo, clonedInfo, "Objects should be equal after applying same changes");

        // Test independence: Modifying data area
        // When: Changing the original's data area
        originalInfo.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        // Then: The clone should remain unchanged
        assertNotEquals(originalInfo, clonedInfo, "Original and clone should be independent after modifying data area");
        
        // When: Applying the same change to the clone
        clonedInfo.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        // Then: They should be equal again
        assertEquals(originalInfo, clonedInfo, "Objects should be equal after applying same data area changes");
    }

    /**
     * Test serialization to ensure objects can be properly serialized and deserialized.
     * This test verifies that the serialization process preserves object equality.
     */
    @Test
    public void testSerialization() {
        // Given: A PlotRenderingInfo instance
        PlotRenderingInfo originalInfo = createBasicPlotRenderingInfo();
        
        // When: Serializing and deserializing the object
        PlotRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);
        
        // Then: The deserialized object should be equal to the original
        assertEquals(originalInfo, deserializedInfo, 
            "Deserialized PlotRenderingInfo should be equal to the original");
    }
}