package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that two default instances of SymbolicXYItemLabelGenerator are equal.
     */
    @Test
    public void testEquals() {
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();
        assertEquals(generator1, generator2, "Two default instances should be equal.");
        assertEquals(generator2, generator1, "Equality should be symmetric.");
    }

    /**
     * Ensures that the hashCode method is consistent with equals.
     */
    @Test
    public void testHashCode() {
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();
        assertEquals(generator1, generator2, "Two default instances should be equal.");
        assertEquals(generator1.hashCode(), generator2.hashCode(), "Equal instances should have the same hash code.");
    }

    /**
     * Confirms that cloning a SymbolicXYItemLabelGenerator creates a distinct but equal object.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator clone = CloneUtils.clone(original);
        assertNotSame(original, clone, "Cloned instance should be a different object.");
        assertSame(original.getClass(), clone.getClass(), "Cloned instance should be of the same class.");
        assertEquals(original, clone, "Cloned instance should be equal to the original.");
    }

    /**
     * Verifies that SymbolicXYItemLabelGenerator implements the PublicCloneable interface.
     */
    @Test
    public void testPublicCloneable() {
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        assertTrue(generator instanceof PublicCloneable, "Instance should implement PublicCloneable.");
    }

    /**
     * Tests serialization and deserialization of a SymbolicXYItemLabelGenerator instance.
     */
    @Test
    public void testSerialization() {
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original.");
    }
}