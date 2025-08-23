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

public class FixedOrderComparator_ESTestTest26 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        FixedOrderComparator<Boolean>[] fixedOrderComparatorArray0 = (FixedOrderComparator<Boolean>[]) Array.newInstance(FixedOrderComparator.class, 4);
        FixedOrderComparator<Object> fixedOrderComparator0 = new FixedOrderComparator<Object>(fixedOrderComparatorArray0);
        FixedOrderComparator.UnknownObjectBehavior fixedOrderComparator_UnknownObjectBehavior0 = FixedOrderComparator.UnknownObjectBehavior.BEFORE;
        fixedOrderComparator0.setUnknownObjectBehavior(fixedOrderComparator_UnknownObjectBehavior0);
        Object object0 = new Object();
        int int0 = fixedOrderComparator0.compare((Object) null, object0);
        assertTrue(fixedOrderComparator0.isLocked());
        assertEquals(1, int0);
    }
}
