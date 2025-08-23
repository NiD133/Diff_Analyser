package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest6 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Document document0 = new Document("", (String) null);
        Class<FormElement> class0 = FormElement.class;
        document0.childNodes = null;
        NodeIterator<FormElement> nodeIterator0 = new NodeIterator<FormElement>(document0, class0);
        // Undeclared exception!
        try {
            nodeIterator0.next();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
