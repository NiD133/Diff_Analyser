package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.CompactLinkedHashSet;
import com.google.common.collect.EmptyImmutableSetMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.Spliterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CompactLinkedHashSetTest extends CompactLinkedHashSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMoveLastEntry() {
        Integer[] integers = new Integer[2];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(integers);
        set.moveLastEntry(0, 0);
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testInsertEntry() {
        Integer[] integers = new Integer[7];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(integers);
        set.insertEntry(2, 1, 1, 2788);
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testInitAndContains() {
        Object[] objects = new Object[2];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(objects);
        set.init(0);
        assertFalse(set.contains(0));
    }

    @Test(timeout = 4000)
    public void testResizeEntries() {
        Object[] objects = new Object[1];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(objects);
        set.resizeEntries(41);
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testToArrayWithEmptyArray() {
        Integer[] integers = new Integer[0];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(integers);
        Integer[] resultArray = set.toArray(integers);
        assertSame(integers, resultArray);
    }

    @Test(timeout = 4000)
    public void testToArrayOnEmptySet() {
        CompactLinkedHashSet<Locale.Category> set = new CompactLinkedHashSet<>();
        Object[] resultArray = set.toArray();
        assertEquals(0, resultArray.length);
    }

    @Test(timeout = 4000)
    public void testSpliteratorNotNull() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(31);
        Spliterator<Integer> spliterator = set.spliterator();
        assertNotNull(spliterator);
    }

    @Test(timeout = 4000)
    public void testFirstEntryIndex() {
        Locale.Category[] categories = new Locale.Category[9];
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(categories);
        int firstIndex = set.firstEntryIndex();
        assertEquals(1, set.size());
        assertEquals(0, firstIndex);
    }

    @Test(timeout = 4000)
    public void testFirstEntryIndexOnEmptySet() {
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        int firstIndex = set.firstEntryIndex();
        assertEquals(-2, firstIndex);
    }

    @Test(timeout = 4000)
    public void testCreateFromEmptyCollection() {
        CompactLinkedHashSet<Locale.Category> set = new CompactLinkedHashSet<>();
        CompactLinkedHashSet<Object> newSet = CompactLinkedHashSet.create((Collection<?>) set);
        assertTrue(newSet.isEmpty());
    }

    @Test(timeout = 4000)
    public void testConvertToHashFloodingResistantImplementation() {
        CompactLinkedHashSet<Integer> set = new CompactLinkedHashSet<>(8232);
        Set<Integer> resultSet = set.convertToHashFloodingResistantImplementation();
        assertEquals(0, resultSet.size());
    }

    @Test(timeout = 4000)
    public void testAdjustAfterRemove() {
        CompactLinkedHashSet<Comparable<Object>> set = CompactLinkedHashSet.createWithExpectedSize(133);
        int adjustedIndex = set.adjustAfterRemove(1941, 0);
        assertEquals(0, adjustedIndex);
    }

    @Test(timeout = 4000)
    public void testAdjustAfterRemoveWithSameIndex() {
        Object[] objects = new Object[2];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(objects);
        int adjustedIndex = set.adjustAfterRemove(1941, 1941);
        assertEquals(1941, adjustedIndex);
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testAdjustAfterRemoveNegativeIndex() {
        Integer[] integers = new Integer[0];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(integers);
        int adjustedIndex = set.adjustAfterRemove(73, -1);
        assertEquals(-1, adjustedIndex);
    }

    @Test(timeout = 4000)
    public void testToArrayThrowsException() {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        ImmutableSet<Locale.Category> immutableSet = ImmutableSet.of(localeCategory);
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(immutableSet);
        Object[] emptyArray = new Object[0];
        set.elements = emptyArray;
        try {
            set.toArray();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testResizeEntriesThrowsException() {
        CompactLinkedHashSet<Integer> set = new CompactLinkedHashSet<>(1);
        try {
            set.resizeEntries(54);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMoveLastEntryThrowsException() {
        CompactLinkedHashSet<Locale.Category> set = new CompactLinkedHashSet<>();
        try {
            set.moveLastEntry(17, 17);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testMoveLastEntryOutOfBounds() {
        Locale.Category[] categories = new Locale.Category[1];
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(categories);
        try {
            set.moveLastEntry(1, 32);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertEntryThrowsException() {
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create();
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        try {
            set.insertEntry(91, localeCategory, 91, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertEntryOutOfBounds() {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        ImmutableSortedSet<Locale.Category> immutableSortedSet = ImmutableSortedSet.of(localeCategory, localeCategory, localeCategory);
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(immutableSortedSet);
        try {
            set.insertEntry(133, localeCategory, 133, 1485);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.google.common.collect.CompactHashSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitWithNegativeSize() {
        CompactLinkedHashSet<Object> set = new CompactLinkedHashSet<>();
        try {
            set.init(-1191);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetSuccessorThrowsException() {
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        try {
            set.getSuccessor(1296);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateWithNegativeExpectedSize() {
        try {
            CompactLinkedHashSet.createWithExpectedSize(-1086);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateWithNullArray() {
        try {
            CompactLinkedHashSet.create((Locale.Category[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.collect.CompactLinkedHashSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateWithNullCollection() {
        try {
            CompactLinkedHashSet.create((Collection<? extends Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.collect.CompactLinkedHashSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testAllocArraysThrowsException() {
        Locale.Category[] categories = new Locale.Category[1];
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(categories);
        CompactLinkedHashSet<Object> newSet = CompactLinkedHashSet.create((Collection<?>) set);
        try {
            newSet.allocArrays();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNegativeSize() {
        try {
            new CompactLinkedHashSet<Locale.Category>(-439);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testClear() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(2497);
        set.clear();
        assertEquals(0, set.size());
    }

    @Test(timeout = 4000)
    public void testClearOnNonEmptySet() {
        Locale.Category[] categories = new Locale.Category[1];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(categories);
        set.clear();
        assertEquals(0, set.size());
    }

    @Test(timeout = 4000)
    public void testRetainAll() {
        Object[] objects = new Object[2];
        objects[0] = new Object();
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create(objects);
        EmptyImmutableSetMultimap emptyMultimap = EmptyImmutableSetMultimap.INSTANCE;
        ImmutableCollection<Object> values = emptyMultimap.createValues();
        boolean changed = set.retainAll(values);
        assertTrue(set.isEmpty());
        assertTrue(changed);
    }

    @Test(timeout = 4000)
    public void testToArrayWithPreallocatedArray() {
        Locale.Category[] categories = new Locale.Category[3];
        CompactLinkedHashSet<Object> set = CompactLinkedHashSet.create();
        Object[] resultArray = set.toArray(categories);
        assertSame(categories, resultArray);
    }

    @Test(timeout = 4000)
    public void testResizeEntriesNegativeSize() {
        Locale.Category[] categories = new Locale.Category[9];
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(categories);
        try {
            set.resizeEntries(-1131);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("java.util.Arrays", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertToHashFloodingResistantImplementationOnNonEmptySet() {
        Integer[] integers = new Integer[6];
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create(integers);
        set.convertToHashFloodingResistantImplementation();
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testToArrayOnNonEmptySet() {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        ImmutableSet<Locale.Category> immutableSet = ImmutableSet.of(localeCategory);
        CompactLinkedHashSet<Locale.Category> set = CompactLinkedHashSet.create(immutableSet);
        set.toArray();
        assertTrue(set.contains(localeCategory));
    }

    @Test(timeout = 4000)
    public void testGetSuccessorThrowsArrayIndexOutOfBoundsException() {
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create();
        set.allocArrays();
        try {
            set.getSuccessor(3);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("com.google.common.collect.CompactLinkedHashSet", e);
        }
    }
}