package org.apache.commons.collections4.set;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the behavior of an empty CompositeSet.
 * It replaces a single, complex auto-generated test with a series of focused,
 * easy-to-understand test cases.
 */
public class CompositeSetEmptySetBehaviorTest {

    private CompositeSet<Integer> emptyCompositeSet;

    @Before
    public void setUp() {
        // Arrange: Create a new, empty CompositeSet before each test.
        emptyCompositeSet = new CompositeSet<>();
    }



    @Test
    public void shouldBeEmptyAndHaveSizeZeroWhenCreated() {
        // Assert: A newly created CompositeSet should be empty.
        assertTrue("A new CompositeSet should be empty", emptyCompositeSet.isEmpty());
        assertEquals("A new CompositeSet should have size 0", 0, emptyCompositeSet.size());
    }

    @Test
    public void toSetOnEmptySetShouldReturnEmptySet() {
        // Act: Convert the empty CompositeSet to a standard Set.
        Set<Integer> resultSet = emptyCompositeSet.toSet();

        // Assert: The resulting set should also be empty.
        assertNotNull("toSet() should not return null", resultSet);
        assertTrue("The returned set should be empty", resultSet.isEmpty());
    }

    @Test
    public void toArrayOnEmptySetShouldReturnEmptyArray() {
        // Act: Convert the empty CompositeSet to an object array.
        Object[] resultArray = emptyCompositeSet.toArray();

        // Assert: The resulting array should be empty.
        assertNotNull("toArray() should not return null", resultArray);
        assertEquals("The returned array should be empty", 0, resultArray.length);
    }

    @Test
    public void toArrayWithSizedArgumentOnEmptySetShouldReturnSameArrayWithFirstElementNull() {
        // Arrange: Create a pre-allocated array with existing elements.
        Integer[] preallocatedArray = {1, 2, 3};

        // Act: Call toArray with the pre-allocated array.
        Integer[] resultArray = emptyCompositeSet.toArray(preallocatedArray);

        // Assert: The method should return the same array instance, with the element
        // at index 0 set to null, as per the Collection.toArray(T[]) contract.
        assertSame("Should return the same array instance", preallocatedArray, resultArray);
        assertNull("The element at index 0 should be set to null", resultArray[0]);
        assertEquals("The element at index 1 should be unchanged", Integer.valueOf(2), resultArray[1]);
        assertEquals("The element at index 2 should be unchanged", Integer.valueOf(3), resultArray[2]);
    }

    @Test
    public void containsAllWithEmptyCollectionShouldReturnTrue() {
        // Arrange: An empty collection to check against.
        Collection<Integer> emptyCollection = Collections.emptyList();

        // Act & Assert: An empty set is considered to contain all elements of another empty collection.
        assertTrue("containsAll with an empty collection should return true",
                   emptyCompositeSet.containsAll(emptyCollection));
    }

    @Test
    public void removeAllWithEmptyCollectionShouldReturnFalse() {
        // Arrange: An empty collection to remove.
        Collection<Integer> emptyCollection = Collections.emptyList();

        // Act: Attempt to remove the elements of the empty collection.
        boolean wasModified = emptyCompositeSet.removeAll(emptyCollection);

        // Assert: The set was not modified, so the method should return false.
        assertFalse("removeAll with an empty collection should return false as the set is not modified", wasModified);
    }

    @Test
    public void hashCodeOnEmptySetShouldReturnZero() {
        // Act & Assert: The hashCode of an empty set is defined to be 0.
        assertEquals("The hashCode of an empty CompositeSet should be 0", 0, emptyCompositeSet.hashCode());
    }
}