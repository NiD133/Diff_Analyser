package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest9 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Class<FormElement> class0 = FormElement.class;
        NodeIterator<FormElement> nodeIterator0 = null;
        try {
            nodeIterator0 = new NodeIterator<FormElement>((Node) null, class0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Object must not be null
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
