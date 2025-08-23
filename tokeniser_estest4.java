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

public class Tokeniser_ESTestTest4 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        Document document0 = xmlTreeBuilder0.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser0 = new Parser(xmlTreeBuilder0);
        StreamParser streamParser0 = new StreamParser(parser0);
        streamParser0.parseFragment("*S(.f;(4%%-%Rr", (Element) document0, "");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        String string0 = tokeniser0.unescapeEntities(true);
        assertEquals("*S(.f;(4%%-%Rr", string0);
    }
}
