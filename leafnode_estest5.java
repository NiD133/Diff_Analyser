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

public class LeafNode_ESTestTest5 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DataNode dataNode0 = new DataNode("gMz?D>{rCwB%:");
        dataNode0.setSiblingIndex((-1447));
        Node node0 = dataNode0.removeAttr("gMz?D>{rCwB%:");
        assertFalse(node0.hasParent());
    }
}
