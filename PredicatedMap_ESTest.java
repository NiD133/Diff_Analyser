package org.apache.commons.collections4.map;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;
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
public class PredicatedMap_ESTest extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNullValueCheckThrowsException() {
        Map<HashMap<Integer, Object>, Integer> emptyMap = new HashMap<>();
        Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);
        AnyPredicate<Object> anyPredicate = new AnyPredicate<>(predicates);
        OnePredicate<Object> onePredicate = new OnePredicate<>(predicates);

        PredicatedMap<HashMap<Integer, Object>, Integer> predicatedMap = PredicatedMap.predicatedMap(
                emptyMap, onePredicate, onePredicate);

        PredicatedMap<HashMap<Integer, Object>, Integer> anotherPredicatedMap = new PredicatedMap<>(
                predicatedMap, anyPredicate, predicates[0]);

        try {
            anotherPredicatedMap.checkSetValue(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.map.PredicatedMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidateWithUniquePredicate() {
        Map<Boolean, Predicate<Integer>> map = new HashMap<>();
        UniquePredicate<Boolean> uniquePredicate = new UniquePredicate<>();
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(anyInt(), anyInt());

        Predicate<Integer> comparatorPredicate = ComparatorPredicate.comparatorPredicate(
                null, comparator, ComparatorPredicate.Criterion.EQUAL);
        OrPredicate<Predicate<Integer>> orPredicate = new OrPredicate<>(
                IdentityPredicate.identityPredicate(comparatorPredicate),
                IdentityPredicate.identityPredicate(comparatorPredicate));

        PredicatedMap<Boolean, Predicate<Integer>> predicatedMap = PredicatedMap.predicatedMap(
                map, uniquePredicate, orPredicate);

        PredicateTransformer<Integer> predicateTransformer = new PredicateTransformer<>(comparatorPredicate);
        Boolean transformedValue = predicateTransformer.transform(-1);
        predicatedMap.validate(transformedValue, comparatorPredicate);

        assertEquals(0, predicatedMap.size());
    }

    @Test(timeout = 4000)
    public void testPredicatedMapIsNotEmpty() {
        Map<Predicate<Object>, Predicate<Integer>> map = new HashMap<>();
        Predicate<Object> notNullPredicate = NotNullPredicate.notNullPredicate();
        Integer integer = 0;
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());

        Predicate<Integer> comparatorPredicate = new ComparatorPredicate<>(
                integer, comparator, ComparatorPredicate.Criterion.GREATER);
        map.put(notNullPredicate, comparatorPredicate);

        PredicatedMap<Predicate<Object>, Predicate<Integer>> predicatedMap = PredicatedMap.predicatedMap(
                map, notNullPredicate, notNullPredicate);

        assertFalse(predicatedMap.isEmpty());
    }

    @Test(timeout = 4000)
    public void testIsSetValueCheckingReturnsTrue() {
        UniquePredicate<Object> uniquePredicate = new UniquePredicate<>();
        Map<HashMap<Integer, Object>, Transformer<Integer, Integer>> map = new HashMap<>();

        PredicatedMap<HashMap<Integer, Object>, Transformer<Integer, Integer>> predicatedMap = new PredicatedMap<>(
                map, uniquePredicate, uniquePredicate);

        assertTrue(predicatedMap.isSetValueChecking());
    }

    @Test(timeout = 4000)
    public void testIsSetValueCheckingReturnsFalse() {
        Map<Object, HashMap<Object, Object>> map = new HashMap<>();
        PredicatedMap<Object, HashMap<Object, Object>> predicatedMap = new PredicatedMap<>(
                map, null, null);

        assertFalse(predicatedMap.isSetValueChecking());
    }

    @Test(timeout = 4000)
    public void testCheckSetValueReturnsNull() {
        Map<HashMap<Object, Object>, Integer> map = new HashMap<>();
        UniquePredicate<Object> uniquePredicate = new UniquePredicate<>();

        PredicatedMap<HashMap<Object, Object>, Integer> predicatedMap = new PredicatedMap<>(
                map, uniquePredicate, uniquePredicate);

        Integer result = predicatedMap.checkSetValue(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testValidateThrowsRuntimeException() {
        Map<Integer, Predicate<Object>> map = new HashMap<>();
        Transformer<Integer, Boolean> nullTransformer = ConstantTransformer.nullTransformer();
        TransformerPredicate<Integer> transformerPredicate = new TransformerPredicate<>(nullTransformer);

        PredicatedMap<Integer, Predicate<Object>> predicatedMap = new PredicatedMap<>(
                map, transformerPredicate, null);

        Integer integer = -2340;

        try {
            predicatedMap.validate(integer, null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.TransformerPredicate", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidateThrowsNullPointerException() {
        Map<Predicate<Integer>, HashMap<Object, Integer>> map = new HashMap<>();
        PredicatedMap<Predicate<Integer>, HashMap<Object, Integer>> predicatedMap = new PredicatedMap<>(
                map, null, null);

        PredicateTransformer<Object> predicateTransformer = new PredicateTransformer<>(null);
        Predicate<Object> transformerPredicate = TransformerPredicate.transformerPredicate(predicateTransformer);

        PredicatedMap<Predicate<Integer>, HashMap<Object, Integer>> anotherPredicatedMap = PredicatedMap.predicatedMap(
                predicatedMap, transformerPredicate, null);

        Predicate<Integer> truePredicate = TruePredicate.truePredicate();
        HashMap<Object, Integer> emptyMap = new HashMap<>();

        try {
            anotherPredicatedMap.validate(truePredicate, emptyMap);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.functors.PredicateTransformer", e);
        }
    }

    // Additional tests can be refactored similarly...

}