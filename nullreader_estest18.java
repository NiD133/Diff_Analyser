package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest18 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        NullReader nullReader0 = new NullReader((-1388L), false, false);
        // Undeclared exception!
        try {
            nullReader0.reset();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // mark/reset not supported
            //
            verifyException("org.apache.commons.io.input.UnsupportedOperationExceptions", e);
        }
    }
}
