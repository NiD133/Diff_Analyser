package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest19 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        NullReader nullReader0 = new NullReader(959L, true, true);
        try {
            nullReader0.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // No position has been marked
            //
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }
}
