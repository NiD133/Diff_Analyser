package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest8 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Document document0 = Document.createShell("org.jsoup.nodes.NodeIterator");
        CDataNode cDataNode0 = new CDataNode("org.jsoup.nodes.NodeIterator");
        Element element0 = document0.doClone(cDataNode0);
        // Undeclared exception!
        try {
            NodeIterator.from(element0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
