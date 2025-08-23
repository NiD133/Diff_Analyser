package org.apache.commons.compress.archivers.ar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.AbstractTest;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.jupiter.api.Test;

public class ArArchiveOutputStreamTestTest1 extends AbstractTest {

    @Test
    void testLongFileNamesCauseExceptionByDefault() throws IOException {
        final ArArchiveOutputStream ref;
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            ref = outputStream;
            final ArArchiveEntry ae = new ArArchiveEntry("this_is_a_long_name.txt", 0);
            final IOException ex = assertThrows(ArchiveException.class, () -> outputStream.putArchiveEntry(ae));
            assertTrue(ex.getMessage().startsWith("File name too long"));
        }
        assertTrue(ref.isClosed());
    }
}
