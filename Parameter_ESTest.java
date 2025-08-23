package com.google.common.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

import org.junit.Test;

/**
 * Unit tests for {@link Parameter}.
 *
 * These tests focus on:
 * - Null handling and exceptions for the AnnotatedElement API.
 * - Behavior with empty annotations.
 * - Equality semantics and basic getters.
 * - AnnotatedType casting rules.
 * - toString formatting.
 */
public class ParameterTest {

  private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];

  @Test
  public void isAnnotationPresent_nullType_throwsNPE() {
    Parameter p = new Parameter(null, 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, () -> p.isAnnotationPresent(null));
  }

  @Test
  public void getAnnotation_nullType_throwsNPE() {
    Parameter p = new Parameter(mock(Invokable.class), 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, () -> p.getAnnotation(null));
  }

  @Test
  public void getAnnotationsByType_nullType_throwsNPE() {
    Parameter p = new Parameter(null, 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, () -> p.getAnnotationsByType(null));
  }

  @Test
  public void getDeclaredAnnotation_nullType_throwsNPE() {
    Parameter p = new Parameter(mock(Invokable.class), 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, () -> p.getDeclaredAnnotation(null));
  }

  @Test
  public void getDeclaredAnnotationsByType_nullType_throwsNPE() {
    Parameter p = new Parameter(mock(Invokable.class), 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, () -> p.getDeclaredAnnotationsByType(null));
  }

  @Test
  public void annotations_empty_returnsEmptyArrays() {
    Parameter p = new Parameter(mock(Invokable.class), 0, null, NO_ANNOTATIONS, null);

    assertEquals(0, p.getAnnotations().length);
    assertEquals(0, p.getDeclaredAnnotations().length);
    assertEquals(0, p.getAnnotationsByType(Annotation.class).length);
    assertEquals(0, p.getDeclaredAnnotationsByType(Annotation.class).length);
    assertNull(p.getAnnotation(Annotation.class));
    assertNull(p.getDeclaredAnnotation(Annotation.class));
  }

  @Test
  public void annotations_defensiveCopies_areNotSameInstanceAsInput() {
    // Even with an empty input array, returned arrays should be distinct instances.
    Parameter p = new Parameter(mock(Invokable.class), 0, TypeToken.of(Object.class), NO_ANNOTATIONS, null);

    assertNotSame("Should return a defensive copy", NO_ANNOTATIONS, p.getAnnotations());
    assertNotSame("Should return a defensive copy", NO_ANNOTATIONS, p.getAnnotationsByType(Annotation.class));
  }

  @Test
  public void isAnnotationPresent_onEmpty_returnsFalse() {
    Parameter p = new Parameter(mock(Invokable.class), 0, TypeToken.of(Object.class), NO_ANNOTATIONS, null);
    assertFalse(p.isAnnotationPresent(Annotation.class));
  }

  @Test
  public void getAnnotatedType_null_throwsNPE() {
    Parameter p = new Parameter(null, 0, null, NO_ANNOTATIONS, null);
    assertThrows(NullPointerException.class, p::getAnnotatedType);
  }

  @Test
  public void getAnnotatedType_notAnAnnotatedType_throwsClassCastException() {
    Parameter p = new Parameter(null, 0, null, NO_ANNOTATIONS, new Object());
    assertThrows(ClassCastException.class, p::getAnnotatedType);
  }

  @Test
  public void getDeclaringInvokable_canBeNull() {
    Parameter p = new Parameter(null, 123, null, NO_ANNOTATIONS, null);
    assertNull(p.getDeclaringInvokable());
  }

  @Test
  public void getType_returnsGivenTypeToken_evenIfNull() {
    Parameter p1 = new Parameter(null, 0, null, NO_ANNOTATIONS, null);
    assertNull(p1.getType());

    Parameter p2 = new Parameter(null, 0, TypeToken.of(Object.class), NO_ANNOTATIONS, null);
    assertEquals(TypeToken.of(Object.class), p2.getType());
  }

  @Test
  public void equals_reflexive_true() {
    Parameter p = new Parameter(mock(Invokable.class), 5, TypeToken.of(String.class), NO_ANNOTATIONS, null);
    assertTrue(p.equals(p));
  }

  @Test
  public void equals_null_false() {
    Parameter p = new Parameter(mock(Invokable.class), 5, TypeToken.of(String.class), NO_ANNOTATIONS, null);
    assertFalse(p.equals(null));
  }

  @Test
  public void equals_differentDeclaration_false() {
    Invokable<?, ?> decl1 = mock(Invokable.class);
    Invokable<?, ?> decl2 = mock(Invokable.class);
    TypeToken<?> type = TypeToken.of(Object.class);

    Parameter p1 = new Parameter(decl1, 10, type, NO_ANNOTATIONS, null);
    Parameter p2 = new Parameter(decl2, 10, type, NO_ANNOTATIONS, null);

    assertFalse(p1.equals(p2));
  }

  @Test
  public void equals_differentPosition_false() {
    Invokable<?, ?> decl = mock(Invokable.class);
    TypeToken<?> type = TypeToken.of(Object.class);

    Parameter p1 = new Parameter(decl, 1, type, NO_ANNOTATIONS, null);
    Parameter p2 = new Parameter(decl, 2, type, NO_ANNOTATIONS, null);

    assertFalse(p1.equals(p2));
  }

  @Test
  public void hashCode_doesNotThrow() {
    Parameter p = new Parameter(mock(Invokable.class), 8, TypeToken.of(Object.class), NO_ANNOTATIONS, Object.class);
    // Just ensure it is callable without throwing.
    p.hashCode();
  }

  @Test
  public void toString_withNullType_includesPosition() {
    Parameter p = new Parameter(null, 11, null, NO_ANNOTATIONS, null);
    assertEquals("null arg11", p.toString());
  }
}