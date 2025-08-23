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

public class LeafNode_ESTestTest40 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        Document.OutputSettings document_OutputSettings0 = new Document.OutputSettings();
        cDataNode0.outerHtmlTail((QuietAppendable) null, document_OutputSettings0);
        assertEquals(0, cDataNode0.siblingIndex());
    }
}
