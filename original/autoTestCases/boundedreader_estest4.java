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
    public void test03() throws Throwable {
        StringReader stringReader0 = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 1);
        char[] charArray0 = new char[6];
        int int0 = boundedReader0.read(charArray0, 1, 1);
        assertEquals(1, int0);
        assertArrayEquals(new char[] { '\u0000', 'o', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
    }
}
