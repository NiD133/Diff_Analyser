package org.apache.commons.compress.archivers.ar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

// The following imports from the original test were removed as they are no longer needed:
// import static org.junit.Assert.*;
// import static org.evosuite.runtime.EvoAssertions.*;
// import java.io.OutputStream;
// import java.nio.file.LinkOption;
// ... and other EvoSuite-specific imports.

import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

// Note: The EvoSuite runner and scaffolding are kept to maintain the original test's execution context.
// The primary improvements are within the test method itself.
@RunWith(EvoRunner.class)
public class ArArchiveOutputStream_ESTestTest35 extends ArArchiveOutputStream_ESTest_scaffolding {

    /**
     * Verifies that createArchiveEntry throws a NullPointerException when the input File is null.
     * The underlying ArArchiveEntry constructor does not permit a null file argument.
     */
    @Test(expected = NullPointerException.class)
    public void createArchiveEntryShouldThrowExceptionForNullFile() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(outputStream);
        final String entryName = "test-entry.txt";

        // Act: Attempt to create an archive entry with a null file.
        // This is expected to throw a NullPointerException.
        arOut.createArchiveEntry((File) null, entryName);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}