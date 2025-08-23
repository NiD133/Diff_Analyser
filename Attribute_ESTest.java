package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Range;
import org.jsoup.nodes.TextNode;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class Attribute_ESTest extends Attribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIsBooleanAttribute() {
        boolean result = Attribute.isBooleanAttribute("allowfullscreen");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyForXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("ZVY", syntax);
        assertEquals("ZVY", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharacters() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("z>Su ", syntax);
        assertEquals("z_Su_", validKey);
        assertNotNull(validKey);
    }

    @Test(timeout = 4000)
    public void testHtmlNoValidateWithNullValues() {
        Charset charset = Charset.defaultCharset();
        Document.OutputSettings settings = new Document.OutputSettings();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FilterOutputStream filterOutputStream = new FilterOutputStream(byteArrayOutputStream);
        CharsetEncoder encoder = charset.newEncoder();
        OutputStreamWriter writer = new OutputStreamWriter(filterOutputStream, encoder);
        QuietAppendable quietAppendable = QuietAppendable.wrap(writer);

        Attribute.htmlNoValidate(null, null, quietAppendable, settings);
        assertTrue(settings.prettyPrint());
    }

    @Test(timeout = 4000)
    public void testCreateFromEncodedAndHtmlOutput() {
        Attribute attribute = Attribute.createFromEncoded("^N{|l+0Tm", "n-d?");
        StringBuilder output = new StringBuilder("^N{|l+0Tm");
        Document.OutputSettings settings = new Document.OutputSettings();
        QuietAppendable quietAppendable = QuietAppendable.wrap(output);

        attribute.html(quietAppendable, settings);
        assertEquals("^N{|l+0Tm^N{|l+0Tm=\"n-d?\"", output.toString());
    }

    @Test(timeout = 4000)
    public void testHtmlWithSpecialCharacters() {
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder output = new StringBuilder();

        Attribute.html("\"4}zG", "_", output, settings);
        assertEquals("_4}zG=\"_\"", output.toString());
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttribute() {
        Attribute attribute = Attribute.createFromEncoded("ismap", "XII YZ}5!");
        assertEquals("XII YZ}5!", attribute.getValue());

        attribute.setValue("ismap");
        Document.OutputSettings settings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(settings);
        assertTrue(shouldCollapse);
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttributeWithNullValue() {
        Document.OutputSettings settings = new Document.OutputSettings();
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("4*it@", null, settings);
        assertTrue(shouldCollapse);
    }

    @Test(timeout = 4000)
    public void testSetValueToEmptyString() {
        Attribute attribute = new Attribute("org.jsoup.select.Evaluator$IsOnlyOfType", "org.jsoup.select.Evaluator$IsOnlyOfType");
        attribute.setValue("");
        assertEquals("", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testIsDataAttribute() {
        boolean isDataAttribute = Attribute.isDataAttribute("data-data@X-");
        assertTrue(isDataAttribute);
    }

    @Test(timeout = 4000)
    public void testGetValueWithNullValue() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("bokmyj\"!bh4db:", null, attributes);
        String value = attribute.getValue();
        assertEquals("", value);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithEmptyString() {
        String validKey = Attribute.getValidKey("", null);
        assertEquals("", validKey);
    }

    @Test(timeout = 4000)
    public void testCloneAttribute() {
        Attribute attribute = new Attribute("``qmo2/5=ohpy:6", null);
        Attribute clonedAttribute = attribute.clone();
        assertEquals("", clonedAttribute.getValue());
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttributeWithNullOutputSettings() {
        Attribute attribute = Attribute.createFromEncoded("`ata-", "`ata-");
        try {
            attribute.shouldCollapseAttribute(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttributeWithNullParameters() {
        try {
            Attribute.shouldCollapseAttribute(null, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetKeyWithEmptyString() {
        Attribute attribute = new Attribute("kgU@cbsv&}$~/K5", "kgU@cbsv&}$~/K5");
        try {
            attribute.setKey("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsDataAttributeWithNullKey() {
        try {
            Attribute.isDataAttribute(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHtmlNoValidateWithNullAppendable() {
        try {
            Attribute.htmlNoValidate("declarx", "75L?/)Wy)4x", null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithBufferOverflow() {
        Attribute attribute = new Attribute("v7JC~OF}qK`", "v7JC~OF}qK`");
        char[] charArray = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        QuietAppendable quietAppendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            attribute.html(quietAppendable, settings);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithBufferOverflowInStaticMethod() {
        char[] charArray = new char[4];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        QuietAppendable quietAppendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            Attribute.html("checked", "org.jsoup.select.Evaluator$AttributeWithValueStarting", quietAppendable, settings);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithUnconnectedPipe() {
        PipedWriter pipedWriter = new PipedWriter();
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            Attribute.html("SAJSG]wWsB0C#[5", "9v/CARD0_U64t>:3", pipedWriter, settings);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.jsoup.internal.QuietAppendable$BaseAppendable", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithUnconnectedPipeInInstanceMethod() {
        Document.OutputSettings settings = new Document.OutputSettings();
        Attribute attribute = new Attribute("zyixbn`lOB*#", "bokmyj\"!bh4db:");
        PipedWriter pipedWriter = new PipedWriter();

        try {
            attribute.html(pipedWriter, settings);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.jsoup.internal.QuietAppendable$BaseAppendable", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithBufferOverflowInInstanceMethod() {
        Attribute attribute = new Attribute("`ata-", "`ata-");
        CharBuffer charBuffer = CharBuffer.allocate(0);
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            attribute.html(charBuffer, settings);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.CharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithNullAppendableInInstanceMethod() {
        Attribute attribute = Attribute.createFromEncoded("g", "g");
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            attribute.html(null, settings);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.internal.QuietAppendable$BaseAppendable", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithNullKey() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;

        try {
            Attribute.getValidKey(null, syntax);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateFromEncodedWithEmptyStrings() {
        try {
            Attribute.createFromEncoded("", "");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateFromEncodedWithNullValues() {
        try {
            Attribute.createFromEncoded(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullKeyAndValue() {
        Attributes attributes = new Attributes();

        try {
            new Attribute(null, null, attributes);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyKey() {
        try {
            new Attribute("", "");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsBooleanAttributeWithEmptyString() {
        boolean isBoolean = Attribute.isBooleanAttribute("");
        assertFalse(isBoolean);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyForHtmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("1$uMT~<$2", syntax);
        assertEquals("1$uMT~<$2", validKey);
    }

    @Test(timeout = 4000)
    public void testHtmlWithNullQuietAppendable() {
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            Attribute.html(">Qu{B", "org.jsoup.select.Evaluator$ContainsOwnText", null, settings);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testNamespaceWithEmptyNamespace() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("AttributeName", "AttributeName", attributes);
        String namespace = attribute.namespace();
        assertEquals("", namespace);
    }

    @Test(timeout = 4000)
    public void testPrefixWithEmptyPrefix() {
        Attribute attribute = new Attribute("QSy4iJ5y", "QSy4iJ5y");
        String prefix = attribute.prefix();
        assertEquals("", prefix);
    }

    @Test(timeout = 4000)
    public void testHtmlWithNullOutputSettings() {
        MockPrintWriter mockPrintWriter = new MockPrintWriter("_5EN!+,Z,`");

        try {
            Attribute.html("'Dnco", "'Dnco", mockPrintWriter, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlWithNullQuietAppendableInInstanceMethod() {
        Attribute attribute = new Attribute(" /@", " /@");
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            attribute.html(null, settings);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Attribute", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneAndSetValue() {
        Attribute attribute = Attribute.createFromEncoded("open", "w'8l4N");
        Attribute clonedAttribute = attribute.clone();
        attribute.setValue("compact");
        boolean isEqual = attribute.equals(clonedAttribute);
        assertEquals("compact", attribute.getValue());
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentAttributes() {
        Attribute attribute1 = Attribute.createFromEncoded("^B_1", "^B_1");
        Attribute attribute2 = new Attribute("g", "=O:,>Mv%I");
        boolean isEqual = attribute2.equals(attribute1);
        assertEquals("=O:,>Mv%I", attribute2.getValue());
        assertFalse(isEqual);
        assertEquals("", attribute2.prefix());
    }

    @Test(timeout = 4000)
    public void testEqualsWithClonedAttribute() {
        Attribute attribute = Attribute.createFromEncoded("^B_1", "^B_1");
        Attribute clonedAttribute = attribute.clone();
        boolean isEqual = clonedAttribute.equals(attribute);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("5|pc", "Bi", attributes);
        boolean isEqual = attribute.equals("5|pc");
        assertEquals("5|pc", attribute.getKey());
        assertEquals("Bi", attribute.getValue());
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameAttribute() {
        Attribute attribute = Attribute.createFromEncoded("_Tr_2_", "ope3n");
        boolean isEqual = attribute.equals(attribute);
        assertEquals("ope3n", attribute.getValue());
        assertEquals("_Tr_2_", attribute.localName());
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullObject() {
        Attribute attribute = Attribute.createFromEncoded("H", "g");
        boolean isEqual = attribute.equals(null);
        assertEquals("g", attribute.getValue());
        assertEquals("H", attribute.localName());
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testToStringWithEmptyValue() {
        TextNode textNode = TextNode.createFromEncoded("");
        Attributes attributes = textNode.attributes();
        Attribute attribute = new Attribute("nyc_q_", "", attributes);
        String stringRepresentation = attribute.toString();
        assertEquals("nyc_q_=\"\"", stringRepresentation);
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttributeWithXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        Document.OutputSettings settings = new Document.OutputSettings();
        Document.OutputSettings updatedSettings = settings.syntax(syntax);
        boolean shouldCollapse = Attribute.shouldCollapseAttribute("", "/icwoq", updatedSettings);
        assertFalse(shouldCollapse);
    }

    @Test(timeout = 4000)
    public void testIsDataAttributeWithInvalidKey() {
        boolean isDataAttribute = Attribute.isDataAttribute("data-");
        assertFalse(isDataAttribute);
    }

    @Test(timeout = 4000)
    public void testIsDataAttributeWithValidKey() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("data-data-2h'w0xmo/lju>^m", "actio", attributes);
        boolean isDataAttribute = attribute.isDataAttribute();
        assertEquals("actio", attribute.getValue());
        assertTrue(isDataAttribute);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharactersInHtmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("4c]oHqR=I", syntax);
        assertNotNull(validKey);
        assertEquals("4c]oHqR_I", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharactersInHtmlSyntax2() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("AkY'Y", syntax);
        assertNotNull(validKey);
        assertEquals("AkY_Y", validKey);
    }

    @Test(timeout = 4000)
    public void testHtmlOutputWithEncodedValue() {
        Attribute attribute = Attribute.createFromEncoded("XII YZ}5!", "async");
        String htmlOutput = attribute.html();
        assertEquals("XII_YZ}5!=\"async\"", htmlOutput);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharactersInHtmlSyntax3() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("w'8l4N", syntax);
        assertNotNull(validKey);
        assertEquals("_w_8l4N", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithNewLineCharacters() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("\r\n\r\n", syntax);
        assertEquals("_", validKey);
        assertNotNull(validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("[^-a-zA-Z0-9_:.]+", syntax);
        assertEquals("_-a-zA-Z0-9_:._", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithDataPrefixInXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("data-_Tr_2_", syntax);
        assertEquals("data-_Tr_2_", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharactersInXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("kQN^LU", syntax);
        assertNotNull(validKey);
        assertEquals("kQ_N_LU", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSelectorInXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey(":has() must have a selector", syntax);
        assertEquals(":has_must_have_a_selector", validKey);
        assertNotNull(validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithSpecialCharactersInXmlSyntax2() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("w'8l4N", syntax);
        assertNotNull(validKey);
        assertEquals("_w_8l4N", validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithEmptyStringInXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("", syntax);
        assertNull(validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithEmptyStringInHtmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String validKey = Attribute.getValidKey("", syntax);
        assertNull(validKey);
    }

    @Test(timeout = 4000)
    public void testGetValidKeyWithAutofocusInXmlSyntax() {
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        String validKey = Attribute.getValidKey("autofocus", syntax);
        assertEquals("autofocus", validKey);
    }

    @Test(timeout = 4000)
    public void testHtmlNoValidateWithXmlSyntax() {
        Document.OutputSettings settings = new Document.OutputSettings();
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        Document.OutputSettings updatedSettings = settings.syntax(syntax);

        Attribute.html("-Gkt9'/sI", "autofocus", null, updatedSettings);
        assertEquals(Entities.EscapeMode.xhtml, updatedSettings.escapeMode());
    }

    @Test(timeout = 4000)
    public void testSourceRange() {
        Attribute attribute = new Attribute("X@BGZ^/[A&M>", "_w_8_4p");
        attribute.sourceRange();
        assertEquals("X@BGZ^/[A&M>", attribute.getKey());
        assertEquals("_w_8_4p", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testSourceRangeWithAttributes() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("o>IL<g>QPd8PNQ+@5Ns", "RU-^t-03BGs9<q?", attributes);
        Range.AttributeRange range = attribute.sourceRange();
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testNamespaceWithNonEmptyNamespace() {
        Attribute attribute = Attribute.createFromEncoded("_Tr_2_", "ope3n");
        attribute.namespace();
        assertEquals("ope3n", attribute.getValue());
        assertEquals("_Tr_2_", attribute.getKey());
    }

    @Test(timeout = 4000)
    public void testLocalName() {
        Attribute attribute = Attribute.createFromEncoded("XII YZ}5!", "async");
        String localName = attribute.localName();
        assertEquals("async", attribute.getValue());
        assertEquals("XII YZ}5!", localName);
    }

    @Test(timeout = 4000)
    public void testLocalNameWithEmptyLocalName() {
        Attribute attribute = new Attribute("Rr7w\u0000k7A:", " g!K?w");
        String localName = attribute.localName();
        assertEquals(" g!K?w", attribute.getValue());
        assertEquals("", localName);
    }

    @Test(timeout = 4000)
    public void testPrefixWithNonEmptyPrefix() {
        Attribute attribute = new Attribute("^-a-A-Z0-z_:.]+", "^-a-A-Z0-z_:.]+");
        String prefix = attribute.prefix();
        assertEquals("^-a-A-Z0-z_", prefix);
    }

    @Test(timeout = 4000)
    public void testSetValueWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.add("5|pc", "@");
        Attribute attribute = new Attribute("5|pc", "Bi", attributes);
        assertEquals("Bi", attribute.getValue());

        String previousValue = attribute.setValue("@");
        assertEquals("@", previousValue);
    }

    @Test(timeout = 4000)
    public void testSetValueWithAttributes2() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("5|pc", "Bi", attributes);
        attribute.setValue("@");
        assertEquals("@", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testHasDeclaredValue() {
        Attribute attribute = new Attribute("pge#v\"q826", "pge#v\"q826");
        boolean hasDeclaredValue = attribute.hasDeclaredValue();
        assertTrue(hasDeclaredValue);
    }

    @Test(timeout = 4000)
    public void testSetValueToNull() {
        Attribute attribute = Attribute.createFromEncoded("data-(i7=", "dvy}i6(v)yl6hca_8");
        attribute.setValue(null);
        boolean hasDeclaredValue = attribute.hasDeclaredValue();
        assertEquals("", attribute.getValue());
        assertFalse(hasDeclaredValue);
    }

    @Test(timeout = 4000)
    public void testSetKeyWithAttributes() {
        Attributes attributes = new Attributes();
        Range.AttributeRange range = Range.AttributeRange.UntrackedAttr;
        Attributes updatedAttributes = attributes.sourceRange("5|pc", range);
        updatedAttributes.add("5|pc", "@");
        Attribute attribute = new Attribute("5|pc", "Bi", attributes);
        assertEquals("5|pc", attribute.getKey());

        attribute.setKey("Bi");
        assertEquals("Bi", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testSetKeyWithAttributes2() {
        Attributes attributes = new Attributes();
        attributes.add("5|pc", "@");
        Attribute attribute = new Attribute("5|pc", "Bi", attributes);
        assertEquals("5|pc", attribute.getKey());

        attribute.setKey("Bi");
        assertEquals("Bi", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testSetKeyWithDeclaredValue() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("RU-^t-03BGs9<q?", "RU-^t-03BGs9<q?", attributes);
        attribute.setKey("jqU#:WT5 $");
        assertTrue(attribute.hasDeclaredValue());
    }

    @Test(timeout = 4000)
    public void testSetKeyWithLocalName() {
        Attribute attribute = Attribute.createFromEncoded("XII YZ}5!", "async");
        assertEquals("XII YZ}5!", attribute.localName());

        attribute.setKey("async");
        assertEquals("async", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testShouldCollapseAttributeWithDeclaredValue() {
        Attribute attribute = Attribute.createFromEncoded("PTOx<F9r`[M>.0RAj)", "PTOx<F9r`[M>.0RAj)");
        Document.OutputSettings settings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(settings);
        assertFalse(shouldCollapse);
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullValue() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("bokmyj\"!bh4db:", null, attributes);
        attribute.hashCode();
        assertEquals("", attribute.getValue());
    }

    @Test(timeout = 4000)
    public void testGetValue() {
        Attribute attribute = Attribute.createFromEncoded("{Q.MqD", "RU-^t-03BGs9<q?");
        String value = attribute.getValue();
        assertEquals("{Q.MqD", attribute.localName());
        assertEquals("RU-^t-03BGs9<q?", value);
    }

    @Test(timeout = 4000)
    public void testGetKey() {
        Attribute attribute = Attribute.createFromEncoded("_Tr_2_", "ope3n");
        String key = attribute.getKey();
        assertEquals("ope3n", attribute.getValue());
        assertEquals("_Tr_2_", key);
    }

    @Test(timeout = 4000)
    public void testIsDataAttributeWithEncodedValue() {
        Attribute attribute = Attribute.createFromEncoded("XII YZ}5!", "async");
        boolean isDataAttribute = attribute.isDataAttribute();
        assertFalse(isDataAttribute);
        assertEquals("async", attribute.getValue());
        assertEquals("XII YZ}5!", attribute.getKey());
    }

    @Test(timeout = 4000)
    public void testHtmlWithSpecialCharactersInInstanceMethod() {
        Attribute attribute = new Attribute("!m\"", "!m\"");
        StringWriter stringWriter = new StringWriter(3969);
        Document.OutputSettings settings = new Document.OutputSettings();
        attribute.html(stringWriter, settings);
        assertEquals("!m_=\"!m&quot;\"", stringWriter.toString());
    }
}