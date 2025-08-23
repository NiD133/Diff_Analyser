package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

// The test retains its original runner and scaffolding to ensure it runs in the same environment.
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

// The original test class name is kept for context.
@RunWith(EvoRunner.class)
public class MappedRandomAccessFile_ESTestTest6 extends MappedRandomAccessFile_ESTest_scaffolding {

    /**
     * Verifies that seeking to a negative position correctly updates the file pointer.
     *
     * This test covers the edge case where the seek position is a negative value.
     * The implementation of MappedRandomAccessFile allows this, and getFilePointer()
     * should reflect the new position accurately.
     */
    @Test(timeout = 4000)
    public void seekToNegativePositionShouldUpdateFilePointer() throws IOException {
        // Arrange
        // The filename "rw" is a placeholder used by the test generation framework (EvoSuite).
        // It is expected to correspond to a valid file created by the test scaffolding.
        MappedRandomAccessFile file = null;
        try {
            file = new MappedRandomAccessFile("rw", "rw");
            final long negativePosition = -2756L;

            // Act
            file.seek(negativePosition);
            long currentPosition = file.getFilePointer();

            // Assert
            assertEquals("The file pointer should be updated to the negative seek position.",
                         negativePosition, currentPosition);
        } finally {
            // Cleanup: Ensure the file resource is closed even if assertions fail.
            if (file != null) {
                file.close();
            }
        }
    }
}