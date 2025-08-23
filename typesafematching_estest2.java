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

public class TypeSafeMatching_ESTestTest2 extends TypeSafeMatching_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        ArgumentMatcherAction argumentMatcherAction0 = TypeSafeMatching.matchesTypeSafe();
        CompareEqual<Integer> compareEqual0 = new CompareEqual<Integer>((Integer) null);
        Not not0 = new Not(compareEqual0);
        // Undeclared exception!
        try {
            argumentMatcherAction0.apply(not0, not0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class org.mockito.internal.matchers.Not cannot be cast to class java.lang.Comparable (org.mockito.internal.matchers.Not is in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @6ab9d1e3; java.lang.Comparable is in module java.base of loader 'bootstrap')
            //
            verifyException("org.mockito.internal.matchers.CompareTo", e);
        }
    }
}
