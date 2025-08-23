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

public class PredicatedMap_ESTestTest8 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        HashMap<Predicate<Integer>, HashMap<Object, Integer>> hashMap0 = new HashMap<Predicate<Integer>, HashMap<Object, Integer>>();
        PredicatedMap<Predicate<Integer>, HashMap<Object, Integer>> predicatedMap0 = new PredicatedMap<Predicate<Integer>, HashMap<Object, Integer>>(hashMap0, (Predicate<? super Predicate<Integer>>) null, (Predicate<? super HashMap<Object, Integer>>) null);
        PredicateTransformer<Object> predicateTransformer0 = new PredicateTransformer<Object>((Predicate<? super Object>) null);
        Predicate<Object> predicate0 = TransformerPredicate.transformerPredicate((Transformer<? super Object, Boolean>) predicateTransformer0);
        PredicatedMap<Predicate<Integer>, HashMap<Object, Integer>> predicatedMap1 = PredicatedMap.predicatedMap((Map<Predicate<Integer>, HashMap<Object, Integer>>) predicatedMap0, (Predicate<? super Predicate<Integer>>) predicate0, (Predicate<? super HashMap<Object, Integer>>) null);
        Predicate<Integer> predicate1 = TruePredicate.truePredicate();
        HashMap<Object, Integer> hashMap1 = new HashMap<Object, Integer>();
        // Undeclared exception!
        try {
            predicatedMap1.validate(predicate1, hashMap1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.functors.PredicateTransformer", e);
        }
    }
}