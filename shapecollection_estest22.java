package org.locationtech.spatial4j.shape;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link ShapeCollection} class.
 * Note: The original test class name and inheritance from scaffolding suggest
 * it was auto-generated. This version uses a more conventional name and structure.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that the static method computeMutualDisjoint throws a
     * NullPointerException when passed a null list.
     */
    @Test
    public void computeMutualDisjoint_withNullList_throwsNullPointerException() {
        // The method under test is expected to fail fast with a NullPointerException
        // when its input list is null, as it does not perform a null check.
        assertThrows(NullPointerException.class, () -> {
            ShapeCollection.computeMutualDisjoint(null);
        });
    }
}