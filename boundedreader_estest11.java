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

public class BoundedReader_ESTestTest11 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        StringReader stringReader0 = new StringReader("4s");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 179);
        char[] charArray0 = new char[0];
        // Undeclared exception!
        try {
            boundedReader0.read(charArray0, 179, 179);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 179
            //
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }
}
