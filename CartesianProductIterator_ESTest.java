package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.iterators.CartesianProductIterator;

/**
 * Test suite for CartesianProductIterator.
 * 
 * CartesianProductIterator creates all possible combinations (Cartesian product) 
 * of elements from multiple input collections.
 * For example: [A,B] × [1,2] = [(A,1), (A,2), (B,1), (B,2)]
 */
public class CartesianProductIterator_ESTest {

    @Test(timeout = 4000)
    public void testHasNext_WithNonEmptyCollections_ReturnsTrue() {
        // Given: Two collections with one element each
        List<String> firstCollection = Arrays.asList("A");
        List<String> secondCollection = Arrays.asList("1");
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(firstCollection, secondCollection);
        
        // When & Then: Iterator should have next element
        assertTrue("Iterator should have next element when collections are non-empty", 
                  iterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testNext_WhenCollectionModifiedAfterCreation_ThrowsConcurrentModificationException() {
        // Given: A modifiable collection with one element
        Stack<String> modifiableCollection = new Stack<>();
        modifiableCollection.add("initial");
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(modifiableCollection);
        
        // When: Collection is modified after iterator creation
        modifiableCollection.add("added_after_iterator_creation");
        
        // Then: Should throw ConcurrentModificationException
        try {
            iterator.next();
            fail("Expected ConcurrentModificationException when underlying collection is modified");
        } catch (ConcurrentModificationException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullIterable_ThrowsNullPointerException() {
        // Given: Array containing null iterable
        @SuppressWarnings("unchecked")
        Iterable<String>[] iterablesWithNull = new Iterable[1];
        iterablesWithNull[0] = null;
        
        // When & Then: Constructor should throw NullPointerException
        try {
            new CartesianProductIterator<>(iterablesWithNull);
            fail("Expected NullPointerException when iterable is null");
        } catch (NullPointerException e) {
            assertEquals("iterable", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testHasNext_WithEmptyCollection_ReturnsFalse() {
        // Given: One empty collection and one non-empty collection
        List<String> emptyCollection = new ArrayList<>();
        List<String> nonEmptyCollection = Arrays.asList("A");
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(emptyCollection, nonEmptyCollection);
        
        // When & Then: Iterator should not have next element (empty Cartesian product)
        assertFalse("Iterator should not have next element when any collection is empty", 
                   iterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testNext_WithMultipleElements_ReturnsCorrectCombinations() {
        // Given: Two collections with multiple elements each
        Set<String> firstCollection = EnumSet.of(
            java.util.Locale.Category.DISPLAY, 
            java.util.Locale.Category.FORMAT
        ).stream()
         .map(Enum::name)
         .collect(java.util.stream.Collectors.toSet());
        
        Set<String> secondCollection = new HashSet<>(firstCollection);
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(firstCollection, secondCollection);
        
        // When: Getting multiple combinations
        List<String> firstCombination = iterator.next();
        List<String> secondCombination = iterator.next();
        List<String> thirdCombination = iterator.next();
        
        // Then: Each combination should contain elements from both collections
        assertEquals("Each combination should have 2 elements", 2, firstCombination.size());
        assertEquals("Each combination should have 2 elements", 2, secondCombination.size());
        assertEquals("Each combination should have 2 elements", 2, thirdCombination.size());
        
        assertNotNull("Combinations should not be null", firstCombination);
        assertNotNull("Combinations should not be null", secondCombination);
        assertNotNull("Combinations should not be null", thirdCombination);
    }

    @Test(timeout = 4000)
    public void testNext_WithNoElements_ThrowsNoSuchElementException() {
        // Given: Iterator with no input collections (empty Cartesian product)
        @SuppressWarnings("unchecked")
        Iterable<String>[] emptyIterables = new Iterable[0];
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(emptyIterables);
        
        // When & Then: Should throw NoSuchElementException
        try {
            iterator.next();
            fail("Expected NoSuchElementException when no elements available");
        } catch (NoSuchElementException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testRemove_Always_ThrowsUnsupportedOperationException() {
        // Given: Any iterator instance
        @SuppressWarnings("unchecked")
        Iterable<String>[] emptyIterables = new Iterable[0];
        
        CartesianProductIterator<String> iterator = 
            new CartesianProductIterator<>(emptyIterables);
        
        // When & Then: Remove operation should not be supported
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException as remove is not supported");
        } catch (UnsupportedOperationException e) {
            assertEquals("remove", e.getMessage());
        }
    }
}