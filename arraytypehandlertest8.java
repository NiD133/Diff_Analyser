package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayTypeHandler}.
 * This test class focuses on scenarios involving null values.
 */
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> ARRAY_TYPE_HANDLER = new ArrayTypeHandler();

  /**
   * This test verifies that the ArrayTypeHandler correctly returns null
   * when the database provides a null SQL ARRAY, accessed by column index.
   * The method signature is defined by the abstract base class {@link BaseTypeHandlerTest}.
   */
  @Override
  @Test
  void shouldGetResultNullFromResultSetByPosition() throws Exception {
    // Arrange: Configure the mocked ResultSet to return null for the array at a specific column index.
    // This simulates reading a NULL value from an array-type column in the database.
    int columnIndex = 1;
    when(rs.getArray(columnIndex)).thenReturn(null);

    // Act: Call the method under test to retrieve the result.
    Object result = ARRAY_TYPE_HANDLER.getResult(rs, columnIndex);

    // Assert: Verify that the handler correctly translated the database NULL to a Java null.
    assertNull(result, "The handler should return null when the database returns a null array.");
  }
}