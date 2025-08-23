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

public class XNode_ESTestTest101 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test100() throws Throwable {
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode();
        iIOMetadataNode0.setAttribute("2pY", "2pY");
        XPathParser xPathParser0 = new XPathParser((Document) null, true);
        Properties properties0 = new Properties();
        Locale.IsoCountryCode locale_IsoCountryCode0 = Locale.IsoCountryCode.PART1_ALPHA2;
        XNode xNode0 = new XNode(xPathParser0, iIOMetadataNode0, properties0);
        Class<Locale.IsoCountryCode> class0 = Locale.IsoCountryCode.class;
        // Undeclared exception!
        try {
            xNode0.getEnumAttribute(class0, "2pY", locale_IsoCountryCode0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No enum constant java.util.Locale.IsoCountryCode.2pY
            //
            verifyException("java.lang.Enum", e);
        }
    }
}
