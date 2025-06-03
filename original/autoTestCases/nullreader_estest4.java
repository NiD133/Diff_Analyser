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
    public void test03() throws Throwable {
        NullReader nullReader0 = new NullReader(597L);
        long long0 = nullReader0.skip(0);
        assertEquals(0L, long0);
        assertTrue(nullReader0.markSupported());
    }
}
