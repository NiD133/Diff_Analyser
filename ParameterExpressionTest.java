package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParameterExpressionTest {

  /**
   * Tests parsing of a simple property without any additional attributes or types.
   */
  @Test
  void testSimplePropertyParsing() {
    Map<String, String> result = new ParameterExpression("id");
    Assertions.assertEquals(1, result.size(), "Expected one entry for a simple property");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
  }

  /**
   * Tests parsing of a property with spaces around it.
   */
  @Test
  void testPropertyWithSpaces() {
    Map<String, String> result = new ParameterExpression(" with spaces ");
    Assertions.assertEquals(1, result.size(), "Expected one entry for a property with spaces");
    Assertions.assertEquals("with spaces", result.get("property"), "Property should be 'with spaces'");
  }

  /**
   * Tests parsing of a property with an old-style JDBC type.
   */
  @Test
  void testPropertyWithOldStyleJdbcType() {
    Map<String, String> result = new ParameterExpression("id:VARCHAR");
    Assertions.assertEquals(2, result.size(), "Expected two entries for property with JDBC type");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "JDBC type should be 'VARCHAR'");
  }

  /**
   * Tests parsing of a property with extra whitespaces and an old-style JDBC type.
   */
  @Test
  void testOldStyleJdbcTypeWithExtraWhitespaces() {
    Map<String, String> result = new ParameterExpression(" id :  VARCHAR ");
    Assertions.assertEquals(2, result.size(), "Expected two entries for property with extra whitespaces and JDBC type");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "JDBC type should be 'VARCHAR'");
  }

  /**
   * Tests parsing of an expression with an old-style JDBC type.
   */
  @Test
  void testExpressionWithOldStyleJdbcType() {
    Map<String, String> result = new ParameterExpression("(id.toString()):VARCHAR");
    Assertions.assertEquals(2, result.size(), "Expected two entries for expression with JDBC type");
    Assertions.assertEquals("id.toString()", result.get("expression"), "Expression should be 'id.toString()'");
    Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "JDBC type should be 'VARCHAR'");
  }

  /**
   * Tests parsing of a simple property with one attribute.
   */
  @Test
  void testSimplePropertyWithOneAttribute() {
    Map<String, String> result = new ParameterExpression("id,name=value");
    Assertions.assertEquals(2, result.size(), "Expected two entries for property with one attribute");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("value", result.get("name"), "Attribute 'name' should be 'value'");
  }

  /**
   * Tests parsing of an expression with one attribute.
   */
  @Test
  void testExpressionWithOneAttribute() {
    Map<String, String> result = new ParameterExpression("(id.toString()),name=value");
    Assertions.assertEquals(2, result.size(), "Expected two entries for expression with one attribute");
    Assertions.assertEquals("id.toString()", result.get("expression"), "Expression should be 'id.toString()'");
    Assertions.assertEquals("value", result.get("name"), "Attribute 'name' should be 'value'");
  }

  /**
   * Tests parsing of a simple property with multiple attributes.
   */
  @Test
  void testSimplePropertyWithManyAttributes() {
    Map<String, String> result = new ParameterExpression("id, attr1=val1, attr2=val2, attr3=val3");
    Assertions.assertEquals(4, result.size(), "Expected four entries for property with multiple attributes");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("val1", result.get("attr1"), "Attribute 'attr1' should be 'val1'");
    Assertions.assertEquals("val2", result.get("attr2"), "Attribute 'attr2' should be 'val2'");
    Assertions.assertEquals("val3", result.get("attr3"), "Attribute 'attr3' should be 'val3'");
  }

  /**
   * Tests parsing of an expression with multiple attributes.
   */
  @Test
  void testExpressionWithManyAttributes() {
    Map<String, String> result = new ParameterExpression("(id.toString()), attr1=val1, attr2=val2, attr3=val3");
    Assertions.assertEquals(4, result.size(), "Expected four entries for expression with multiple attributes");
    Assertions.assertEquals("id.toString()", result.get("expression"), "Expression should be 'id.toString()'");
    Assertions.assertEquals("val1", result.get("attr1"), "Attribute 'attr1' should be 'val1'");
    Assertions.assertEquals("val2", result.get("attr2"), "Attribute 'attr2' should be 'val2'");
    Assertions.assertEquals("val3", result.get("attr3"), "Attribute 'attr3' should be 'val3'");
  }

  /**
   * Tests parsing of a simple property with an old-style JDBC type and multiple attributes.
   */
  @Test
  void testSimplePropertyWithOldStyleJdbcTypeAndAttributes() {
    Map<String, String> result = new ParameterExpression("id:VARCHAR, attr1=val1, attr2=val2");
    Assertions.assertEquals(4, result.size(), "Expected four entries for property with JDBC type and attributes");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "JDBC type should be 'VARCHAR'");
    Assertions.assertEquals("val1", result.get("attr1"), "Attribute 'attr1' should be 'val1'");
    Assertions.assertEquals("val2", result.get("attr2"), "Attribute 'attr2' should be 'val2'");
  }

  /**
   * Tests parsing of a simple property with spaces and multiple attributes.
   */
  @Test
  void testSimplePropertyWithSpaceAndManyAttributes() {
    Map<String, String> result = new ParameterExpression("user name, attr1=val1, attr2=val2, attr3=val3");
    Assertions.assertEquals(4, result.size(), "Expected four entries for property with spaces and attributes");
    Assertions.assertEquals("user name", result.get("property"), "Property should be 'user name'");
    Assertions.assertEquals("val1", result.get("attr1"), "Attribute 'attr1' should be 'val1'");
    Assertions.assertEquals("val2", result.get("attr2"), "Attribute 'attr2' should be 'val2'");
    Assertions.assertEquals("val3", result.get("attr3"), "Attribute 'attr3' should be 'val3'");
  }

  /**
   * Tests that leading and trailing spaces are ignored in parsing.
   */
  @Test
  void testIgnoreLeadingAndTrailingSpaces() {
    Map<String, String> result = new ParameterExpression(" id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ");
    Assertions.assertEquals(4, result.size(), "Expected four entries ignoring leading and trailing spaces");
    Assertions.assertEquals("id", result.get("property"), "Property should be 'id'");
    Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "JDBC type should be 'VARCHAR'");
    Assertions.assertEquals("val1", result.get("attr1"), "Attribute 'attr1' should be 'val1'");
    Assertions.assertEquals("val2", result.get("attr2"), "Attribute 'attr2' should be 'val2'");
  }

  /**
   * Tests handling of invalid old JDBC type format.
   */
  @Test
  void testInvalidOldJdbcTypeFormat() {
    try {
      new ParameterExpression("id:");
      Assertions.fail("Expected BuilderException due to invalid JDBC type format");
    } catch (BuilderException e) {
      Assertions.assertTrue(e.getMessage().contains("Parsing error in {id:} in position 3"), "Exception message should indicate parsing error");
    }
  }

  /**
   * Tests handling of invalid JDBC type option using an expression.
   */
  @Test
  void testInvalidJdbcTypeOptUsingExpression() {
    try {
      new ParameterExpression("(expression)+");
      Assertions.fail("Expected BuilderException due to invalid JDBC type option");
    } catch (BuilderException e) {
      Assertions.assertTrue(e.getMessage().contains("Parsing error in {(expression)+} in position 12"), "Exception message should indicate parsing error");
    }
  }
}