package org.mockito.internal.invocation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;

public class TypeSafeMatching_ESTestTest5 extends TypeSafeMatching_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        ArgumentMatcherAction argumentMatcherAction0 = TypeSafeMatching.matchesTypeSafe();
        Integer integer0 = new Integer(781);
        GreaterOrEqual<Integer> greaterOrEqual0 = new GreaterOrEqual<Integer>(integer0);
        Object object0 = new Object();
        boolean boolean0 = argumentMatcherAction0.apply(greaterOrEqual0, object0);
        assertFalse(boolean0);
    }
}
