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
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Tests that the equals method correctly distinguishes between different
     * instances of DialBackground based on their properties.
     */
    @Test
    public void testEquals() {
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();
        assertEquals(background1, background2, "Default instances should be equal");

        // Test paint property
        GradientPaint paint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.YELLOW);
        background1.setPaint(paint);
        assertNotEquals(background1, background2, "Instances should not be equal after changing paint");
        background2.setPaint(paint);
        assertEquals(background1, background2, "Instances should be equal after setting same paint");

        // Test gradient paint transformer property
        StandardGradientPaintTransformer transformer = new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL);
        background1.setGradientPaintTransformer(transformer);
        assertNotEquals(background1, background2, "Instances should not be equal after changing transformer");
        background2.setGradientPaintTransformer(transformer);
        assertEquals(background1, background2, "Instances should be equal after setting same transformer");

        // Test visibility property (inherited attribute)
        background1.setVisible(false);
        assertNotEquals(background1, background2, "Instances should not be equal after changing visibility");
        background2.setVisible(false);
        assertEquals(background1, background2, "Instances should be equal after setting same visibility");
    }

    /**
     * Tests that two equal DialBackground instances have the same hash code.
     */
    @Test
    public void testHashCode() {
        DialBackground background1 = new DialBackground(Color.RED);
        DialBackground background2 = new DialBackground(Color.RED);
        assertEquals(background1, background2, "Instances should be equal");
        assertEquals(background1.hashCode(), background2.hashCode(), "Equal instances should have same hash code");
    }

    /**
     * Tests that cloning a DialBackground instance results in a new instance
     * that is equal but not the same (i.e., a deep copy).
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Test default instance cloning
        DialBackground original = new DialBackground();
        DialBackground clone = CloneUtils.clone(original);
        assertNotSame(original, clone, "Clone should be a different instance");
        assertEquals(original, clone, "Clone should be equal to the original");

        // Test cloning of a customized instance
        original.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
        original.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL));
        clone = (DialBackground) original.clone();
        assertNotSame(original, clone, "Clone should be a different instance");
        assertEquals(original, clone, "Clone should be equal to the original");

        // Ensure listener lists are independent
        MyDialLayerChangeListener listener = new MyDialLayerChangeListener();
        original.addChangeListener(listener);
        assertTrue(original.hasListener(listener), "Original should have the listener");
        assertFalse(clone.hasListener(listener), "Clone should not have the listener");
    }

    /**
     * Tests that a DialBackground instance can be serialized and deserialized
     * correctly, maintaining its state.
     */
    @Test
    public void testSerialization() {
        // Test serialization of a default instance
        DialBackground original = new DialBackground();
        DialBackground deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");

        // Test serialization of a customized instance
        original.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
        original.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL));
        deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");
    }
}