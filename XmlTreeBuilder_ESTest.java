package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.junit.Test;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for XmlTreeBuilder.
 * 
 * These tests highlight:
 * - default configuration
 * - basic parsing behavior
 * - argument validation and error propagation
 * - clear expectations with minimal internal coupling
 */
public class XmlTreeBuilderTest {

    // --------------------
    // Defaults & setup
    // --------------------

    @Test
    public void defaultConfiguration_isXmlFriendly() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        assertEquals("http://www.w3.org/XML/1998/namespace", b.defaultNamespace());
        assertTrue("XML parser should preserve tag case", b.defaultSettings().preserveTagCase());
        assertNotNull("Default tag set should be provided", b.defaultTagSet());
    }

    @Test
    public void newInstance_startsWithSameDefaults() {
        XmlTreeBuilder original = new XmlTreeBuilder();
        XmlTreeBuilder fresh = original.newInstance();

        assertNotSame("newInstance should return a different builder", original, fresh);
        assertEquals(original.defaultNamespace(), fresh.defaultNamespace());
        assertEquals(original.defaultSettings(), fresh.defaultSettings());
        assertNotNull(fresh.defaultTagSet());
    }

    // --------------------
    // Parsing: basic usage
    // --------------------

    @Test
    public void parseReader_buildsDocumentAndSetsBaseUri() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        Document doc = b.parse(new StringReader("<root/>"), "base-uri");
        assertEquals("base-uri", doc.location());
        assertEquals("Should contain exactly one <root> element", 1, doc.select("root").size());
    }

    // --------------------
    // Parsing: argument validation
    // --------------------

    @Test
    public void parseString_requiresBaseUri() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> b.parse("<x/>", null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("baseuri"));
    }

    @Test
    public void parseString_nullInputYieldsNpeFromReader() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        // When input is null, implementations typically create a StringReader and NPE is thrown.
        assertThrows(NullPointerException.class, () -> b.parse(null, null));
    }

    @Test
    public void parseReader_requiresBaseUri() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> b.parse(new StringReader("<x/>"), null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("baseuri"));
    }

    @Test
    public void parseReader_propagatesReaderIoErrors() throws Exception {
        XmlTreeBuilder b = new XmlTreeBuilder();
        // An unconnected pipe will throw on read; the parser should surface it as UncheckedIOException.
        try (PipedReader reader = new PipedReader()) {
            assertThrows(UncheckedIOException.class, () -> b.parse(reader, "base"));
        }
    }

    @Test
    public void initialiseParse_requiresReader() {
        XmlTreeBuilder b = new XmlTreeBuilder();
        Parser parser = Parser.xmlParser();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> b.initialiseParse((Reader) null, "base", parser)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("input"));
    }

    @Test
    public void initialiseParseFragment_allowsNullContext() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        // Using a null context is valid for XML fragments; should not throw.
        b.initialiseParseFragment(null);
        assertEquals("http://www.w3.org/XML/1998/namespace", b.defaultNamespace());
    }

    // --------------------
    // Token insertion: argument validation
    // --------------------

    @Test
    public void insertElementFor_rejectsEmptyName() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        // Construct a StartTag with no name to trigger validation.
        Token.StartTag emptyName = new Token.StartTag(b); // name is empty by default

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> b.insertElementFor(emptyName)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }

    @Test
    public void insertElementFor_rejectsNullToken() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        assertThrows(NullPointerException.class, () -> b.insertElementFor(null));
    }

    @Test
    public void insertLeafNode_rejectsNull() {
        XmlTreeBuilder b = new XmlTreeBuilder();

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> b.insertLeafNode((LeafNode) null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("must not be null"));
    }

    // --------------------
    // Misc: reader errors surfaced during initialiseParse too
    // --------------------

    @Test
    public void initialiseParse_propagatesReaderIoErrors() throws Exception {
        XmlTreeBuilder b = new XmlTreeBuilder();
        Parser parser = Parser.xmlParser();

        try (PipedReader reader = new PipedReader()) { // not connected -> read will fail
            assertThrows(UncheckedIOException.class, () -> b.initialiseParse(reader, "base", parser));
        }
    }
}