package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute.Annotation;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute.ElementValue;
import org.apache.commons.compress.harmony.unpack200.bytecode.RuntimeVisibleorInvisibleAnnotationsAttribute;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CodeAttribute_ESTest extends CodeAttribute_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeLengthCalculation() throws Throwable {
        byte[] byteArray = new byte[4];
        OperandManager operandManager = createOperandManagerWithNullArrays();
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(-2553, 85, byteArray, null, operandManager, exceptionTable);

        CPUTF8 utf8 = new CPUTF8("i2f", 38);
        CPClass cpClass = new CPClass(utf8, 85);
        ExceptionTableEntry exceptionEntry = new ExceptionTableEntry(87, 38, 87, cpClass);
        exceptionTable.add(exceptionEntry);

        int length = codeAttribute.getLength();

        assertEquals(85, codeAttribute.maxLocals);
        assertEquals(-2553, codeAttribute.maxStack);
        assertEquals(24, length);
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeWithByteArray() throws Throwable {
        byte[] byteArray = new byte[7];
        byteArray[4] = (byte) (-60);
        int[] intArray = new int[7];
        OperandManager operandManager = createOperandManagerWithSameArrays(intArray);
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(28, 17, byteArray, null, operandManager, exceptionTable);

        assertEquals(17, codeAttribute.maxLocals);
        assertEquals(28, codeAttribute.maxStack);
        assertEquals(9, codeAttribute.codeLength);
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeResolution() throws Throwable {
        byte[] byteArray = new byte[4];
        OperandManager operandManager = createOperandManagerWithNullArrays();
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(85, 85, byteArray, null, operandManager, exceptionTable);

        ClassConstantPool classConstantPool = new ClassConstantPool();
        classConstantPool.resolve(null);
        codeAttribute.resolve(classConstantPool);

        CodeAttribute nestedCodeAttribute = new CodeAttribute(39, 100, byteArray, null, operandManager, codeAttribute.exceptionTable);
        nestedCodeAttribute.resolve(classConstantPool);
        codeAttribute.addAttribute(nestedCodeAttribute);

        assertEquals(0, exceptionTable.size());
        assertEquals(39, nestedCodeAttribute.maxStack);

        MockFileOutputStream mockOutputStream = new MockFileOutputStream("!", true);
        DataOutputStream dataOutputStream = new DataOutputStream(mockOutputStream);
        codeAttribute.writeBody(dataOutputStream);

        assertEquals(85, codeAttribute.maxLocals);
        assertEquals(4, codeAttribute.codeLength);
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeWithLocalVariableTable() throws Throwable {
        int[] intArray = new int[1];
        OperandManager operandManager = createOperandManagerWithSameArrays(intArray);
        byte[] byteArray = new byte[0];
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(939, 1168, byteArray, null, operandManager, exceptionTable);

        CPUTF8[] utf8Array = new CPUTF8[1];
        LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute(-2518, intArray, intArray, utf8Array, utf8Array, intArray);
        codeAttribute.addAttribute(localVariableTableAttribute);

        int length = codeAttribute.getLength();

        assertEquals(1168, codeAttribute.maxLocals);
        assertEquals(939, codeAttribute.maxStack);
        assertEquals(-25160, length);
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeWriteBodyWithNullAttribute() throws Throwable {
        byte[] byteArray = new byte[1];
        int[] intArray = new int[1];
        OperandManager operandManager = createOperandManagerWithSomeNullArrays(intArray);
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(3668, 3534, byteArray, null, operandManager, exceptionTable);

        ClassConstantPool classConstantPool = new ClassConstantPool();
        classConstantPool.resolve(null);
        codeAttribute.resolve(classConstantPool);
        codeAttribute.addAttribute(null);

        MockFileOutputStream mockOutputStream = new MockFileOutputStream("fconst_1", true);
        DataOutputStream dataOutputStream = new DataOutputStream(mockOutputStream);

        try {
            codeAttribute.writeBody(dataOutputStream);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeWriteBodyWithIOException() throws Throwable {
        int[] intArray = new int[1];
        OperandManager operandManager = createOperandManagerWithSameArrays(intArray);
        byte[] byteArray = new byte[3];
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute((byte) 0, (byte) 0, byteArray, null, operandManager, exceptionTable);

        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);

        try {
            codeAttribute.writeBody(dataOutputStream);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedOutputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testCodeAttributeToStringWithAnnotations() throws Throwable {
        byte[] byteArray = new byte[2];
        int[] intArray = new int[6];
        OperandManager operandManager = createOperandManagerWithSameArrays(intArray);
        LinkedList<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CodeAttribute codeAttribute = new CodeAttribute(97, 97, byteArray, null, operandManager, exceptionTable);

        Annotation[] annotations = new Annotation[4];
        CPUTF8[] utf8Array = new CPUTF8[6];
        ElementValue[] elementValues = new ElementValue[1];
        ElementValue elementValue = new ElementValue(-84, utf8Array[0]);
        elementValues[0] = elementValue;
        Annotation annotation = new Annotation(2, null, utf8Array, elementValues);
        annotations[0] = annotation;

        RuntimeVisibleorInvisibleAnnotationsAttribute annotationsAttribute = new RuntimeVisibleorInvisibleAnnotationsAttribute(null, annotations);
        codeAttribute.addAttribute(annotationsAttribute);

        try {
            codeAttribute.toString();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute$Annotation", e);
        }
    }

    private OperandManager createOperandManagerWithNullArrays() {
        return new OperandManager(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private OperandManager createOperandManagerWithSameArrays(int[] array) {
        return new OperandManager(array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array, array);
    }

    private OperandManager createOperandManagerWithSomeNullArrays(int[] array) {
        return new OperandManager(array, array, array, array, array, array, array, array, array, null, null, null, null, array, null, null, null, null, null, array, array);
    }
}