package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TextHelpAppendable_ESTestTest51 extends TextHelpAppendable_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        TextHelpAppendable textHelpAppendable0 = TextHelpAppendable.systemOut();
        StringWriter stringWriter0 = new StringWriter(7);
        textHelpAppendable0.setIndent((-84));
        StringBuffer stringBuffer0 = stringWriter0.getBuffer();
        TextHelpAppendable textHelpAppendable1 = new TextHelpAppendable(stringBuffer0);
        textHelpAppendable1.printWrapped("org.apache.commons.cli.help.Util");
        // Undeclared exception!
        try {
            textHelpAppendable0.appendTitle(stringBuffer0);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.cli.help.Util", e);
        }
    }
}
