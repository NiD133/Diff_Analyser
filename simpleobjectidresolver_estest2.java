package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleObjectIdResolver_ESTestTest2 extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        SimpleObjectIdResolver simpleObjectIdResolver0 = new SimpleObjectIdResolver();
        // Undeclared exception!
        try {
            simpleObjectIdResolver0.canUseFor((ObjectIdResolver) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }
}
