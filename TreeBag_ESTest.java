package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TreeBag_ESTest extends TreeBag_ESTest_scaffolding {

    // ===== Constructor Tests =====
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testConstructor_WithNullIterable_ThrowsNullPointerException() {
        new TreeBag<Object>((Iterable<?>) null);
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNonComparableElementInCollection_ThrowsIllegalArgumentException() {
        LinkedHashSet<Object> set = new LinkedHashSet<>();
        set.add(new Object());
        
        try {
            new TreeBag<Object>((Iterable<?>) set);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Objects without Comparable cannot be added to naturally ordered TreeBag
        }
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testConstructor_WithNullCollection_ThrowsNullPointerException() {
        new TreeBag<AbstractMapBag.MutableInteger>((Collection<? extends AbstractMapBag.MutableInteger>) null);
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNonComparablePredicate_ThrowsIllegalArgumentException() {
        HashBag<Predicate<Object>> bag = new HashBag<>();
        Predicate<Object> predicate = AnyPredicate.anyPredicate((Collection<? extends Predicate<? super Object>>) bag);
        bag.add(predicate);
        
        try {
            new TreeBag<Predicate<Object>>((Collection<? extends Predicate<Object>>) bag);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Non-comparable objects cannot be added to naturally ordered TreeBag
        }
    }

    // ===== add() Method Tests =====
    @Test(timeout = 4000)
    public void testAdd_WithCustomComparatorThatConsidersAllObjectsEqual_ReturnsDifferentBooleans() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null, (String) null).when(comparator).toString();
        doReturn(0, 0).when(comparator).compare(any(), any());
        
        TreeBag<Object> bag = new TreeBag<>(comparator);
        TreeBag<Locale.FilteringMode> element1 = new TreeBag<>();
        AbstractMapBag.MutableInteger element2 = new AbstractMapBag.MutableInteger(-45);
        
        boolean firstAddResult = bag.add(element1);
        boolean secondAddResult = bag.add(element2);
        
        assertNotEquals(firstAddResult, secondAddResult);
    }

    @Test(timeout = 4000)
    public void testAdd_NonComparableObjectToNaturalOrderedBag_ThrowsIllegalArgumentException() {
        TreeBag<Object> bag = new TreeBag<>();
        
        try {
            bag.add(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Non-comparable objects cannot be added to naturally ordered TreeBag
        }
    }

    @Test(timeout = 4000)
    public void testAdd_StringToBagInitializedWithEnumSet_ThrowsClassCastException() {
        EnumSet<Locale.Category> enumSet = EnumSet.of(Locale.Category.FORMAT);
        TreeBag<Object> bag = new TreeBag<>((Iterable<?>) enumSet);
        
        try {
            bag.add("InvalidType");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected: String cannot be cast to enum type
        }
    }

    @Test(timeout = 4000)
    public void testAdd_NullToNaturalOrderedBag_ThrowsNullPointerException() {
        TreeBag<Predicate<Object>> bag = new TreeBag<>();
        
        try {
            bag.add(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected: Null elements not allowed in naturally ordered TreeBag
        }
    }

    // ===== first() Method Tests =====
    @Test(timeout = 4000)
    public void testFirst_WithNullElementAndCustomComparator_ReturnsNull() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparator).toString();
        doReturn(0).when(comparator).compare(any(), any());
        
        TreeBag<AbstractMapBag.MutableInteger> bag = new TreeBag<>(comparator);
        bag.add(null);
        
        assertNull(bag.first());
    }

    @Test(timeout = 4000, expected = NoSuchElementException.class)
    public void testFirst_OnEmptyBag_ThrowsNoSuchElementException() {
        TreeBag<AbstractMapBag.MutableInteger> bag = new TreeBag<>();
        bag.first();
    }

    @Test(timeout = 4000)
    public void testFirst_WithEnumSet_ReturnsFirstElement() {
        EnumSet<Locale.Category> enumSet = EnumSet.of(Locale.Category.FORMAT);
        TreeBag<Locale.Category> bag = new TreeBag<>(enumSet);
        
        assertEquals(Locale.Category.FORMAT, bag.first());
    }

    // ===== last() Method Tests =====
    @Test(timeout = 4000)
    public void testLast_WithNullElementAndCustomComparator_ReturnsNull() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparator).toString();
        doReturn(0).when(comparator).compare(any(), any());
        
        TreeBag<Predicate<Object>> bag = new TreeBag<>(comparator);
        bag.add(null);
        
        assertNull(bag.last());
    }

    @Test(timeout = 4000)
    public void testLast_WithSingleElement_ReturnsSameElement() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>(null);
        Locale.FilteringMode element = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        bag.add(element);
        
        assertSame(element, bag.last());
    }

    @Test(timeout = 4000, expected = NoSuchElementException.class)
    public void testLast_OnEmptyBagWithCustomComparator_ThrowsNoSuchElementException() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<AbstractMapBag.MutableInteger> bag = new TreeBag<>(comparator);
        bag.last();
    }

    // ===== getMap() Method Tests =====
    @Test(timeout = 4000)
    public void testGetMap_OnEmptyBag_ReturnsEmptyMap() {
        TreeBag<Locale.Category> bag = new TreeBag<>();
        SortedMap<Locale.Category, AbstractMapBag.MutableInteger> map = bag.getMap();
        assertEquals(0, map.size());
    }

    @Test(timeout = 4000)
    public void testGetMap_AfterAddingElement_ReturnsNonEmptyMap() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>(null);
        bag.add(Locale.FilteringMode.IGNORE_EXTENDED_RANGES);
        SortedMap<Locale.FilteringMode, AbstractMapBag.MutableInteger> map = bag.getMap();
        assertFalse(map.isEmpty());
    }

    // ===== comparator() Method Tests =====
    @Test(timeout = 4000)
    public void testComparator_WhenCreatedFromCollection_ReturnsNull() {
        LinkedList<Boolean> list = new LinkedList<>();
        TreeBag<Boolean> bag = new TreeBag<>(list);
        assertNull(bag.comparator());
    }

    @Test(timeout = 4000)
    public void testComparator_WithCustomComparator_ReturnsSameComparator() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Object> bag = new TreeBag<>(comparator);
        assertSame(comparator, bag.comparator());
    }
}