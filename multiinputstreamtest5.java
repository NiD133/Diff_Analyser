package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

public class MultiInputStreamTestTest5 extends IoTestCase {

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

    public void testReadSingle_noStackOverflow() throws IOException {
        // https://github.com/google/guava/issues/2996
        // no data, just testing that there's no StackOverflowException
        assertEquals(-1, tenMillionEmptySources().read());
    }
}
