package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link OptionGroup} class, focusing on mutual exclusion behavior.
 */
// Renamed from OptionGroupTestTest7 for clarity and convention.
public class OptionGroupTest {

    /**
     * Verifies that providing two options from the same mutually exclusive group
     * results in an AlreadySelectedException.
     */
    @Test
    void shouldThrowExceptionWhenMultipleOptionsFromSameGroupAreUsed() {
        // Arrange: Create a mutually exclusive group for file/directory options.
        final OptionGroup fileOrDirectoryGroup = new OptionGroup();
        fileOrDirectoryGroup.addOption(new Option("f", "file", false, "file to process"));
        fileOrDirectoryGroup.addOption(new Option("d", "directory", false, "directory to process"));

        final Options options = new Options();
        options.addOptionGroup(fileOrDirectoryGroup);

        // Command line arguments containing two conflicting options from the same group.
        final String[] args = {"--file", "--directory"};
        final Parser parser = new PosixParser();

        // Act & Assert: Verify that parsing these arguments throws the expected exception.
        final AlreadySelectedException e = assertThrows(
            AlreadySelectedException.class,
            () -> parser.parse(options, args),
            "Expected AlreadySelectedException when two options from the same group are provided."
        );

        // Assert: Check the details of the exception to ensure it correctly identifies the conflict.
        assertNotNull(e.getOptionGroup(), "Exception should contain the conflicting option group.");
        assertTrue(e.getOptionGroup().isSelected(), "The option group should be marked as selected.");

        // The exception should report that the first option ('file') was selected,
        // and the second option ('directory') caused the conflict.
        assertEquals("f", e.getOptionGroup().getSelected(),
            "The first option ('file') should be recorded as selected in the group.");
        assertEquals("d", e.getOption().getOpt(),
            "The second option ('directory') should be identified as the conflicting option.");
    }
}