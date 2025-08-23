package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    @Test
    public void skip_withNegativeArgument_returnsZero() throws IOException {
        // Arrange: According to the InputStream.skip() contract, a negative argument
        // should result in 0 bytes being skipped.
        byte[] data = new byte[10];
        ByteSource byteSource = ByteSource.wrap(data);
        Iterator<ByteSource> iterator = Collections.singletonList(byteSource).iterator();
        
        MultiInputStream multiInputStream = new MultiInputStream(iterator);

        // Act
        long bytesSkipped = multiInputStream.skip(-500L);

        // Assert
        assertEquals("Calling skip() with a negative number should not skip any bytes.", 0L, bytesSkipped);
    }
}