package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.runner.RunWith;

public class Tokeniser_ESTestTest22 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        StringReader stringReader0 = new StringReader("amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&");
        Parser parser0 = new Parser(xmlTreeBuilder0);
        parser0.setTrackErrors(39);
        Document document0 = xmlTreeBuilder0.parse(stringReader0, "userAgent", parser0);
        assertEquals("userAgent", document0.location());
    }
}
