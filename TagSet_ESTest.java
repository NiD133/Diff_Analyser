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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class TagSet_ESTest extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testTagSetEquality() throws Throwable {
        TagSet defaultTagSet = TagSet.initHtmlDefault();
        TagSet copiedTagSet = new TagSet(defaultTagSet);
        
        boolean areEqual = copiedTagSet.equals(defaultTagSet);
        
        assertFalse(defaultTagSet.equals((Object) copiedTagSet));
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testAddAndRetrieveTag() throws Throwable {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag customTag = new Tag("customTag");
        Tag.PreserveWhitespace = 2667;
        Tag namespacedTag = customTag.namespace("namespace");
        
        tagSet.add(namespacedTag);
        
        ParseSettings parseSettings = new ParseSettings(true, true);
        Tag retrievedTag = tagSet.valueOf("customTag", "namespace", parseSettings);
        
        assertTrue(retrievedTag.isKnownTag());
    }

    @Test(timeout = 4000)
    public void testTagProperties() throws Throwable {
        TagSet tagSet = new TagSet();
        Tag tag = tagSet.valueOf("tagName", "namespace", "namespace", true);
        
        Tag.FormSubmittable = 4;
        Tag.Known = 4;
        
        tagSet.add(tag);
        
        ParseSettings parseSettings = new ParseSettings(true, false);
        Tag retrievedTag = tagSet.valueOf("tagName", "namespace", parseSettings);
        
        assertFalse(retrievedTag.isSelfClosing());
        assertTrue(retrievedTag.isBlock());
    }

    @Test(timeout = 4000)
    public void testNamespaceAndTagName() throws Throwable {
        TagSet tagSet = TagSet.HtmlTagSet;
        Tag tag = new Tag("optgroup");
        Tag namespacedTag = tag.namespace("j");
        
        namespacedTag.tagName = "Z";
        namespacedTag.set(10);
        
        tagSet.add(tag);
        
        ParseSettings parseSettings = new ParseSettings(false, false);
        Tag retrievedTag = tagSet.valueOf("Z", "j", parseSettings);
        
        assertFalse(retrievedTag.isBlock());
        assertEquals("optgroup", retrievedTag.normalName());
    }

    @Test(timeout = 4000)
    public void testPreserveWhitespace() throws Throwable {
        TagSet tagSet = new TagSet();
        Tag.PreserveWhitespace = 67;
        
        Tag tag = tagSet.valueOf("tagName", "namespace");
        tag.set(18);
        
        Tag retrievedTag = tagSet.valueOf("tagName", "namespace", "namespace", true);
        
        assertEquals("tagname", retrievedTag.normalName());
    }

    @Test(timeout = 4000)
    public void testSelfClosingAndBlockProperties() throws Throwable {
        TagSet tagSet = new TagSet();
        Tag tag = tagSet.valueOf("tagName", "namespace", "namespace", true);
        
        tag.namespace = "f";
        Tag.FormSubmittable = 4;
        Tag.Known = 4;
        
        tagSet.add(tag);
        
        Tag retrievedTag = tagSet.valueOf("tagName", "otherNamespace", "f", false);
        
        assertFalse(retrievedTag.isSelfClosing());
        assertTrue(retrievedTag.isBlock());
    }

    @Test(timeout = 4000)
    public void testPreserveCase() throws Throwable {
        Tag.Block = 38;
        TagSet tagSet = TagSet.Html();
        ParseSettings parseSettings = ParseSettings.preserveCase;
        
        Tag tag = tagSet.valueOf("tagName", "namespace", parseSettings);
        Tag.Block = 3;
        
        Tag modifiedTag = tag.set(8);
        Tag retrievedTag = tagSet.valueOf("namespace", "tagName", "namespace", true);
        
        assertEquals("tagname", retrievedTag.normalName());
        assertNotSame(retrievedTag, modifiedTag);
        assertFalse(retrievedTag.equals((Object) modifiedTag));
    }

    @Test(timeout = 4000)
    public void testFormatAsBlockAndPreserveWhitespace() throws Throwable {
        TagSet tagSet = TagSet.HtmlTagSet;
        Tag tag = new Tag("tagName", "namespace", "namespace");
        Tag namespacedTag = tag.namespace("body");
        
        Tag.Known = 2791;
        tagSet.add(namespacedTag);
        
        Tag retrievedTag = tagSet.valueOf("tagName", "body");
        
        assertFalse(retrievedTag.formatAsBlock());
        assertTrue(retrievedTag.preserveWhitespace());
    }

    @Test(timeout = 4000)
    public void testAddAndRetrieveSameTag() throws Throwable {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.valueOf("tagName", "center", "namespace", false);
        
        Tag.FormSubmittable = -3765;
        TagSet updatedTagSet = tagSet.add(tag);
        
        Tag retrievedTag = updatedTagSet.valueOf("center", "namespace");
        
        assertSame(retrievedTag, tag);
    }

    @Test(timeout = 4000)
    public void testSelfClosingAndFormSubmittableProperties() throws Throwable {
        TagSet tagSet = TagSet.HtmlTagSet;
        Tag tag = new Tag("tagName", "namespace", "namespace");
        
        Tag modifiedTag = tag.set(3515);
        Tag namespacedTag = modifiedTag.namespace("body");
        
        tagSet.add(namespacedTag);
        
        Tag retrievedTag = tagSet.valueOf("tagName", "body");
        
        assertTrue(retrievedTag.isSelfClosing());
        assertFalse(retrievedTag.isFormSubmittable());
    }

    @Test(timeout = 4000)
    public void testTagSetAdditionAndRetrieval() throws Throwable {
        TagSet tagSet = TagSet.HtmlTagSet;
        ParseSettings parseSettings = new ParseSettings(false, false);
        
        Tag tag = Tag.valueOf("tagName", "thead", parseSettings);
        assertNotNull(tag);
        
        tag.set(732);
        TagSet updatedTagSet = tagSet.add(tag);
        
        Tag retrievedTag = updatedTagSet.get("tagName", "thead");
        
        assertEquals("tagName", retrievedTag.normalName());
        assertNotNull(retrievedTag);
        assertFalse(retrievedTag.isEmpty());
    }

    @Test(timeout = 4000)
    public void testRetrieveKnownTag() throws Throwable {
        TagSet tagSet = TagSet.initHtmlDefault();
        
        Tag tag = tagSet.get("hr", "http://www.w3.org/1999/xhtml");
        
        assertNotNull(tag);
        assertEquals("hr", tag.normalName());
        assertFalse(tag.isFormSubmittable());
        assertTrue(tag.isBlock());
    }

    @Test(timeout = 4000)
    public void testRetrieveTagWithInlineContainer() throws Throwable {
        TagSet tagSet = new TagSet();
        Tag tag = new Tag("tagName", "namespace", "namespace");
        
        Tag.InlineContainer = 25;
        tagSet.add(tag);
        
        Tag retrievedTag = tagSet.get("tagName", "namespace");
        
        assertNotNull(retrievedTag);
        assertFalse(retrievedTag.isSelfClosing());
    }

    @Test(timeout = 4000)
    public void testEmptyTagNameThrowsException() throws Throwable {
        TagSet tagSet = TagSet.Html();
        ParseSettings parseSettings = ParseSettings.preserveCase;
        
        try {
            tagSet.valueOf("", "", parseSettings);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullParseSettingsThrowsException() throws Throwable {
        TagSet tagSet = TagSet.Html();
        
        try {
            tagSet.valueOf("area", "area", (ParseSettings) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TagSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmptyTagNameAndNamespaceThrowsException() throws Throwable {
        TagSet tagSet = TagSet.Html();
        
        try {
            tagSet.valueOf("", "", "", false);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullConsumerThrowsException() throws Throwable {
        TagSet tagSet = TagSet.Html();
        
        try {
            tagSet.onNewTag((Consumer<Tag>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullTagNameAndNamespaceThrowsException() throws Throwable {
        TagSet tagSet = new TagSet();
        
        try {
            tagSet.get((String) null, (String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullTagThrowsException() throws Throwable {
        TagSet tagSet = TagSet.HtmlTagSet;
        
        try {
            tagSet.add((Tag) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // No message in exception
        }
    }

    @Test(timeout = 4000)
    public void testTagSetEqualityAfterAddition() throws Throwable {
        TagSet tagSet = TagSet.Html();
        ParseSettings parseSettings = ParseSettings.htmlDefault;
        
        tagSet.valueOf("=", "=", parseSettings);
        
        TagSet copiedTagSet = new TagSet(tagSet);
        Tag retrievedTag = copiedTagSet.get("=", "=");
        
        assertTrue(copiedTagSet.equals((Object) tagSet));
        assertNotNull(retrievedTag);
    }

    @Test(timeout = 4000)
    public void testRetrieveNonExistentTag() throws Throwable {
        TagSet tagSet = TagSet.initHtmlDefault();
        
        Tag tag = tagSet.get("<fd;IrW{", "f,mTPmI~gx");
        
        assertNull(tag);
    }

    @Test(timeout = 4000)
    public void testRetrieveNonExistentTagWithDifferentNamespace() throws Throwable {
        TagSet tagSet = TagSet.Html();
        ParseSettings parseSettings = ParseSettings.htmlDefault;
        
        tagSet.valueOf("=", "=", parseSettings);
        
        Tag tag = tagSet.get("G", "=");
        
        assertNull(tag);
    }

    @Test(timeout = 4000)
    public void testTagSetInequality() throws Throwable {
        TagSet tagSet = new TagSet();
        
        boolean areEqual = tagSet.equals("f");
        
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testTagSetSelfEquality() throws Throwable {
        TagSet tagSet = TagSet.initHtmlDefault();
        
        boolean areEqual = tagSet.equals(tagSet);
        
        assertTrue(areEqual);
    }

    @Test(timeout = 4000)
    public void testOnNewTagChaining() throws Throwable {
        TagSet tagSet = new TagSet();
        Consumer<Tag> firstConsumer = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        
        TagSet updatedTagSet = tagSet.onNewTag(firstConsumer);
        
        Consumer<Tag> secondConsumer = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet finalTagSet = updatedTagSet.onNewTag(secondConsumer);
        
        assertSame(finalTagSet, updatedTagSet);
    }

    @Test(timeout = 4000)
    public void testRetrieveSameTagWithDifferentSettings() throws Throwable {
        TagSet tagSet = TagSet.Html();
        
        Tag tag = tagSet.valueOf("tagName", "namespace", "namespace", true);
        Tag retrievedTag = tagSet.valueOf("otherTagName", "namespace", "namespace", false);
        
        assertSame(retrievedTag, tag);
        assertNotNull(retrievedTag);
    }

    @Test(timeout = 4000)
    public void testTagValueOf() throws Throwable {
        Tag tag = Tag.valueOf("A");
        
        assertNotNull(tag);
        assertTrue(tag.isKnownTag());
        assertEquals("A", tag.name());
    }

    @Test(timeout = 4000)
    public void testOnNewTagWithConsumer() throws Throwable {
        TagSet tagSet = TagSet.Html();
        Consumer<Tag> consumer = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        
        TagSet updatedTagSet = tagSet.onNewTag(consumer);
        
        Tag tag = updatedTagSet.valueOf("localName", "normalName", "namespace", true);
        
        assertEquals("normalName", tag.normalName());
        assertEquals("localName", tag.localName());
        assertEquals("namespace", tag.namespace());
        assertNotNull(tag);
    }

    @Test(timeout = 4000)
    public void testTagSetHashCode() throws Throwable {
        TagSet tagSet = TagSet.Html();
        
        tagSet.hashCode();
    }

    @Test(timeout = 4000)
    public void testNullTagNameThrowsException() throws Throwable {
        TagSet tagSet = new TagSet();
        
        try {
            tagSet.valueOf((String) null, "namespace");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}