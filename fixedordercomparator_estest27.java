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

public class FixedOrderComparator_ESTestTest27 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 14);
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(predicateArray0);
        FixedOrderComparator.UnknownObjectBehavior fixedOrderComparator_UnknownObjectBehavior0 = FixedOrderComparator.UnknownObjectBehavior.BEFORE;
        fixedOrderComparator0.setUnknownObjectBehavior(fixedOrderComparator_UnknownObjectBehavior0);
        int int0 = fixedOrderComparator0.compare(fixedOrderComparator0, (Object) null);
        assertTrue(fixedOrderComparator0.isLocked());
        assertEquals((-1), int0);
    }
}
