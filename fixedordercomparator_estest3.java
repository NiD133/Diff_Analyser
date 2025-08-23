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

public class FixedOrderComparator_ESTestTest3 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Object[] objectArray0 = new Object[2];
        Object object0 = new Object();
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(objectArray0);
        Object object1 = new Object();
        // Undeclared exception!
        try {
            fixedOrderComparator0.addAsEqual(object1, object0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // java.lang.Object@2fb7d513 not known to org.apache.commons.collections4.comparators.FixedOrderComparator@1cc73c3c
            //
            verifyException("org.apache.commons.collections4.comparators.FixedOrderComparator", e);
        }
    }
}
