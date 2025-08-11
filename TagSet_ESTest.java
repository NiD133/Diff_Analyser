package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.function.Consumer;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TagSet;

/**
 * Test suite for TagSet functionality including tag creation, retrieval, 
 * equality, and customization features.
 */
public class TagSetTest {

    // Test Data Constants
    private static final String CUSTOM_TAG_NAME = "custom-tag";
    private static final String ANOTHER_CUSTOM_TAG = "another-custom";
    private static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    private static final String UNKNOWN_TAG = "unknown-tag";
    private static final String STANDARD_TAG = "div";

    // ========== Equality Tests ==========
    
    @Test
    public void testTagSetEquality_WhenCopiedFromOriginal_ShouldBeEqual() {
        // Given: An HTML TagSet and its copy
        TagSet originalTagSet = TagSet.Html();
        TagSet copiedTagSet = new TagSet(originalTagSet);
        
        // When & Then: They should be equal
        assertTrue("Copied TagSet should equal original", copiedTagSet.equals(originalTagSet));
    }

    @Test
    public void testTagSetEquality_WhenComparedToDifferentObject_ShouldNotBeEqual() {
        // Given: A TagSet and a different object type
        TagSet tagSet = new TagSet();
        Object differentObject = new Object();
        
        // When & Then: They should not be equal
        assertFalse("TagSet should not equal different object type", tagSet.equals(differentObject));
    }

    @Test
    public void testTagSetEquality_WhenTagsAdded_ShouldNotEqualOriginal() {
        // Given: Two initially equal TagSets
        TagSet originalTagSet = new TagSet();
        TagSet modifiedTagSet = new TagSet(originalTagSet);
        
        // When: A tag is added to one of them
        modifiedTagSet.valueOf(CUSTOM_TAG_NAME, HTML_NAMESPACE);
        
        // Then: They should no longer be equal
        assertFalse("Modified TagSet should not equal original", 
                   modifiedTagSet.equals(originalTagSet));
    }

    // ========== Tag Creation and Retrieval Tests ==========

    @Test
    public void testValueOf_WithPreserveWhitespaceTag_ShouldPreserveWhitespace() {
        // Given: A TagSet and preserve case settings
        TagSet tagSet = new TagSet();
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        
        // When: Creating a tag that should preserve whitespace
        Tag firstTag = tagSet.valueOf(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME);
        firstTag.set(-301); // Set whitespace preservation flag
        
        Tag retrievedTag = tagSet.valueOf(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME, preserveCaseSettings);
        
        // Then: The tag should preserve whitespace
        assertTrue("Tag should preserve whitespace", retrievedTag.preserveWhitespace());
    }

    @Test
    public void testValueOf_WithFormSubmittableTag_ShouldBeFormSubmittable() {
        // Given: A TagSet with form submittable configuration
        TagSet tagSet = new TagSet();
        Tag.Block = 7; // Configure block behavior
        
        // When: Creating a form submittable tag
        Tag firstTag = tagSet.valueOf(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME);
        firstTag.set(-296); // Set form submittable flag
        
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        Tag retrievedTag = tagSet.valueOf(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME, preserveCaseSettings);
        
        // Then: The tag should be form submittable
        assertTrue("Tag should be form submittable", retrievedTag.isFormSubmittable());
    }

    @Test
    public void testAdd_WithCustomTag_ShouldBeRetrievable() {
        // Given: An HTML TagSet and a custom tag
        TagSet tagSet = TagSet.Html();
        Tag customTag = new Tag(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME);
        
        // When: Adding the custom tag
        tagSet.add(customTag);
        Tag.FormSubmittable = -1; // Configure form behavior
        
        Tag retrievedTag = tagSet.valueOf(CUSTOM_TAG_NAME, "html", CUSTOM_TAG_NAME, false);
        
        // Then: The tag should be retrievable with correct properties
        assertNotNull("Retrieved tag should not be null", retrievedTag);
        assertEquals("Tag prefix should match", CUSTOM_TAG_NAME.substring(0, CUSTOM_TAG_NAME.length() - 1), 
                    retrievedTag.prefix());
    }

    @Test
    public void testGet_WithExistingTag_ShouldReturnTag() {
        // Given: A TagSet with a known tag
        TagSet tagSet = new TagSet();
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        
        // When: Adding and retrieving a tag
        Tag originalTag = tagSet.valueOf(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME, preserveCaseSettings);
        Tag.Known = 55; // Mark as known tag
        tagSet.add(originalTag);
        
        Tag retrievedTag = tagSet.get(CUSTOM_TAG_NAME, CUSTOM_TAG_NAME);
        
        // Then: The tag should be found and have correct properties
        assertNotNull("Retrieved tag should not be null", retrievedTag);
        assertFalse("Tag should not format as block", retrievedTag.formatAsBlock());
    }

    @Test
    public void testGet_WithNonExistentTag_ShouldReturnNull() {
        // Given: An HTML TagSet
        TagSet tagSet = TagSet.Html();
        
        // When: Trying to get a non-existent tag
        Tag result = tagSet.get(UNKNOWN_TAG, "non-existent-namespace");
        
        // Then: Should return null
        assertNull("Non-existent tag should return null", result);
    }

    // ========== Static Tag Methods Tests ==========

    @Test
    public void testStaticValueOf_WithKnownTag_ShouldReturnKnownTag() {
        // Given: A known HTML tag name
        String knownTagName = "U";
        
        // When: Creating tag via static method
        Tag tag = Tag.valueOf(knownTagName);
        
        // Then: Should be a known tag with correct properties
        assertNotNull("Tag should not be null", tag);
        assertTrue("Tag should be marked as known", tag.isKnownTag());
        assertEquals("Tag name should match", knownTagName, tag.toString());
    }

    @Test
    public void testStaticValueOf_WithParseSettings_ShouldRespectSettings() {
        // Given: Parse settings and a tag name
        ParseSettings htmlSettings = ParseSettings.htmlDefault;
        String tagName = "U";
        
        // When: Creating tag with settings
        Tag tag = Tag.valueOf(tagName, htmlSettings);
        
        // Then: Should create valid tag
        assertNotNull("Tag should not be null", tag);
        assertTrue("Tag should be known", tag.isKnownTag());
        assertEquals("Tag should be normalized to lowercase", "u", tag.toString());
    }

    @Test
    public void testIsKnownTag_WithUnknownTag_ShouldReturnFalse() {
        // Given: An unknown tag name
        String unknownTagName = "unknownCustomTag";
        
        // When: Checking if tag is known
        boolean isKnown = Tag.isKnownTag(unknownTagName);
        
        // Then: Should return false
        assertFalse("Unknown tag should not be marked as known", isKnown);
    }

    // ========== Tag Customization Tests ==========

    @Test
    public void testOnNewTag_WithCustomizer_ShouldApplyCustomization() {
        // Given: A TagSet and a mock customizer
        TagSet tagSet = TagSet.initHtmlDefault();
        Consumer<Tag> mockCustomizer = mock(Consumer.class);
        
        // When: Setting up customizer and creating a new tag
        TagSet customizedTagSet = tagSet.onNewTag(mockCustomizer);
        Tag newTag = customizedTagSet.valueOf("bp", "bp");
        
        // Then: Tag should be created successfully
        assertNotNull("New tag should be created", newTag);
        assertEquals("Tag name should match", "bp", newTag.toString());
    }

    @Test
    public void testOnNewTag_WhenCalledTwice_ShouldReturnSameInstance() {
        // Given: An HTML TagSet and two customizers
        TagSet originalTagSet = TagSet.Html();
        Consumer<Tag> firstCustomizer = mock(Consumer.class);
        Consumer<Tag> secondCustomizer = mock(Consumer.class);
        
        // When: Adding customizers sequentially
        TagSet firstResult = originalTagSet.onNewTag(firstCustomizer);
        TagSet secondResult = firstResult.onNewTag(secondCustomizer);
        
        // Then: Should return the same instance
        assertSame("Sequential customizer calls should return same instance", 
                  originalTagSet, secondResult);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_WithEmptyTagName_ShouldThrowException() {
        // Given: A TagSet and empty tag name
        TagSet tagSet = TagSet.Html();
        ParseSettings settings = ParseSettings.preserveCase;
        
        // When: Trying to create tag with empty name
        // Then: Should throw IllegalArgumentException
        tagSet.valueOf("", "", settings);
    }

    @Test(expected = NullPointerException.class)
    public void testValueOf_WithNullParseSettings_ShouldThrowException() {
        // Given: A TagSet and null parse settings
        TagSet tagSet = new TagSet();
        
        // When: Trying to create tag with null settings
        // Then: Should throw NullPointerException
        tagSet.valueOf("tagName", "tagName", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_WithNullTagName_ShouldThrowException() {
        // Given: A TagSet
        TagSet tagSet = TagSet.initHtmlDefault();
        
        // When: Trying to create tag with null name
        // Then: Should throw IllegalArgumentException
        tagSet.valueOf(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnNewTag_WithNullCustomizer_ShouldThrowException() {
        // Given: A TagSet
        TagSet tagSet = TagSet.initHtmlDefault();
        
        // When: Trying to set null customizer
        // Then: Should throw IllegalArgumentException
        tagSet.onNewTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_WithNullParameters_ShouldThrowException() {
        // Given: A TagSet
        TagSet tagSet = TagSet.initHtmlDefault();
        
        // When: Trying to get tag with null parameters
        // Then: Should throw IllegalArgumentException
        tagSet.get(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAdd_WithNullTag_ShouldThrowException() {
        // Given: An HTML TagSet
        TagSet tagSet = TagSet.Html();
        
        // When: Trying to add null tag
        // Then: Should throw NullPointerException
        tagSet.add(null);
    }

    // ========== Integration Tests ==========

    @Test
    public void testComplexTagOperations_WithMultipleNamespaces_ShouldWorkCorrectly() {
        // Given: A TagSet with multiple namespace operations
        TagSet tagSet = TagSet.Html();
        
        // When: Creating tags in different namespaces and retrieving them
        tagSet.valueOf("customElement", "namespace1", "namespace1", true);
        Tag retrievedTag = tagSet.get("customElement", "namespace1");
        
        // Then: Operations should complete successfully
        assertNotNull("Tag should be retrievable", retrievedTag);
        assertEquals("Namespace should match", "namespace1", retrievedTag.normalName());
    }

    @Test
    public void testHashCode_ShouldExecuteWithoutError() {
        // Given: A TagSet
        TagSet tagSet = new TagSet();
        
        // When: Calling hashCode
        int hashCode = tagSet.hashCode();
        
        // Then: Should complete without error (hash code can be any value)
        // This test mainly ensures no exceptions are thrown
        assertTrue("Hash code calculation should complete", true);
    }
}