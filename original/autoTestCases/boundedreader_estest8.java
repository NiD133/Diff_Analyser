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
    public void test07() throws Throwable {
        StringReader stringReader0 = new StringReader("j");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 1);
        boundedReader0.close();
        char[] charArray0 = new char[6];
        try {
            boundedReader0.read(charArray0, (-2537), 699);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Stream closed
            //
            verifyException("java.io.StringReader", e);
        }
    }
}
