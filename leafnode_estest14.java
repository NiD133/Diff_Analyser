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

public class LeafNode_ESTestTest14 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        CDataNode cDataNode0 = new CDataNode(">eaHx`");
        Comment comment0 = new Comment("");
        cDataNode0.siblingIndex = (-1);
        LeafNode leafNode0 = cDataNode0.doClone(comment0);
        assertTrue(leafNode0.hasParent());
    }
}
