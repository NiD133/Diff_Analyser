package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest8 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Comparable<Object> comparable0 = (Comparable<Object>) mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparable0).toString();
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem(comparable0, (Object) null);
        ComparableObjectItem comparableObjectItem1 = new ComparableObjectItem(comparableObjectItem0, (Object) null);
        // Undeclared exception!
        try {
            comparableObjectItem1.compareTo(comparableObjectItem0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class org.evosuite.shaded.org.mockito.codegen.Comparable$MockitoMock$1384067238 cannot be cast to class org.jfree.data.ComparableObjectItem (org.evosuite.shaded.org.mockito.codegen.Comparable$MockitoMock$1384067238 is in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @4995c230; org.jfree.data.ComparableObjectItem is in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @3d9f867a)
            //
            verifyException("org.jfree.data.ComparableObjectItem", e);
        }
    }
}
