package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        LineIterator lineIterator0 = null;
        try {
            lineIterator0 = new LineIterator((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // reader
            //
            verifyException("java.util.Objects", e);
        }
    }
}
