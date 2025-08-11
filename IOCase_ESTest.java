package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.IOCase;
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
public class IOCase_ESTest extends IOCase_ESTest_scaffolding {

    // Tests for checkCompareTo
    @Test(timeout = 4000)
    public void checkCompareTo_returnsPositiveForDifferentStrings() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int result = insensitive.checkCompareTo("LINUX", "\"#S6?U_R7?'mwf");
        assertEquals(74, result);
    }

    @Test(timeout = 4000)
    public void checkCompareTo_returnsNegativeForNonMatchingSubstring() {
        IOCase system = IOCase.SYSTEM;
        int result = system.checkCompareTo("\"#s6?ulinuxmwf", "#s6?u");
        assertEquals(-1, result);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void checkCompareTo_throwsNullPointerForNullInput() {
        IOCase.SENSITIVE.checkCompareTo(null, null);
    }

    @Test(timeout = 4000)
    public void checkCompareTo_returnsZeroForEqualStrings() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int result = insensitive.checkCompareTo("1bq26gL^", "1bq26gL^");
        assertEquals(0, result);
    }

    // Tests for value method
    @Test(timeout = 4000)
    public void value_returnsDefaultWhenPrimaryIsNull() {
        IOCase result = IOCase.value(null, IOCase.INSENSITIVE);
        assertEquals(IOCase.INSENSITIVE, result);
    }

    @Test(timeout = 4000)
    public void value_returnsPrimaryWhenNonNull() {
        IOCase result = IOCase.value(IOCase.SENSITIVE, IOCase.INSENSITIVE);
        assertEquals(IOCase.SENSITIVE, result);
    }

    @Test(timeout = 4000)
    public void value_returnsSameInstanceForSameInputs() {
        IOCase result = IOCase.value(IOCase.INSENSITIVE, IOCase.INSENSITIVE);
        assertEquals(IOCase.INSENSITIVE, result);
    }

    @Test(timeout = 4000)
    public void value_returnsSystemDefaultWhenPrimaryIsNull() {
        IOCase result = IOCase.value(null, IOCase.SYSTEM);
        assertEquals(IOCase.SYSTEM, result);
    }

    // Tests for checkIndexOf
    @Test(timeout = 4000)
    public void checkIndexOf_findsSubstringAtStart() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int index = insensitive.checkIndexOf("1", 0, "1");
        assertEquals(0, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_findsSubstringWithNegativeStartAdjusted() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int index = insensitive.checkIndexOf("\"93F*1y", -794, "\"93F*1y");
        assertEquals(0, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_findsSubstringAtPosition() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int index = insensitive.checkIndexOf("XH{o(jPCKq3L?>", -31, "p");
        assertEquals(6, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_returnsNegativeForNoMatch() {
        IOCase system = IOCase.SYSTEM;
        int index = system.checkIndexOf("5La,\"KK)#Ep.w:5veX", 0, "1");
        assertEquals(-1, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_returnsNegativeForEmptySearchString() {
        IOCase system = IOCase.SYSTEM;
        int index = system.checkIndexOf("aRG9v=v)b", 2351, "");
        assertEquals(-1, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_returnsNegativeForNullSearchString() {
        IOCase sensitive = IOCase.SENSITIVE;
        int index = sensitive.checkIndexOf("gEp63)S_DE]h,eyp", -231, null);
        assertEquals(-1, index);
    }

    @Test(timeout = 4000)
    public void checkIndexOf_returnsNegativeForNullSourceString() {
        IOCase insensitive = IOCase.INSENSITIVE;
        int index = insensitive.checkIndexOf(null, 6, null);
        assertEquals(-1, index);
    }

    @Test(timeout = 4000, expected = Exception.class)
    public void checkIndexOf_throwsExceptionForInvalidStartIndex() {
        IOCase.INSENSITIVE.checkIndexOf("H{o(4Kq#L?M", -70537784, "H{o(4Kq#L?M");
    }

    // Tests for checkRegionMatches
    @Test(timeout = 4000)
    public void checkRegionMatches_returnsFalseForNegativePosition() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkRegionMatches("hM'", -537, "hM'");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkRegionMatches_returnsTrueForMatchingSubstring() {
        IOCase sensitive = IOCase.forName("Sensitive");
        boolean result = sensitive.checkRegionMatches("NUL", 0, "NUL");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void checkRegionMatches_returnsFalseForPositionBeyondLength() {
        IOCase sensitive = IOCase.SENSITIVE;
        boolean result = sensitive.checkRegionMatches("Sensitive", 258, "Insensitive");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkRegionMatches_returnsFalseForNullSearchString() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkRegionMatches("System", -1121, null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkRegionMatches_returnsFalseForNullSourceString() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkRegionMatches(null, 493, null);
        assertFalse(result);
    }

    // Tests for checkEquals
    @Test(timeout = 4000)
    public void checkEquals_returnsTrueForSameString() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkEquals(")", ")");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void checkEquals_returnsTrueForCaseInsensitiveMatch() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkEquals("System", "SYSTEM");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void checkEquals_returnsFalseForDifferentStrings() {
        IOCase sensitive = IOCase.SENSITIVE;
        boolean result = sensitive.checkEquals("", "&b");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkEquals_returnsFalseForNonMatchingStrings() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkEquals("krmipp?jw", "1bq26gL^");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkEquals_returnsFalseForNullAndNonNull() {
        IOCase sensitive = IOCase.forName("Sensitive");
        boolean result = sensitive.checkEquals(null, "|*+9<&3 LH");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkEquals_returnsTrueForEmptyStrings() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkEquals("", "");
        assertTrue(result);
    }

    // Tests for checkStartsWith
    @Test(timeout = 4000)
    public void checkStartsWith_returnsFalseForNonMatchingPrefix() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkStartsWith("vHnm-dNXF4", "2");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkStartsWith_returnsTrueForMatchingPrefix() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkStartsWith("\"#s6?", "\"#s6?");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void checkStartsWith_returnsFalseForNullPrefix() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkStartsWith("I", null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkStartsWith_returnsFalseForNullSource() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkStartsWith(null, null);
        assertFalse(result);
    }

    // Tests for checkEndsWith
    @Test(timeout = 4000)
    public void checkEndsWith_returnsTrueForMatchingSuffix() {
        IOCase sensitive = IOCase.SENSITIVE;
        boolean result = sensitive.checkEndsWith("|,?])9N.", "|,?])9N.");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void checkEndsWith_returnsFalseForNonMatchingSuffix() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkEndsWith("Sensitive", "gX@Wm5dEh2O");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkEndsWith_returnsFalseForNullSuffix() {
        IOCase insensitive = IOCase.INSENSITIVE;
        boolean result = insensitive.checkEndsWith("krmipp?jw", null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void checkEndsWith_returnsFalseForNullSource() {
        IOCase system = IOCase.SYSTEM;
        boolean result = system.checkEndsWith(null, null);
        assertFalse(result);
    }

    // Tests for isCaseSensitive (static and instance methods)
    @Test(timeout = 4000)
    public void isCaseSensitive_returnsTrueForSensitiveInstance() {
        IOCase sensitive = IOCase.SENSITIVE;
        assertTrue(sensitive.isCaseSensitive());
    }

    @Test(timeout = 4000)
    public void isCaseSensitive_returnsFalseForInsensitiveInstance() {
        IOCase insensitive = IOCase.INSENSITIVE;
        assertFalse(insensitive.isCaseSensitive());
    }

    @Test(timeout = 4000)
    public void staticIsCaseSensitive_returnsFalseForInsensitive() {
        boolean result = IOCase.isCaseSensitive(IOCase.INSENSITIVE);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void staticIsCaseSensitive_returnsTrueForSystemWhenSensitive() {
        boolean result = IOCase.isCaseSensitive(IOCase.SYSTEM);
        assertTrue(result); // Assuming system is case-sensitive in test env
    }

    @Test(timeout = 4000)
    public void staticIsCaseSensitive_returnsFalseForNull() {
        boolean result = IOCase.isCaseSensitive(null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void isCaseSensitive_returnsFalseForInsensitiveConstant() {
        IOCase insensitive = IOCase.forName("Insensitive");
        assertFalse(insensitive.isCaseSensitive());
    }

    // Tests for name, toString, and valueOf
    @Test(timeout = 4000)
    public void getName_returnsCorrectNameForInsensitive() {
        IOCase insensitive = IOCase.INSENSITIVE;
        assertEquals("Insensitive", insensitive.getName());
    }

    @Test(timeout = 4000)
    public void toString_returnsName() {
        IOCase insensitive = IOCase.INSENSITIVE;
        assertEquals("Insensitive", insensitive.toString());
    }

    @Test(timeout = 4000)
    public void valueOf_returnsSensitiveForName() {
        IOCase sensitive = IOCase.valueOf("SENSITIVE");
        assertEquals(IOCase.SENSITIVE, sensitive);
    }

    @Test(timeout = 4000)
    public void valueOf_returnsInsensitiveForName() {
        IOCase insensitive = IOCase.valueOf("INSENSITIVE");
        assertEquals("Insensitive", insensitive.getName());
    }

    // Tests for values and exception cases
    @Test(timeout = 4000)
    public void values_returnsAllConstants() {
        IOCase[] cases = IOCase.values();
        assertEquals(3, cases.length);
    }

    @Test(timeout = 4000)
    public void forName_throwsExceptionForInvalidName() {
        try {
            IOCase.forName("7VKlZdfe6fjn*5");
            fail("Expected IllegalArgumentException for invalid IOCase name");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal IOCase name: 7VKlZdfe6fjn*5", e.getMessage());
        }
    }
}