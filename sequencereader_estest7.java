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

public class SequenceReader_ESTestTest7 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Vector<StringReader> vector0 = new Vector<StringReader>();
        SequenceReader sequenceReader0 = new SequenceReader(vector0);
        StringReader stringReader0 = new StringReader("_cA{/)2>I@4NJ(");
        vector0.add(stringReader0);
        // Undeclared exception!
        try {
            sequenceReader0.close();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.Vector$Itr", e);
        }
    }
}
