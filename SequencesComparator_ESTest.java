package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ConcurrentModificationException;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;

/**
 * Test suite for SequencesComparator class.
 * Tests the comparison of two sequences and generation of edit scripts.
 */
public class SequencesComparatorTest {

    // Test data constants for better readability
    private static final String ITEM_A = "A";
    private static final String ITEM_B = "B";
    private static final String ITEM_C = "C";
    private static final Integer NUMBER_1 = 1;
    private static final Integer NUMBER_2 = 2;

    @Test
    public void shouldReturnZeroModificationsForIdenticalSequences() {
        // Given: Two identical empty sequences
        List<String> sequence1 = new LinkedList<>();
        List<String> sequence2 = new LinkedList<>();
        
        // When: Comparing the sequences
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> editScript = comparator.getScript();
        
        // Then: No modifications should be needed
        assertEquals("Identical sequences should require no modifications", 
                     0, editScript.getModifications());
    }

    @Test
    public void shouldReturnZeroModificationsForSameSequenceReference() {
        // Given: A sequence compared with itself
        List<String> sequence = createSequence(ITEM_A, ITEM_B);
        
        // When: Comparing the sequence with itself
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence, sequence);
        EditScript<String> editScript = comparator.getScript();
        
        // Then: No modifications should be needed
        assertEquals("Same sequence reference should require no modifications", 
                     0, editScript.getModifications());
    }

    @Test
    public void shouldCalculateCorrectModificationsForDifferentSequences() {
        // Given: Two sequences with some common and different elements
        List<String> originalSequence = createSequence(ITEM_A, ITEM_B, ITEM_C);
        List<String> targetSequence = createSequence(ITEM_A, ITEM_B);
        
        // When: Comparing the sequences
        SequencesComparator<String> comparator = new SequencesComparator<>(originalSequence, targetSequence);
        EditScript<String> editScript = comparator.getScript();
        
        // Then: Should require one modification (delete C)
        assertEquals("Should require one deletion", 1, editScript.getModifications());
    }

    @Test
    public void shouldHandleSequencesWithDuplicateElements() {
        // Given: Sequences with duplicate elements
        List<String> sequence1 = createSequence(ITEM_A, ITEM_A, ITEM_B);
        List<String> sequence2 = createSequence(ITEM_A, ITEM_A);
        
        // When: Comparing the sequences
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> editScript = comparator.getScript();
        
        // Then: Should require one modification (delete B)
        assertEquals("Should handle duplicate elements correctly", 1, editScript.getModifications());
    }

    @Test
    public void shouldWorkWithCustomEquator() {
        // Given: A sequence with null elements and a custom equator
        List<String> sequenceWithNull = new LinkedList<>();
        sequenceWithNull.add(null);
        sequenceWithNull.add(ITEM_A);
        
        Equator<String> customEquator = DefaultEquator.defaultEquator();
        
        // When: Comparing with custom equator
        SequencesComparator<String> comparator = new SequencesComparator<>(
            sequenceWithNull, sequenceWithNull, customEquator);
        EditScript<String> editScript = comparator.getScript();
        
        // Then: Should work correctly with custom equator
        assertEquals("Custom equator should work correctly", 0, editScript.getModifications());
    }

    @Test
    public void shouldCalculateModificationsForComplexSequences() {
        // Given: Two complex sequences with mixed types
        List<Object> sequence1 = new LinkedList<>();
        List<Object> sequence2 = new LinkedList<>();
        
        Object sharedObject = new Object();
        sequence1.addAll(Arrays.asList(sharedObject, NUMBER_1, NUMBER_2));
        sequence2.addAll(Arrays.asList(new Object(), sharedObject, NUMBER_1));
        
        // When: Comparing complex sequences
        SequencesComparator<Object> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Object> editScript = comparator.getScript();
        
        // Then: Should calculate appropriate number of modifications
        assertTrue("Should require some modifications for different sequences", 
                   editScript.getModifications() > 0);
    }

    // Exception handling tests
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullSequences() {
        // When: Creating comparator with null sequences
        // Then: Should throw NullPointerException
        new SequencesComparator<>(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullEquator() {
        // Given: Valid sequences but null equator
        List<String> sequence = createSequence(ITEM_A);
        
        // When: Creating comparator with null equator and getting script
        // Then: Should throw NullPointerException
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence, sequence, null);
        comparator.getScript();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void shouldThrowExceptionForConcurrentModification() {
        // Given: A sequence and its sublist
        LinkedList<Object> originalList = new LinkedList<>();
        List<Object> subList = originalList.subList(0, 0);
        
        // Modify original list after creating sublist
        originalList.add(new Object());
        
        // When: Creating comparator with modified sublist
        // Then: Should throw ConcurrentModificationException
        new SequencesComparator<>(originalList, subList);
    }

    @Test(expected = StackOverflowError.class)
    public void shouldHandleCircularReferences() {
        // Given: Lists with circular references
        LinkedList<Object> list1 = new LinkedList<>();
        LinkedList<Object> list2 = new LinkedList<>();
        
        list1.add(list2);
        list2.add(list1);
        
        // When: Comparing lists with circular references
        // Then: Should throw StackOverflowError due to infinite recursion in equals()
        SequencesComparator<Object> comparator = new SequencesComparator<>(list1, list2);
        comparator.getScript();
    }

    // Helper methods for better test readability
    
    private List<String> createSequence(String... items) {
        return new LinkedList<>(Arrays.asList(items));
    }
    
    private List<Object> createObjectSequence(Object... items) {
        return new LinkedList<>(Arrays.asList(items));
    }
}