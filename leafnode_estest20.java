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

public class LeafNode_ESTestTest20 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("SYSTEM");
        cDataNode0.setSiblingIndex(5696);
        Node node0 = cDataNode0.attr("", "<![CDATA[SYSTEM]]>");
        assertEquals(0, node0.childNodeSize());
    }
}
