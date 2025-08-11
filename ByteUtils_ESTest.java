/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ByteUtils_ESTest extends ByteUtils_ESTest_scaffolding {

    // ===== Byte Writing Tests =====
    
    @Test(timeout = 4000)
    public void testWriteToOutputStreamWithLargeLength() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteUtils.toLittleEndian(outputStream, -392L, 1633);
        assertEquals(1633, outputStream.size());
    }

    @Test(timeout = 4000)
    public void testWriteToOutputStreamWithNegativeLength() throws Exception {
        MockFileOutputStream outputStream = new MockFileOutputStream("%`QcQ", true);
        ByteUtils.toLittleEndian(outputStream, 8L, -1);
    }

    @Test(timeout = 4000)
    public void testWriteToDataOutputWithNegativeLength() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(byteStream);
        ByteUtils.toLittleEndian(dataOutput, -1257L, -2038);
        assertEquals(0, byteStream.size());
    }

    @Test(timeout = 4000)
    public void testWriteToByteArrayWithSingleByte() {
        byte[] buffer = new byte[2];
        ByteUtils.toLittleEndian(buffer, -30L, 1, 1);
        assertArrayEquals(new byte[] {0, (byte) -30}, buffer);
    }

    @Test(timeout = 4000)
    public void testWriteToByteArrayWithNegativeLength() {
        byte[] buffer = new byte[2];
        ByteUtils.toLittleEndian(buffer, -10L, 8, -2448);
        assertArrayEquals(new byte[] {0, 0}, buffer);
    }

    @Test(timeout = 4000)
    public void testOutputStreamByteConsumerFunctionality() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteUtils.OutputStreamByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(outputStream);
        consumer.accept(1);
        assertEquals(1, outputStream.toByteArray()[0]);
    }

    @Test(timeout = 4000)
    public void testWriteToByteArrayWithInvalidOffsetThrowsException() {
        byte[] buffer = new byte[3];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            ByteUtils.toLittleEndian(buffer, 1L, 1, 63)
        );
    }

    // ===== Byte Reading Tests =====
    
    @Test(timeout = 4000)
    public void testReadFromInputStream() throws Exception {
        byte[] data = {0, 0, 91, 0, 0, 0, 0};
        InputStream inputStream = new ByteArrayInputStream(data);
        long value = ByteUtils.fromLittleEndian(inputStream, 4);
        assertEquals(5963776L, value);
        assertEquals(3, inputStream.available());
    }

    @Test(timeout = 4000)
    public void testReadFromDataInput() throws Exception {
        byte[] data = {0, -32, 0, 0, 0};
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(data));
        long value = ByteUtils.fromLittleEndian(dataInput, 5);
        assertEquals(57344L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromDataInputWithNegativeLength() throws Exception {
        PipedOutputStream pipedOutput = new PipedOutputStream();
        DataInput dataInput = new DataInputStream(
            new BufferedInputStream(
                new PushbackInputStream(
                    new PipedInputStream(pipedOutput)
                )
            )
        );
        long value = ByteUtils.fromLittleEndian(dataInput, -325);
        assertEquals(0L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromByteSupplier() throws Exception {
        ByteUtils.ByteSupplier supplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(-325, -250, 1623, 0, -325, 0, 0, 0).when(supplier).getAsByte();
        long value = ByteUtils.fromLittleEndian(supplier, 8);
        assertEquals(-325L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromByteArrayWithNegativeLength() {
        byte[] data = new byte[2];
        long value = ByteUtils.fromLittleEndian(data, 3737, -10);
        assertEquals(0L, value);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromByteArray() {
        byte[] data = {0, -68, 0, 0, 0, 0, 0, 0};
        long value = ByteUtils.fromLittleEndian(data, 1, 1);
        assertEquals(188L, value);
    }

    @Test(timeout = 4000)
    public void testReadFullByteArray() {
        byte[] data = {0, 0, 0, 0, 0, -5, 0, 0};
        long value = ByteUtils.fromLittleEndian(data);
        assertEquals(275977418571776L, value);
    }

    @Test(timeout = 4000)
    public void testReadFullByteArrayWithNegativeValue() {
        byte[] data = {0, 0, 0, 0, 0, 0, 0, -72};
        long value = ByteUtils.fromLittleEndian(data);
        assertEquals(-5188146770730811392L, value);
    }

    @Test(timeout = 4000)
    public void testReadSingleByteFromSupplier() throws Exception {
        ByteUtils.ByteSupplier supplier = mock(ByteUtils.ByteSupplier.class);
        doReturn(8).when(supplier).getAsByte();
        long value = ByteUtils.fromLittleEndian(supplier, 1);
        assertEquals(8L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromInputStreamWithNegativeLength() throws Exception {
        byte[] data = new byte[5];
        InputStream inputStream = new ByteArrayInputStream(data);
        long value = ByteUtils.fromLittleEndian(inputStream, -1);
        assertEquals(0L, value);
        assertEquals(5, inputStream.available());
    }

    @Test(timeout = 4000)
    public void testReadFromSupplierWithZeroByte() throws Exception {
        ByteUtils.ByteSupplier supplier = mock(ByteUtils.ByteSupplier.class);
        doReturn(0).when(supplier).getAsByte();
        long value = ByteUtils.fromLittleEndian(supplier, 1);
        assertEquals(0L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromSupplierWithNegativeLength() throws Exception {
        long value = ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, -2049);
        assertEquals(0L, value);
    }

    @Test(timeout = 4000)
    public void testReadFromByteArrayWithTwoZeros() {
        byte[] data = new byte[2];
        long value = ByteUtils.fromLittleEndian(data);
        assertEquals(0L, value);
    }

    // ===== Exception Handling Tests =====
    
    @Test(timeout = 4000)
    public void testWriteToNullByteArrayThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.toLittleEndian(null, -2448L, 8, 8)
        );
    }

    @Test(timeout = 4000)
    public void testWriteViaNullByteConsumerThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, -1659L, 63)
        );
    }

    @Test(timeout = 4000)
    public void testWriteToNullOutputStreamThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.toLittleEndian((OutputStream) null, -1L, 3)
        );
    }

    @Test(timeout = 4000)
    public void testWriteToUnconnectedOutputStreamThrowsException() throws Exception {
        OutputStream outputStream = new PipedOutputStream();
        assertThrows(IOException.class, () -> 
            ByteUtils.toLittleEndian(outputStream, 1L, 2970)
        );
    }

    @Test(timeout = 4000)
    public void testWriteToNullDataOutputThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.toLittleEndian((DataOutput) null, 0L, 872)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromNullByteArraySliceThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.fromLittleEndian(null, 8, 8)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromNullByteArrayThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.fromLittleEndian(null)
        );
    }

    @Test(timeout = 4000)
    public void testReadOversizedByteArrayThrowsException() {
        byte[] data = new byte[18];
        assertThrows(IllegalArgumentException.class, () -> 
            ByteUtils.fromLittleEndian(data)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromNullByteSupplierThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1)
        );
    }

    @Test(timeout = 4000)
    public void testReadOversizedFromNullSupplierThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1781)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromNullInputStreamThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.fromLittleEndian((InputStream) null, 8)
        );
    }

    @Test(timeout = 4000)
    public void testReadOversizedFromNullInputStreamThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ByteUtils.fromLittleEndian((InputStream) null, 357)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromInvalidInputStreamThrowsException() {
        byte[] data = new byte[1];
        InputStream inputStream = new ByteArrayInputStream(data, -1, 6);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            ByteUtils.fromLittleEndian(inputStream, 1)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromNullDataInputThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ByteUtils.fromLittleEndian((DataInput) null, 1)
        );
    }

    @Test(timeout = 4000)
    public void testReadOversizedFromNullDataInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ByteUtils.fromLittleEndian((DataInput) null, 1302)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromUnconnectedDataInputThrowsException() {
        DataInput dataInput = new DataInputStream(new PipedInputStream());
        assertThrows(IOException.class, () -> 
            ByteUtils.fromLittleEndian(dataInput, 1)
        );
    }

    @Test(timeout = 4000)
    public void testReadInsufficientDataFromDataInputThrowsException() {
        byte[] data = new byte[2];
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(data));
        assertThrows(EOFException.class, () -> 
            ByteUtils.fromLittleEndian(dataInput, 6)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromByteArrayWithInvalidOffsetThrowsException() {
        byte[] data = new byte[10];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            ByteUtils.fromLittleEndian(data, 8, 8)
        );
    }

    @Test(timeout = 4000)
    public void testReadInsufficientDataFromInputStreamThrowsException() {
        byte[] data = new byte[6];
        InputStream inputStream = new ByteArrayInputStream(data);
        Exception exception = assertThrows(IOException.class, () -> 
            ByteUtils.fromLittleEndian(inputStream, 8)
        );
        assertEquals("Premature end of data", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testReadFromSupplierWithPrematureEndThrowsException() throws Exception {
        ByteUtils.ByteSupplier supplier = mock(ByteUtils.ByteSupplier.class);
        doReturn(-1).when(supplier).getAsByte();
        assertThrows(IOException.class, () -> 
            ByteUtils.fromLittleEndian(supplier, 1)
        );
    }

    @Test(timeout = 4000)
    public void testReadFromByteArrayWithOversizedLengthThrowsException() {
        byte[] data = new byte[1];
        assertThrows(IllegalArgumentException.class, () -> 
            ByteUtils.fromLittleEndian(data, 582, 582)
        );
    }

    @Test(timeout = 4000)
    public void testWriteToDataOutputWithLargeLength() throws Exception {
        MockFileOutputStream fileOutput = new MockFileOutputStream("zM{+~O5n/O/LjZTOA", true);
        DataOutput dataOutput = new DataOutputStream(fileOutput);
        ByteUtils.toLittleEndian(dataOutput, -294L, 2026);
    }

    @Test(timeout = 4000)
    public void testWriteToByteConsumerWithLargeLength() throws Exception {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ByteUtils.OutputStreamByteConsumer consumer = new ByteUtils.OutputStreamByteConsumer(
            new DataOutputStream(byteOutput)
        );
        ByteUtils.toLittleEndian(consumer, -2038L, 20);
        assertEquals(20, byteOutput.size());
    }
}