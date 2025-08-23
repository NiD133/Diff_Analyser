package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest7 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        String string0 = FieldNamingPolicy.upperCaseFirstLetter("\"1+ejk5p_l;*");
        assertEquals("\"1+Ejk5p_l;*", string0);
    }
}
