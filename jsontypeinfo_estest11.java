package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class JsonTypeInfo_ESTestTest11 extends JsonTypeInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        JsonTypeInfo.Id jsonTypeInfo_Id0 = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As jsonTypeInfo_As0 = JsonTypeInfo.As.WRAPPER_OBJECT;
        Class<Object> class0 = Object.class;
        Boolean boolean0 = Boolean.valueOf("@2LLQRbW9{J2*\"1GY");
        JsonTypeInfo.Value jsonTypeInfo_Value0 = JsonTypeInfo.Value.construct(jsonTypeInfo_Id0, jsonTypeInfo_As0, "@2LLQRbW9{J2*\"1GY", class0, false, boolean0);
        String string0 = jsonTypeInfo_Value0.toString();
        assertEquals("JsonTypeInfo.Value(idType=MINIMAL_CLASS,includeAs=WRAPPER_OBJECT,propertyName=@2LLQRbW9{J2*\"1GY,defaultImpl=java.lang.Object,idVisible=false,requireTypeIdForSubtypes=false)", string0);
    }
}
