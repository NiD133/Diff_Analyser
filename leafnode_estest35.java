package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class LeafNode_ESTestTest35 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        TextNode textNode0 = new TextNode("body");
        Document document0 = Parser.parseBodyFragment("body", "org.jsoup.internal.Normalizer");
        textNode0.parentNode = (Node) document0;
        String string0 = textNode0.baseUri();
        assertEquals("org.jsoup.internal.Normalizer", string0);
    }
}
