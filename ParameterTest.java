/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link Parameter}.
 *
 * @author Ben Yu
 */
@NullUnmarked
public class ParameterTest extends TestCase {

  /**
   * Tests that all public methods of Parameter instances handle null arguments correctly.
   * 
   * <p>This test is skipped on Android VMs because Parameter references AnnotatedType,
   * which is not available on Android and would cause NoClassDefFoundError when
   * NullPointerTester calls Class.getDeclaredMethods().
   */
  public void testNullPointerHandling() {
    if (isRunningOnAndroidVm()) {
      return; // Skip test on Android VMs due to AnnotatedType unavailability
    }
    
    for (Parameter parameter : getAllParametersFromTestMethods()) {
      new NullPointerTester().testAllPublicInstanceMethods(parameter);
    }
  }

  /**
   * Tests that Parameter instances implement equals() and hashCode() correctly.
   * Each parameter should be equal only to itself and have consistent hash codes.
   */
  public void testEqualsAndHashCode() {
    EqualsTester equalsTester = new EqualsTester();
    
    // Each parameter should be in its own equality group (equal only to itself)
    for (Parameter parameter : getAllParametersFromTestMethods()) {
      equalsTester.addEqualityGroup(parameter);
    }
    
    equalsTester.testEquals();
  }

  // Helper methods for better code organization and readability

  /**
   * Checks if the current VM is an Android VM by attempting to load AnnotatedType.
   * 
   * @return true if running on Android VM (where AnnotatedType is not available)
   */
  private boolean isRunningOnAndroidVm() {
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
      return false; // AnnotatedType is available, not Android
    } catch (ClassNotFoundException e) {
      return true; // AnnotatedType not found, likely Android VM
    }
  }

  /**
   * Extracts all Parameter instances from all declared methods in this test class.
   * 
   * @return an array of all parameters from test methods
   */
  private Parameter[] getAllParametersFromTestMethods() {
    Method[] testMethods = ParameterTest.class.getDeclaredMethods();
    
    // Count total parameters first to size array correctly
    int totalParameterCount = 0;
    for (Method method : testMethods) {
      totalParameterCount += Invokable.from(method).getParameters().size();
    }
    
    // Collect all parameters
    Parameter[] allParameters = new Parameter[totalParameterCount];
    int parameterIndex = 0;
    
    for (Method method : testMethods) {
      for (Parameter parameter : Invokable.from(method).getParameters()) {
        allParameters[parameterIndex++] = parameter;
      }
    }
    
    return allParameters;
  }

  // Test fixture methods - these provide parameters for the tests above

  /**
   * Test method with multiple parameters of the same type.
   * Used as test data for Parameter testing.
   */
  @SuppressWarnings("unused")
  private void methodWithSameTypeParameters(int firstInteger, int secondInteger) {
    // Method body intentionally empty - used only for parameter reflection
  }

  /**
   * Test method with parameters of different types.
   * Used as test data for Parameter testing.
   */
  @SuppressWarnings("unused") 
  private void methodWithDifferentTypeParameters(int integerParam, String stringParam) {
    // Method body intentionally empty - used only for parameter reflection
  }
}