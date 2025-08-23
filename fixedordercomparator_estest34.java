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

public class FixedOrderComparator_ESTestTest34 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 14);
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(predicateArray0);
        // Undeclared exception!
        try {
            fixedOrderComparator0.compare(fixedOrderComparator0, (Object) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Attempting to compare unknown object org.apache.commons.collections4.comparators.FixedOrderComparator@1ccc9b9e
            //
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }
}
