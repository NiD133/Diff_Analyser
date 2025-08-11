package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SymbolicXYItemLabelGenerator}.
 * 
 * Focus: object semantics (equals, hashCode), cloning, and serialization.
 */
@DisplayName("SymbolicXYItemLabelGenerator")
public class SymbolicXYItemLabelGeneratorTest {

    // Factory method to emphasize "default instance" intent and reduce duplication.
    private static SymbolicXYItemLabelGenerator newDefaultGenerator() {
        return new SymbolicXYItemLabelGenerator();
    }

    @Test
    @DisplayName("equals(): two default instances are equal (symmetry)")
    public void equals_twoDefaultInstances_areEqualAndSymmetric() {
        SymbolicXYItemLabelGenerator g1 = newDefaultGenerator();
        SymbolicXYItemLabelGenerator g2 = newDefaultGenerator();

        assertEquals(g1, g2, "Two default generators should be equal");
        assertEquals(g2, g1, "Equals should be symmetric");
    }

    @Test
    @DisplayName("hashCode(): equal instances produce the same hash")
    public void hashCode_equalInstances_produceSameHash() {
        SymbolicXYItemLabelGenerator g1 = newDefaultGenerator();
        SymbolicXYItemLabelGenerator g2 = newDefaultGenerator();

        assertEquals(g1, g2, "Precondition: instances must be equal");
        assertEquals(g1.hashCode(), g2.hashCode(),
                "Equal instances must have the same hashCode");
    }

    @Test
    @DisplayName("clone(): cloned instance is distinct but equal")
    public void clone_createsDistinctButEqualInstance() throws CloneNotSupportedException {
        SymbolicXYItemLabelGenerator original = newDefaultGenerator();
        SymbolicXYItemLabelGenerator clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different object reference");
        assertSame(original.getClass(), clone.getClass(), "Clone should have the same runtime type");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    @Test
    @DisplayName("Implements PublicCloneable")
    public void implementsPublicCloneable() {
        SymbolicXYItemLabelGenerator generator = newDefaultGenerator();
        assertTrue(generator instanceof PublicCloneable,
                "Default generator should implement PublicCloneable");
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void serialization_roundTrip_preservesEquality() {
        SymbolicXYItemLabelGenerator original = newDefaultGenerator();
        SymbolicXYItemLabelGenerator restored = TestUtils.serialised(original);

        assertEquals(original, restored,
                "Serialized-then-deserialized instance should be equal to the original");
    }
}