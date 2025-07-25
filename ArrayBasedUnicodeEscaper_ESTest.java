package com.google.common.escape;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ArrayBasedUnicodeEscaper_Test extends ArrayBasedUnicodeEscaper_Test_scaffolding {
  @Test
  public void testEscape() {
    // Arrange
    Map<Character, String> replacementMap = new HashMap<>();
    replacementMap.put('a', "&amp;");
    replacementMap.put('b', "&quot;");
    ArrayBasedUnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(replacementMap, 0x21, 0x7E);
    
    // Act
    String escaped = escaper.escape("abc");
    
    // Assert
    assertEquals("&amp;b&quot;c", escaped);
  }
}