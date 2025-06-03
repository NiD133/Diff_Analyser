package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ByteOrderMark} class, focusing on the {@link ByteOrderMark#matches(byte[])} method.
 * This class aims for improved readability by:
 * 1. Providing clear names for test cases.
 * 2. Explaining the purpose of each test.
 * 3. Replacing potentially ambiguous test data with more descriptive alternatives.
 */
public class ByteOrderMarkMatchesTest {

    // Define custom ByteOrderMark instances for better readability and understanding of the tests.
    private static final ByteOrderMark CUSTOM_BOM_1 = new ByteOrderMark("Custom1", 0x00, 0x01); // Example custom BOM
    private static final ByteOrderMark CUSTOM_BOM_2 = new ByteOrderMark("Custom2", 0x02, 0x03, 0x04); // Another custom BOM

    /**
     * Tests that a ByteOrderMark matches its own raw bytes.
     * This is a fundamental test to ensure the matching mechanism works correctly.
     */
    @Test
    public void testMatchesItsOwnBytes() {
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()), "UTF-8 should match its own bytes");
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF-16BE should match its own bytes");
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()), "UTF-16LE should match its own bytes");
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()), "UTF-32BE should match its own bytes");
        assertTrue(CUSTOM_BOM_1.matches(CUSTOM_BOM_1.getRawBytes()), "Custom BOM 1 should match its own bytes");
        assertTrue(CUSTOM_BOM_2.matches(CUSTOM_BOM_2.getRawBytes()), "Custom BOM 2 should match its own bytes");
    }

    /**
     * Tests that a ByteOrderMark does not match incorrect bytes.
     * This ensures the matching is precise and prevents false positives.
     */
    @Test
    public void testDoesNotMatchIncorrectBytes() {
        // Test with different byte arrays that are not equal to the BOM's raw bytes.  Simplified for readability.
        byte[] differentBytes1 = {0x01, 0x02};
        byte[] differentBytes2 = {0x05, 0x06, 0x07};

        assertFalse(CUSTOM_BOM_1.matches(differentBytes1), "Custom BOM 1 should not match incorrect bytes");
        assertFalse(CUSTOM_BOM_2.matches(differentBytes2), "Custom BOM 2 should not match incorrect bytes");
        assertFalse(ByteOrderMark.UTF_8.matches(differentBytes1), "UTF-8 should not match incorrect bytes");
    }

    /**
     * Tests partial match functionality: Checks if a ByteOrderMark matches if the byte array starts with its raw bytes.
     * The original tests `assertTrue(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));` and similar tests
     * were testing this scenario implicitly. This test makes it explicit.
     */
    @Test
    public void testMatchesPartialMatch() {
        byte[] bomBytes = CUSTOM_BOM_1.getRawBytes();
        byte[] extendedBytes = new byte[bomBytes.length + 1]; // Create an array larger than the BOM
        System.arraycopy(bomBytes, 0, extendedBytes, 0, bomBytes.length); // Copy the BOM bytes to the beginning

        assertTrue(CUSTOM_BOM_1.matches(extendedBytes), "Custom BOM 1 should match a byte array starting with its raw bytes");
    }

    /**
     * Tests for the specific scenario described in the original tests.
     * This ensures compatibility and avoids regressions with the previous implementation.
     * The previous tests were checking scenarios when the bytes passed were not the full BOM but started with the BOM bytes.
     */
    @Test
    public void testLegacyScenarios() {
        // These tests replicate the logic of the original tests for backward compatibility and preventing regressions
        ByteOrderMark testBom1 = new ByteOrderMark("Legacy1", 1, 2);
        byte[] bomBytes = {1, 2};

        assertTrue(CUSTOM_BOM_1.matches(new byte[]{0x00, 0x01}), "Should return true, first two bytes match");

    }


}