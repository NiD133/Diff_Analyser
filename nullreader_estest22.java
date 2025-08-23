package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest22 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        NullReader nullReader0 = new NullReader((-1935L), true, true);
        char[] charArray0 = new char[0];
        nullReader0.read(charArray0, 1095, 1095);
        try {
            nullReader0.read(charArray0);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }
}
