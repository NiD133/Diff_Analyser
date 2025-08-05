/* 
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 * Project Info: https://www.jfree.org/jfreechart/index.html
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
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

    // Helper method to create a basic PlotRenderingInfo instance
    private PlotRenderingInfo createInfo() {
        return new PlotRenderingInfo(new ChartRenderingInfo());
    }

    // Helper method to create an instance with subplots
    private PlotRenderingInfo createInfoWithSubplots() {
        PlotRenderingInfo info = createInfo();
        info.addSubplotInfo(new PlotRenderingInfo(null));
        return info;
    }

    /**
     * Tests that two empty instances are equal.
     */
    @Test
    public void testEqualsForEmptyInstances() {
        PlotRenderingInfo p1 = createInfo();
        PlotRenderingInfo p2 = createInfo();
        assertEquals(p1, p2, "Empty instances should be equal");
    }

    /**
     * Tests that plot area differences are detected.
     */
    @Test
    public void testEqualsWithDifferentPlotAreas() {
        PlotRenderingInfo p1 = createInfo();
        PlotRenderingInfo p2 = createInfo();
        
        p1.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertNotEquals(p1, p2, "Instances should differ after setting plot area in one");
        
        p2.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertEquals(p1, p2, "Instances should be equal after setting identical plot areas");
    }

    /**
     * Tests that data area differences are detected.
     */
    @Test
    public void testEqualsWithDifferentDataAreas() {
        PlotRenderingInfo p1 = createInfo();
        PlotRenderingInfo p2 = createInfo();
        p1.setPlotArea(new Rectangle(0, 0, 10, 10));
        p2.setPlotArea(new Rectangle(0, 0, 10, 10));
        
        p1.setDataArea(new Rectangle(2, 4, 6, 8));
        assertNotEquals(p1, p2, "Instances should differ after setting data area in one");
        
        p2.setDataArea(new Rectangle(2, 4, 6, 8));
        assertEquals(p1, p2, "Instances should be equal after setting identical data areas");
    }

    /**
     * Tests that subplot count differences are detected.
     */
    @Test
    public void testEqualsWithDifferentSubplotCounts() {
        PlotRenderingInfo p1 = createInfo();
        PlotRenderingInfo p2 = createInfo();
        
        p1.addSubplotInfo(new PlotRenderingInfo(null));
        assertNotEquals(p1, p2, "Instances should differ when subplot counts differ");
        
        p2.addSubplotInfo(new PlotRenderingInfo(null));
        assertEquals(p1, p2, "Instances should be equal when subplot counts match");
    }

    /**
     * Tests that subplot content differences are detected.
     */
    @Test
    public void testEqualsWithDifferentSubplotContents() {
        PlotRenderingInfo p1 = createInfoWithSubplots();
        PlotRenderingInfo p2 = createInfoWithSubplots();
        
        p1.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(p1, p2, "Instances should differ when subplot contents differ");
        
        p2.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(p1, p2, "Instances should be equal when subplot contents match");
    }

    /**
     * Tests that cloning creates an independent copy with same values.
     */
    @Test
    public void testCloningCreatesIndependentCopy() throws CloneNotSupportedException {
        // Setup original instance
        PlotRenderingInfo original = createInfo();
        original.setPlotArea(new Rectangle2D.Double(1, 2, 3, 4));
        original.setDataArea(new Rectangle2D.Double(5, 6, 7, 8));
        original.addSubplotInfo(createInfo());

        // Verify clone matches original
        PlotRenderingInfo clone = CloneUtils.clone(original);
        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");

        // Verify independence by modifying fields
        original.getPlotArea().setRect(10, 20, 30, 40);
        assertNotEquals(original, clone, "Clone should not match after modifying original's plot area");
        
        clone.getPlotArea().setRect(10, 20, 30, 40);
        assertEquals(original, clone, "Should match after same modification to clone's plot area");
        
        original.getDataArea().setRect(50, 60, 70, 80);
        assertNotEquals(original, clone, "Clone should not match after modifying original's data area");
    }

    /**
     * Tests that serialization preserves object equality.
     */
    @Test
    public void testSerializationPreservesEquality() {
        PlotRenderingInfo original = createInfo();
        original.setPlotArea(new Rectangle(1, 2, 3, 4));
        original.setDataArea(new Rectangle(5, 6, 7, 8));
        original.addSubplotInfo(createInfo());
        
        PlotRenderingInfo deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should match original");
    }
}