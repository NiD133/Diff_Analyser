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
    public void test19() throws Throwable {
        NullReader nullReader0 = new NullReader();
        nullReader0.mark((-1791));
        try {
            nullReader0.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Marked position [0] is no longer valid - passed the read limit [-1791]
            //
            verifyException("org.example.NullReader", e);
        }
    }
}
