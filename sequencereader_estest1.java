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

public class SequenceReader_ESTestTest1 extends SequenceReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        ArrayDeque<StringReader> arrayDeque0 = new ArrayDeque<StringReader>();
        StringReader stringReader0 = new StringReader("!DROK>c");
        arrayDeque0.add(stringReader0);
        StringReader stringReader1 = new StringReader("org.apache.commons.io.filefilter.CanExecuteFileFilter");
        arrayDeque0.add(stringReader1);
        SequenceReader sequenceReader0 = new SequenceReader(arrayDeque0);
        long long0 = sequenceReader0.skip(204L);
        assertEquals(60L, long0);
    }
}
