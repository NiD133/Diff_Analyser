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

public class PredicatedMap_ESTestTest9 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        HashMap<Object, HashMap<Integer, Integer>> hashMap0 = new HashMap<Object, HashMap<Integer, Integer>>();
        HashMap<HashMap<Integer, Object>, Object> hashMap1 = new HashMap<HashMap<Integer, Object>, Object>();
        Predicate<Object> predicate0 = NullPredicate.nullPredicate();
        PredicatedMap<HashMap<Integer, Object>, Object> predicatedMap0 = new PredicatedMap<HashMap<Integer, Object>, Object>(hashMap1, predicate0, predicate0);
        HashMap<Integer, Object> hashMap2 = new HashMap<Integer, Object>();
        // Undeclared exception!
        try {
            predicatedMap0.validate(hashMap2, hashMap0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add key - Predicate rejected it
            //
            verifyException("org.apache.commons.collections4.map.PredicatedMap", e);
        }
    }
}