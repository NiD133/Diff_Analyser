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

public class FixedOrderComparator_ESTestTest7 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        LinkedList<FixedOrderComparator<Object>> linkedList0 = new LinkedList<FixedOrderComparator<Object>>();
        FixedOrderComparator<FixedOrderComparator<Object>> fixedOrderComparator0 = new FixedOrderComparator<FixedOrderComparator<Object>>(linkedList0);
        // Undeclared exception!
        try {
            fixedOrderComparator0.setUnknownObjectBehavior((FixedOrderComparator.UnknownObjectBehavior) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // unknownObjectBehavior
            //
            verifyException("java.util.Objects", e);
        }
    }
}
