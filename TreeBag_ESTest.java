package org.apache.commons.collections4.bag;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.apache.commons.collections4.bag.AbstractMapBag.MutableInteger;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class TreeBag_ESTest extends TreeBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAddNullPredicateWithComparator() throws Throwable {
        // Create a mock comparator that always returns 0 for comparisons
        Comparator<Object> mockComparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(mockComparator).toString();
        doReturn(0).when(mockComparator).compare(any(), any());

        // Create a TreeBag with the mock comparator and add a null Predicate
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(mockComparator);
        treeBag.add(null);

        // Assert that the last element is null
        Predicate<Object> lastPredicate = treeBag.last();
        assertNull(lastPredicate);
    }

    @Test(timeout = 4000)
    public void testAddLocaleFilteringMode() throws Throwable {
        // Create a TreeBag for Locale.FilteringMode without a comparator
        TreeBag<Locale.FilteringMode> treeBag = new TreeBag<>(null);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;

        // Add a Locale.FilteringMode and assert it's the last element
        treeBag.add(filteringMode);
        Locale.FilteringMode lastFilteringMode = treeBag.last();
        assertSame(lastFilteringMode, filteringMode);
    }

    @Test(timeout = 4000)
    public void testEmptySortedMap() throws Throwable {
        // Create an empty TreeBag for Locale.Category
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        SortedMap<Locale.Category, MutableInteger> sortedMap = treeBag.getMap();

        // Assert that the sorted map is empty
        assertEquals(0, sortedMap.size());
    }

    @Test(timeout = 4000)
    public void testNonEmptySortedMap() throws Throwable {
        // Create a TreeBag for Locale.FilteringMode without a comparator
        TreeBag<Locale.FilteringMode> treeBag = new TreeBag<>(null);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;

        // Add a Locale.FilteringMode and assert the sorted map is not empty
        treeBag.add(filteringMode);
        SortedMap<Locale.FilteringMode, MutableInteger> sortedMap = treeBag.getMap();
        assertFalse(sortedMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testAddNullMutableIntegerWithComparator() throws Throwable {
        // Create a mock comparator that always returns 0 for comparisons
        Comparator<Object> mockComparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(mockComparator).toString();
        doReturn(0).when(mockComparator).compare(any(), any());

        // Create a TreeBag with the mock comparator and add a null MutableInteger
        TreeBag<MutableInteger> treeBag = new TreeBag<>(mockComparator);
        treeBag.add(null);

        // Assert that the first element is null
        MutableInteger firstElement = treeBag.first();
        assertNull(firstElement);
    }

    @Test(timeout = 4000)
    public void testComparatorIsNull() throws Throwable {
        // Create a TreeBag from an empty LinkedList
        LinkedList<Boolean> linkedList = new LinkedList<>();
        TreeBag<Boolean> treeBag = new TreeBag<>(linkedList);

        // Assert that the comparator is null
        Comparator<? super Boolean> comparator = treeBag.comparator();
        assertNull(comparator);
    }

    @Test(timeout = 4000)
    public void testAddDifferentObjects() throws Throwable {
        // Create a mock comparator that always returns 0 for comparisons
        Comparator<Object> mockComparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null, (String) null).when(mockComparator).toString();
        doReturn(0, 0).when(mockComparator).compare(any(), any());

        // Create a TreeBag with the mock comparator and add different objects
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);
        TreeBag<Locale.FilteringMode> filteringModeTreeBag = new TreeBag<>();
        boolean addedFirst = treeBag.add(filteringModeTreeBag);

        MutableInteger mutableInteger = new MutableInteger(-45);
        boolean addedSecond = treeBag.add(mutableInteger);

        // Assert that the results of adding different objects are not the same
        assertFalse(addedFirst == addedSecond);
    }

    @Test(timeout = 4000)
    public void testFirstElementNoSuchElementException() throws Throwable {
        // Create an empty TreeBag for MutableInteger
        TreeBag<MutableInteger> treeBag = new TreeBag<>();

        // Assert that calling first() on an empty TreeBag throws NoSuchElementException
        try {
            treeBag.first();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddObjectIllegalArgumentException() throws Throwable {
        // Create an empty TreeBag for Object
        TreeBag<Object> treeBag = new TreeBag<>();
        Object object = new Object();

        // Assert that adding a non-Comparable object throws IllegalArgumentException
        try {
            treeBag.add(object);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bag.TreeBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddStringToEnumSetTreeBag() throws Throwable {
        // Create a TreeBag from an EnumSet
        Locale.Category category = Locale.Category.FORMAT;
        EnumSet<Locale.Category> enumSet = EnumSet.of(category, category);
        TreeBag<Object> treeBag = new TreeBag<>(enumSet);

        // Assert that adding a String to a TreeBag of Locale.Category throws ClassCastException
        try {
            treeBag.add("");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullIterableNullPointerException() throws Throwable {
        // Assert that creating a TreeBag with a null Iterable throws NullPointerException
        try {
            new TreeBag<Object>((Iterable<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bag.AbstractMapBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddObjectToLinkedHashSetTreeBag() throws Throwable {
        // Create a LinkedHashSet with one Object
        LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<>();
        Object object = new Object();
        linkedHashSet.add(object);

        // Assert that creating a TreeBag with a LinkedHashSet of non-Comparable objects throws IllegalArgumentException
        try {
            new TreeBag<Object>(linkedHashSet);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bag.TreeBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddNullPredicateNullPointerException() throws Throwable {
        // Create an empty TreeBag for Predicate<Object>
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>();

        // Assert that adding a null Predicate throws NullPointerException
        try {
            treeBag.add(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testComparatorIsNotNull() throws Throwable {
        // Create a mock comparator
        Comparator<Object> mockComparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(mockComparator).toString();

        // Create a TreeBag with the mock comparator
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);

        // Assert that the comparator is not null
        Comparator<? super Object> comparator = treeBag.comparator();
        assertNotNull(comparator);
    }

    @Test(timeout = 4000)
    public void testAddPredicateToHashBagTreeBag() throws Throwable {
        // Create a HashBag and add a Predicate
        HashBag<Predicate<Object>> hashBag = new HashBag<>();
        Predicate<Object> anyPredicate = AnyPredicate.anyPredicate(hashBag);
        hashBag.add(anyPredicate);

        // Assert that creating a TreeBag with a HashBag of non-Comparable objects throws IllegalArgumentException
        try {
            new TreeBag<Predicate<Object>>(hashBag);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.bag.TreeBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullCollectionNullPointerException() throws Throwable {
        // Assert that creating a TreeBag with a null Collection throws NullPointerException
        try {
            new TreeBag<MutableInteger>((Collection<? extends MutableInteger>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bag.AbstractMapBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testLastElementNoSuchElementException() throws Throwable {
        // Create a mock comparator
        Comparator<Object> mockComparator = mock(Comparator.class, new ViolatedAssumptionAnswer());

        // Create a TreeBag with the mock comparator
        TreeBag<MutableInteger> treeBag = new TreeBag<>(mockComparator);

        // Assert that calling last() on an empty TreeBag throws NoSuchElementException
        try {
            treeBag.last();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testFirstElementFromEnumSet() throws Throwable {
        // Create a TreeBag from an EnumSet
        Locale.Category category = Locale.Category.FORMAT;
        EnumSet<Locale.Category> enumSet = EnumSet.of(category, category);
        TreeBag<Locale.Category> treeBag = new TreeBag<>(enumSet);

        // Assert that the first element is Locale.Category.FORMAT
        Locale.Category firstCategory = treeBag.first();
        assertEquals(Locale.Category.FORMAT, firstCategory);
    }
}