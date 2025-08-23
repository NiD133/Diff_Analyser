package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.OnePredicate;
import org.apache.commons.collections4.functors.OrPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class PredicatedMap_ESTestTest3 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        HashMap<Predicate<Object>, Predicate<Integer>> hashMap0 = new HashMap<Predicate<Object>, Predicate<Integer>>();
        Predicate<Object> predicate0 = NotNullPredicate.notNullPredicate();
        Integer integer0 = new Integer(0);
        Comparator<Integer> comparator0 = (Comparator<Integer>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        ComparatorPredicate.Criterion comparatorPredicate_Criterion0 = ComparatorPredicate.Criterion.GREATER;
        ComparatorPredicate<Integer> comparatorPredicate0 = new ComparatorPredicate<Integer>(integer0, comparator0, comparatorPredicate_Criterion0);
        hashMap0.put(predicate0, comparatorPredicate0);
        PredicatedMap<Predicate<Object>, Predicate<Integer>> predicatedMap0 = PredicatedMap.predicatedMap((Map<Predicate<Object>, Predicate<Integer>>) hashMap0, (Predicate<? super Predicate<Object>>) predicate0, (Predicate<? super Predicate<Integer>>) predicate0);
        assertFalse(predicatedMap0.isEmpty());
    }
}