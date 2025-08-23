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

public class FixedOrderComparator_ESTestTest2 extends FixedOrderComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ConstantTransformer<Boolean, Boolean> constantTransformer0 = new ConstantTransformer<Boolean, Boolean>((Boolean) null);
        Function<Boolean, Boolean>[] functionArray0 = (Function<Boolean, Boolean>[]) Array.newInstance(Function.class, 7);
        functionArray0[0] = (Function<Boolean, Boolean>) constantTransformer0;
        FixedOrderComparator<Function<Boolean, Boolean>> fixedOrderComparator0 = new FixedOrderComparator<Function<Boolean, Boolean>>(functionArray0);
        int int0 = fixedOrderComparator0.compare(constantTransformer0, functionArray0[3]);
        assertTrue(fixedOrderComparator0.isLocked());
        assertEquals((-1), int0);
    }
}
