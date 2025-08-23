package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

public class MultiInputStreamTestTest4 extends IoTestCase {

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

    // these calls to skip always return 0
    @SuppressWarnings("CheckReturnValue")
    public void testSkip() throws Exception {
        MultiInputStream multi = new MultiInputStream(Collections.singleton(new ByteSource() {

            @Override
            public InputStream openStream() {
                return new ByteArrayInputStream(newPreFilledByteArray(0, 50)) {

                    @Override
                    public long skip(long n) {
                        return 0;
                    }
                };
            }
        }).iterator());
        assertEquals(0, multi.skip(-1));
        assertEquals(0, multi.skip(-1));
        assertEquals(0, multi.skip(0));
        ByteStreams.skipFully(multi, 20);
        assertEquals(20, multi.read());
    }
}
