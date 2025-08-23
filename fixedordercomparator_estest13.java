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

public class FixedOrderComparator_ESTestTest13 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        FixedOrderComparator<Object>[] fixedOrderComparatorArray0 = (FixedOrderComparator<Object>[]) Array.newInstance(FixedOrderComparator.class, 5);
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(linkedList0);
        fixedOrderComparatorArray0[0] = fixedOrderComparator0;
        FixedOrderComparator<FixedOrderComparator<Object>> fixedOrderComparator1 = new FixedOrderComparator<FixedOrderComparator<Object>>(fixedOrderComparatorArray0);
        boolean boolean0 = fixedOrderComparator1.add(fixedOrderComparator0);
        assertFalse(boolean0);
    }
}
