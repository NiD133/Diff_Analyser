package com.google.common.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

public class MultiInputStreamTestTest1 extends IoTestCase {

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

    public void testJoin() throws Exception {
        joinHelper(0);
        joinHelper(1);
        joinHelper(0, 0, 0);
        joinHelper(10, 20);
        joinHelper(10, 0, 20);
        joinHelper(0, 10, 20);
        joinHelper(10, 20, 0);
        joinHelper(10, 20, 1);
        joinHelper(1, 1, 1, 1, 1, 1, 1, 1);
        joinHelper(1, 0, 1, 0, 1, 0, 1, 0);
    }
}
