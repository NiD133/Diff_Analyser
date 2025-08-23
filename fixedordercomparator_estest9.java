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

public class FixedOrderComparator_ESTestTest9 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(linkedList0);
        Object object0 = new Object();
        IdentityPredicate<Object> identityPredicate0 = new IdentityPredicate<Object>(object0);
        PredicateTransformer<Comparable<Boolean>> predicateTransformer0 = new PredicateTransformer<Comparable<Boolean>>(identityPredicate0);
        FixedOrderComparator.UnknownObjectBehavior fixedOrderComparator_UnknownObjectBehavior0 = FixedOrderComparator.UnknownObjectBehavior.AFTER;
        fixedOrderComparator0.setUnknownObjectBehavior(fixedOrderComparator_UnknownObjectBehavior0);
        Boolean boolean0 = predicateTransformer0.transform(false);
        fixedOrderComparator0.compare(boolean0, object0);
        // Undeclared exception!
        try {
            fixedOrderComparator0.add(linkedList0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Cannot modify a FixedOrderComparator after a comparison
            //
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }
}
