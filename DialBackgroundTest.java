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

    // Test data constants for better maintainability
    private static final Color PRIMARY_COLOR = Color.RED;
    private static final Color SECONDARY_COLOR = Color.YELLOW;
    private static final Color ALTERNATE_COLOR = Color.GREEN;
    
    private static final GradientPaint TEST_GRADIENT = new GradientPaint(
        1.0f, 2.0f, PRIMARY_COLOR, 
        3.0f, 4.0f, SECONDARY_COLOR
    );
    
    private static final GradientPaint ALTERNATE_GRADIENT = new GradientPaint(
        1.0f, 2.0f, PRIMARY_COLOR, 
        3.0f, 4.0f, ALTERNATE_COLOR
    );
    
    private static final StandardGradientPaintTransformer TEST_TRANSFORMER = 
        new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);

    /**
     * Tests that the equals method correctly distinguishes between different DialBackground configurations.
     * This ensures that two backgrounds are equal only when all their properties match.
     */
    @Test
    public void testEquals() {
        // Given: Two identical default DialBackground instances
        DialBackground defaultBackground1 = new DialBackground();
        DialBackground defaultBackground2 = new DialBackground();
        
        // Then: They should be equal
        assertEquals(defaultBackground1, defaultBackground2, 
            "Two default DialBackground instances should be equal");

        // When: First background gets a gradient paint
        defaultBackground1.setPaint(TEST_GRADIENT);
        
        // Then: They should no longer be equal
        assertNotEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with different paints should not be equal");
        
        // When: Second background gets the same gradient paint
        defaultBackground2.setPaint(TEST_GRADIENT);
        
        // Then: They should be equal again
        assertEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with identical gradient paints should be equal");

        // When: First background gets a gradient paint transformer
        defaultBackground1.setGradientPaintTransformer(TEST_TRANSFORMER);
        
        // Then: They should not be equal
        assertNotEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with different gradient transformers should not be equal");
        
        // When: Second background gets the same transformer
        defaultBackground2.setGradientPaintTransformer(TEST_TRANSFORMER);
        
        // Then: They should be equal again
        assertEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with identical gradient transformers should be equal");

        // When: Testing inherited attribute (visibility)
        defaultBackground1.setVisible(false);
        
        // Then: They should not be equal
        assertNotEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with different visibility should not be equal");
        
        // When: Second background also becomes invisible
        defaultBackground2.setVisible(false);
        
        // Then: They should be equal again
        assertEquals(defaultBackground1, defaultBackground2, 
            "DialBackgrounds with identical visibility should be equal");
    }

    /**
     * Tests that equal DialBackground objects produce the same hash code.
     * This is required by the Java contract for hashCode and equals.
     */
    @Test
    public void testHashCode() {
        // Given: Two DialBackground instances with the same color
        DialBackground background1 = new DialBackground(PRIMARY_COLOR);
        DialBackground background2 = new DialBackground(PRIMARY_COLOR);
        
        // When: They are equal
        assertEquals(background1, background2, 
            "Backgrounds with same color should be equal");
        
        // Then: Their hash codes should also be equal
        int hashCode1 = background1.hashCode();
        int hashCode2 = background2.hashCode();
        assertEquals(hashCode1, hashCode2, 
            "Equal DialBackground objects must have equal hash codes");
    }

    /**
     * Tests that DialBackground objects can be properly cloned, ensuring:
     * 1. The clone is a different object instance
     * 2. The clone has the same class
     * 3. The clone is equal to the original
     * 4. The clone has independent event listeners
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        testCloningOfDefaultInstance();
        testCloningOfCustomizedInstance();
        testCloneHasIndependentListeners();
    }
    
    private void testCloningOfDefaultInstance() throws CloneNotSupportedException {
        // Given: A default DialBackground instance
        DialBackground originalBackground = new DialBackground();
        
        // When: Cloning the background
        DialBackground clonedBackground = CloneUtils.clone(originalBackground);
        
        // Then: Verify clone properties
        assertNotSame(originalBackground, clonedBackground, 
            "Clone should be a different object instance");
        assertSame(originalBackground.getClass(), clonedBackground.getClass(), 
            "Clone should have the same class as original");
        assertEquals(originalBackground, clonedBackground, 
            "Clone should be equal to the original");
    }
    
    private void testCloningOfCustomizedInstance() throws CloneNotSupportedException {
        // Given: A customized DialBackground instance
        DialBackground originalBackground = createCustomizedBackground();
        
        // When: Cloning the customized background
        DialBackground clonedBackground = (DialBackground) originalBackground.clone();
        
        // Then: Verify clone properties
        assertNotSame(originalBackground, clonedBackground, 
            "Clone should be a different object instance");
        assertSame(originalBackground.getClass(), clonedBackground.getClass(), 
            "Clone should have the same class as original");
        assertEquals(originalBackground, clonedBackground, 
            "Clone should be equal to the original");
    }
    
    private void testCloneHasIndependentListeners() throws CloneNotSupportedException {
        // Given: An original background and its clone
        DialBackground originalBackground = new DialBackground();
        DialBackground clonedBackground = CloneUtils.clone(originalBackground);
        
        // When: Adding a listener to the original
        MyDialLayerChangeListener testListener = new MyDialLayerChangeListener();
        originalBackground.addChangeListener(testListener);
        
        // Then: Original should have the listener, clone should not
        assertTrue(originalBackground.hasListener(testListener), 
            "Original should have the added listener");
        assertFalse(clonedBackground.hasListener(testListener), 
            "Clone should have independent listener list");
    }

    /**
     * Tests that DialBackground objects can be serialized and deserialized correctly,
     * maintaining equality between the original and restored objects.
     */
    @Test
    public void testSerialization() {
        testSerializationOfDefaultInstance();
        testSerializationOfCustomizedInstance();
    }
    
    private void testSerializationOfDefaultInstance() {
        // Given: A default DialBackground instance
        DialBackground originalBackground = new DialBackground();
        
        // When: Serializing and deserializing
        DialBackground deserializedBackground = TestUtils.serialised(originalBackground);
        
        // Then: Deserialized object should equal the original
        assertEquals(originalBackground, deserializedBackground, 
            "Deserialized default DialBackground should equal the original");
    }
    
    private void testSerializationOfCustomizedInstance() {
        // Given: A customized DialBackground instance
        DialBackground originalBackground = createCustomizedBackground();
        
        // When: Serializing and deserializing
        DialBackground deserializedBackground = TestUtils.serialised(originalBackground);
        
        // Then: Deserialized object should equal the original
        assertEquals(originalBackground, deserializedBackground, 
            "Deserialized customized DialBackground should equal the original");
    }
    
    /**
     * Creates a DialBackground with custom paint and gradient transformer for testing.
     * 
     * @return A customized DialBackground instance
     */
    private DialBackground createCustomizedBackground() {
        DialBackground background = new DialBackground();
        background.setPaint(ALTERNATE_GRADIENT);
        background.setGradientPaintTransformer(TEST_TRANSFORMER);
        return background;
    }
}