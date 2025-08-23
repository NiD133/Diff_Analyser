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

public class ArArchiveOutputStreamTestTest2 extends AbstractTest {

    @Test
    void testLongFileNamesWorkUsingBSDDialect() throws Exception {
        final File file = createTempFile();
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(file.toPath()))) {
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            final ArArchiveEntry ae = new ArArchiveEntry("this_is_a_long_name.txt", 14);
            outputStream.putArchiveEntry(ae);
            outputStream.write(new byte[] { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!', '\n' });
            outputStream.closeArchiveEntry();
            final List<String> expected = new ArrayList<>();
            expected.add("this_is_a_long_name.txt");
            checkArchiveContent(file, expected);
        }
    }
}
