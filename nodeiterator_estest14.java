package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest14 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Document document0 = new Document("org.jsoup.>odes.NodeIterator", "org.jsoup.>odes.NodeIterator");
        String string0 = document0.wholeText();
        assertEquals("", string0);
    }
}
