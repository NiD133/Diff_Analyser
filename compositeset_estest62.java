package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;

/**
 * This class contains test cases for the {@link CompositeSet#equals(Object)} method.
 */
public class CompositeSetEqualityTest {

    @Test
    public void equals_shouldReturnTrue_whenComparingTwoEmptyCompositeSets() {
        // Arrange
        // Create an empty CompositeSet using the default constructor.
        final CompositeSet<String> set1 = new CompositeSet<>();

        // Create another empty CompositeSet by composing two empty component sets.
        // This set should also be considered empty and thus equal to the first one.
        final CompositeSet<String> set2 = new CompositeSet<>(new HashSet<>(), new HashSet<>());

        // Act & Assert
        // According to the Set contract, two sets are equal if they have the same size
        // and contain the same elements. Both sets are empty, so they should be equal.
        assertEquals(set1, set2);

        // For completeness, verify that the hashCode contract is met and equality is symmetric.
        assertEquals("Equal objects must have equal hash codes", set1.hashCode(), set2.hashCode());
        assertEquals("Equality must be symmetric", set2, set1);
    }
}