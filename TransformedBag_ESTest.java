package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TransformedBag_ESTest extends TransformedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEmptyUniqueSet() throws Throwable {
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Object> transformer = InvokerTransformer.invokerTransformer("pA)M[)T~9fY");
        TransformedSortedBag<Object> transformedSortedBag = TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        Set<Object> uniqueSet = transformedSortedBag.uniqueSet();
        assertEquals(0, uniqueSet.size());
    }

    @Test(timeout = 4000)
    public void testRemoveFromTransformedBag() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0, 0, 0, 0).when(comparator).compare(any(), any());
        TreeBag<Integer> treeBag = new TreeBag<>(comparator);
        ConstantTransformer<Object, Integer> transformer = new ConstantTransformer<>(null);
        TransformedSortedBag<Integer> transformedSortedBag = new TransformedSortedBag<>(treeBag, transformer);
        Integer value = 862;
        treeBag.add(value);
        boolean removed = transformedSortedBag.remove(new HashMap<>(), 3791);
        assertFalse(treeBag.contains(0));
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistentElement() throws Throwable {
        TreeBag<Object> treeBag = new TreeBag<>();
        TransformedBag<Integer> transformedBag = new TransformedBag<>(new HashBag<>(), ConstantTransformer.nullTransformer());
        boolean removed = transformedBag.remove(treeBag, 0);
        assertFalse(removed);
    }

    @Test(timeout = 4000)
    public void testGetCountInTransformedBag() throws Throwable {
        Integer value = 2;
        ConstantTransformer<Object, Integer> transformer = new ConstantTransformer<>(value);
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(mock(Comparator.class, new ViolatedAssumptionAnswer()));
        TransformedSortedBag<Predicate<Object>> transformedSortedBag = new TransformedSortedBag<>(treeBag, InvokerTransformer.invokerTransformer("jA]A&z5z!a+Jbr(nH"));
        TreeBag<Integer> integerTreeBag = new TreeBag<>(mock(Comparator.class, new ViolatedAssumptionAnswer()));
        integerTreeBag.add(value);
        TransformedSortedBag<Integer> integerTransformedSortedBag = new TransformedSortedBag<>(integerTreeBag, transformer);
        int count = integerTransformedSortedBag.getCount(transformedSortedBag);
        assertEquals(1, count);
    }

    @Test(timeout = 4000)
    public void testGetBagSize() throws Throwable {
        TreeBag<HashBag<Object>> treeBag = new TreeBag<>();
        TransformedSortedBag<HashBag<Object>> transformedSortedBag = TransformedSortedBag.transformingSortedBag(treeBag, ConstantTransformer.nullTransformer());
        Bag<HashBag<Object>> bag = transformedSortedBag.getBag();
        assertEquals(0, bag.size());
    }

    @Test(timeout = 4000)
    public void testContainsInTransformedBag() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        Integer value = 165;
        hashBag.add(value);
        FactoryTransformer<Object, Integer> transformer = new FactoryTransformer<>(new ConstantFactory<>(value));
        TreeBag<Object> treeBag = new TreeBag<>(hashBag);
        TransformedSortedBag<Object> transformedSortedBag = new TransformedSortedBag<>(treeBag, transformer);
        Bag<Object> bag = transformedSortedBag.getBag();
        assertTrue(bag.contains(165));
    }

    @Test(timeout = 4000)
    public void testAddNegativeCopies() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        Integer value = 165;
        FactoryTransformer<Object, Integer> transformer = new FactoryTransformer<>(new ConstantFactory<>(value));
        TreeBag<Integer> treeBag = new TreeBag<>(hashBag);
        TransformedSortedBag<Integer> transformedSortedBag = TransformedSortedBag.transformedSortedBag(treeBag, transformer);
        boolean added = transformedSortedBag.add(value, -803);
        assertFalse(added);
    }

    @Test(timeout = 4000)
    public void testTransformingBagWithNullTransformer() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        try {
            TransformedBag.transformingBag(hashBag, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransformedBagWithUnmodifiableBag() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        hashBag.add(null);
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        Bag<Integer> unmodifiableBag = UnmodifiableBag.unmodifiableBag(hashBag);
        try {
            TransformedBag.transformedBag(unmodifiableBag, transformer);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.bag.UnmodifiableBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransformedBagWithNullBag() throws Throwable {
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        try {
            TransformedBag.transformedBag(null, transformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransformedBagWithPredicatedBag() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        Integer value = 417;
        hashBag.add(value);
        ConstantTransformer<Object, Integer> transformer = new ConstantTransformer<>(value);
        Predicate<Integer> predicate = UniquePredicate.uniquePredicate();
        PredicatedBag<Integer> predicatedBag = PredicatedBag.predicatedBag(hashBag, predicate);
        IfTransformer<Integer, Integer> ifTransformer = new IfTransformer<>(predicate, transformer, transformer);
        try {
            TransformedBag.transformedBag(predicatedBag, ifTransformer);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.collections4.collection.PredicatedCollection", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransformedBagWithSwitchTransformer() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        hashBag.add(null);
        Predicate<Object>[] predicates = new Predicate[2];
        Integer value = 5;
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        Predicate<Object> predicate = ComparatorPredicate.comparatorPredicate(value, comparator, ComparatorPredicate.Criterion.EQUAL);
        predicates[0] = predicate;
        Transformer<Object, Integer>[] transformers = new Transformer[0];
        Map<Object, Integer> map = new HashMap<>();
        Transformer<Object, Integer> transformer = MapTransformer.mapTransformer(map);
        SwitchTransformer<Object, Integer> switchTransformer = new SwitchTransformer<>(predicates, transformers, transformer);
        try {
            TransformedBag.transformedBag(hashBag, switchTransformer);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.collections4.functors.SwitchTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveFromUnmodifiableSortedBag() throws Throwable {
        Transformer<Object, Integer> transformer = ExceptionTransformer.exceptionTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        SortedBag<Integer> sortedBag = UnmodifiableSortedBag.unmodifiableSortedBag(treeBag);
        TransformedSortedBag<Integer> transformedSortedBag = TransformedSortedBag.transformingSortedBag(sortedBag, transformer);
        try {
            transformedSortedBag.remove(sortedBag, 2694);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.bag.UnmodifiableSortedBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveWithNullValue() throws Throwable {
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        TreeBag<Object> treeBag = new TreeBag<>(null);
        TransformedSortedBag<Object> transformedSortedBag = new TransformedSortedBag<>(treeBag, transformer);
        try {
            transformedSortedBag.remove(null, -2770);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithNullTransformer() throws Throwable {
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        TreeBag<Integer> treeBag = new TreeBag<>();
        TransformedSortedBag<Integer> transformedSortedBag = new TransformedSortedBag<>(treeBag, transformer);
        Integer value = 3787;
        try {
            transformedSortedBag.add(value, 3787);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypes() throws Throwable {
        Integer value = 2;
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(comparator);
        TransformedSortedBag<Predicate<Object>> transformedSortedBag = new TransformedSortedBag<>(treeBag, InvokerTransformer.invokerTransformer("jA]A&z5z!a+Jbr(nH"));
        boolean equals = transformedSortedBag.equals(value);
        assertFalse(equals);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        HashBag<Integer> hashBag = new HashBag<>();
        Integer value = 165;
        FactoryTransformer<Object, Integer> transformer = new FactoryTransformer<>(new ConstantFactory<>(value));
        TreeBag<Integer> treeBag = new TreeBag<>(hashBag);
        TransformedSortedBag<Integer> transformedSortedBag = TransformedSortedBag.transformedSortedBag(treeBag, transformer);
        boolean equals = transformedSortedBag.equals(transformedSortedBag);
        assertTrue(equals);
    }
}