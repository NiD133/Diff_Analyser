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

public class PredicatedMap_ESTestTest29 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        HashMap<Predicate<Object>, Predicate<Object>> hashMap0 = new HashMap<Predicate<Object>, Predicate<Object>>();
        IdentityPredicate<Object> identityPredicate0 = new IdentityPredicate<Object>(hashMap0);
        PredicatedMap<Predicate<Object>, Predicate<Object>> predicatedMap0 = new PredicatedMap<Predicate<Object>, Predicate<Object>>(hashMap0, identityPredicate0, identityPredicate0);
        // Undeclared exception!
        try {
            predicatedMap0.put(identityPredicate0, identityPredicate0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot add key - Predicate rejected it
            //
            verifyException("org.apache.commons.collections4.map.PredicatedMap", e);
        }
    }
}