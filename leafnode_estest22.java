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

public class LeafNode_ESTestTest22 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        DataNode dataNode0 = new DataNode("PUBLIC");
        Node node0 = dataNode0.wrap("org.jsoup.nodes.LeafNode");
        Node node1 = node0.attr("org.jsoup.nodes.LeafNode", "1jNKR5#n*");
        assertSame(node1, node0);
    }
}
