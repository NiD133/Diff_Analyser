package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleObjectIdResolver_ESTestTest7 extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test6() throws Throwable {
        SimpleObjectIdResolver simpleObjectIdResolver0 = new SimpleObjectIdResolver();
        Class<String> class0 = String.class;
        ObjectIdGenerator.IdKey objectIdGenerator_IdKey0 = new ObjectIdGenerator.IdKey(class0, class0, "");
        simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, "");
        // Undeclared exception!
        try {
            simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, "");
            //  fail("Expecting exception: IllegalStateException");
            // Unstable assertion
        } catch (IllegalStateException e) {
            //
            // Object Id conflict: Id [ObjectId: key=, type=java.lang.String, scope=java.lang.String] already bound to an Object (type: `java.lang.String`, value: \"\"): attempt to re-bind to a different Object (type: `java.lang.String`, value: \"\")
            //
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }
}
