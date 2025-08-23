package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

public class MultiInputStreamTestTest3 extends IoTestCase {

    private void joinHelper(Integer... spans) throws Exception {
        List<ByteSource> sources = new ArrayList<>();
        int start = 0;
        for (Integer span : spans) {
            sources.add(newByteSource(start, span));
            start += span;
        }
        ByteSource joined = ByteSource.concat(sources);
        assertTrue(newByteSource(0, start).contentEquals(joined));
    }

    private static MultiInputStream tenMillionEmptySources() throws IOException {
        return new MultiInputStream(Collections.nCopies(10_000_000, ByteSource.empty()).iterator());
    }

    private static ByteSource newByteSource(int start, int size) {
        return new ByteSource() {

            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(newPreFilledByteArray(start, size));
            }
        };
    }

    public void testReadSingleByte() throws Exception {
        ByteSource source = newByteSource(0, 10);
        ByteSource joined = ByteSource.concat(source, source);
        assertEquals(20, joined.size());
        InputStream in = joined.openStream();
        assertFalse(in.markSupported());
        assertEquals(10, in.available());
        int total = 0;
        while (in.read() != -1) {
            total++;
        }
        assertEquals(0, in.available());
        assertEquals(20, total);
    }
}
