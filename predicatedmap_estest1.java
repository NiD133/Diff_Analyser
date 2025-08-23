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

public class PredicatedMap_ESTestTest1 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        HashMap<HashMap<Integer, Object>, Integer> hashMap0 = new HashMap<HashMap<Integer, Object>, Integer>();
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);
        AnyPredicate<Object> anyPredicate0 = new AnyPredicate<Object>(predicateArray0);
        OnePredicate<Object> onePredicate0 = new OnePredicate<Object>(predicateArray0);
        PredicatedMap<HashMap<Integer, Object>, Integer> predicatedMap0 = PredicatedMap.predicatedMap((Map<HashMap<Integer, Object>, Integer>) hashMap0, (Predicate<? super HashMap<Integer, Object>>) onePredicate0, (Predicate<? super Integer>) onePredicate0);
        PredicatedMap<HashMap<Integer, Object>, Integer> predicatedMap1 = new PredicatedMap<HashMap<Integer, Object>, Integer>(predicatedMap0, anyPredicate0, predicateArray0[0]);
        // Undeclared exception!
        try {
            predicatedMap1.checkSetValue((Integer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.map.PredicatedMap", e);
        }
    }
}
