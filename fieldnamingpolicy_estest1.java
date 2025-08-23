package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest1 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        FieldNamingPolicy[] fieldNamingPolicyArray0 = FieldNamingPolicy.values();
        assertEquals(7, fieldNamingPolicyArray0.length);
    }
}
