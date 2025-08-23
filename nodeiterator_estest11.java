package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class NodeIterator_ESTestTest11 extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Document document0 = Parser.parseBodyFragment("", "");
        NodeIterator<Node> nodeIterator0 = NodeIterator.from(document0);
        nodeIterator0.restart(document0);
        assertFalse(document0.isBlock());
    }
}
