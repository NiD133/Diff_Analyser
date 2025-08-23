package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest3 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Document document0 = Parser.parseBodyFragment("", "");
        Class<FormElement> class0 = FormElement.class;
        NodeIterator<FormElement> nodeIterator0 = new NodeIterator<FormElement>(document0, class0);
        boolean boolean0 = nodeIterator0.hasNext();
        assertFalse(boolean0);
    }
}
