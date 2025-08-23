package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TagSet;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TagSet_ESTest extends TagSet_ESTest_scaffolding {

    private static final String SAMPLE_TAG_NAME = "tt=`d4!p|";
    private static final String SAMPLE_NAMESPACE = "html";
    private static final String EMPTY_STRING = "";

    @Test(timeout = 4000)
    public void testTagSetEquality() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        TagSet tagSet1 = new TagSet(tagSet0);
        assertTrue(tagSet1.equals(tagSet0));
    }

    @Test(timeout = 4000)
    public void testPreserveWhitespace() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Tag tag0 = tagSet0.valueOf(SAMPLE_TAG_NAME, SAMPLE_TAG_NAME);
        tag0.set(-301);
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag1 = tagSet0.valueOf(SAMPLE_TAG_NAME, SAMPLE_TAG_NAME, parseSettings0);
        assertTrue(tag1.preserveWhitespace());
    }

    @Test(timeout = 4000)
    public void testFormSubmittableTag() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Tag.Block = 7;
        Tag tag0 = tagSet0.valueOf(SAMPLE_TAG_NAME, SAMPLE_TAG_NAME);
        tag0.set(-296);
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag1 = tagSet0.valueOf(SAMPLE_TAG_NAME, SAMPLE_TAG_NAME, parseSettings0);
        assertTrue(tag1.isFormSubmittable());
    }

    @Test(timeout = 4000)
    public void testAddTagToSet() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = new Tag("fg6U2^oJ<:", "fg6U2^oJ<:");
        tagSet0.add(tag0);
        assertFalse(tag0.isFormSubmittable());

        Tag.FormSubmittable = -1;
        Tag tag1 = tagSet0.valueOf("fg6U2^oJ<:", SAMPLE_NAMESPACE, "fg6U2^oJ<:", false);
        assertEquals("fg6U2^oJ<", tag1.prefix());
    }

    @Test(timeout = 4000)
    public void testFormatAsBlock() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag0 = tagSet0.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", parseSettings0);
        Tag.Known = 55;
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", true);
        assertFalse(tag1.formatAsBlock());
        assertEquals(":_{p!x%}x,o'^63]{>", tag1.normalName());
    }

    @Test(timeout = 4000)
    public void testNamespace() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Tag.Block = 179;
        Tag tag0 = tagSet0.valueOf("noscript", "noscript");
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.valueOf("noscript", "noscript", "noscript", false);
        assertEquals("noscript", tag1.namespace());
    }

    @Test(timeout = 4000)
    public void testSelfClosing() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Tag.InlineContainer = 1629;
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag0 = tagSet0.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", parseSettings0);
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", true);
        assertEquals(":_{p!x%}x,o'^63]{>", tag1.normalName());
        assertFalse(tag1.isSelfClosing());
    }

    @Test(timeout = 4000)
    public void testTagEquality() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.htmlDefault;
        Tag tag0 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|", parseSettings0);
        tag0.options = 64;
        Tag tag1 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|");
        assertNotSame(tag1, tag0);
        assertFalse(tag1.equals(tag0));
        assertEquals("wtt=`d4p|", tag1.normalName());
        assertTrue(tag1.preserveWhitespace());
    }

    @Test(timeout = 4000)
    public void testAddNamespaceTag() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Tag tag0 = Tag.valueOf("kwt=j`D4p|");
        Tag tag1 = tag0.namespace("kwt=j`D4p|");
        tagSet0.add(tag1);
        assertEquals("kwt=j`D4p|", tag1.toString());

        Tag tag2 = tagSet0.valueOf("kwt=j`D4p|", "kwt=j`D4p|");
        assertEquals("kwt=j`D4p|", tag2.name());
    }

    @Test(timeout = 4000)
    public void testInlineTag() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.htmlDefault;
        Tag tag0 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|", parseSettings0);
        Tag.FormSubmittable = -41;
        tag0.options = 46;
        Tag tag1 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|");
        assertEquals("wtt=`d4p|", tag1.normalName());
        assertNotSame(tag1, tag0);
        assertFalse(tag1.equals(tag0));
        assertFalse(tag1.isInline());
    }

    @Test(timeout = 4000)
    public void testNonEmptyTag() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = tagSet0.valueOf("_-$>", "pr!", "pr!", true);
        Tag.FormSubmittable = 1;
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.get("_-$>", "pr!");
        assertEquals("pr!", tag1.normalName());
        assertNotNull(tag1);
        assertFalse(tag1.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetTag() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag0 = tagSet0.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", parseSettings0);
        assertNotNull(tag0);

        Tag.Known = 55;
        tagSet0.add(tag0);
        Tag tag1 = tagSet0.get(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>");
        assertEquals(":_{p!x%}x,o'^63]{>", tag1.normalName());
        assertNotNull(tag1);
        assertFalse(tag1.formatAsBlock());
    }

    @Test(timeout = 4000)
    public void testEmptyStringException() throws Throwable {
        TagSet tagSet0 = TagSet.HtmlTagSet;
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        try {
            tagSet0.valueOf(EMPTY_STRING, EMPTY_STRING, parseSettings0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullParseSettingsException() throws Throwable {
        TagSet tagSet0 = new TagSet();
        try {
            tagSet0.valueOf("org.jsoup.select.Evaluator$AttributeWithValueMatching", "org.jsoup.select.Evaluator$AttributeWithValueMatching", (ParseSettings) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TagSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmptyStringWithNamespaceException() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        try {
            tagSet0.valueOf(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, true);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullObjectException() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        try {
            tagSet0.valueOf((String) null, (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullConsumerException() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        try {
            tagSet0.onNewTag((Consumer<Tag>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullGetException() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        try {
            tagSet0.get((String) null, (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullTagException() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        try {
            tagSet0.add((Tag) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testKnownTag() throws Throwable {
        ParseSettings parseSettings0 = ParseSettings.htmlDefault;
        Tag tag0 = Tag.valueOf("U", parseSettings0);
        assertNotNull(tag0);
        assertTrue(tag0.isKnownTag());
        assertEquals("u", tag0.toString());
    }

    @Test(timeout = 4000)
    public void testGetBlockTag() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = tagSet0.get("pre", "http://www.w3.org/1999/xhtml");
        assertEquals(4, Tag.Block);
    }

    @Test(timeout = 4000)
    public void testIsKnownTag() throws Throwable {
        assertFalse(Tag.isKnownTag("cxpPE"));
    }

    @Test(timeout = 4000)
    public void testGetTagWithNamespace() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        tagSet0.valueOf("_-$>", "pr!", "pr!", true);
        Tag tag0 = tagSet0.get("_-$>", "pr!");
        assertEquals("pr!", tag0.normalName());
        assertNotNull(tag0);
    }

    @Test(timeout = 4000)
    public void testGetUnknownTag() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = tagSet0.get("}+z\")6FP@lJ{n*Mufp", "org.jsoup.nodes.Document$OutputSettings$Syntax");
        assertNull(tag0);
    }

    @Test(timeout = 4000)
    public void testTagSetInequality() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Object object0 = new Object();
        assertFalse(tagSet0.equals(object0));
    }

    @Test(timeout = 4000)
    public void testValueOfKnownTag() throws Throwable {
        Tag tag0 = Tag.valueOf("U");
        assertNotNull(tag0);
        assertEquals("U", tag0.toString());
        assertTrue(tag0.isKnownTag());
    }

    @Test(timeout = 4000)
    public void testTagSetCopy() throws Throwable {
        TagSet tagSet0 = new TagSet();
        TagSet tagSet1 = new TagSet(tagSet0);
        tagSet1.valueOf("wtt=`d4p|", "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        assertFalse(tagSet1.equals(tagSet0));
    }

    @Test(timeout = 4000)
    public void testTagSetHashCode() throws Throwable {
        TagSet tagSet0 = new TagSet();
        tagSet0.hashCode();
    }

    @Test(timeout = 4000)
    public void testOnNewTagConsumer() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Consumer<Tag> consumer0 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet1 = tagSet0.onNewTag(consumer0);
        Tag tag0 = tagSet1.valueOf("bp", "bp");
        assertEquals("bp", tag0.toString());
    }

    @Test(timeout = 4000)
    public void testMultipleConsumers() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Consumer<Tag> consumer0 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet1 = tagSet0.onNewTag(consumer0);
        Consumer<Tag> consumer1 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet2 = tagSet1.onNewTag(consumer1);
        assertSame(tagSet0, tagSet2);
    }
}