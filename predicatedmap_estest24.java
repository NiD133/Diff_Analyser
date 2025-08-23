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

public class PredicatedMap_ESTestTest24 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        HashMap<Integer, HashMap<Integer, Integer>> hashMap0 = new HashMap<Integer, HashMap<Integer, Integer>>();
        LinkedList<Predicate<Object>> linkedList0 = new LinkedList<Predicate<Object>>();
        Predicate<Object> predicate0 = NonePredicate.nonePredicate((Collection<? extends Predicate<? super Object>>) linkedList0);
        PredicatedMap<Integer, HashMap<Integer, Integer>> predicatedMap0 = PredicatedMap.predicatedMap((Map<Integer, HashMap<Integer, Integer>>) hashMap0, (Predicate<? super Integer>) predicate0, (Predicate<? super HashMap<Integer, Integer>>) predicate0);
        Integer integer0 = new Integer(0);
        HashMap<Integer, Integer> hashMap1 = new HashMap<Integer, Integer>();
        predicatedMap0.put(integer0, hashMap1);
        HashMap<Integer, Object> hashMap2 = new HashMap<Integer, Object>();
        Predicate<Integer> predicate1 = NullPredicate.nullPredicate();
        PredicatedMap<Integer, Object> predicatedMap1 = PredicatedMap.predicatedMap((Map<Integer, Object>) hashMap2, (Predicate<? super Integer>) predicate1, (Predicate<? super Object>) predicate0);
        // Undeclared exception!
        try {
            predicatedMap1.putAll(hashMap0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add key - Predicate rejected it
            //
            verifyException("org.apache.commons.collections4.map.PredicatedMap", e);
        }
    }
}
