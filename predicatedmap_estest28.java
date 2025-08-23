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

public class PredicatedMap_ESTestTest28 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Predicate<Object> predicate0 = ExceptionPredicate.exceptionPredicate();
        HashMap<HashMap<Object, Integer>, Object> hashMap0 = new HashMap<HashMap<Object, Integer>, Object>();
        PredicatedMap<HashMap<Object, Integer>, Object> predicatedMap0 = PredicatedMap.predicatedMap((Map<HashMap<Object, Integer>, Object>) hashMap0, (Predicate<? super HashMap<Object, Integer>>) predicate0, (Predicate<? super Object>) predicate0);
        HashMap<Object, Integer> hashMap1 = new HashMap<Object, Integer>();
        // Undeclared exception!
        try {
            predicatedMap0.put(hashMap1, hashMap1);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // ExceptionPredicate invoked
            //
            verifyException("org.apache.commons.collections4.functors.ExceptionPredicate", e);
        }
    }
}
