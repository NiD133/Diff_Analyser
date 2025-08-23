package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest10 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Document document0 = Document.createShell("et\"yoy;tZU`>?U[");
        CDataNode cDataNode0 = new CDataNode("et\"yoy;tZU`>?U[");
        Element element0 = document0.doClone(cDataNode0);
        document0.parentNode = (Node) element0;
        Class<FormElement> class0 = FormElement.class;
        NodeIterator<FormElement> nodeIterator0 = null;
        try {
            nodeIterator0 = new NodeIterator<FormElement>(document0.parentNode, class0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
