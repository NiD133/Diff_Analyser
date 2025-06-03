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
    public void test30() throws Throwable {
        NullReader nullReader0 = new NullReader((-1324L));
        boolean boolean0 = nullReader0.markSupported();
        assertEquals((-1324L), nullReader0.getSize());
        assertTrue(boolean0);
    }
}
