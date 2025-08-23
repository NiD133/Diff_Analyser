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

public class Tokeniser_ESTestTest1 extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        xmlTreeBuilder0.parse("", "");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        TokeniserState tokeniserState0 = TokeniserState.DoctypeSystemIdentifier_singleQuoted;
        tokeniser0.transition(tokeniserState0);
    }
}
