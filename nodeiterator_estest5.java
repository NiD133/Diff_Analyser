package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest5 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Document document0 = Parser.parseBodyFragment("   ", "   ");
        Element element0 = document0.prependChild(document0);
        Class<FormElement> class0 = FormElement.class;
        NodeIterator<FormElement> nodeIterator0 = new NodeIterator<FormElement>(element0, class0);
        // Undeclared exception!
        nodeIterator0.next();
    }
}
