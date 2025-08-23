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

public class LeafNode_ESTestTest37 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        CDataNode cDataNode0 = new CDataNode(">eaHx`");
        cDataNode0.attr(">eaHx`", ">eaHx`");
        Node node0 = cDataNode0.attr(">eaHx`", ">eaHx`");
        assertFalse(node0.hasParent());
    }
}
