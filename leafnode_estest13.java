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

public class LeafNode_ESTestTest13 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        DataNode dataNode0 = new DataNode("org.jsoup.select.Evaluator$IndexGreaterThan");
        dataNode0.siblingIndex = 74;
        CDataNode cDataNode0 = new CDataNode("'mMH");
        LeafNode leafNode0 = dataNode0.doClone(cDataNode0);
        assertTrue(leafNode0.hasParent());
    }
}
