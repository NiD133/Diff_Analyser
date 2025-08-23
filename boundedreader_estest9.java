package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BoundedReader_ESTestTest9 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        StringReader stringReader0 = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 0);
        boundedReader0.close();
        try {
            boundedReader0.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Stream closed
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
