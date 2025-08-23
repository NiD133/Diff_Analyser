package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest24 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        JsonTypeInfo jsonTypeInfo0 = mock(JsonTypeInfo.class, CALLS_REAL_METHODS);
        doReturn((Class) null).when(jsonTypeInfo0).defaultImpl();
        doReturn((JsonTypeInfo.As) null).when(jsonTypeInfo0).include();
        doReturn((String) null).when(jsonTypeInfo0).property();
        doReturn((OptBoolean) null).when(jsonTypeInfo0).requireTypeIdForSubtypes();
        doReturn((JsonTypeInfo.Id) null).when(jsonTypeInfo0).use();
        doReturn(false).when(jsonTypeInfo0).visible();
        // Undeclared exception!
        try {
            JsonTypeInfo.Value.from(jsonTypeInfo0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.annotation.JsonTypeInfo$Value", e);
        }
    }
}
