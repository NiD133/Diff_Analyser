package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    /**
     * Tests the {@code equals()} method to ensure it correctly distinguishes all fields.
     */
    @Test
    public void testEquals() {
        // Create two default instances of DeviationRenderer
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();

        // Verify that two default instances are equal
        assertEquals(renderer1, renderer2);
        assertEquals(renderer2, renderer1);

        // Modify a field in one instance and verify inequality
        renderer1.setAlpha(0.1f);
        assertNotEquals(renderer1, renderer2);

        // Set the same field in the second instance and verify equality
        renderer2.setAlpha(0.1f);
        assertEquals(renderer1, renderer2);
    }

    /**
     * Tests that two equal objects have the same hash code.
     */
    @Test
    public void testHashcode() {
        // Create two default instances of DeviationRenderer
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();

        // Verify that two default instances are equal
        assertEquals(renderer1, renderer2);

        // Verify that their hash codes are equal
        assertEquals(renderer1.hashCode(), renderer2.hashCode());
    }

    /**
     * Tests that cloning a DeviationRenderer instance produces a distinct but equal object.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Create an instance of DeviationRenderer
        DeviationRenderer original = new DeviationRenderer();

        // Clone the instance
        DeviationRenderer clone = (DeviationRenderer) original.clone();

        // Verify that the clone is a distinct object but equal to the original
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    /**
     * Tests that the DeviationRenderer class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        // Create an instance of DeviationRenderer
        DeviationRenderer renderer = new DeviationRenderer();

        // Verify that it implements PublicCloneable
        assertTrue(renderer instanceof PublicCloneable);
    }

    /**
     * Tests serialization and deserialization of a DeviationRenderer instance.
     */
    @Test
    public void testSerialization() {
        // Create an instance of DeviationRenderer
        DeviationRenderer original = new DeviationRenderer();

        // Serialize and deserialize the instance
        DeviationRenderer deserialized = TestUtils.serialised(original);

        // Verify that the deserialized instance is equal to the original
        assertEquals(original, deserialized);
    }
}