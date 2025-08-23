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

public class BoundedReader_ESTestTest13 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        BoundedReader boundedReader0 = new BoundedReader((Reader) null, 1);
        // Undeclared exception!
        try {
            boundedReader0.read();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }
}
