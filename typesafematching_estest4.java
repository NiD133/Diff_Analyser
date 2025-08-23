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

public class TypeSafeMatching_ESTestTest4 extends TypeSafeMatching_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        ArgumentMatcherAction argumentMatcherAction0 = TypeSafeMatching.matchesTypeSafe();
        Class<Integer> class0 = Integer.class;
        NotNull<Integer> notNull0 = new NotNull<Integer>(class0);
        boolean boolean0 = argumentMatcherAction0.apply(notNull0, (Object) null);
        assertFalse(boolean0);
    }
}
