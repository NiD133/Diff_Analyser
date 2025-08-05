package org.jsoup.parser;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * A readable and maintainable test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    //region Constructor and Equality Tests

    @Test
    public void copyConstructorCreatesEqualButIndependentInstance() {
        // Arrange
        TagSet original = TagSet.Html();
        original.valueOf("newtag", "html"); // Add a custom tag to the original set

        // Act
        TagSet copy = new TagSet(original);

        // Assert: The copy is equal to the original initially
        assertTrue(original.equals(copy));
        assertEquals(original.hashCode(), copy.hashCode());

        // Act: Modify the copy
        copy.valueOf("anothertag", "html");

        // Assert: The original remains unaffected by changes to the copy
        assertFalse("Modifying the copy should not affect the original", original.equals(copy));
        assertNull("Tag added to copy should not exist in original", original.get("anothertag", "html"));
    }

    @Test
    public void equals_withDifferentObjectType_returnsFalse() {
        // Arrange
        TagSet tagSet = new TagSet();
        Object other = new Object();

        // Act & Assert
        assertFalse(tagSet.equals(other));
    }

    //endregion

    //region valueOf() Tests

    @Test
    public void valueOf_withHtmlSettings_normalizesTagNameToLowerCase() {
        // Arrange
        TagSet tagSet = new TagSet();
        ParseSettings settings = ParseSettings.htmlDefault;

        // Act
        Tag tag = tagSet.valueOf("DIV", "html", settings);

        // Assert
        assertEquals("div", tag.getName());
        assertEquals("div", tag.normalName());
    }

    @Test
    public void valueOf_withPreserveCaseSettings_preservesTagNameCase() {
        // Arrange
        TagSet tagSet = new TagSet();
        ParseSettings settings = ParseSettings.preserveCase;

        // Act
        Tag tag = tagSet.valueOf("DIV", "html", settings);

        // Assert
        assertEquals("DIV", tag.getName());
        assertEquals("DIV", tag.normalName());
    }

    @Test
    public void valueOf_forNewTag_createsAndCachesTag() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "custom-tag";
        String namespace = "custom-ns";

        // Act
        Tag firstCall = tagSet.valueOf(tagName, namespace);
        Tag secondCall = tagSet.valueOf(tagName, namespace);

        // Assert
        assertNotNull(firstCall);
        assertEquals(tagName, firstCall.getName());
        assertEquals(namespace, firstCall.namespace());
        assertFalse("A newly created tag should not be a 'known' tag by default", firstCall.isKnownTag());

        // The same instance should be returned from the cache on subsequent calls
        assertSame(firstCall, secondCall);
    }

    @Test
    public void valueOf_forKnownHtmlTag_returnsCorrectTag() {
        // Arrange
        TagSet tagSet = TagSet.Html();

        // Act
        Tag tag = tagSet.valueOf("div", "html");

        // Assert
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void valueOf_isNotAffectedByDirectFieldMutationOfPreviouslyReturnedTag() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "my-tag";
        
        // Act
        Tag tag1 = tagSet.valueOf(tagName, "html");
        assertFalse("Tag should not preserve whitespace by default", tag1.preserveWhitespace());

        // Mutate the returned tag instance directly. This is bad practice, but this test
        // ensures the TagSet is robust against it by not returning the mutated instance.
        tag1.options = 64; // 64 is the internal bitmask for preserveWhitespace
        assertTrue("Tag should now preserve whitespace after mutation", tag1.preserveWhitespace());

        // Act again: request the same tag from the set
        Tag tag2 = tagSet.valueOf(tagName, "html");

        // Assert: The TagSet should return a fresh instance with default properties, not the mutated one.
        assertFalse("Newly retrieved tag should have default options", tag2.preserveWhitespace());
        assertNotSame("TagSet should not return the mutated instance from its cache", tag1, tag2);
    }

    //endregion

    //region get() Tests

    @Test
    public void get_forExistingTag_returnsTag() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "existing-tag";
        String namespace = "ns";
        tagSet.valueOf(tagName, namespace); // Ensure tag exists in the set

        // Act
        Tag foundTag = tagSet.get(tagName, namespace);

        // Assert
        assertNotNull(foundTag);
        assertEquals(tagName, foundTag.getName());
    }

    @Test
    public void get_forKnownHtmlTagWithXhtmlNamespace_returnsTag() {
        // Arrange
        TagSet tagSet = TagSet.Html();
        // The default HTML tags are in the XHTML namespace
        String xhtmlNamespace = "http://www.w3.org/1999/xhtml";

        // Act
        Tag preTag = tagSet.get("pre", xhtmlNamespace);

        // Assert
        assertNotNull(preTag);
        assertEquals("pre", preTag.getName());
        assertTrue(preTag.isKnownTag());
    }

    @Test
    public void get_forUnknownTag_returnsNull() {
        // Arrange
        TagSet tagSet = TagSet.Html();

        // Act
        Tag foundTag = tagSet.get("non-existent-tag", "html");

        // Assert
        assertNull(foundTag);
    }

    //endregion

    //region add() Tests

    @Test
    public void add_makesTagKnownToTheSet() {
        // Arrange
        TagSet tagSet = new TagSet();
        Tag newTag = Tag.valueOf("my-new-tag");
        newTag.namespace("my-ns");

        // Pre-condition assert
        assertNull(tagSet.get("my-new-tag", "my-ns"));

        // Act
        tagSet.add(newTag);

        // Assert
        Tag retrievedTag = tagSet.get("my-new-tag", "my-ns");
        assertNotNull(retrievedTag);
        assertSame("get() should return the exact instance that was added", newTag, retrievedTag);
    }

    //endregion

    //region onNewTag() Customizer Tests

    @Test
    public void onNewTag_customizerIsCalledForNewTags() {
        // Arrange
        TagSet tagSet = new TagSet();
        AtomicBoolean customizerCalled = new AtomicBoolean(false);

        // Act
        tagSet.onNewTag(tag -> {
            if (tag.getName().equals("custom")) {
                customizerCalled.set(true);
            }
        });
        tagSet.valueOf("custom", "html");

        // Assert
        assertTrue("Customizer should have been called for the new tag", customizerCalled.get());
    }

    @Test
    public void onNewTag_customizerCanModifyNewTags() {
        // Arrange
        TagSet tagSet = new TagSet();
        
        // Act: Configure the TagSet to make all new, unknown tags self-closing
        tagSet.onNewTag(tag -> {
            if (!tag.isKnownTag()) {
                tag.setSelfClosing();
            }
        });

        Tag customTag = tagSet.valueOf("custom-component", "html");
        Tag knownTag = tagSet.valueOf("div", "html"); // Known tags should be unaffected

        // Assert
        assertTrue("Custom tag should be modified to be self-closing", customTag.isSelfClosing());
        assertFalse("Known tag 'div' should not be self-closing", knownTag.isSelfClosing());
    }

    @Test
    public void onNewTag_isChainable() {
        // Arrange
        TagSet tagSet = new TagSet();
        
        // Act
        TagSet returnedTagSet = tagSet.onNewTag(tag -> {});

        // Assert
        assertSame("onNewTag should return the same TagSet instance", tagSet, returnedTagSet);
    }

    //endregion

    //region Exception Tests

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_withEmptyTagName_throwsIllegalArgumentException() {
        new TagSet().valueOf("", "html", ParseSettings.htmlDefault);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_withNullTagName_throwsIllegalArgumentException() {
        new TagSet().valueOf(null, "html");
    }

    @Test(expected = NullPointerException.class)
    public void valueOf_withNullParseSettings_throwsNullPointerException() {
        new TagSet().valueOf("tag", "html", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_withNullTagName_throwsIllegalArgumentException() {
        new TagSet().get(null, "html");
    }
    
    @Test(expected = NullPointerException.class)
    public void add_withNullTag_throwsNullPointerException() {
        new TagSet().add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onNewTag_withNullConsumer_throwsIllegalArgumentException() {
        new TagSet().onNewTag(null);
    }

    //endregion
}