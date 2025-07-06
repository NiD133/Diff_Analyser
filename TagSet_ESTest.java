package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;

import org.jsoup.nodes.Document;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagSetTest {

    @Test
    public void testTagSetsWithDifferentSourcesAreNotEqual() {
        // Arrange: Create two TagSets, one initialized from the default HTML TagSet, and the other not.
        TagSet defaultHtmlTagSet = TagSet.initHtmlDefault();
        TagSet derivedTagSet = new TagSet(defaultHtmlTagSet);

        // Act: Check if the two TagSets are equal.
        boolean areEqual = derivedTagSet.equals(defaultHtmlTagSet);

        // Assert: The derived TagSet should not be equal to the original, as they are distinct instances,
        // even if they initially contain the same tags.  Also ensure that the original is *not* considered equal to the copy.
        assertFalse(areEqual);
        assertFalse(defaultHtmlTagSet.equals(derivedTagSet));
    }

    @Test
    public void testValueOfReturnsExistingTagWhenCaseInsensitiveAndTagExists() {
        // Arrange: Initialize a TagSet and add a Tag with a specific name and namespace.
        TagSet tagSet = TagSet.initHtmlDefault();
        String tagName = "customtag";
        String namespace = "customns";
        Tag customTag = new Tag(tagName);
        customTag = customTag.namespace(namespace);
        tagSet.add(customTag);
        ParseSettings caseInsensitive = new ParseSettings(false, false);


        // Act: Call valueOf with a different casing of the tag name and the defined namespace, and check if the correct Tag is returned.
        Tag retrievedTag = tagSet.valueOf(tagName.toUpperCase(), namespace, caseInsensitive);

        // Assert: The retrieved Tag should be the same instance as the original customTag.
        assertEquals(customTag, retrievedTag);
    }

    @Test
    public void testValueOfCreatesNewTagWhenTagDoesNotExist() {
        // Arrange: Initialize an empty TagSet and define a tag name and namespace.
        TagSet tagSet = new TagSet();
        String tagName = "newtag";
        String namespace = "newns";

        // Act: Call valueOf to get a tag with the specified name and namespace.
        Tag newTag = tagSet.valueOf(tagName, namespace);

        // Assert: A new Tag should be created, and it should have the correct tag name and namespace.
        assertNotNull(newTag);
        assertEquals(tagName, newTag.name());
        assertEquals(namespace, newTag.namespace());
    }

     @Test
    public void testValueOfReturnsCorrectTagAfterSettingProperties() {
        // Arrange: Create a tag set and a tag with a name and namespace
        TagSet tagSet = new TagSet();
        String tagName = "testtag";
        String namespace = "testns";
        Tag tag = tagSet.valueOf(tagName, namespace);

        // Act: Set Tag properties and call valueOf again, ensuring it returns the existing Tag
        tag.set(Tag.Flag.SELF_CLOSING); //Example of setting a tag property
        Tag retrievedTag = tagSet.valueOf(tagName, namespace);

        // Assert: The retrieved tag should be the same and have the properties set
        assertSame(tag, retrievedTag);
        assertTrue(retrievedTag.isSelfClosing());
    }

    @Test
    public void testGetReturnsNullForNonExistentTag() {
        // Arrange: Initialize a TagSet.
        TagSet tagSet = TagSet.initHtmlDefault();

        // Act: Attempt to get a Tag with a non-existent tag name and namespace.
        Tag retrievedTag = tagSet.get("nonexistenttag", "nonexistentns");

        // Assert: The retrieved Tag should be null.
        assertNull(retrievedTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfThrowsExceptionWhenTagNameIsEmpty() {
        // Arrange: Initialize a TagSet.
        TagSet tagSet = TagSet.Html();
        ParseSettings parseSettings = ParseSettings.htmlDefault;

        // Act & Assert: Calling valueOf with an empty tag name should throw an IllegalArgumentException.
        tagSet.valueOf("", "", parseSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnNewTagThrowsExceptionWhenCustomizerIsNull() {
        // Arrange: Initialize a TagSet.
        TagSet tagSet = new TagSet();

        // Act & Assert: Calling onNewTag with a null customizer should throw an IllegalArgumentException.
        tagSet.onNewTag(null);
    }

    @Test
    public void testOnNewTagIsCalledWhenNewTagIsCreated() {
        // Arrange: Initialize a TagSet and mock a Consumer.
        TagSet tagSet = new TagSet();
        @SuppressWarnings("unchecked")
		Consumer<Tag> tagCustomizer = (Consumer<Tag>) mock(Consumer.class);
        tagSet.onNewTag(tagCustomizer);
        String tagName = "dynamicTag";
        String namespace = "dynamicNamespace";

        // Act: Call valueOf to create a new tag.
        tagSet.valueOf(tagName, namespace);

        // Assert: The mock Consumer should be invoked once, with a Tag as the argument.
        verify(tagCustomizer, times(1)).accept(any(Tag.class));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentObjects() {
        // Arrange: Initialize a TagSet and create a different object.
        TagSet tagSet = TagSet.initHtmlDefault();
        String otherObject = "not a tagset";

        // Act: Check if the TagSet is equal to the other object.
        boolean areEqual = tagSet.equals(otherObject);

        // Assert: The TagSet should not be equal to the other object.
        assertFalse(areEqual);
    }

    @Test
    public void testEqualsReturnsTrueForSameInstance() {
        // Arrange: Initialize a TagSet.
        TagSet tagSet = TagSet.initHtmlDefault();

        // Act: Check if the TagSet is equal to itself.
        boolean areEqual = tagSet.equals(tagSet);

        // Assert: The TagSet should be equal to itself.
        assertTrue(areEqual);
    }

    @Test
    public void testHashCodeIsConsistent() {
        // Arrange: Initialize a TagSet.
        TagSet tagSet = TagSet.Html();
        int initialHashCode = tagSet.hashCode();

        // Act: Get the hash code again.
        int subsequentHashCode = tagSet.hashCode();

        // Assert: The hash code should be the same.
        assertEquals(initialHashCode, subsequentHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfThrowsExceptionForNullTagName() {
        // Arrange: Initialize a TagSet
        TagSet tagSet = new TagSet();

        // Act & Assert: Calling valueOf with a null tag name should throw an IllegalArgumentException
        tagSet.valueOf(null, "namespace");
    }
}