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
    public void test11() throws Throwable {
        NullReader nullReader0 = NullReader.INSTANCE;
        long long0 = nullReader0.getSize();
        assertEquals(0L, long0);
    }
}
