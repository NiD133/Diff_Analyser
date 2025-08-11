package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PatternOptionBuilder;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class PatternOptionBuilder_ESTest extends PatternOptionBuilder_ESTest_scaffolding {

    private static final char STRING_CHAR = ':';
    private static final char NUMBER_CHAR = '%';
    private static final char FILE_CHAR = '>';
    private static final char URL_CHAR = '/';
    private static final char CLASS_CHAR = '+';
    private static final char OBJECT_CHAR = '@';
    private static final char DATE_CHAR = '#';
    private static final char FILE_INPUT_STREAM_CHAR = '<';
    private static final char FILES_CHAR = '*';
    private static final char INVALID_CHAR = ',';

    @Test(timeout = 4000)
    public void testGetValueClassForString() throws Throwable {
        Object valueClass = PatternOptionBuilder.getValueClass(STRING_CHAR);
        assertNotNull(valueClass);
        assertEquals("class java.lang.String", valueClass.toString());
    }

    @Test(timeout = 4000)
    public void testParsePatternWithNullThrowsNullPointerException() throws Throwable {
        try {
            PatternOptionBuilder.parsePattern(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.cli.PatternOptionBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValueCodeForNumber() throws Throwable {
        assertTrue(PatternOptionBuilder.isValueCode(NUMBER_CHAR));
    }

    @Test(timeout = 4000)
    public void testIsValueCodeForString() throws Throwable {
        assertTrue(PatternOptionBuilder.isValueCode(STRING_CHAR));
    }

    @Test(timeout = 4000)
    public void testIsValueCodeForFileInputStream() throws Throwable {
        assertTrue(PatternOptionBuilder.isValueCode(FILE_INPUT_STREAM_CHAR));
    }

    @Test(timeout = 4000)
    public void testIsValueCodeForInvalidChar() throws Throwable {
        assertFalse(PatternOptionBuilder.isValueCode(INVALID_CHAR));
    }

    @Test(timeout = 4000)
    public void testGetValueTypeForString() throws Throwable {
        Class<?> valueType = PatternOptionBuilder.getValueType(STRING_CHAR);
        assertNotNull(valueType);
        assertEquals("class java.lang.String", valueType.toString());
    }

    @Test(timeout = 4000)
    public void testGetValueTypeForNumber() throws Throwable {
        Class<?> valueType = PatternOptionBuilder.getValueType(NUMBER_CHAR);
        assertNotNull(valueType);
        assertEquals(1025, valueType.getModifiers());
    }

    @Test(timeout = 4000)
    public void testGetValueTypeForFile() throws Throwable {
        Class<?> valueType = PatternOptionBuilder.getValueType(FILE_CHAR);
        assertNotNull(valueType);
        assertEquals("class java.io.File", valueType.toString());
    }

    @Test(timeout = 4000)
    public void testParsePatternWithEmptyString() throws Throwable {
        Options options = PatternOptionBuilder.parsePattern("");
        assertNotNull(options);
    }

    @Test(timeout = 4000)
    public void testParsePatternWithValidString() throws Throwable {
        Options options = PatternOptionBuilder.parsePattern("0dpy>mb!!Q*1_");
        assertNotNull(options);
    }

    @Test(timeout = 4000)
    public void testParsePatternWithIllegalCharacterThrowsIllegalArgumentException() throws Throwable {
        try {
            PatternOptionBuilder.parsePattern("4K<:s%L=GX$");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.OptionValidator", e);
        }
    }

    @Test(timeout = 4000)
    public void testParsePatternWithIllegalCharacterInStringThrowsIllegalArgumentException() throws Throwable {
        try {
            PatternOptionBuilder.parsePattern("The option '%s' contains an illegal character : '%s'.");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.OptionValidator", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor() throws Throwable {
        PatternOptionBuilder patternOptionBuilder = new PatternOptionBuilder();
        assertNotNull(patternOptionBuilder);
    }
}