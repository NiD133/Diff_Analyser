package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest10 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.EMPTY;
        String string0 = jsonTypeInfo_Value0.toString();
        assertEquals("JsonTypeInfo.Value(idType=NONE,includeAs=PROPERTY,propertyName=null,defaultImpl=NULL,idVisible=false,requireTypeIdForSubtypes=null)", string0);
    }
}
