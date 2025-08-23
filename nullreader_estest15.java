package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest15 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        NullReader nullReader0 = new NullReader();
        nullReader0.ready();
        int int0 = 2143;
        nullReader0.mark(2143);
        nullReader0.markSupported();
        nullReader0.getSize();
        nullReader0.getSize();
        nullReader0.read();
        char[] charArray0 = new char[0];
        int int1 = 0;
        int int2 = (-574);
        try {
            nullReader0.INSTANCE.read(charArray0, 0, 2143);
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
