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

public class PredicatedMap_ESTestTest7 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        HashMap<Integer, Predicate<Object>> hashMap0 = new HashMap<Integer, Predicate<Object>>();
        Transformer<Integer, Boolean> transformer0 = ConstantTransformer.nullTransformer();
        TransformerPredicate<Integer> transformerPredicate0 = new TransformerPredicate<Integer>(transformer0);
        PredicatedMap<Integer, Predicate<Object>> predicatedMap0 = new PredicatedMap<Integer, Predicate<Object>>(hashMap0, transformerPredicate0, (Predicate<? super Predicate<Object>>) null);
        Integer integer0 = new Integer((-2340));
        // Undeclared exception!
        try {
            predicatedMap0.validate(integer0, (Predicate<Object>) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Transformer must return an instanceof Boolean, it was a null object
            //
            verifyException("org.apache.commons.collections4.functors.TransformerPredicate", e);
        }
    }
}
