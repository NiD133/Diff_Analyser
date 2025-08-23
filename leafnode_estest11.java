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

public class LeafNode_ESTestTest11 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        TextNode textNode0 = TextNode.createFromEncoded(".");
        textNode0.setSiblingIndex((-2159));
        Node node0 = textNode0.empty();
        assertSame(node0, textNode0);
    }
}
