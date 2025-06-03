package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.TempFile;
import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testCloseTest() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean();
        try (Reader sr = new BufferedReader(new StringReader("01234567890")) {

            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        }) {
            try (BoundedReader mr = new BoundedReader(sr, 3)) {
                // nothing
            }
        }
        assertTrue(closed.get());
    }
}
