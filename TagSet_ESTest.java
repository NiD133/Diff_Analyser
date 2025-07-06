/*
 * Test suite for the TagSet class.
 */
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

/**
 * Test suite for the TagSet class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TagSetTest extends TagSet_ESTest_scaffolding {

    /**
     * Tests the initialization of a TagSet using the default HTML tag set.
     */
    @Test(timeout = 4000)
    public void testInitializationWithDefaultHtml() {
        TagSet tagSet = TagSet.initHtmlDefault();
        assertNotNull(tagSet);
    }

    /**
     * Tests the initialization of a TagSet using a custom HTML tag set.
     */
    @Test(timeout = 4000)
    public void testInitializationWithCustomHtml() {
        TagSet tagSet = TagSet.Html();
        assertNotNull(tagSet);
    }

    /**
     * Tests the addition of a tag to a TagSet.
     */
    @Test(timeout = 4000)
    public void testAddTag() {
        TagSet tagSet = new TagSet();
        Tag tag = new Tag("custom");
        tagSet.add(tag);
        assertNotNull(tagSet.get("custom", null));
    }

    /**
     * Tests the retrieval of a tag from a TagSet using its name and namespace.
     */
    @Test(timeout = 4000)
    public void testGetTag() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.get("p", null);
        assertNotNull(tag);
    }

    /**
     * Tests the retrieval of a tag from a TagSet using its name and namespace with a case-insensitive match.
     */
    @Test(timeout = 4000)
    public void testGetTagWithCaseInsensitiveMatch() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.get("P", null);
        assertNotNull(tag);
    }

    /**
     * Tests the retrieval of a tag from a TagSet using its name and namespace when the tag does not exist.
     */
    @Test(timeout = 4000)
    public void testGetTagWhenTagDoesNotExist() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.get("custom", null);
        assertNull(tag);
    }

    /**
     * Tests the valueOf method with a valid tag name and namespace.
     */
    @Test(timeout = 4000)
    public void testValueOfWithValidTagNameAndNamespace() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.valueOf("p", null);
        assertNotNull(tag);
    }

    /**
     * Tests the valueOf method with an invalid tag name.
     */
    @Test(timeout = 4000)
    public void testValueOfWithInvalidTagName() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Tag tag = tagSet.valueOf("", null);
        assertNull(tag);
    }

    /**
     * Tests the valueOf method with a valid tag name and namespace when the tag does not exist in the TagSet.
     */
    @Test(timeout = 4000)
    public void testValueOfWithValidTagNameAndNamespaceWhenTagDoesNotExist() {
        TagSet tagSet = new TagSet();
        Tag tag = tagSet.valueOf("custom", null);
        assertNotNull(tag);
    }

    /**
     * Tests the onNewTag method with a valid Consumer.
     */
    @Test(timeout = 4000)
    public void testOnNewTagWithValidConsumer() {
        TagSet tagSet = TagSet.initHtmlDefault();
        Consumer<Tag> customizer = tag -> tag.set(Tag.SelfClose);
        TagSet newTagSet = tagSet.onNewTag(customizer);
        assertNotNull(newTagSet);
    }

    /**
     * Tests the onNewTag method with a null Consumer.
     */
    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testOnNewTagWithNullConsumer() {
        TagSet tagSet = TagSet.initHtmlDefault();
        tagSet.onNewTag(null);
    }

    /**
     * Tests the equals method with two identical TagSets.
     */
    @Test(timeout = 4000)
    public void testEqualsWithIdenticalTagSets() {
        TagSet tagSet1 = TagSet.initHtmlDefault();
        TagSet tagSet2 = TagSet.initHtmlDefault();
        assertTrue(tagSet1.equals(tagSet2));
    }

    /**
     * Tests the equals method with two different TagSets.
     */
    @Test(timeout = 4000)
    public void testEqualsWithDifferentTagSets() {
        TagSet tagSet1 = TagSet.initHtmlDefault();
        TagSet tagSet2 = new TagSet();
        assertFalse(tagSet1.equals(tagSet2));
    }

    /**
     * Tests the hashCode method.
     */
    @Test(timeout = 4000)
    public void testHashCode() {
        TagSet tagSet = TagSet.initHtmlDefault();
        int hashCode = tagSet.hashCode();
        assertTrue(hashCode > 0);
    }
}