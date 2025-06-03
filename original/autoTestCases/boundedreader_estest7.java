package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        StringReader stringReader0 = new StringReader("g`8B;^5");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 820);
        // Undeclared exception!
        try {
            boundedReader0.read((char[]) null, 103, 78);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }
}
