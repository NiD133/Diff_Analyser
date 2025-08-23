package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test case provides an understandable and robust test for the 
 * hashCode() method of the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that two equal, empty collections produce the same hash code.
     * This test ensures the class adheres to the Object.hashCode() contract,
     * which states that if two objects are equal, their hash codes must also be equal.
     */
    @Test
    public void hashCode_whenCollectionsAreEqual_shouldBeEqual() {
        // Arrange: Create two separate but identical (empty) collections.
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();

        // Act & Assert:
        // First, confirm the precondition that two new empty collections are, in fact, equal.
        assertEquals("Precondition: Two new empty collections should be equal.",
                collection1, collection2);

        // Now, assert that their hash codes are also equal, fulfilling the contract.
        assertEquals("Equal objects must have equal hash codes.",
                collection1.hashCode(), collection2.hashCode());
    }
}