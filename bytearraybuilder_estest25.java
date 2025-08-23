package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest25 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(0);
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, (-129));
        // Undeclared exception!
        try {
            byteArrayBuilder1.write((-129));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -129
            //
            verifyException("com.fasterxml.jackson.core.util.ByteArrayBuilder", e);
        }
    }
}
