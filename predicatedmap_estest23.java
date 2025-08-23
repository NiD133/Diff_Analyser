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

public class PredicatedMap_ESTestTest23 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        UniquePredicate<Object> uniquePredicate0 = new UniquePredicate<Object>();
        HashMap<Object, HashMap<Integer, Integer>> hashMap0 = new HashMap<Object, HashMap<Integer, Integer>>();
        PredicatedMap<Object, HashMap<Integer, Integer>> predicatedMap0 = new PredicatedMap<Object, HashMap<Integer, Integer>>(hashMap0, (Predicate<? super Object>) null, uniquePredicate0);
        HashMap<Integer, Integer> hashMap1 = new HashMap<Integer, Integer>();
        predicatedMap0.putIfAbsent(hashMap0, hashMap1);
        PredicatedMap<Object, HashMap<Integer, Integer>> predicatedMap1 = null;
        try {
            predicatedMap1 = new PredicatedMap<Object, HashMap<Integer, Integer>>(predicatedMap0, uniquePredicate0, (Predicate<? super HashMap<Integer, Integer>>) null);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
