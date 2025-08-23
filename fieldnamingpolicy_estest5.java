package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest5 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        String string0 = FieldNamingPolicy.upperCaseFirstLetter("cA:[IY:hB?-NT@IV/y");
        assertEquals("CA:[IY:hB?-NT@IV/y", string0);
    }
}
