package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DefaultIndenter} class.
 * Tests the immutable builder pattern methods for creating indenter instances
 * with custom line feeds and indentation strings.
 */
class DefaultIndenterTest {

    @Test
    void withLinefeed_shouldCreateNewInstanceWithCustomLinefeed() {
        // Given
        DefaultIndenter originalIndenter = new DefaultIndenter();
        String customLinefeed = "-XG'#x";
        
        // When
        DefaultIndenter indenterWithCustomLinefeed = originalIndenter.withLinefeed(customLinefeed);
        DefaultIndenter indenterWithSameLinefeed = indenterWithCustomLinefeed.withLinefeed(customLinefeed);
        
        // Then
        assertEquals(customLinefeed, indenterWithSameLinefeed.getEol(), 
            "End-of-line should match the custom linefeed");
        assertNotSame(indenterWithCustomLinefeed, originalIndenter, 
            "Should create new instance when linefeed differs from original");
        assertSame(indenterWithSameLinefeed, indenterWithCustomLinefeed, 
            "Should return same instance when linefeed is unchanged");
    }

    @Test
    void withIndent_shouldCreateNewInstanceWithCustomIndentation() {
        // Given
        DefaultIndenter originalIndenter = new DefaultIndenter();
        String customIndent = "9Qh/6,~n";
        
        // When
        DefaultIndenter indenterWithCustomIndent = originalIndenter.withIndent(customIndent);
        DefaultIndenter indenterWithSameIndent = indenterWithCustomIndent.withIndent(customIndent);
        
        // Then
        assertEquals(System.lineSeparator(), indenterWithSameIndent.getEol(), 
            "End-of-line should remain system default when only indent is changed");
        assertNotSame(indenterWithCustomIndent, originalIndenter, 
            "Should create new instance when indent differs from original");
        assertSame(indenterWithSameIndent, indenterWithCustomIndent, 
            "Should return same instance when indent is unchanged");
    }
}