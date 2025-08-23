package org.jfree.chart.urls;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link CustomCategoryURLGenerator} class, focusing on its core contracts.
 */
@DisplayName("CustomCategoryURLGenerator")
class CustomCategoryURLGeneratorTest {

    @Test
    @DisplayName("should be publicly cloneable")
    void shouldImplementPublicCloneable() {
        // The PublicCloneable interface is a marker to indicate that the clone()
        // method is public and intended for general use. This test verifies
        // that contract is met.
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();
        
        assertInstanceOf(PublicCloneable.class, generator,
                "CustomCategoryURLGenerator must implement PublicCloneable to support proper cloning.");
    }
}