package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest12 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Document document0 = Parser.parse("http://www.w3.org/1999/xhtml", "http://www.w3.org/2000/svg");
        Class<FormElement> class0 = FormElement.class;
        NodeIterator<FormElement> nodeIterator0 = new NodeIterator<FormElement>(document0, class0);
        // Undeclared exception!
        try {
            nodeIterator0.restart((Node) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.nodes.NodeIterator", e);
        }
    }
}
