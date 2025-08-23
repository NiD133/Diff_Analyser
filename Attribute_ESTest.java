package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

import static org.junit.Assert.*;

public class AttributeTest {

    // -------- boolean attribute detection --------

    @Test
    public void isBooleanAttribute_recognizesKnownAndUnknown() {
        assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        assertFalse(Attribute.isBooleanAttribute(""));
        assertFalse(Attribute.isBooleanAttribute("not-a-boolean-attr"));
    }

    // -------- key validation (getValidKey) --------

    @Test
    public void getValidKey_xml_preservesValidKey() {
        assertEquals("ZVY", Attribute.getValidKey("ZVY", Syntax.xml));
        assertEquals("AfterFrameset", Attribute.getValidKey("AfterFrameset", Syntax.xml));
        assertEquals("autofocus", Attribute.getValidKey("autofocus", Syntax.xml));
        assertEquals("data-_Tr_2_", Attribute.getValidKey("data-_Tr_2_", Syntax.xml));
    }

    @Test
    public void getValidKey_xml_sanitizesInvalidCharacters() {
        assertEquals("z_Su_", Attribute.getValidKey("z>Su ", Syntax.xml));
        assertEquals("_-a-zA-Z0-9_:._", Attribute.getValidKey("[^-a-zA-Z0-9_:.]+", Syntax.xml));
        assertEquals("kQ_N_LU", Attribute.getValidKey("kQ\u007Fw'LU".replace("w'", "N^"), Syntax.xml)); // equals "kQN^LU" -> "kQ_N_LU"
        assertEquals(":has_must_have_a_selector", Attribute.getValidKey(":has() must have a selector", Syntax.xml));
        assertEquals("_w_8l4N", Attribute.getValidKey("\u007Fw'8l4N", Syntax.xml));
    }

    @Test
    public void getValidKey_html_preservesOrSanitizes() {
        assertEquals("1$uMT~<$2", Attribute.getValidKey("1$uMT~<$2", Syntax.html));
        assertEquals("4c]oHqR_I", Attribute.getValidKey("4c]oHqR=I", Syntax.html));
        assertEquals("AkY_Y", Attribute.getValidKey("AkY'Y", Syntax.html));
        assertEquals("_w_8l4N", Attribute.getValidKey("\u007Fw'8l4N", Syntax.html));
        assertEquals("_", Attribute.getValidKey("\r\n\r\n", Syntax.html));
    }

    @Test
    public void getValidKey_empty_returnsNullForBothSyntaxes() {
        assertNull(Attribute.getValidKey("", Syntax.xml));
        assertNull(Attribute.getValidKey("", Syntax.html));
    }

    @Test
    public void getValidKey_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> Attribute.getValidKey(null, Syntax.html));
    }

    // -------- data- attribute detection --------

    @Test
    public void isDataAttribute_staticAndInstance() {
        assertTrue(Attribute.isDataAttribute("data-data@X-")); // only checks prefix and length
        assertFalse(Attribute.isDataAttribute("data-"));       // just the prefix is not enough

        Attributes attrs = new Attributes();
        Attribute a = new Attribute("data-foo", "bar", attrs);
        assertTrue(a.isDataAttribute());
    }

    @Test
    public void isDataAttribute_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> Attribute.isDataAttribute(null));
    }

    // -------- construction and simple getters/setters --------

    @Test
    public void constructor_nullOrEmptyKey_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(null, null));
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", ""));
    }

    @Test
    public void getValue_returnsEmptyStringIfNull() {
        Attributes attrs = new Attributes();
        Attribute a = new Attribute("some", null, attrs);
        assertEquals("", a.getValue());
    }

    @Test
    public void clone_retainsEmptyValue() {
        Attribute a = new Attribute("k", null);
        Attribute b = a.clone();
        assertEquals("", b.getValue());
    }

    @Test
    public void setKey_empty_throws() {
        Attribute a = new Attribute("key", "val");
        assertThrows(IllegalArgumentException.class, () -> a.setKey(""));
    }

    @Test
    public void setValue_andHasDeclaredValue() {
        Attribute a = new Attribute("k", "v");
        assertTrue(a.hasDeclaredValue());
        a.setValue(null);
        assertEquals("", a.getValue());
        assertFalse(a.hasDeclaredValue());
    }

    // -------- prefix, localName, namespace --------

    @Test
    public void prefix_localName_namespace_basic() {
        Attributes attrs = new Attributes();
        Attribute a = new Attribute("AttributeName", "AttributeValue", attrs);
        assertEquals("", a.namespace());
        assertEquals("", a.prefix());
        assertEquals("AttributeName", a.localName());
    }

    @Test
    public void prefix_and_localName_withColon() {
        Attribute a = new Attribute("og:title", "x");
        assertEquals("og", a.prefix());
        assertEquals("title", a.localName());
    }

    @Test
    public void localName_emptyWhenKeyEndsWithColon() {
        Attribute a = new Attribute("Rr7w\u0000k7A:", "value");
        assertEquals("", a.localName());
    }

    // -------- equals, hashCode, toString --------

    @Test
    public void equals_and_hashCode() {
        Attribute a = Attribute.createFromEncoded("_Tr_2_", "open");
        Attribute b = a.clone();
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        b.setValue("changed");
        assertFalse(a.equals(b));
        assertFalse(a.equals(null));
        assertFalse(a.equals("not-attribute"));
    }

    @Test
    public void toString_rendersHtmlFormat() {
        Attributes attrs = new Attributes();
        Attribute a = new Attribute("nyc_q_", "", attrs);
        assertEquals("nyc_q_=\"\"", a.toString());
    }

    // -------- HTML rendering to String (html()) --------

    @Test
    public void html_toString_sanitizesKey_andEscapesValue() {
        // Key has invalid char "!" and quote, value includes a quote -> expect sanitization and escaping
        Attribute a = new Attribute("!m\"", "!m\"");
        assertEquals("!m_=\"!m&quot;\"", a.html());
    }

    @Test
    public void html_toString_withSpaceInKey_isSanitized() {
        Attribute a = Attribute.createFromEncoded("XII YZ}5!", "async");
        assertEquals("XII_YZ}5!=\"async\"", a.html());
    }

    // -------- HTML rendering to Appendable / QuietAppendable --------

    @Test
    public void html_appendsToAppendable() throws IOException {
        Attribute a = new Attribute("key", "value");
        StringWriter out = new StringWriter();
        a.html(out, new OutputSettings());
        assertEquals("key=\"value\"", out.toString());
    }

    @Test
    public void htmlAppends_escapingViaWriter() throws IOException {
        Attribute a = new Attribute("quote", "\"value\"");
        StringWriter out = new StringWriter();
        a.html(out, new OutputSettings());
        assertEquals("quote=\"&quot;value&quot;\"", out.toString());
    }

    @Test
    public void html_toAppendable_charBuffer_overflow_throws() {
        Attribute a = new Attribute("v7JC~OF}qK`", "v7JC~OF}qK`");
        CharBuffer tiny = CharBuffer.allocate(6);
        QuietAppendable qa = QuietAppendable.wrap(tiny);
        assertThrows(BufferOverflowException.class, () -> a.html(qa, new OutputSettings()));
    }

    @Test
    public void html_static_toAppendable_charBuffer_overflow_throws() {
        CharBuffer tiny = CharBuffer.allocate(4);
        QuietAppendable qa = QuietAppendable.wrap(tiny);
        assertThrows(BufferOverflowException.class, () ->
            Attribute.html("checked", "someValue", qa, new OutputSettings())
        );
    }

    @Test
    public void html_toAppendable_pipedWriter_notConnected_wrapsIOException() {
        PipedWriter pw = new PipedWriter(); // not connected
        OutputSettings out = new OutputSettings();
        Attribute a = new Attribute("k", "v");
        assertThrows(RuntimeException.class, () -> a.html(pw, out));
        assertThrows(RuntimeException.class, () -> Attribute.html("k", "v", pw, out));
    }

    @Test
    public void html_toAppendable_nulls_throw() {
        Attribute a = new Attribute("k", "v");
        assertThrows(NullPointerException.class, () -> a.html((QuietAppendable) null, new OutputSettings()));
        assertThrows(NullPointerException.class, () -> a.html(null, new OutputSettings()));
        assertThrows(NullPointerException.class, () -> Attribute.html("k", "v", (QuietAppendable) null, new OutputSettings()));
        assertThrows(NullPointerException.class, () -> Attribute.html("k", "v", new StringWriter(), null));
    }

    @Test
    public void htmlNoValidate_allowsNullKeyAndValue_butRequiresAppendableAndOut() {
        StringWriter sw = new StringWriter();
        QuietAppendable qa = QuietAppendable.wrap(sw);
        OutputSettings out = new OutputSettings();

        // Accepts null key and value; should not throw
        Attribute.htmlNoValidate(null, null, qa, out);
        assertTrue(out.prettyPrint());

        // But requires non-null appendable and out
        assertThrows(NullPointerException.class, () -> Attribute.htmlNoValidate("k", "v", null, null));
    }

    @Test
    public void outputSettings_xml_impliesXhtmlEscapeMode() {
        OutputSettings out = new OutputSettings().syntax(Syntax.xml);
        // This call should not throw, but the key is irrelevant; the assert is about output settings
        Attribute.html("k", "autofocus", QuietAppendable.wrap(new StringWriter()), out);
        assertEquals(Entities.EscapeMode.xhtml, out.escapeMode());
    }

    // -------- createFromEncoded --------

    @Test
    public void createFromEncoded_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> Attribute.createFromEncoded("", ""));
        assertThrows(NullPointerException.class, () -> Attribute.createFromEncoded(null, null));
    }

    @Test
    public void createFromEncoded_basic() {
        Attribute a = Attribute.createFromEncoded("{Q.MqD", "RU-^t-03BGs9<q?");
        assertEquals("{Q.MqD", a.localName());
        assertEquals("RU-^t-03BGs9<q?", a.getValue());
    }

    // -------- collapsing attributes --------

    @Test
    public void shouldCollapseAttribute_unknownNullValue_collapses() {
        OutputSettings out = new OutputSettings();
        assertTrue(Attribute.shouldCollapseAttribute("unknown", null, out));
    }

    @Test
    public void shouldCollapseAttribute_booleanAttrWithMatchingValue_collapses() {
        Attribute a = Attribute.createFromEncoded("ismap", "value");
        a.setValue("ismap"); // value equals key
        assertTrue(a.shouldCollapseAttribute(new OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_nonBooleanWithValue_doesNotCollapse() {
        Attribute a = Attribute.createFromEncoded("PTOx<F9r`[M>.0RAj)", "PTOx<F9r`[M>.0RAj)");
        assertFalse(a.shouldCollapseAttribute(new OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_respectsNullOut_throws() {
        Attribute a = Attribute.createFromEncoded("attr", "val");
        assertThrows(NullPointerException.class, () -> a.shouldCollapseAttribute(null));
        assertThrows(NullPointerException.class, () -> Attribute.shouldCollapseAttribute(null, null, null));
    }

    @Test
    public void shouldCollapseAttribute_emptyKeyWithValue_inXmlSyntax_doesNotCollapse() {
        OutputSettings out = new OutputSettings().syntax(Syntax.xml);
        assertFalse(Attribute.shouldCollapseAttribute("", "/icwoq", out));
    }

    // -------- misc getters --------

    @Test
    public void getters_key_value_localName() {
        Attribute a = Attribute.createFromEncoded("_Tr_2_", "open");
        assertEquals("_Tr_2_", a.getKey());
        assertEquals("open", a.getValue());
        assertEquals("_Tr_2_", a.localName());

        Attribute b = Attribute.createFromEncoded("XII YZ}5!", "async");
        assertEquals("XII YZ}5!", b.localName());
        assertEquals("async", b.getValue());
    }
}