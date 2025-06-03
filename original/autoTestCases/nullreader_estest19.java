package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        NullReader nullReader0 = new NullReader();
        char[] charArray0 = new char[5];
        nullReader0.read(charArray0, 640, 0);
        try {
            nullReader0.skip(0L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Skip after end of file
            //
            verifyException("org.example.NullReader", e);
        }
    }
}
