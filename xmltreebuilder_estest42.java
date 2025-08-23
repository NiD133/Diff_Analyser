package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;

public class XmlTreeBuilder_ESTestTest42 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        Parser parser0 = new Parser(xmlTreeBuilder0);
        StreamParser streamParser0 = new StreamParser(parser0);
        Element element0 = new Element("http://www.w3.org/XML/1998/namespace", "http://www.w3.org/2000/svg");
        streamParser0.parseFragment("http://www.w3.org/2000/svg", element0, "http://www.w3.org/1998/Math/MathML");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        Token.StartTag token_StartTag0 = tokeniser0.startPending;
        Attributes attributes0 = new Attributes();
        Token.StartTag token_StartTag1 = token_StartTag0.nameAttr("V#,{/v8G", attributes0);
        xmlTreeBuilder0.insertElementFor(token_StartTag1);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder0.defaultNamespace());
    }
}
