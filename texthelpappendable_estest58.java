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

public class TextHelpAppendable_ESTestTest58 extends TextHelpAppendable_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test57() throws Throwable {
        TextHelpAppendable textHelpAppendable0 = TextHelpAppendable.systemOut();
        LinkedList<TextStyle> linkedList0 = new LinkedList<TextStyle>();
        TextStyle textStyle0 = TextStyle.DEFAULT;
        linkedList0.add(textStyle0);
        Vector<String> vector0 = new Vector<String>();
        HashSet<List<String>> hashSet0 = new HashSet<List<String>>();
        TableDefinition tableDefinition0 = TableDefinition.from("Hi 2F$fWw", linkedList0, vector0, hashSet0);
        // Undeclared exception!
        try {
            textHelpAppendable0.appendTable(tableDefinition0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // Array index out of range: 0
            //
            verifyException("java.util.Vector", e);
        }
    }
}
