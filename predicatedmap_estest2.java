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

public class PredicatedMap_ESTestTest2 extends PredicatedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        HashMap<Boolean, Predicate<Integer>> hashMap0 = new HashMap<Boolean, Predicate<Integer>>();
        UniquePredicate<Boolean> uniquePredicate0 = new UniquePredicate<Boolean>();
        Comparator<Integer> comparator0 = (Comparator<Integer>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator0).compare(anyInt(), anyInt());
        ComparatorPredicate.Criterion comparatorPredicate_Criterion0 = ComparatorPredicate.Criterion.EQUAL;
        Predicate<Integer> predicate0 = ComparatorPredicate.comparatorPredicate((Integer) null, comparator0, (ComparatorPredicate.Criterion) comparatorPredicate_Criterion0);
        Predicate<Predicate<Integer>> predicate1 = IdentityPredicate.identityPredicate(predicate0);
        OrPredicate<Predicate<Integer>> orPredicate0 = new OrPredicate<Predicate<Integer>>(predicate1, predicate1);
        PredicatedMap<Boolean, Predicate<Integer>> predicatedMap0 = PredicatedMap.predicatedMap((Map<Boolean, Predicate<Integer>>) hashMap0, (Predicate<? super Boolean>) uniquePredicate0, (Predicate<? super Predicate<Integer>>) orPredicate0);
        PredicateTransformer<Integer> predicateTransformer0 = new PredicateTransformer<Integer>(predicate0);
        Integer integer0 = new Integer((-1));
        Boolean boolean0 = predicateTransformer0.transform(integer0);
        predicatedMap0.validate(boolean0, predicate0);
        assertEquals(0, predicatedMap0.size());
    }
}
