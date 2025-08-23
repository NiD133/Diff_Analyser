package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.io.RandomAccessSourceFactory;

public class RandomAccessFileOrArrayTestTest4 {

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
    public void testFilePositionWithPushback() throws Exception {
        RandomAccessFileOrArray rafoa = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
        long offset = 72;
        rafoa.seek(offset);
        Assert.assertEquals(offset, rafoa.getFilePointer());
        byte pushbackVal = 42;
        rafoa.pushBack(pushbackVal);
        Assert.assertEquals(offset - 1, rafoa.getFilePointer());
    }
}
