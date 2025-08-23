package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.io.RandomAccessSourceFactory;

public class RandomAccessFileOrArrayTestTest2 {

    byte[] data;

    RandomAccessFileOrArray rafoa;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int i = 0; i < 10000; i++) {
            os.write(i);
        }
        data = os.toByteArray();
        rafoa = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSimple() throws Exception {
        for (int i = 0; i < data.length; i++) {
            Assert.assertEquals(data[i], (byte) rafoa.read());
        }
    }
}
