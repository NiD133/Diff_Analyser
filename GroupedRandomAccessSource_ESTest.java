package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.itextpdf.text.io.ArrayRandomAccessSource;
import com.itextpdf.text.io.ByteBufferRandomAccessSource;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.GroupedRandomAccessSource;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.MappedChannelRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.WindowRandomAccessSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
public class GroupedRandomAccessSourceTest extends GroupedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadFromGroupedSource() throws Throwable {
        // Arrange
        byte[] data = new byte[11];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int bytesRead = groupedSource.get(10L, data, 5, 5);

        // Assert
        assertEquals(5, bytesRead);
    }

    @Test(timeout = 4000)
    public void testInvalidReadParameters() throws Throwable {
        // Arrange
        byte[] data = new byte[1];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int bytesRead = groupedSource.get(0L, data, -10, -10);

        // Assert
        assertEquals(2L, groupedSource.length());
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testSourceInUse() throws Throwable {
        // Arrange
        byte[] data = new byte[0];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.sourceInUse(sources[0]);

        // Assert
        assertEquals(0L, groupedSource.length());
    }

    @Test(timeout = 4000)
    public void testGroupedSourceLength() throws Throwable {
        // Arrange
        byte[] data = new byte[5];
        RandomAccessSource[] sources = {
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data)),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data)),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        long length = groupedSource.length();

        // Assert
        assertEquals(15L, length);
    }

    @Test(timeout = 4000)
    public void testWindowSourceNegativeLength() throws Throwable {
        // Arrange
        byte[] data = new byte[11];
        RandomAccessSource[] sources = new RandomAccessSource[9];
        sources[0] = new ArrayRandomAccessSource(data);
        sources[1] = new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data));
        sources[2] = new WindowRandomAccessSource(sources[1], -2024L, -2024L);
        for (int i = 3; i < 9; i++) {
            sources[i] = sources[1];
        }
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        long length = groupedSource.length();

        // Assert
        assertEquals(-1936L, length);
    }

    @Test(timeout = 4000)
    public void testStartingSourceIndex() throws Throwable {
        // Arrange
        byte[] data = new byte[1];
        RandomAccessSource[] sources = {
            new IndependentRandomAccessSource(new WindowRandomAccessSource(new ArrayRandomAccessSource(data), 7L, 7L)),
            new WindowRandomAccessSource(new ArrayRandomAccessSource(data), 7L, 7L)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int startingIndex = groupedSource.getStartingSourceIndex(7L);

        // Assert
        assertEquals(1, startingIndex);
        assertEquals(14L, groupedSource.length());
    }

    @Test(timeout = 4000)
    public void testReadSpecificByte() throws Throwable {
        // Arrange
        byte[] data = new byte[4];
        data[2] = (byte) 3;
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data)),
            new ArrayRandomAccessSource(data),
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int byteValue = groupedSource.get(2L);

        // Assert
        assertEquals(3, byteValue);
        assertEquals(20L, groupedSource.length());
    }

    @Test(timeout = 4000)
    public void testCloseAndRead() throws Throwable {
        // Arrange
        byte[] data = new byte[3];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.close();

        // Assert
        try {
            groupedSource.get(1L, data, 1, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCloseAndReadSingleByte() throws Throwable {
        // Arrange
        byte[] data = new byte[1];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.close();

        // Assert
        try {
            groupedSource.get(1L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadBeyondBounds() throws Throwable {
        // Arrange
        byte[] data = new byte[7];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act & Assert
        try {
            groupedSource.get(1L, data, 5239, 1);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadFromClosedSource() throws Throwable {
        // Arrange
        byte[] data = new byte[10];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.close();

        // Assert
        try {
            groupedSource.get(10L);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullSourceArray() throws Throwable {
        // Act & Assert
        try {
            new GroupedRandomAccessSource((RandomAccessSource[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEmptySourceArray() throws Throwable {
        // Arrange
        RandomAccessSource[] sources = new RandomAccessSource[0];

        // Act & Assert
        try {
            new GroupedRandomAccessSource(sources);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffset() throws Throwable {
        // Arrange
        byte[] data = new byte[7];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int bytesRead = groupedSource.get(-3L, data, -3, -3);

        // Assert
        assertEquals(7L, groupedSource.length());
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleByte() throws Throwable {
        // Arrange
        byte[] data = new byte[2];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int byteValue = groupedSource.get(0L);

        // Assert
        assertEquals(2L, groupedSource.length());
        assertEquals(0, byteValue);
    }

    @Test(timeout = 4000)
    public void testSourceReleased() throws Throwable {
        // Arrange
        byte[] data = new byte[0];
        RandomAccessSource[] sources = {
            new ArrayRandomAccessSource(data),
            new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(data))
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.sourceReleased(sources[0]);

        // Assert
        assertEquals(0L, groupedSource.length());
    }
}