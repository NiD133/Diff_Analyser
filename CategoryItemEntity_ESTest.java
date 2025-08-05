package org.jfree.chart.entity;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.time.Clock;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.JapaneseDate;
import javax.swing.JLayeredPane;
import javax.swing.text.DefaultCaret;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CategoryItemEntity_ESTest extends CategoryItemEntity_ESTest_scaffolding {

    // Constructor Tests
    @Test
    public void testConstructorWithNullDatasetThrowsException() {
        DefaultCaret area = new DefaultCaret();
        try {
            new CategoryItemEntity<>(area, "tooltip", "url", null, 1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'dataset' argument.", e.getMessage());
        }
    }

    // Setter Tests
    @Test
    public void testSetRowKeyWithNull() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        entity.setRowKey(null);
        assertNull(entity.getRowKey());
    }

    @Test
    public void testSetColumnKeyWithNull() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        entity.setColumnKey(null);
        assertNull(entity.getColumnKey());
    }

    @Test
    public void testSetDatasetWithNullThrowsException() {
        Rectangle area = new Rectangle();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        try {
            entity.setDataset(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'dataset' argument.", e.getMessage());
        }
    }

    // Getter Tests
    @Test
    public void testGetRowKey() {
        Polygon area = new Polygon();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        LocalDate rowKey = MockLocalDate.now();
        CategoryItemEntity<ChronoLocalDate, Integer> entity = 
            new CategoryItemEntity<>(area, "tooltip", "url", dataset, rowKey, 0);
        
        assertSame(rowKey, entity.getRowKey());
    }

    @Test
    public void testGetColumnKey() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        Clock clock = MockClock.systemUTC();
        JapaneseDate columnKey = MockJapaneseDate.now(clock);
        CategoryItemEntity<Integer, ChronoLocalDate> entity = 
            new CategoryItemEntity<>(area, "tooltip", "url", dataset, 0, columnKey);
        
        assertSame(columnKey, entity.getColumnKey());
    }

    @Test
    public void testGetDataset() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        assertSame(dataset, entity.getDataset());
    }

    // Equals Tests
    @Test
    public void testEqualsWithSameObject() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        assertTrue(entity.equals(entity));
    }

    @Test
    public void testEqualsWithDifferentType() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<String, String> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<String, String> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, "rowKey", "colKey");
        
        assertFalse(entity.equals("different-type"));
    }

    @Test
    public void testEqualsWithNullKeys() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity1 = 
            new CategoryItemEntity<>(area, null, null, dataset, 0, 0);
        CategoryItemEntity<Integer, Integer> entity2 = 
            new CategoryItemEntity<>(area, null, null, dataset, 0, 0);
        
        assertTrue(entity1.equals(entity2));
    }

    @Test
    public void testEqualsWithDifferentRowKeys() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        DefaultStatisticalCategoryDataset<Integer, Integer> dataset = new DefaultStatisticalCategoryDataset<>();
        
        CategoryItemEntity<Integer, Integer> entity1 = 
            new CategoryItemEntity<>(area, "tooltip1", "url", dataset, 0, 0);
        CategoryItemEntity<Integer, Integer> entity2 = 
            new CategoryItemEntity<>(area, "tooltip2", "url", dataset, 0, 0);
        
        assertFalse(entity1.equals(entity2));
    }

    @Test
    public void testEqualsWithDifferentColumnKeys() {
        Polygon area1 = new Polygon();
        DefaultCaret area2 = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        
        CategoryItemEntity<Integer, Integer> entity1 = 
            new CategoryItemEntity<>(area1, "tooltip", "url1", dataset, 0, 0);
        CategoryItemEntity<Integer, Integer> entity2 = 
            new CategoryItemEntity<>(area2, "tooltip", "url2", dataset, 0, 0);
        
        assertFalse(entity1.equals(entity2));
    }

    @Test
    public void testEqualsWithDifferentDatasets() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset1 = new DefaultMultiValueCategoryDataset<>();
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> dataset2 = new DefaultBoxAndWhiskerCategoryDataset<>();
        
        CategoryItemEntity<Integer, Integer> entity1 = 
            new CategoryItemEntity<>(area, null, null, dataset1, 0, 0);
        entity1.setDataset(dataset2);
        CategoryItemEntity<Integer, Integer> entity2 = 
            new CategoryItemEntity<>(area, null, null, dataset1, 0, 0);
        
        assertFalse(entity1.equals(entity2));
    }

    // Clone Tests
    @Test
    public void testCloneWithNullFieldsThrowsNPE() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = 
            new CategoryItemEntity<>(area, null, null, dataset, null, null);
        
        Object clone = entity.clone();
        try {
            entity.equals(clone);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // toString Test
    @Test
    public void testToString() {
        DefaultCaret area = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = 
            new CategoryItemEntity<>(area, "row", "col", dataset, 0, 0);
        
        String result = entity.toString();
        assertNotNull(result);
        assertTrue(result.contains("CategoryItemEntity"));
    }
}