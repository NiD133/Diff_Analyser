package org.jsoup.helper;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link W3CDom} class, focusing on input validation.
 */
public class W3CDomTest {

    /**
     * Verifies that calling fromJsoup(Element) with a null input
     * correctly throws an IllegalArgumentException.
     */
    @Test
    public void fromJsoupElementShouldThrowExceptionWhenInputIsNull() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        String expectedErrorMessage = "Object must not be null";

        // Act & Assert
        // The method call is expected to throw an exception.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> w3cDom.fromJsoup((Element) null)
        );

        // Verify that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}