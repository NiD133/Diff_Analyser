package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest13 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Document document0 = Document.createShell("org.jsoup.>odes.NodeIterator");
        document0.appendElement("org.jsoup.>odes.NodeIterator");
        String string0 = document0.wholeText();
        assertEquals("", string0);
    }
}
