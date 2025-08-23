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

public class FixedOrderComparator_ESTestTest28 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Boolean[] booleanArray0 = new Boolean[21];
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(booleanArray0);
        FixedOrderComparator.UnknownObjectBehavior fixedOrderComparator_UnknownObjectBehavior0 = FixedOrderComparator.UnknownObjectBehavior.AFTER;
        // Undeclared exception!
        try {
            fixedOrderComparator0.compare((Object) null, fixedOrderComparator_UnknownObjectBehavior0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Attempting to compare unknown object AFTER
            //
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }
}
