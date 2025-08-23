package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest13 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        NullReader nullReader0 = new NullReader(7L, true, true);
        nullReader0.skip(7L);
        try {
            nullReader0.read();
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }
}
