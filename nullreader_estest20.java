package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest20 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        NullReader nullReader0 = NullReader.INSTANCE;
        try {
            nullReader0.read();
            //  fail("Expecting exception: IOException");
            // Unstable assertion
        } catch (IOException e) {
            //
            // Read after end of file
            //
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }
}
