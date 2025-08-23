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

public class LeafNode_ESTestTest16 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        CDataNode cDataNode0 = new CDataNode(">eaHxh");
        Document document0 = new Document("/4k>HzZAM{i:j+  b");
        LeafNode leafNode0 = cDataNode0.doClone(document0);
        Node node0 = leafNode0.removeAttr("/4k>HzZAM{i:j+  b");
        assertTrue(node0.hasParent());
    }
}
