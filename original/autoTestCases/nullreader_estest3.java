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
    public void test02() throws Throwable {
        NullReader nullReader0 = new NullReader();
        char[] charArray0 = new char[2];
        nullReader0.processChars(charArray0, 1542, 1542);
        assertTrue(nullReader0.markSupported());
    }
}
