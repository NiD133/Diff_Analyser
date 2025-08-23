package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest31 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial((byte[]) null, 2000);
        // Undeclared exception!
        try {
            byteArrayBuilder0.finishCurrentSegment();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
