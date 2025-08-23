package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on the
 * {@code hashCode()} method.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that the hashCode() method adheres to the general contract for
     * {@link Object#hashCode()}. Specifically, it checks that two equal objects
     * produce the same hash code, and that the hash code is consistent across
     * multiple invocations.
     */
    @Test
    public void testHashCodeContract() {
        // Arrange: Create two separate, but identical, generator instances.
        // According to the default implementation, they should be equal.
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Assert:
        // 1. The two instances should be equal. This is a prerequisite for
        //    checking the hash code contract.
        assertTrue("Two default instances should be considered equal.",
                generator1.equals(generator2));

        // 2. Equal objects must have the same hash code.
        int hashCode1 = generator1.hashCode();
        int hashCode2 = generator2.hashCode();
        assertEquals("Equal objects must return the same hash code.",
                hashCode1, hashCode2);

        // 3. The hash code must be consistent when called multiple times on the same object.
        assertEquals("hashCode() should be consistent across multiple calls.",
                hashCode1, generator1.hashCode());
    }
}