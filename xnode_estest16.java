package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ext.DefaultHandler2;

public class XNode_ESTestTest16 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test015() throws Throwable {
        Properties properties0 = new Properties();
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode();
        XNode xNode0 = new XNode((XPathParser) null, iIOMetadataNode0, properties0);
        Long long0 = new Long(2541L);
        Long long1 = xNode0.getLongAttribute("%CjKdy/@j1\"{KuK'", long0);
        assertEquals(2541L, (long) long1);
    }
}
