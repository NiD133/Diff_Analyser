package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SequenceReader_ESTestTest3 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ArrayDeque<StringReader> arrayDeque0 = new ArrayDeque<StringReader>();
        SequenceReader sequenceReader0 = new SequenceReader(arrayDeque0);
        // Undeclared exception!
        try {
            sequenceReader0.read((char[]) null, (-1), (-1));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // cbuf
            //
            verifyException("java.util.Objects", e);
        }
    }
}
