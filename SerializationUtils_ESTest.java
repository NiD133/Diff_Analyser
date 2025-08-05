package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class SerializationUtils_ESTest extends SerializationUtils_ESTest_scaffolding {

    // ========== Serialize Method Tests ==========
    
    @Test(timeout = 4000)
    public void testSerializeNullObjectToOutputStream() throws Throwable {
        // Given: A mock output stream
        MockPrintStream outputStream = new MockPrintStream("objectData");
        
        // When: Serializing null to the output stream
        SerializationUtils.serialize((Serializable) null, outputStream);
        
        // Then: No exception should be thrown (null serialization is allowed)
    }

    @Test(timeout = 4000)
    public void testSerializeNullObjectToByteArray() throws Throwable {
        // When: Serializing null object to byte array
        byte[] serializedData = SerializationUtils.serialize((Serializable) null);
        
        // Then: Should return valid byte array representing null
        assertNotNull("Serialized null should produce a byte array", serializedData);
    }

    @Test(timeout = 4000)
    public void testSerializeValidLocaleObject() throws Throwable {
        // Given: A valid serializable object (Locale)
        Locale originalLocale = Locale.PRC;
        
        // When: Serializing the locale to byte array
        byte[] serializedData = SerializationUtils.serialize(originalLocale);
        
        // And: Deserializing it back
        Locale deserializedLocale = (Locale) SerializationUtils.deserialize(serializedData);
        
        // Then: The deserialized object should have correct properties
        assertEquals("Deserialized locale should have empty variant", "", deserializedLocale.getVariant());
    }

    @Test(timeout = 4000)
    public void testSerializeToDisconnectedPipeThrowsException() throws Throwable {
        // Given: A disconnected piped output stream
        PipedOutputStream disconnectedPipe = new PipedOutputStream();
        Class<Object> serializableClass = Object.class;
        
        // When/Then: Serializing to disconnected pipe should throw RuntimeException
        try { 
            SerializationUtils.serialize(serializableClass, disconnectedPipe);
            fail("Expected RuntimeException due to disconnected pipe");
        } catch(RuntimeException e) {
            verifyException("org.apache.commons.lang3.SerializationUtils", e);
            assertTrue("Exception should mention pipe connection", 
                      e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testSerializeNonSerializableObjectThrowsException() throws Throwable {
        // Given: A HashMap containing non-serializable objects
        HashMap<MockFileInputStream, Object> nonSerializableMap = new HashMap<>();
        File tempFile = MockFile.createTempFile("test", "tmp");
        MockFileInputStream nonSerializableStream = new MockFileInputStream(tempFile);
        nonSerializableMap.put(nonSerializableStream, tempFile);
        
        // When/Then: Serializing non-serializable object should throw RuntimeException
        try { 
            SerializationUtils.serialize(nonSerializableMap);
            fail("Expected RuntimeException due to non-serializable object");
        } catch(RuntimeException e) {
            verifyException("org.apache.commons.lang3.SerializationUtils", e);
            assertTrue("Exception should mention NotSerializableException", 
                      e.getMessage().contains("NotSerializableException"));
        }
    }

    @Test(timeout = 4000)
    public void testSerializeWithNullOutputStreamThrowsException() throws Throwable {
        // Given: A valid serializable object and null output stream
        Integer validObject = new Integer(-106);
        
        // When/Then: Serializing to null output stream should throw NullPointerException
        try { 
            SerializationUtils.serialize(validObject, (OutputStream) null);
            fail("Expected NullPointerException for null output stream");
        } catch(NullPointerException e) {
            verifyException("java.util.Objects", e);
            assertEquals("Exception should mention outputStream parameter", "outputStream", e.getMessage());
        }
    }

    // ========== Deserialize Method Tests ==========

    @Test(timeout = 4000)
    public void testDeserializeNullFromByteArray() throws Throwable {
        // Given: Serialized null object
        byte[] serializedNull = SerializationUtils.serialize((Serializable) null);
        
        // When: Deserializing the null object
        Object deserializedObject = SerializationUtils.deserialize(serializedNull);
        
        // Then: Should return null
        assertNull("Deserialized null should be null", deserializedObject);
    }

    @Test(timeout = 4000)
    public void testDeserializeNullFromInputStream() throws Throwable {
        // Given: Serialized null object as input stream
        byte[] serializedNull = SerializationUtils.serialize((Serializable) null);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedNull);
        SequenceInputStream sequenceStream = new SequenceInputStream(inputStream, inputStream);
        
        // When: Deserializing from input stream
        Object deserializedObject = SerializationUtils.deserialize(sequenceStream);
        
        // Then: Should return null
        assertNull("Deserialized null from stream should be null", deserializedObject);
    }

    @Test(timeout = 4000)
    public void testDeserializeValidLocaleFromInputStream() throws Throwable {
        // Given: A serialized Locale object
        Locale originalLocale = Locale.JAPANESE;
        byte[] serializedLocale = SerializationUtils.serialize(originalLocale);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedLocale);
        
        // When: Deserializing from input stream
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        
        // Then: Should return the same locale instance
        assertSame("Deserialized locale should be same instance", originalLocale, deserializedObject);
    }

    @Test(timeout = 4000)
    public void testDeserializeInvalidByteArrayThrowsException() throws Throwable {
        // Given: Invalid byte array (too short)
        byte[] invalidData = new byte[1];
        
        // When/Then: Deserializing invalid data should throw RuntimeException
        try { 
            SerializationUtils.deserialize(invalidData);
            fail("Expected RuntimeException for invalid serialized data");
        } catch(RuntimeException e) {
            verifyException("org.apache.commons.lang3.SerializationUtils", e);
            assertTrue("Exception should mention EOFException", e.getMessage().contains("EOFException"));
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeNullByteArrayThrowsException() throws Throwable {
        // When/Then: Deserializing null byte array should throw NullPointerException
        try { 
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected NullPointerException for null byte array");
        } catch(NullPointerException e) {
            verifyException("java.util.Objects", e);
            assertEquals("Exception should mention objectData parameter", "objectData", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromDisconnectedPipeThrowsException() throws Throwable {
        // Given: A disconnected piped input stream
        PipedInputStream disconnectedPipe = new PipedInputStream();
        
        // When/Then: Deserializing from disconnected pipe should throw RuntimeException
        try { 
            SerializationUtils.deserialize(disconnectedPipe);
            fail("Expected RuntimeException due to disconnected pipe");
        } catch(RuntimeException e) {
            verifyException("org.apache.commons.lang3.SerializationUtils", e);
            assertTrue("Exception should mention pipe connection", 
                      e.getMessage().contains("Pipe not connected"));
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromNullInputStreamThrowsException() throws Throwable {
        // When/Then: Deserializing from null input stream should throw NullPointerException
        try { 
            SerializationUtils.deserialize((InputStream) null);
            fail("Expected NullPointerException for null input stream");
        } catch(NullPointerException e) {
            verifyException("java.util.Objects", e);
            assertEquals("Exception should mention inputStream parameter", "inputStream", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDeserializeFromInvalidByteArrayInputStreamThrowsException() throws Throwable {
        // Given: Valid serialized data but invalid stream parameters
        byte[] validSerializedData = SerializationUtils.serialize((Serializable) (byte) 114);
        ByteArrayInputStream invalidStream = new ByteArrayInputStream(validSerializedData, -1, 163);
        
        // When/Then: Deserializing from invalid stream should throw ArrayIndexOutOfBoundsException
        try { 
            SerializationUtils.deserialize(invalidStream);
            fail("Expected ArrayIndexOutOfBoundsException for invalid stream parameters");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }

    // ========== Clone Method Tests ==========

    @Test(timeout = 4000)
    public void testCloneNullObjectReturnsNull() throws Throwable {
        // When: Cloning null object
        Integer clonedObject = SerializationUtils.clone((Integer) null);
        
        // Then: Should return null
        assertNull("Cloned null should be null", clonedObject);
    }

    @Test(timeout = 4000)
    public void testCloneValidIntegerObject() throws Throwable {
        // Given: A valid Integer object
        Integer originalInteger = new Integer((byte) 114);
        
        // When: Cloning the integer
        Integer clonedInteger = SerializationUtils.clone(originalInteger);
        
        // Then: Cloned object should be equal but not same instance
        assertTrue("Cloned integer should equal original", clonedInteger.equals(originalInteger));
        assertNotSame("Cloned integer should be different instance", originalInteger, clonedInteger);
    }

    // ========== Roundtrip Method Tests ==========

    @Test(timeout = 4000)
    public void testRoundtripNullObjectReturnsNull() throws Throwable {
        // When: Performing roundtrip on null object
        Integer roundtrippedObject = SerializationUtils.roundtrip((Integer) null);
        
        // Then: Should return null
        assertNull("Roundtripped null should be null", roundtrippedObject);
    }

    // ========== ClassLoaderAwareObjectInputStream Tests ==========

    @Test(timeout = 4000)
    public void testClassLoaderAwareObjectInputStreamWithNullInputThrowsException() throws Throwable {
        // Given: System class loader and null input stream
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        
        // When/Then: Creating ClassLoaderAwareObjectInputStream with null input should throw NullPointerException
        try {
            new SerializationUtils.ClassLoaderAwareObjectInputStream(null, systemClassLoader);
            fail("Expected NullPointerException for null input stream");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    // ========== Constructor Tests ==========

    @Test(timeout = 4000)
    public void testSerializationUtilsConstructorCreatesInstance() throws Throwable {
        // When: Creating SerializationUtils instance
        SerializationUtils instance = new SerializationUtils();
        
        // Then: Instance should be created successfully
        assertNotNull("SerializationUtils instance should be created", instance);
    }
}