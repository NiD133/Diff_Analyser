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
    public void test23() throws Throwable {
        NullReader nullReader0 = new NullReader(0L);
        try {
            nullReader0.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // No position has been marked
            //
            verifyException("org.example.NullReader", e);
        }
    }
}
