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

public class TypeSafeMatching_ESTestTest1 extends TypeSafeMatching_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        ArgumentMatcherAction argumentMatcherAction0 = TypeSafeMatching.matchesTypeSafe();
        // Undeclared exception!
        try {
            argumentMatcherAction0.apply((ArgumentMatcher) null, (Object) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.mockito.internal.invocation.TypeSafeMatching", e);
        }
    }
}
