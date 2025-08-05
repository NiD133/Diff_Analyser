package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import org.apache.commons.lang3.SerializationUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SerializationUtils_ESTest extends SerializationUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSerializeNullObjectToOutputStream() throws Throwable {
        MockPrintStream mockPrintStream = new MockPrintStream("objectData");
        SerializationUtils.serialize(null, mockPrintStream);
    }

    @Test(timeout = 4000)
    public void testClassLoaderAwareObjectInputStreamWithNullInputStream() throws Throwable {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            new SerializationUtils.ClassLoaderAwareObjectInputStream(null, classLoader);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRoundtripWithNullInteger() throws Throwable {
        Integer result = SerializationUtils.roundtrip(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSerializeAndDeserializeNullObject() throws Throwable {
        byte[] serializedData = SerializationUtils.serialize(null);
        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNull(deserializedObject);
    }

    @Test(timeout = 4000)
    public void testSerializeAndDeserializeLocale() throws Throwable {
        Locale originalLocale = Locale.PRC;
        byte[] serializedData = SerializationUtils.serialize(originalLocale);
        Locale deserializedLocale = (Locale) SerializationUtils.deserialize(serializedData);
        assertEquals("", deserializedLocale.getVariant());
    }

    @Test(timeout = 4000)
    public void testDeserializeFromSequenceInputStream() throws Throwable {
        byte[] serializedData = SerializationUtils.serialize(null);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(byteArrayInputStream, byteArrayInputStream);
        Object deserializedObject = SerializationUtils.deserialize(sequenceInputStream);
        assertNull(deserializedObject);
    }

    @Test(timeout = 4000)
    public void testSerializeAndDeserializeJapaneseLocale() throws Throwable {
        Locale originalLocale = Locale.JAPANESE;
        byte[] serializedData = SerializationUtils.serialize(originalLocale);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(byteArrayInputStream);
        assertSame(deserializedObject, originalLocale);
    }

    @Test(timeout = 4000)
    public void testSerializeObjectToUnconnectedPipedOutputStream() throws Throwable {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        Class<Object> objectClass = Object.class;
        try {
            SerializationUtils.serialize(objectClass, pipedOutputStream);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception: java.io.IOException: Pipe not connected
        }
    }

    @Test(timeout = 4000)
    public void testSerializeNonSerializableObject() throws Throwable {
        HashMap<MockFileInputStream, Object> nonSerializableMap = new HashMap<>();
        File tempFile = MockFile.createTempFile("4*54", "4*54");
        MockFileInputStream mockFileInputStream = new MockFileInputStream(tempFile);
        nonSerializableMap.put(mockFileInputStream, tempFile);
        try {
            SerializationUtils.serialize(nonSerializableMap);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception: java.io.NotSerializableException
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromIncompleteByteArray() throws Throwable {
        byte[] incompleteData = new byte[1];
        try {
            SerializationUtils.deserialize(incompleteData);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception: java.io.EOFException
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromNullByteArray() throws Throwable {
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromUnconnectedPipedInputStream() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            SerializationUtils.deserialize(pipedInputStream);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception: java.io.IOException: Pipe not connected
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromNullInputStream() throws Throwable {
        try {
            SerializationUtils.deserialize((InputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeWithInvalidByteArrayInputStreamRange() throws Throwable {
        byte[] serializedData = SerializationUtils.serialize((Serializable) (byte) 114);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData, -1, 163);
        try {
            SerializationUtils.deserialize(byteArrayInputStream);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testSerializeToNullOutputStream() throws Throwable {
        Integer integer = new Integer(-106);
        try {
            SerializationUtils.serialize(integer, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloneNullInteger() throws Throwable {
        Integer result = SerializationUtils.clone(null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testCloneInteger() throws Throwable {
        Integer originalInteger = new Integer((byte) 114);
        Integer clonedInteger = SerializationUtils.clone(originalInteger);
        assertEquals(originalInteger, clonedInteger);
    }

    @Test(timeout = 4000)
    public void testSerializationUtilsConstructor() throws Throwable {
        SerializationUtils serializationUtils = new SerializationUtils();
        assertNotNull(serializationUtils);
    }
}