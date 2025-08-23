package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FixedOrderComparator_ESTestTest30 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Class<Object> class0 = Object.class;
        InstanceofPredicate instanceofPredicate0 = new InstanceofPredicate(class0);
        PredicateTransformer<Comparable<Boolean>> predicateTransformer0 = new PredicateTransformer<Comparable<Boolean>>(instanceofPredicate0);
        Boolean boolean0 = predicateTransformer0.transform((Comparable<Boolean>) null);
        Boolean[] booleanArray0 = new Boolean[4];
        booleanArray0[0] = boolean0;
        FixedOrderComparator<Boolean> fixedOrderComparator0 = new FixedOrderComparator<Boolean>(booleanArray0);
        boolean boolean1 = fixedOrderComparator0.addAsEqual(boolean0, boolean0);
        assertFalse(boolean1);
    }
}