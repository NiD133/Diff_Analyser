/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Tests for EnumOrdinalTypeHandler which handles enum values by their ordinal position.
 * The handler converts enum values to integers (0-based ordinals) for database storage
 * and converts integers back to enum values when retrieving from database.
 */
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  /**
   * Test enum with ordinal values: ONE=0, TWO=1
   */
  enum TestEnum {
    ONE,  // ordinal = 0
    TWO   // ordinal = 1
  }

  // Test constants for better readability
  private static final int PARAMETER_INDEX = 1;
  private static final String COLUMN_NAME = "column";
  private static final int ENUM_ONE_ORDINAL = 0;
  private static final int ENUM_TWO_ORDINAL = 1;
  
  private static final TypeHandler<TestEnum> enumTypeHandler = new EnumOrdinalTypeHandler<>(TestEnum.class);

  // ========== Parameter Setting Tests ==========

  @Override
  @Test
  public void shouldSetParameter() throws Exception {
    // Given: An enum value ONE with ordinal 0
    TestEnum enumValue = TestEnum.ONE;
    
    // When: Setting the parameter
    enumTypeHandler.setParameter(ps, PARAMETER_INDEX, enumValue, null);
    
    // Then: Should set integer value 0 (the ordinal of ONE)
    verify(ps).setInt(PARAMETER_INDEX, ENUM_ONE_ORDINAL);
  }

  @Test
  void shouldSetNullParameter() throws Exception {
    // Given: A null enum value
    TestEnum nullEnumValue = null;
    JdbcType jdbcType = JdbcType.VARCHAR;
    
    // When: Setting null parameter
    enumTypeHandler.setParameter(ps, PARAMETER_INDEX, nullEnumValue, jdbcType);
    
    // Then: Should set null with correct JDBC type
    verify(ps).setNull(PARAMETER_INDEX, jdbcType.TYPE_CODE);
  }

  @Test
  void shouldSetParameterWithSecondEnumValue() throws Exception {
    // Given: An enum value TWO with ordinal 1
    TestEnum enumValue = TestEnum.TWO;
    
    // When: Setting the parameter
    enumTypeHandler.setParameter(ps, PARAMETER_INDEX, enumValue, null);
    
    // Then: Should set integer value 1 (the ordinal of TWO)
    verify(ps).setInt(PARAMETER_INDEX, ENUM_TWO_ORDINAL);
  }

  // ========== Result Retrieval by Column Name Tests ==========

  @Override
  @Test
  public void shouldGetResultFromResultSetByName() throws Exception {
    // Given: Database returns ordinal 0 for the column
    when(rs.getInt(COLUMN_NAME)).thenReturn(ENUM_ONE_ORDINAL);
    when(rs.wasNull()).thenReturn(false);
    
    // When: Getting result by column name
    TestEnum result = enumTypeHandler.getResult(rs, COLUMN_NAME);
    
    // Then: Should return enum ONE (ordinal 0)
    assertEquals(TestEnum.ONE, result);
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByName() throws Exception {
    // Given: Database returns a value but wasNull() indicates it was NULL
    when(rs.getInt(COLUMN_NAME)).thenReturn(ENUM_ONE_ORDINAL);
    when(rs.wasNull()).thenReturn(true);  // This indicates the value was NULL
    
    // When: Getting result by column name
    TestEnum result = enumTypeHandler.getResult(rs, COLUMN_NAME);
    
    // Then: Should return null despite the integer value
    assertNull(result);
  }

  @Test
  void shouldGetSecondEnumValueFromResultSetByName() throws Exception {
    // Given: Database returns ordinal 1 for the column
    when(rs.getInt(COLUMN_NAME)).thenReturn(ENUM_TWO_ORDINAL);
    when(rs.wasNull()).thenReturn(false);
    
    // When: Getting result by column name
    TestEnum result = enumTypeHandler.getResult(rs, COLUMN_NAME);
    
    // Then: Should return enum TWO (ordinal 1)
    assertEquals(TestEnum.TWO, result);
  }

  // ========== Result Retrieval by Column Position Tests ==========

  @Override
  @Test
  public void shouldGetResultFromResultSetByPosition() throws Exception {
    // Given: Database returns ordinal 0 at position 1
    when(rs.getInt(PARAMETER_INDEX)).thenReturn(ENUM_ONE_ORDINAL);
    when(rs.wasNull()).thenReturn(false);
    
    // When: Getting result by column position
    TestEnum result = enumTypeHandler.getResult(rs, PARAMETER_INDEX);
    
    // Then: Should return enum ONE (ordinal 0)
    assertEquals(TestEnum.ONE, result);
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByPosition() throws Exception {
    // Given: Database returns a value but wasNull() indicates it was NULL
    when(rs.getInt(PARAMETER_INDEX)).thenReturn(ENUM_ONE_ORDINAL);
    when(rs.wasNull()).thenReturn(true);  // This indicates the value was NULL
    
    // When: Getting result by column position
    TestEnum result = enumTypeHandler.getResult(rs, PARAMETER_INDEX);
    
    // Then: Should return null despite the integer value
    assertNull(result);
  }

  // ========== CallableStatement Tests ==========

  @Override
  @Test
  public void shouldGetResultFromCallableStatement() throws Exception {
    // Given: CallableStatement returns ordinal 0 at position 1
    when(cs.getInt(PARAMETER_INDEX)).thenReturn(ENUM_ONE_ORDINAL);
    when(cs.wasNull()).thenReturn(false);
    
    // When: Getting result from callable statement
    TestEnum result = enumTypeHandler.getResult(cs, PARAMETER_INDEX);
    
    // Then: Should return enum ONE (ordinal 0)
    assertEquals(TestEnum.ONE, result);
  }

  @Override
  @Test
  public void shouldGetResultNullFromCallableStatement() throws Exception {
    // Given: CallableStatement returns a value but wasNull() indicates it was NULL
    when(cs.getInt(PARAMETER_INDEX)).thenReturn(ENUM_ONE_ORDINAL);
    when(cs.wasNull()).thenReturn(true);  // This indicates the value was NULL
    
    // When: Getting result from callable statement
    TestEnum result = enumTypeHandler.getResult(cs, PARAMETER_INDEX);
    
    // Then: Should return null despite the integer value
    assertNull(result);
  }
}