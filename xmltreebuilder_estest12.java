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

public class XmlTreeBuilder_ESTestTest12 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        Parser parser0 = new Parser(xmlTreeBuilder0);
        StreamParser streamParser0 = new StreamParser(parser0);
        Element element0 = new Element("Doctype", "nfHa(P+.f");
        streamParser0.parseFragment("$M{RJ]hM9&ek.Vtk$I", element0, "http://www.w3.org/1999/xhtml");
        Tokeniser tokeniser0 = new Tokeniser(xmlTreeBuilder0);
        Token.XmlDecl token_XmlDecl0 = tokeniser0.createXmlDeclPending(true);
        xmlTreeBuilder0.insertXmlDeclarationFor(token_XmlDecl0);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder0.defaultNamespace());
    }
}
