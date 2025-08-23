package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class XmlTreeBuilder_ESTest extends XmlTreeBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testProcessStartTagAndPopStack() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/XML/1998/namespace");
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", element, "{FDP|4G0#6");
        xmlTreeBuilder.processStartTag("http://www.w3.org/1998/Math/MathML", null);
        
        Token.EndTag endTag = new Token.EndTag(xmlTreeBuilder);
        boolean processed = xmlTreeBuilder.processStartTag("~BQ~ug8>");
        assertTrue(processed);
        
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessStartTagThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("jHQIs@:5#c&", "2#0f[w|.T$lBU[oa^f");
        
        try {
            xmlTreeBuilder.processStartTag(":contains(%s)");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitialiseParseFragmentThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = new Document("d/uP=K>");
        Element element = document.head();
        
        try {
            xmlTreeBuilder.initialiseParseFragment(element);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.HashMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseFragmentAndPopStackToClose() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(">: ", ">: ");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", document, "http://www.w3.org/XML/1998/namespace");
        assertEquals(">: ", document.location());
        
        Token.EndTag endTag = new Token.EndTag(xmlTreeBuilder);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInsertLeafNode() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(">: ", ">: ");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", document, "http://www.w3.org/XML/1998/namespace");
        assertEquals(">: ", document.location());
        
        Comment comment = new Comment("KRq7");
        xmlTreeBuilder.insertLeafNode(comment);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInsertDoctypeFor() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("http://www.w3.org/1999/xhtml", "http://www.w3.org/XML/1998/namespace");
        
        streamParser.parseFragment("z>!GVh", element, "u");
        Token.Doctype doctype = new Token.Doctype();
        xmlTreeBuilder.insertDoctypeFor(doctype);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInsertCommentFor() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(">: ", ">: ");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        
        streamParser.parseFragment("}VF\"~1WF,kf?;\"Lf2", document, "");
        assertEquals(">: ", document.location());
        
        Token.Comment comment = new Token.Comment();
        xmlTreeBuilder.insertCommentFor(comment);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInsertCharacterFor() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        Elements elements = document.getAllElements();
        xmlTreeBuilder.stack = (ArrayList<Element>) elements;
        assertEquals("", document.location());
        
        Token.Character character = new Token.Character();
        xmlTreeBuilder.insertCharacterFor(character);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInitialiseParse() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("xmlns:#text");
        Parser parser = Parser.xmlParser();
        
        xmlTreeBuilder.initialiseParse(reader, "xmlns:#text", parser);
        assertEquals("http://www.w3.org/XML/1998/namespace", parser.defaultNamespace());
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testCompleteParseFragment() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("ME;}", "ME;}");
        assertEquals("ME;}", document.location());
        
        xmlTreeBuilder.completeParseFragment();
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessXmlDecl() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(">: ", ">: ");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", document, "http://www.w3.org/XML/1998/namespace");
        assertEquals(">: ", document.location());
        
        Token.XmlDecl xmlDecl = tokeniser.createXmlDeclPending(true);
        boolean processed = xmlTreeBuilder.process(xmlDecl);
        assertTrue(processed);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInsertXmlDeclarationFor() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("Doctype", "nfHa(P+.f");
        
        streamParser.parseFragment("$M{RJ]hM9&ek.Vtk$I", element, "http://www.w3.org/1999/xhtml");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.XmlDecl xmlDecl = tokeniser.createXmlDeclPending(true);
        
        xmlTreeBuilder.insertXmlDeclarationFor(xmlDecl);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessDoctype() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("Doctype", "nfHa(P+.f");
        
        streamParser.parseFragment("$M{RJ]hM9&ek.Vtk$I", element, "http://www.w3.org/1999/xhtml");
        Token.Doctype doctype = new Token.Doctype();
        
        boolean processed = xmlTreeBuilder.process(doctype);
        assertTrue(processed);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessComment() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(">: ", ">: ");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", document, "http://www.w3.org/XML/1998/namespace");
        assertEquals(">: ", document.location());
        
        Token.Comment comment = tokeniser.commentPending;
        boolean processed = xmlTreeBuilder.process(comment);
        assertTrue(processed);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessStartTagAndPop() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("http://www.w3.org/2000/svg", "http://www.w3.org/1999/xhtml");
        
        streamParser.parseFragment("7~n", element, "7~n");
        boolean processed = xmlTreeBuilder.processStartTag("http://www.w3.org/2000/svg");
        assertTrue(processed);
        
        Element poppedElement = xmlTreeBuilder.pop();
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
        assertEquals("http://www.w3.org/2000/svg", poppedElement.tagName());
    }

    @Test(timeout = 4000)
    public void testDefaultTagSet() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        TagSet tagSet = xmlTreeBuilder.defaultTagSet();
        assertNotNull(tagSet);
    }

    @Test(timeout = 4000)
    public void testDefaultSettings() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test(timeout = 4000)
    public void testPopStackToCloseThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag(xmlTreeBuilder);
        
        try {
            xmlTreeBuilder.popStackToClose(endTag);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.XmlTreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testPopThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(",Ix<T(pi#>?mbGzH", ",Ix<T(pi#>?mbGzH");
        
        try {
            xmlTreeBuilder.pop();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testPopThrowsArrayIndexOutOfBoundsException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("Doctype", "nfHa(P+.f");
        
        streamParser.parseFragment("$M{RJ]hM9&ek.Vtk$I", element, "http://www.w3.org/1999/xhtml");
        
        try {
            xmlTreeBuilder.pop();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testParseWithNullBaseUriThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.parse(",Ix<T(pi#>?mbGzH", (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithNullInputsThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.parse((String) null, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithPipedReaderAndNullBaseUriThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader(pipedWriter);
        
        try {
            xmlTreeBuilder.parse(pipedReader, (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithDisconnectedPipedReaderThrowsUncheckedIOException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        PipedReader pipedReader = new PipedReader();
        
        try {
            xmlTreeBuilder.parse(pipedReader, "kxrewut");
            fail("Expecting exception: UncheckedIOException");
        } catch (UncheckedIOException e) {
            verifyException("org.jsoup.parser.CharacterReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertXmlDeclarationForNullThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.insertXmlDeclarationFor((Token.XmlDecl) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.XmlTreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertLeafNodeThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Document document = parser.parseInput("class", "class");
        
        streamParser.parseFragment("{FDP|4G0#6", document, "_',=~G~eEf3?aX_HU");
        
        try {
            xmlTreeBuilder.insertLeafNode((LeafNode) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertLeafNodeThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        CDataNode cDataNode = new CDataNode("xl`t");
        
        try {
            xmlTreeBuilder.insertLeafNode(cDataNode);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertElementForThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(",Ix<T(pi#>?mbGzH", "jsoup.xmlns-");
        Token.StartTag startTag = new Token.StartTag(xmlTreeBuilder);
        
        try {
            xmlTreeBuilder.insertElementFor(startTag);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertElementForNullThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.insertElementFor((Token.StartTag) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.HashMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertDoctypeForThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        
        try {
            xmlTreeBuilder.insertDoctypeFor(doctype);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.XmlTreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitialiseParseWithNullReaderThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = Parser.xmlParser();
        
        try {
            xmlTreeBuilder.initialiseParse((Reader) null, "ScriptData", parser);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitialiseParseWithDisconnectedPipedReaderThrowsUncheckedIOException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        PipedReader pipedReader = new PipedReader();
        Parser parser = new Parser(xmlTreeBuilder);
        
        try {
            xmlTreeBuilder.initialiseParse(pipedReader, "IuI=", parser);
            fail("Expecting exception: UncheckedIOException");
        } catch (UncheckedIOException e) {
            verifyException("org.jsoup.parser.CharacterReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testCompleteParseFragmentThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.completeParseFragment();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.XmlTreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessCommentThrowsNullPointerException() throws Throwable {
        Token.Comment comment = new Token.Comment();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.process(comment);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testDefaultNamespace() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String namespace = xmlTreeBuilder.defaultNamespace();
        assertEquals("http://www.w3.org/XML/1998/namespace", namespace);
    }

    @Test(timeout = 4000)
    public void testProcessEndTag() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/XML/1998/namespace");
        
        streamParser.parseFragment("http://www.w3.org/XML/1998/namespace", element, "http://www.w3.org/1999/xhtml");
        Token.EndTag endTag = new Token.EndTag(xmlTreeBuilder);
        
        boolean processed = xmlTreeBuilder.process(endTag);
        assertTrue(processed);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testProcessXmlDeclThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.XmlDecl xmlDecl = new Token.XmlDecl(xmlTreeBuilder);
        xmlDecl.newAttribute();
        
        try {
            xmlTreeBuilder.process(xmlDecl);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertCharacterForCDataThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.CData cData = new Token.CData("");
        
        try {
            xmlTreeBuilder.insertCharacterFor(cData);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessStartTagWithEmptyNameThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("xmlns:{2u;3rllkw{s)I", "xmlns:{2u;3rllkw{s)I");
        CDataNode cDataNode = new CDataNode("xmlns:{2u;3rllkw{s)I");
        Attributes attributes = cDataNode.attributes();
        Attributes newAttributes = attributes.add("xmlns:{2u;3rllkw{s)I", "6x J(VBX=|so_zGPsa");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.StartTag startTag = tokeniser.startPending;
        startTag.nameAttr(null, newAttributes);
        
        try {
            xmlTreeBuilder.process(startTag);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessStartTagWithAttributesThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(",Ix<T(pi#>?mbGzH", ",Ix<T(pi#>?mbGzH");
        Token.StartTag startTag = new Token.StartTag(xmlTreeBuilder);
        CDataNode cDataNode = new CDataNode("1'm<`CcR11r1m =@q");
        Attributes attributes = cDataNode.attributes();
        attributes.add("xmlns", "1'm<`CcR11r1m =@q");
        Token.StartTag newStartTag = startTag.nameAttr("X7,c,ddhqJ", attributes);
        
        try {
            xmlTreeBuilder.process(newStartTag);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testProcessStartTagWithNamespaceThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("http://www.w3.org/XML/1998/namespace", "http://www.w3.org/XML/1998/namespace");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.StartTag startTag = tokeniser.startPending;
        Attributes attributes = new Attributes();
        attributes.add("http://www.w3.org/XML/1998/namespace", "}5ov4zKP6");
        startTag.nameAttr("numeric referencD with no nu)erals", attributes);
        
        try {
            xmlTreeBuilder.process(startTag);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testInsertElementForStartTag() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("http://www.w3.org/XML/1998/namespace", "http://www.w3.org/2000/svg");
        
        streamParser.parseFragment("http://www.w3.org/2000/svg", element, "http://www.w3.org/1998/Math/MathML");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.StartTag startTag = tokeniser.startPending;
        Attributes attributes = new Attributes();
        Token.StartTag newStartTag = startTag.nameAttr("V#,{/v8G", attributes);
        
        xmlTreeBuilder.insertElementFor(newStartTag);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testInitialiseParseFragmentWithNullElement() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParseFragment(null);
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlTreeBuilder.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testParseWithStringReader() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader reader = new StringReader("xmlns");
        xmlTreeBuilder.parse(reader, "xmlns");
    }

    @Test(timeout = 4000)
    public void testInsertCommentForThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment();
        
        try {
            xmlTreeBuilder.insertCommentFor(comment);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TreeBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testNewInstance() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder newInstance = xmlTreeBuilder.newInstance();
        assertEquals("http://www.w3.org/XML/1998/namespace", newInstance.defaultNamespace());
    }

    @Test(timeout = 4000)
    public void testPopThrowsNoSuchElementException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        try {
            xmlTreeBuilder.pop();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("java.util.ArrayDeque", e);
        }
    }
}