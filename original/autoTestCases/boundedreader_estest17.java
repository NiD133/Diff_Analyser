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
    public void test16() throws Throwable {
        StringReader stringReader0 = new StringReader("QN?");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 2680);
        char[] charArray0 = new char[9];
        int int0 = boundedReader0.read(charArray0);
        assertArrayEquals(new char[] { 'Q', 'N', '?', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
        assertEquals(3, int0);
        boundedReader0.mark(1);
        int int1 = boundedReader0.read();
        assertEquals((-1), int1);
    }
}
