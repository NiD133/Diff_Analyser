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
 * -----------------------
 * DialBackgroundTest.java
 * -----------------------
 * (C) Copyright 2006-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.plot.dial;

import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.chart.internal.CloneUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    // Factory methods for creating test instances ==============================
    
    private DialBackground createDefaultDialBackground() {
        return new DialBackground();
    }
    
    private DialBackground createCustomDialBackground() {
        DialBackground background = new DialBackground();
        background.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
        background.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL));
        return background;
    }
    
    // Test cases =============================================================
    
    @Test
    public void identicalDefaultInstancesShouldBeEqual() {
        DialBackground bg1 = createDefaultDialBackground();
        DialBackground bg2 = createDefaultDialBackground();
        assertEquals(bg1, bg2, "Default instances should be equal");
    }
    
    @Test
    public void paintChangesShouldBreakEquality() {
        DialBackground bg1 = createDefaultDialBackground();
        DialBackground bg2 = createDefaultDialBackground();
        
        bg1.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
        assertNotEquals(bg1, bg2, "Instances should differ after paint change");
        
        bg2.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW));
        assertEquals(bg1, bg2, "Instances should be equal after same paint change");
    }
    
    @Test
    public void gradientTransformerChangesShouldBreakEquality() {
        DialBackground bg1 = createCustomDialBackground();
        DialBackground bg2 = createCustomDialBackground();
        
        bg1.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertNotEquals(bg1, bg2, "Instances should differ after transformer change");
        
        bg2.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertEquals(bg1, bg2, "Instances should be equal after same transformer change");
    }
    
    @Test
    public void visibilityChangesShouldBreakEquality() {
        DialBackground bg1 = createDefaultDialBackground();
        DialBackground bg2 = createDefaultDialBackground();
        
        bg1.setVisible(false);
        assertNotEquals(bg1, bg2, "Instances should differ after visibility change");
        
        bg2.setVisible(false);
        assertEquals(bg1, bg2, "Instances should be equal after same visibility change");
    }
    
    @Test
    public void hashCodesShouldBeEqualForEqualObjects() {
        DialBackground bg1 = new DialBackground(Color.RED);
        DialBackground bg2 = new DialBackground(Color.RED);
        
        assertEquals(bg1, bg2, "Instances must be equal for hashcode comparison");
        assertEquals(bg1.hashCode(), bg2.hashCode(), "Equal objects must have equal hashcodes");
    }
    
    @Test
    public void defaultInstanceCloningShouldCreateEqualCopy() throws CloneNotSupportedException {
        DialBackground original = createDefaultDialBackground();
        DialBackground clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }
    
    @Test
    public void customInstanceCloningShouldCreateEqualCopy() throws CloneNotSupportedException {
        DialBackground original = createCustomDialBackground();
        DialBackground clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Clone should have same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }
    
    @Test
    public void cloneShouldHaveIndependentListenerList() throws CloneNotSupportedException {
        DialBackground original = createDefaultDialBackground();
        DialBackground clone = CloneUtils.clone(original);
        
        MyDialLayerChangeListener listener = new MyDialLayerChangeListener();
        original.addChangeListener(listener);
        
        assertTrue(original.hasListener(listener), "Original should have listener");
        assertFalse(clone.hasListener(listener), "Clone should not share listener");
    }
    
    @Test
    public void defaultInstanceSerializationShouldRoundtrip() {
        DialBackground original = createDefaultDialBackground();
        DialBackground deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should equal original");
    }
    
    @Test
    public void customInstanceSerializationShouldRoundtrip() {
        DialBackground original = createCustomDialBackground();
        DialBackground deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should equal original");
    }
}