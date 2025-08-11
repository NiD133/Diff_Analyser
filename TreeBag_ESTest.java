package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.SortedMap;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.bag.AbstractMapBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.functors.AnyPredicate;

/**
 * Test suite for TreeBag functionality including construction, ordering, and edge cases.
 */
public class TreeBagTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void shouldCreateEmptyTreeBagWithNaturalOrdering() {
        TreeBag<Locale.Category> bag = new TreeBag<>();
        SortedMap<Locale.Category, AbstractMapBag.MutableInteger> map = bag.getMap();
        
        assertEquals(0, map.size());
        assertNull(bag.comparator()); // Natural ordering uses null comparator
    }
    
    @Test
    public void shouldCreateTreeBagWithCustomComparator() {
        Comparator<Object> customComparator = mock(Comparator.class);
        TreeBag<Object> bag = new TreeBag<>(customComparator);
        
        assertSame(customComparator, bag.comparator());
    }
    
    @Test
    public void shouldCreateTreeBagFromCollection() {
        LinkedList<Boolean> sourceList = new LinkedList<>();
        TreeBag<Boolean> bag = new TreeBag<>(sourceList);
        
        assertNull(bag.comparator()); // Uses natural ordering
    }
    
    @Test
    public void shouldCreateTreeBagFromEnumSet() {
        Locale.Category formatCategory = Locale.Category.FORMAT;
        EnumSet<Locale.Category> enumSet = EnumSet.of(formatCategory);
        
        TreeBag<Locale.Category> bag = new TreeBag<>(enumSet);
        
        assertEquals(1, bag.size());
        assertEquals(formatCategory, bag.first());
    }

    // ========== Adding Elements Tests ==========
    
    @Test
    public void shouldAddComparableElementsSuccessfully() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>();
        Locale.FilteringMode element = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        
        boolean added = bag.add(element);
        
        assertTrue(added);
        assertEquals(1, bag.size());
        assertFalse(bag.getMap().isEmpty());
    }
    
    @Test
    public void shouldAddMultipleElementsWithCustomComparator() {
        Comparator<Object> alwaysEqualComparator = mock(Comparator.class);
        when(alwaysEqualComparator.compare(any(), any())).thenReturn(0);
        
        TreeBag<Object> bag = new TreeBag<>(alwaysEqualComparator);
        TreeBag<Locale.FilteringMode> firstElement = new TreeBag<>();
        AbstractMapBag.MutableInteger secondElement = new AbstractMapBag.MutableInteger(-45);
        
        boolean firstAdded = bag.add(firstElement);
        boolean secondAdded = bag.add(secondElement);
        
        assertTrue(firstAdded);
        assertFalse(secondAdded); // Should be false due to comparator returning 0 (equal)
    }

    // ========== First/Last Element Tests ==========
    
    @Test
    public void shouldReturnFirstElementWhenBagContainsElements() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>();
        Locale.FilteringMode element = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        bag.add(element);
        
        Locale.FilteringMode first = bag.first();
        
        assertSame(element, first);
    }
    
    @Test
    public void shouldReturnLastElementWhenBagContainsElements() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>();
        Locale.FilteringMode element = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        bag.add(element);
        
        Locale.FilteringMode last = bag.last();
        
        assertSame(element, last);
    }
    
    @Test
    public void shouldReturnNullElementsWithCustomComparator() {
        Comparator<Object> nullToleratingComparator = mock(Comparator.class);
        when(nullToleratingComparator.compare(any(), any())).thenReturn(0);
        
        TreeBag<Predicate<Object>> bag = new TreeBag<>(nullToleratingComparator);
        bag.add(null);
        
        assertNull(bag.last());
        
        TreeBag<AbstractMapBag.MutableInteger> integerBag = new TreeBag<>(nullToleratingComparator);
        integerBag.add(null);
        
        assertNull(integerBag.first());
    }

    // ========== Exception Tests ==========
    
    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenGettingFirstFromEmptyBag() {
        TreeBag<AbstractMapBag.MutableInteger> emptyBag = new TreeBag<>();
        emptyBag.first();
    }
    
    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenGettingLastFromEmptyBag() {
        Comparator<Object> comparator = mock(Comparator.class);
        TreeBag<AbstractMapBag.MutableInteger> emptyBag = new TreeBag<>(comparator);
        emptyBag.last();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAddingNonComparableObjectWithNaturalOrdering() {
        TreeBag<Object> bag = new TreeBag<>();
        Object nonComparableObject = new Object();
        
        bag.add(nonComparableObject);
    }
    
    @Test(expected = ClassCastException.class)
    public void shouldThrowExceptionWhenAddingIncompatibleType() {
        // Create bag with enum elements
        Locale.Category category = Locale.Category.FORMAT;
        EnumSet<Locale.Category> enumSet = EnumSet.of(category);
        TreeBag<Object> bag = new TreeBag<>(enumSet);
        
        // Try to add incompatible type
        bag.add("");
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenAddingNullWithNaturalOrdering() {
        TreeBag<Predicate<Object>> bag = new TreeBag<>();
        bag.add(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenConstructingWithNullIterable() {
        new TreeBag<>((Iterable<?>) null);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenConstructingWithNullCollection() {
        new TreeBag<>((Collection<? extends AbstractMapBag.MutableInteger>) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConstructingWithNonComparableElements() {
        LinkedHashSet<Object> setWithNonComparable = new LinkedHashSet<>();
        setWithNonComparable.add(new Object());
        
        new TreeBag<>(setWithNonComparable);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConstructingFromBagWithNonComparablePredicates() {
        HashBag<Predicate<Object>> sourceBag = new HashBag<>();
        Predicate<Object> nonComparablePredicate = AnyPredicate.anyPredicate(sourceBag);
        sourceBag.add(nonComparablePredicate);
        
        new TreeBag<>(sourceBag);
    }
}