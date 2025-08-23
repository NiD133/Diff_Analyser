package org.jsoup.parser;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that the Consumer registered via onNewTag() is invoked when a new,
     * previously unknown tag is created through the valueOf() method.
     */
    @Test
    public void onNewTagConsumerIsInvokedWhenCreatingUnknownTag() {
        // Arrange
        // Create a mock Consumer to track its interactions.
        @SuppressWarnings("unchecked") // A safe and common suppression for generic mocks.
        Consumer<Tag> mockNewTagConsumer = mock(Consumer.class);

        TagSet tagSet = TagSet.initHtmlDefault();
        tagSet.onNewTag(mockNewTagConsumer);

        String unknownTagName = "custom-tag";
        String namespace = Parser.NamespaceHtml;

        // Act
        // Calling valueOf with an unknown tag should create a new Tag instance
        // and trigger the registered onNewTag consumer.
        Tag createdTag = tagSet.valueOf(unknownTagName, namespace);

        // Assert
        // 1. Validate the returned Tag object is correct.
        assertNotNull("The created tag should not be null.", createdTag);
        assertEquals("The tag name should match the requested name.", unknownTagName, createdTag.getName());
        assertEquals("The namespace should match the requested namespace.", namespace, createdTag.namespace());

        // 2. Verify the consumer was called exactly once with the newly created tag.
        ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
        verify(mockNewTagConsumer).accept(tagCaptor.capture());

        // 3. Ensure the tag passed to the consumer is the same one returned by valueOf().
        assertEquals("The consumer should have been called with the newly created tag.", createdTag, tagCaptor.getValue());
    }
}