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

public class FixedOrderComparator_ESTestTest14 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>();
        Object object0 = new Object();
        boolean boolean0 = fixedOrderComparator0.add(object0);
        assertTrue(boolean0);
    }
}
