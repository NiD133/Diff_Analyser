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
    public void test11() throws Throwable {
        StringReader stringReader0 = new StringReader("SQ\"Rz~>o\"ggtg97eV");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, (-585));
        // Undeclared exception!
        try {
            boundedReader0.mark((-585));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Read-ahead limit < 0
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
