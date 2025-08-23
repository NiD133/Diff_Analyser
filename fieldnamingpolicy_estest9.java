package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest9 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        String string0 = FieldNamingPolicy.separateCamelCase("LOWER_CASE_WITH_UNDERSCORES", 'D');
        assertEquals("LDODWDEDR_DCDADSDE_DWDIDTDH_DUDNDDDEDRDSDCDODRDEDS", string0);
    }
}
