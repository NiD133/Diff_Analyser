package org.apache.commons.collections4.multimap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.NotPredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.ArrayListValuedLinkedHashMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.collections4.multimap.LinkedHashSetValuedLinkedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TransformedMultiValuedMap_ESTest extends TransformedMultiValuedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testTransformAndPutAll() throws Throwable {
        Integer key = 1270;
        ConstantTransformer<Object, Integer> constantTransformer = new ConstantTransformer<>(key);
        ArrayDeque<Integer> values = new ArrayDeque<>(2934);
        ArrayListValuedLinkedHashMap<Integer, Object> baseMap = new ArrayListValuedLinkedHashMap<>(2934, 1270);
        TransformedMultiValuedMap<Integer, Object> transformedMap = new TransformedMultiValuedMap<>(baseMap, constantTransformer, constantTransformer);

        values.offerFirst(key);
        transformedMap.putAll(key, values);
        transformedMap.putAll(null, values);

        TransformedMultiValuedMap.transformedMap(baseMap, constantTransformer, constantTransformer);
        assertFalse(baseMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testSwitchTransformer() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Object, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>(2566);
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 0);
        Transformer<Object, Integer>[] transformers = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 1);
        HashMap<Object, Integer> map = new HashMap<>();
        Integer value = 2566;
        map.put(baseMap, value);

        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(map);
        SwitchTransformer<Object, Integer> switchTransformer = new SwitchTransformer<>(predicates, transformers, mapTransformer);
        map.put(switchTransformer, null);

        TransformedMultiValuedMap<Object, Integer> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, switchTransformer, mapTransformer);
        boolean result = transformedMap.putAll(map);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testInvokerTransformer() throws Throwable {
        HashSetValuedHashMap<Integer, Integer> baseMap = new HashSetValuedHashMap<>(0);
        Transformer<Object, Integer> invokerTransformer = InvokerTransformer.invokerTransformer("");
        Integer key = -3473;
        Transformer<Object, Object> constantTransformer = ConstantTransformer.constantTransformer(key);

        baseMap.put(key, key);
        ArrayListValuedHashMap<Integer, Object> arrayListMap = new ArrayListValuedHashMap<>(baseMap);
        TransformedMultiValuedMap<Integer, Object> transformedMap = TransformedMultiValuedMap.transformingMap(arrayListMap, invokerTransformer, constantTransformer);
        assertFalse(transformedMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCloneTransformer() throws Throwable {
        Transformer<Integer, Integer> nopTransformer = NOPTransformer.nopTransformer();
        HashMap<Integer, Boolean> map = new HashMap<>();
        ArrayListValuedHashMap<Integer, Boolean> arrayListMap = new ArrayListValuedHashMap<>(map);
        LinkedHashSetValuedLinkedHashMap<Integer, Boolean> linkedHashSetMap = new LinkedHashSetValuedLinkedHashMap<>(arrayListMap);
        Transformer<Boolean, Boolean> cloneTransformer = CloneTransformer.cloneTransformer();

        TransformedMultiValuedMap<Integer, Boolean> transformedMap = TransformedMultiValuedMap.transformedMap(linkedHashSetMap, nopTransformer, cloneTransformer);
        Boolean result = transformedMap.transformValue(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testTransformValueWithNullTransformers() throws Throwable {
        ArrayListValuedHashMap<Integer, Object> baseMap = new ArrayListValuedHashMap<>(0, 0);
        TransformedMultiValuedMap<Integer, Object> transformedMap = TransformedMultiValuedMap.transformingMap(baseMap, null, null);

        Object object = new Object();
        Object transformedObject = transformedMap.transformValue(object);
        assertSame(object, transformedObject);
    }

    @Test(timeout = 4000)
    public void testTransformKeyWithNullTransformer() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> baseMap = new LinkedHashSetValuedLinkedHashMap<>(0);
        ConstantTransformer<Object, Integer> constantTransformer = new ConstantTransformer<>(null);
        Class<Integer>[] classArray = (Class<Integer>[]) Array.newInstance(Class.class, 4);
        InvokerTransformer<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> invokerTransformer = new InvokerTransformer<>("", classArray, classArray);

        TransformedMultiValuedMap<Object, AbstractMap.SimpleImmutableEntry<Integer, Integer>> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, constantTransformer, invokerTransformer);
        Object result = transformedMap.transformKey(invokerTransformer);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNullPredicateTransformer() throws Throwable {
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        Transformer<Object, Predicate<Integer>> constantTransformer = ConstantTransformer.constantTransformer(nullPredicate);
        LinkedHashSetValuedLinkedHashMap<Predicate<Integer>, Object> baseMap = new LinkedHashSetValuedLinkedHashMap<>();

        TransformedMultiValuedMap<Predicate<Integer>, Object> transformedMap = TransformedMultiValuedMap.transformingMap(baseMap, constantTransformer, constantTransformer);
        Predicate<Integer> result = transformedMap.transformKey(nullPredicate);
        assertSame(nullPredicate, result);
    }

    @Test(timeout = 4000)
    public void testPutAllWithNullTransformer() throws Throwable {
        ArrayListValuedHashMap<Integer, Integer> baseMap = new ArrayListValuedHashMap<>(1, 1);
        Transformer<Object, Integer> nullTransformer = ConstantTransformer.nullTransformer();
        TransformedMultiValuedMap<Integer, Integer> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, null, nullTransformer);

        Integer key = 1;
        transformedMap.put(key, null);

        HashSetValuedHashMap<Integer, Object> hashSetMap = new HashSetValuedHashMap<>();
        TransformedMultiValuedMap<Integer, Object> transformedMap2 = new TransformedMultiValuedMap<>(hashSetMap, nullTransformer, nullTransformer);
        boolean result = transformedMap2.putAll(baseMap);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testPutAllWithSwitchTransformer() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Integer, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>();
        HashSetValuedHashMap<Integer, Integer> hashSetMap = new HashSetValuedHashMap<>(baseMap);
        HashMap<Predicate<Object>, Transformer<Integer, Integer>> map = new HashMap<>();
        Transformer<Integer, Integer> switchTransformer = SwitchTransformer.switchTransformer(map);

        TransformedMultiValuedMap<Integer, Integer> transformedMap = TransformedMultiValuedMap.transformingMap(hashSetMap, switchTransformer, switchTransformer);
        Integer key = -1;
        boolean firstPut = transformedMap.put(key, key);
        boolean secondPut = transformedMap.put(key, key);
        assertFalse(firstPut == secondPut);
    }

    @Test(timeout = 4000)
    public void testTransformingMapWithNullMap() throws Throwable {
        Transformer<Integer, Integer> nopTransformer = NOPTransformer.nopTransformer();
        try {
            TransformedMultiValuedMap.transformingMap(null, nopTransformer, nopTransformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowError() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Object, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>(2566);
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 0);
        Transformer<Object, Integer>[] transformers = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 1);
        HashMap<Object, Integer> map = new HashMap<>();
        Integer value = 2566;
        map.put(baseMap, value);
        baseMap.putAll(map);

        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(map);
        SwitchTransformer<Object, Integer> switchTransformer = new SwitchTransformer<>(predicates, transformers, mapTransformer);

        try {
            TransformedMultiValuedMap.transformedMap(baseMap, switchTransformer, mapTransformer);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullMap() throws Throwable {
        ConstantTransformer<Object, Integer> constantTransformer = new ConstantTransformer<>(null);
        HashMap<Locale.FilteringMode, Locale.FilteringMode> map = new HashMap<>();
        Transformer<Locale.FilteringMode, Locale.FilteringMode> mapTransformer = MapTransformer.mapTransformer(map);

        try {
            TransformedMultiValuedMap.transformedMap(null, mapTransformer, constantTransformer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testExceptionTransformer() throws Throwable {
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayListValuedLinkedHashMap<Integer, Object> baseMap = new ArrayListValuedLinkedHashMap<>(map);
        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();

        TransformedMultiValuedMap<Integer, Object> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, null, exceptionTransformer);
        try {
            transformedMap.transformValue(null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testChainedTransformer() throws Throwable {
        HashSetValuedHashMap<Object, Integer> baseMap = new HashSetValuedHashMap<>(0);
        Transformer<Integer, Integer>[] transformers = (Transformer<Integer, Integer>[]) Array.newInstance(Transformer.class, 1);
        ChainedTransformer<Integer> chainedTransformer = new ChainedTransformer<>(transformers);

        TransformedMultiValuedMap<Object, Integer> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, null, chainedTransformer);
        Integer key = 16;
        try {
            transformedMap.transformValue(key);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.ChainedTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokerTransformerException() throws Throwable {
        HashSetValuedHashMap<Integer, Integer> baseMap = new HashSetValuedHashMap<>(0);
        Transformer<Object, Integer> invokerTransformer = InvokerTransformer.invokerTransformer("");
        Integer key = -3473;

        TransformedMultiValuedMap<Integer, Integer> transformedMap = new TransformedMultiValuedMap<>(baseMap, invokerTransformer, invokerTransformer);
        baseMap.put(key, key);
        try {
            transformedMap.putAll(baseMap);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutAllWithEmptyMap() throws Throwable {
        HashSetValuedHashMap<Integer, Integer> baseMap = new HashSetValuedHashMap<>(0);
        Transformer<Object, Integer> invokerTransformer = InvokerTransformer.invokerTransformer("");

        TransformedMultiValuedMap<Integer, Integer> transformedMap = new TransformedMultiValuedMap<>(baseMap, invokerTransformer, invokerTransformer);
        boolean result = transformedMap.putAll(baseMap);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testSwitchTransformerWithEmptyMap() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Object, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>(2566);
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 0);
        Transformer<Object, Integer>[] transformers = (Transformer<Object, Integer>[]) Array.newInstance(Transformer.class, 1);
        HashMap<Object, Integer> map = new HashMap<>();

        Transformer<Object, Integer> mapTransformer = MapTransformer.mapTransformer(map);
        SwitchTransformer<Object, Integer> switchTransformer = new SwitchTransformer<>(predicates, transformers, mapTransformer);

        TransformedMultiValuedMap<Object, Integer> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, switchTransformer, mapTransformer);
        boolean result = transformedMap.putAll(map);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testFactoryTransformer() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Integer, Object> baseMap = new LinkedHashSetValuedLinkedHashMap<>(0);
        Integer key = 0;
        Factory<Integer> factory = ConstantFactory.constantFactory(key);
        FactoryTransformer<Object, Integer> factoryTransformer = new FactoryTransformer<>(factory);

        TransformedMultiValuedMap<Integer, Object> transformedMap = new TransformedMultiValuedMap<>(baseMap, factoryTransformer, factoryTransformer);
        ArrayDeque<Integer> values = new ArrayDeque<>();
        values.offerFirst(key);

        boolean firstPut = transformedMap.putAll(key, values);
        assertTrue(firstPut);

        boolean secondPut = transformedMap.putAll(key, values);
        assertFalse(secondPut);
    }

    @Test(timeout = 4000)
    public void testPutAllWithPriorityQueue() throws Throwable {
        HashMap<Predicate<Object>, Transformer<Integer, Integer>> map = new HashMap<>();
        Transformer<Integer, Integer> switchTransformer = SwitchTransformer.switchTransformer(map);
        LinkedHashSetValuedLinkedHashMap<Object, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>();

        Transformer<Object, Object> invokerTransformer = InvokerTransformer.invokerTransformer("82{wMeA6MsD+dn#");
        TransformedMultiValuedMap<Object, Integer> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, invokerTransformer, switchTransformer);

        Object key = new Object();
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(1, comparator);

        boolean result = transformedMap.putAll(key, priorityQueue);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testTransformWithConstantTransformer() throws Throwable {
        Integer key = 914;
        ArrayDeque<Integer> values = new ArrayDeque<>();
        values.offerFirst(key);

        HashMap<Integer, Object> map = new HashMap<>();
        ArrayListValuedLinkedHashMap<Integer, Object> baseMap = new ArrayListValuedLinkedHashMap<>(map);
        Transformer<Object, Object> constantTransformer = ConstantTransformer.constantTransformer(map);

        TransformedMultiValuedMap<Integer, Object> transformedMap = TransformedMultiValuedMap.transformedMap(baseMap, null, constantTransformer);
        transformedMap.putAll(key, values);

        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        try {
            TransformedMultiValuedMap.transformedMap(transformedMap, null, exceptionTransformer);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testPutWithUnmodifiableMap() throws Throwable {
        LinkedHashSetValuedLinkedHashMap<Integer, Predicate<Object>> baseMap = new LinkedHashSetValuedLinkedHashMap<>(1843);
        UnmodifiableMultiValuedMap<Integer, Predicate<Object>> unmodifiableMap = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(baseMap);

        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        AllPredicate<Object> allPredicate = new AllPredicate<>(predicates);
        NullIsExceptionPredicate<Object> nullIsExceptionPredicate = new NullIsExceptionPredicate<>(allPredicate);
        AnyPredicate<Object> anyPredicate = new AnyPredicate<>(predicates);
        NotPredicate<Object> notPredicate = new NotPredicate<>(anyPredicate);

        ConstantTransformer<Object, Predicate<Object>> constantTransformer = new ConstantTransformer<>(allPredicate);
        IfTransformer<Object, Predicate<Object>> ifTransformer = new IfTransformer<>(notPredicate, constantTransformer, constantTransformer);

        TransformedMultiValuedMap<Integer, Predicate<Object>> transformedMap = TransformedMultiValuedMap.transformingMap(unmodifiableMap, null, ifTransformer);

        Integer key = 1843;
        try {
            transformedMap.put(key, nullIsExceptionPredicate);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.AnyPredicate", e);
        }
    }
}