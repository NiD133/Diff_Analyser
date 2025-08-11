package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ComparatorChainTest extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAddComparatorAndHashCode() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        Comparator<Object> comparator = Comparator.nullsLast(comparatorChain);
        comparatorChain.addComparator(comparator, true);
        comparatorChain.hashCode();
    }

    @Test(timeout = 4000)
    public void testAddSameComparatorTwice() throws Throwable {
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>();
        comparatorChain.addComparator(comparatorChain, true);
        comparatorChain.addComparator(comparatorChain, true);
        assertFalse(comparatorChain.isLocked());
    }

    @Test(timeout = 4000)
    public void testSetReverseSortOnEmptyChain() throws Throwable {
        ComparatorChain<ComparatorChain<Integer>> comparatorChain = new ComparatorChain<>();
        comparatorChain.setReverseSort(1306);
        assertEquals(0, comparatorChain.size());
    }

    @Test(timeout = 4000)
    public void testCompareWithMockedToLongFunction() throws Throwable {
        ToLongFunction<Object> toLongFunction = mock(ToLongFunction.class, new ViolatedAssumptionAnswer());
        doReturn(513L, -971L).when(toLongFunction).applyAsLong(any());
        Comparator<Object> comparator = Comparator.comparingLong(toLongFunction);
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(comparator);
        LinkedList<Comparator<ComparatorChain<LongBuffer>>> linkedList = new LinkedList<>();
        ComparatorChain<ComparatorChain<LongBuffer>> comparatorChain1 = new ComparatorChain<>(linkedList);
        Object object = new Object();
        comparatorChain.compare(comparatorChain1, object);
        assertTrue(comparatorChain.isLocked());
    }

    @Test(timeout = 4000)
    public void testSizeWithNullComparatorList() throws Throwable {
        BitSet bitSet = new BitSet();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, bitSet);
        try {
            comparatorChain.size();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetReverseSortWithInvalidIndex() throws Throwable {
        LinkedList<Comparator<ComparatorPredicate.Criterion>> linkedList = new LinkedList<>();
        ComparatorChain<ComparatorPredicate.Criterion> comparatorChain = new ComparatorChain<>(linkedList);
        try {
            comparatorChain.setReverseSort(-2145);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.BitSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetForwardSortWithNullBitSet() throws Throwable {
        LinkedList<Comparator<Integer>> linkedList = new LinkedList<>();
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(linkedList, null);
        try {
            comparatorChain.setForwardSort(0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetForwardSortWithNegativeIndex() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        try {
            comparatorChain.setForwardSort(-1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.BitSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithNullList() throws Throwable {
        long[] longArray = new long[0];
        BitSet bitSet = BitSet.valueOf(longArray);
        ComparatorChain<ComparatorChain<Object>> comparatorChain = new ComparatorChain<>(null, bitSet);
        try {
            comparatorChain.setComparator(2147483645, comparatorChain, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithEmptyChain() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        try {
            comparatorChain.setComparator(0, comparatorChain, false);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithNegativeIndex() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        try {
            comparatorChain.setComparator(-2, comparatorChain, false);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorAfterComparison() throws Throwable {
        Comparator<ComparatorChain<Object>> comparator = Comparator.nullsLast(null);
        ComparatorChain<ComparatorChain<Object>> comparatorChain = new ComparatorChain<>(comparator);
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        ComparatorChain<Object> comparatorChain1 = new ComparatorChain<>(linkedList, null);
        comparatorChain.compare(comparatorChain1, comparatorChain1);
        try {
            comparatorChain.setComparator(-67, comparator);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testExceptionClosureComparison() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList);
        Closure<Object> closure = ExceptionClosure.exceptionClosure();
        ClosureTransformer<Object> closureTransformer = new ClosureTransformer<>(closure);
        Comparator<ComparatorChain<Object>> comparator = Comparator.comparing(closureTransformer, comparatorChain);
        ComparatorChain<ComparatorChain<Object>> comparatorChain1 = new ComparatorChain<>(comparator);
        try {
            comparatorChain1.compare(comparatorChain, comparatorChain);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionClosure", e);
        }
    }

    @Test(timeout = 4000)
    public void testCompareWithNullComparator() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, false);
        Object object = new Object();
        ComparatorChain<ComparatorChain<Integer>> comparatorChain1 = new ComparatorChain<>();
        try {
            comparatorChain.compare(object, comparatorChain1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddComparatorWithNullList() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, null);
        try {
            comparatorChain.addComparator(comparatorChain);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullList() throws Throwable {
        try {
            ComparatorChain<ByteBuffer> comparatorChain = new ComparatorChain<>(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithNullComparator() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, false);
        comparatorChain.setComparator(0, null, false);
        assertFalse(comparatorChain.isLocked());
    }

    @Test(timeout = 4000)
    public void testCompareWithMockedToIntFunction() throws Throwable {
        ToIntFunction<Integer> toIntFunction = mock(ToIntFunction.class, new ViolatedAssumptionAnswer());
        doReturn(1, -1576, -1576, -206).when(toIntFunction).applyAsInt(anyInt());
        Comparator<Integer> comparator = Comparator.comparingInt(toIntFunction);
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(comparator, true);
        Integer integer = -318;
        comparatorChain.compare(integer, integer);
        int result = comparatorChain.compare(integer, integer);
        assertTrue(comparatorChain.isLocked());
        assertEquals(1, result);
    }

    @Test(timeout = 4000)
    public void testAddComparatorAndSize() throws Throwable {
        BitSet bitSet = new BitSet();
        LinkedList<Comparator<Integer>> linkedList = new LinkedList<>();
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(linkedList, bitSet);
        comparatorChain.addComparator(comparatorChain, false);
        comparatorChain.size();
        assertEquals("{}", bitSet.toString());
        assertEquals(0, bitSet.cardinality());
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithReverseOrder() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, false);
        comparatorChain.setComparator(0, null, true);
        assertFalse(comparatorChain.isLocked());
    }

    @Test(timeout = 4000)
    public void testSetComparatorAndSize() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        Comparator<ComparatorChain<Object>> comparator = Comparator.nullsFirst(comparatorChain);
        ComparatorChain<ComparatorChain<Object>> comparatorChain1 = new ComparatorChain<>(comparator);
        comparatorChain1.setComparator(0, comparator);
        assertEquals(1, comparatorChain1.size());
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullList() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(null, null);
        comparatorChain.hashCode();
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentChains() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList);
        ComparatorChain<Object> comparatorChain1 = new ComparatorChain<>(comparatorChain);
        assertFalse(comparatorChain.equals(comparatorChain1));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentOrder() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        ComparatorChain<Object> comparatorChain1 = new ComparatorChain<>(comparatorChain, true);
        assertFalse(comparatorChain.equals(comparatorChain1));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameChains() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        ComparatorChain<Object> comparatorChain1 = new ComparatorChain<>();
        assertTrue(comparatorChain.equals(comparatorChain1));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        assertFalse(comparatorChain.equals(null));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        BitSet bitSet = new BitSet();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList, bitSet);
        assertFalse(comparatorChain.equals("ComparatorChains must contain at least one Comparator"));
    }

    @Test(timeout = 4000)
    public void testSetComparatorAfterComparisonWithMockedToIntFunction() throws Throwable {
        ToIntFunction<Integer> toIntFunction = mock(ToIntFunction.class, new ViolatedAssumptionAnswer());
        doReturn(780, -1576).when(toIntFunction).applyAsInt(anyInt());
        Comparator<Integer> comparator = Comparator.comparingInt(toIntFunction);
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(comparator, true);
        Integer integer = 780;
        comparatorChain.compare(integer, integer);
        try {
            comparatorChain.setComparator(780, comparator, false);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testCompareWithEmptyChain() throws Throwable {
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>();
        try {
            comparatorChain.compare(comparatorChain, comparatorChain);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetComparatorWithNegativeIndexAfterComparison() throws Throwable {
        Comparator<ComparatorChain<Object>> comparator = Comparator.nullsLast(null);
        ComparatorChain<ComparatorChain<Object>> comparatorChain = new ComparatorChain<>(comparator);
        try {
            comparatorChain.setComparator(-67, comparator);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList);
        assertTrue(comparatorChain.equals(comparatorChain));
    }

    @Test(timeout = 4000)
    public void testSetForwardSortOnEmptyChain() throws Throwable {
        ComparatorChain<Comparator<Object>> comparatorChain = new ComparatorChain<>();
        comparatorChain.setForwardSort(0);
        assertEquals(0, comparatorChain.size());
    }

    @Test(timeout = 4000)
    public void testAddComparatorAndSizeIncrease() throws Throwable {
        ToIntFunction<Integer> toIntFunction = mock(ToIntFunction.class, new ViolatedAssumptionAnswer());
        Comparator<Integer> comparator = Comparator.comparingInt(toIntFunction);
        ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(comparator, true);
        comparatorChain.addComparator(comparator);
        assertEquals(2, comparatorChain.size());
    }

    @Test(timeout = 4000)
    public void testSizeOfEmptyChain() throws Throwable {
        ComparatorChain<ComparatorPredicate.Criterion> comparatorChain = new ComparatorChain<>();
        assertEquals(0, comparatorChain.size());
    }

    @Test(timeout = 4000)
    public void testSetReverseSortWithNullBitSet() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList, null);
        try {
            comparatorChain.setReverseSort(-1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsLockedWithEmptyChain() throws Throwable {
        LinkedList<Comparator<Object>> linkedList = new LinkedList<>();
        BitSet bitSet = new BitSet();
        ComparatorChain<Object> comparatorChain = new ComparatorChain<>(linkedList, bitSet);
        assertFalse(comparatorChain.isLocked());
    }
}