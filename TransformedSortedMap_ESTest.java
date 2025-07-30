package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.junit.runner.RunWith;
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
public class TransformedSortedMapTest extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEmptySubMapWithSameKeys() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        LinkedList<Transformer<Integer, Integer>> transformers = new LinkedList<>();
        Transformer<Integer, Integer> chainedTransformer = ChainedTransformer.chainedTransformer(transformers);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(treeMap, null, chainedTransformer);
        Integer key = -3410;
        SortedMap<Integer, Integer> subMap = transformedMap.subMap(key, key);
        assertTrue(subMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testEmptyHeadMapWithConstantFactory() throws Throwable {
        Integer value = 3005;
        Factory<Integer> constantFactory = ConstantFactory.constantFactory(value);
        FactoryTransformer<Object, Integer> factoryTransformer = new FactoryTransformer<>(constantFactory);
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Class<Integer>[] classArray = (Class<Integer>[]) Array.newInstance(Class.class, 7);
        InvokerTransformer<Object, Integer> invokerTransformer = new InvokerTransformer<>("\"3Kz{N`TV^sYZ s\"p5Q", classArray, classArray);
        TransformedSortedMap<Integer, Integer> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, invokerTransformer, factoryTransformer);
        SortedMap<Integer, Integer> headMap = transformedMap.headMap(value);
        assertTrue(headMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testEmptyTailMapWithConstantTransformer() throws Throwable {
        TreeMap<Integer, Object> treeMap = new TreeMap<>();
        Integer key = -2921;
        ConstantTransformer<Integer, Integer> constantTransformer = new ConstantTransformer<>(key);
        Class<Object>[] classArray = (Class<Object>[]) Array.newInstance(Class.class, 2);
        InvokerTransformer<Object, Integer> invokerTransformer = new InvokerTransformer<>(null, classArray, null);
        TransformedSortedMap<Integer, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, constantTransformer, invokerTransformer);
        SortedMap<Integer, Object> tailMap = transformedMap.tailMap(key);
        assertTrue(tailMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testNonEmptyTailMapAfterPut() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key = 850;
        Transformer<Object, Integer> constantTransformer = ConstantTransformer.constantTransformer(key);
        TransformedSortedMap<Integer, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(treeMap, null, constantTransformer);
        treeMap.put(key, key);
        SortedMap<Integer, Integer> tailMap = transformedMap.tailMap(key);
        assertFalse(tailMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testNonEmptySubMapAfterPut() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key1 = -1;
        Integer key2 = 3152;
        treeMap.put(key1, key2);
        ConstantTransformer<Integer, Integer> constantTransformer = new ConstantTransformer<>(key1);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(treeMap, constantTransformer, constantTransformer);
        SortedMap<Integer, Integer> subMap = transformedMap.subMap(key1, key2);
        assertFalse(subMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testLastKeyWithMockedComparator() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Transformer<Object, Object> mapTransformer = MapTransformer.mapTransformer(treeMap);
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(1, 1).when(comparator).compare(any(), any());
        TreeMap<Object, Integer> treeMapWithComparator = new TreeMap<>(comparator);
        Integer key = 61;
        ConstantTransformer<Integer, Integer> constantTransformer = new ConstantTransformer<>(key);
        treeMapWithComparator.put(key, key);
        TransformedSortedMap<Object, Integer> transformedMap = TransformedSortedMap.transformedSortedMap(treeMapWithComparator, mapTransformer, constantTransformer);
        Object lastKey = transformedMap.lastKey();
        assertNull(lastKey);
    }

    @Test(timeout = 4000)
    public void testLastKeyWithMockedComparatorAndObjectClass() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        TreeMap<Object, Integer> treeMap = new TreeMap<>(comparator);
        Class<Object>[] classArray = (Class<Object>[]) Array.newInstance(Class.class, 3);
        Class<Object> objectClass = Object.class;
        Integer value = 5;
        treeMap.put(objectClass, value);
        InvokerTransformer<Object, Integer> invokerTransformer = new InvokerTransformer<>("Lu", classArray, classArray);
        TransformedSortedMap<Object, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(treeMap, invokerTransformer, invokerTransformer);
        Object lastKey = transformedMap.lastKey();
        assertEquals("class java.lang.Object", lastKey.toString());
    }

    @Test(timeout = 4000)
    public void testHeadMapWithSingleElement() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        LinkedList<Transformer<Integer, Integer>> transformers = new LinkedList<>();
        Transformer<Integer, Integer> chainedTransformer = ChainedTransformer.chainedTransformer(transformers);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(treeMap, chainedTransformer, chainedTransformer);
        Integer key = -17;
        treeMap.put(key, key);
        Integer upperBound = 0;
        SortedMap<Integer, Integer> headMap = transformedMap.headMap(upperBound);
        assertEquals(1, headMap.size());
    }

    @Test(timeout = 4000)
    public void testEmptyTransformedSortedMap() throws Throwable {
        TreeMap<Closure<Object>, Object> treeMap = new TreeMap<>();
        Class<Integer>[] classArray = (Class<Integer>[]) Array.newInstance(Class.class, 7);
        InvokerTransformer<Object, Closure<Object>> invokerTransformer = new InvokerTransformer<>("org.apache.commons.collections4.functors.ComparatorPredicate", classArray, classArray);
        TransformedSortedMap<Closure<Object>, Object> transformedMap = new TransformedSortedMap<>(treeMap, invokerTransformer, invokerTransformer);
        TransformedSortedMap<Closure<Object>, Object> transformedMapAgain = TransformedSortedMap.transformingSortedMap(transformedMap, invokerTransformer, invokerTransformer);
        SortedMap<Closure<Object>, Object> sortedMap = transformedMapAgain.getSortedMap();
        assertEquals(0, sortedMap.size());
    }

    @Test(timeout = 4000)
    public void testNonEmptyTransformedSortedMapAfterPut() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key = 784;
        Transformer<Integer, Integer> constantTransformer = ConstantTransformer.constantTransformer(key);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(treeMap, constantTransformer, constantTransformer);
        transformedMap.put(key, key);
        TransformedSortedMap<Integer, Integer> transformedMapAgain = TransformedSortedMap.transformedSortedMap(transformedMap, constantTransformer, constantTransformer);
        SortedMap<Integer, Integer> sortedMap = transformedMapAgain.getSortedMap();
        assertEquals(1, sortedMap.size());
    }

    @Test(timeout = 4000)
    public void testComparatorIsNull() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Transformer<Object, Object> mapTransformer = MapTransformer.mapTransformer(treeMap);
        TransformedSortedMap<Object, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, mapTransformer, mapTransformer);
        Comparator<? super Object> comparator = transformedMap.comparator();
        assertNull(comparator);
    }

    @Test(timeout = 4000)
    public void testTransformingSortedMapWithNullMap() throws Throwable {
        Transformer<Object, Integer> nullTransformer = ConstantTransformer.nullTransformer();
        try {
            TransformedSortedMap.transformingSortedMap(null, nullTransformer, nullTransformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransformedSortedMapWithExceptionTransformer() throws Throwable {
        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        TreeMap<Object, Integer> treeMap = new TreeMap<>();
        Integer key = -1;
        treeMap.put(key, key);
        try {
            TransformedSortedMap.transformedSortedMap(treeMap, exceptionTransformer, exceptionTransformer);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorWithSelfReferencingMap() throws Throwable {
        TreeMap<Object, Predicate<Integer>> treeMap = new TreeMap<>();
        TransformedSortedMap<Object, Predicate<Integer>> transformedMap = TransformedSortedMap.transformingSortedMap(treeMap, null, null);
        transformedMap.map = transformedMap;
        try {
            TransformedSortedMap.transformedSortedMap(transformedMap, null, null);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTransformedSortedMapWithNullMapAndTransformer() throws Throwable {
        Transformer<Integer, Integer> mapTransformer = MapTransformer.mapTransformer(null);
        try {
            TransformedSortedMap.transformedSortedMap(null, mapTransformer, mapTransformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testClassCastExceptionWithTreeMap() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Integer key = 1;
        treeMap.put(key, key);
        ConstantTransformer<Object, Object> constantTransformer = new ConstantTransformer<>(treeMap);
        try {
            TransformedSortedMap.transformedSortedMap(treeMap, constantTransformer, constantTransformer);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithSubMap() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key = 512;
        SortedMap<Integer, Integer> subMap = treeMap.subMap(key, key);
        Transformer<Integer, Integer> mapTransformer = MapTransformer.mapTransformer(subMap);
        TransformedSortedMap<Integer, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(subMap, mapTransformer, mapTransformer);
        try {
            transformedMap.tailMap(key);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.TreeMap$AscendingSubMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testClassCastExceptionWithTailMap() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Transformer<Object, Object> mapTransformer = MapTransformer.mapTransformer(treeMap);
        TransformedSortedMap<Object, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, mapTransformer, mapTransformer);
        try {
            transformedMap.tailMap(treeMap);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithSubMapRange() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key = 835;
        SortedMap<Integer, Integer> subMap = treeMap.subMap(key, key);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(subMap, null, null);
        try {
            transformedMap.subMap(key, key);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.TreeMap$AscendingSubMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testClassCastExceptionWithHeadMap() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Transformer<Object, Object> mapTransformer = MapTransformer.mapTransformer(treeMap);
        TransformedSortedMap<Object, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, mapTransformer, mapTransformer);
        try {
            transformedMap.headMap(treeMap);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithHeadMap() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        TransformedSortedMap<Object, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, null, null);
        try {
            transformedMap.headMap(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithHeadMapRange() throws Throwable {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        Integer key = -1116;
        NavigableMap<Integer, Integer> navigableMap = treeMap.headMap(key, true);
        TransformedSortedMap<Integer, Integer> transformedMap = new TransformedSortedMap<>(navigableMap, null, null);
        Integer upperBound = 0;
        try {
            transformedMap.headMap(upperBound);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.TreeMap$AscendingSubMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithTailMap() throws Throwable {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        TransformedSortedMap<Object, Object> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, null, null);
        try {
            transformedMap.tailMap(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testComparatorNotNullWithMockedComparator() throws Throwable {
        String methodName = "i]oNU\\u7I%x";
        Transformer<Object, Integer> invokerTransformer = InvokerTransformer.invokerTransformer(methodName);
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(comparator).toString();
        TreeMap<Integer, Integer> treeMap = new TreeMap<>(comparator);
        TransformedSortedMap<Integer, Integer> transformedMap = TransformedSortedMap.transformedSortedMap(treeMap, invokerTransformer, invokerTransformer);
        Comparator<? super Integer> resultComparator = transformedMap.comparator();
        assertNotNull(resultComparator);
    }
}