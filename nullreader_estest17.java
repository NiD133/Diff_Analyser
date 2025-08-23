package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest17 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        NullReader nullReader0 = new NullReader();
        nullReader0.read();
        try {
            nullReader0.skip((-1));
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Skip after end of file
            //
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }
}
