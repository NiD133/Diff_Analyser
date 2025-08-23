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

public class XmlTreeBuilder_ESTestTest9 extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder0 = new XmlTreeBuilder();
        StringReader stringReader0 = new StringReader("xmlns:#text");
        Parser parser0 = Parser.xmlParser();
        xmlTreeBuilder0.initialiseParse(stringReader0, "xmlns:#text", parser0);
        assertEquals("http://www.w3.org/XML/1998/namespace", parser0.defaultNamespace());
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder0.defaultNamespace());
    }
}
