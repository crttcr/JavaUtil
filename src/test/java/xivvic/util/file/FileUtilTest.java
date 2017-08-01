package xivvic.util.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilTest
{
	private static final String[] JAVA_FILE_PATTERN_LIST = { ".*\\.java" };

	@Mock FileChannel channel;

	@Test
	public void onGetPaths_withNullPath_thenReturnEmptyList() throws Exception
	{
		assertTrue(FileUtil.getPaths(null, FileUtil.PATH_PREDICATE_TRUE).isEmpty());
	}

	@Test
	public void onGetPaths_withNullTest_thenReturnAllFiles() throws Exception
	{
		// Arrange
		//
		File  tmp = File.createTempFile("/tmp", ".junk.tmp");
		Path path = tmp.toPath().getParent();

		// Act
		//
		List<Path> result = FileUtil.getPaths(path, null);

		// Assert
		//
		assertNotNull(result);
		assertTrue(result.size() >= 1);

		// Cleanup
		//
		tmp.delete();
	}

	@Test
	public void onGetPaths_withPredicate_thenReturnMatchingFiles() throws Exception

	{
		// Arrange
		//
		String                     suffix = "funky.junk.tmp";
		File                          tmp = File.createTempFile("/tmp", suffix);
		File                         tmp2 = File.createTempFile("/tmp", suffix + ".foo");
		DirectoryStream.Filter<Path> test = p -> p.toString().endsWith(suffix);
		Path                         path = tmp.toPath().getParent();

		// Act
		//
		List<Path> result = FileUtil.getPaths(path, test);

		// Assert
		//
		assertNotNull(result);
		assertEquals(1, result.size());

		// Cleanup
		//
		tmp.delete();
		tmp2.delete();
	}

	@Test(expected = NullPointerException.class)
	public void onOpenOutputChannel_withNull_thenThrowException()
	{
		FileUtil.openFileOutputChannel(null);
	}

	@Test
	public void onOpenOutputChannel_withValidPath_thenReturnChannel() throws Exception
	{
		// Arrange
		//
		File tmp = File.createTempFile("/tmp", ".junk.tmp");

		// Act
		//
		FileChannel c = FileUtil.openFileOutputChannel(tmp.toPath());

		// Assert
		//
		assertNotNull(c);

		// Cleanup
		//
		FileUtil.closeOutputChannel(c);
		tmp.delete();
	}

	@Test(expected = NullPointerException.class)
	public void onString2file_withNullContent_thenThrowException() throws Exception
	{
		// Arrange
		//
		File      tmp = File.createTempFile("/tmp", ".junk.tmp");
		FileChannel c = FileUtil.openFileOutputChannel(tmp.toPath());

		// Act
		//
		try
		{
			FileUtil.string2file(null, c);
			FileUtil.closeOutputChannel(c);
			fail("Should have thrown exception");
		}
		finally
		{
			FileUtil.closeOutputChannel(c);
			tmp.delete();
		}
	}

	@Test
	public void onString2file_withEmptyString_thenCreateEmptyFile() throws Exception
	{
		// Arrange
		//
		File      tmp = File.createTempFile("/tmp", ".junk.tmp");
		FileChannel c = FileUtil.openFileOutputChannel(tmp.toPath());

		// Act
		//
		int count = FileUtil.string2file("", c);
		FileUtil.closeOutputChannel(c);
		String result = new String(Files.readAllBytes(tmp.toPath()));

		// Assert
		//
		assertEquals(0, count);
		assertNotNull(result);
		assertEquals("", result);

		// Cleanup
		//
		tmp.delete();
	}

	@Test
	public void onString2file_withValidString_thenWriteToFile() throws Exception
	{
		// Arrange
		//
		String   text = "Hickory was a strange nickname.";
		File      tmp = File.createTempFile("/tmp", ".junk.tmp");
		FileChannel c = FileUtil.openFileOutputChannel(tmp.toPath());

		// Act
		//
		int count = FileUtil.string2file(text, c);
		FileUtil.closeOutputChannel(c);
		String result = new String(Files.readAllBytes(tmp.toPath()));

		// Assert
		//
		assertEquals(text.length(), count);
		assertNotNull(result);
		assertEquals(text, result);

		// Cleanup
		//
		tmp.delete();
	}

	@Test
	public void onString2file_withMultipleStrings_thenWriteAllToFile() throws Exception
	{
		// Arrange
		//
		String       a = "In the town\n";
		String       b = "where I was born\n";
		String       c = "lived a man.";
		File       tmp = File.createTempFile("/tmp", ".junk.tmp");
		FileChannel fc = FileUtil.openFileOutputChannel(tmp.toPath());

		// Act
		//
		FileUtil.string2file(a, fc);
		FileUtil.string2file(b, fc);
		FileUtil.string2file(c, fc);
		FileUtil.closeOutputChannel(fc);
		String result = new String(Files.readAllBytes(tmp.toPath()));

		// Assert
		//
		assertNotNull(result);
		assertTrue(result.contains(a));
		assertTrue(result.contains(b));
		assertTrue(result.contains(c));

		// Cleanup
		//
		tmp.delete();
	}

	@Test(expected = NullPointerException.class)
	public void onString2file_withNullChannel_thenThrowException()
	{
		FileUtil.string2file("abc", null);
	}

	@Test
	public void onString2file_withChannelThatThrowsException_thenReturnZeroBytesWritten() throws Exception
	{
		// Arrange
		//
		FileChannel mock_fc = mock(FileChannel.class);
		IOException ex = new IOException("Boom");
		when(mock_fc.write(any(ByteBuffer.class))).thenThrow(ex);

		// Act
		//
		int written = FileUtil.string2file("abc", mock_fc);

		// Assert
		//
		assertEquals(0, written);
	}

	@Test(expected = NullPointerException.class)
	public void onCloseOutputChannel_withNull_thenThrowException()
	{
		FileUtil.closeOutputChannel(null);
	}

	@Test
	public void onCloseOutputChannel_withClosedChannel_thenIgnore() throws Exception
	{
		// Arrange
		//
		File tmp = File.createTempFile("/tmp", ".junk.tmp");
		FileChannel c = FileUtil.openFileOutputChannel(tmp.toPath());
		c.close();

		// Act
		//
		FileUtil.closeOutputChannel(c);

		// Cleanup
		//
		tmp.delete();
	}

	@Test
	public void onListMatchingFiles_withValidInputs_thenReturnAListOfFiles()
	{
		// Arrange
		//
		Path path = pathForThisPackage();

		// Act
		//
		List<String> ns = FileUtil.filesMatchingPatterns(path, JAVA_FILE_PATTERN_LIST, false);

		// Assert
		//
		assertNotNull(ns);
		assertTrue(ns.size() > 0);
		for (String n: ns)
		{
			assertTrue(n.contains(".java"));
		}
	}

	@Test
	public void onListMatchingFiles_withNullPath_thenReturnEmptyList()
	{
		assertTrue(FileUtil.filesMatchingPatterns(null, JAVA_FILE_PATTERN_LIST, true ).isEmpty());
		assertTrue(FileUtil.filesMatchingPatterns(null, JAVA_FILE_PATTERN_LIST, false).isEmpty());
	}

	@Test
	public void onListMatchingFiles_withNullPatterns_thenReturnEmptyList()
	{
		assertTrue(FileUtil.filesMatchingPatterns(pathForThisPackage(), null, true ).isEmpty());
		assertTrue(FileUtil.filesMatchingPatterns(pathForThisPackage(), null, false).isEmpty());
	}

	@Test
	public void onListMatchingFiles_withEmptyPatterns_thenReturnEmptyList()
	{
		assertTrue(FileUtil.filesMatchingPatterns(pathForThisPackage(), new String[0], true ).isEmpty());
		assertTrue(FileUtil.filesMatchingPatterns(pathForThisPackage(), new String[0], false).isEmpty());
	}

	@Test
	public void onListMatchingFiles_withBadPath_thenReturnEmptyList()
	{
		assertTrue(FileUtil.filesMatchingPatterns(Paths.get("##"), JAVA_FILE_PATTERN_LIST, true ).isEmpty());
		assertTrue(FileUtil.filesMatchingPatterns(Paths.get("##"), JAVA_FILE_PATTERN_LIST, false).isEmpty());
	}



	///////////////////////////////
	// Helpers                   //
	///////////////////////////////

	private Path pathForThisPackage()
	{
		String middle = "src/main/java";

		try
		{
			String current = new java.io.File( "." ).getCanonicalPath();
			String pkgpath = FileUtilTest.class.getPackage().getName().replace('.', '/');
			Path        rv = Paths.get(current, middle, pkgpath);

			if (! Files.exists(rv))
			{
				String   s = rv.toAbsolutePath().toString();
				String msg = String.format("Constructed path [%s] does not exist",  s);
				fail(msg);
			}

			return rv;
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to get path for this package");
		}
	}

}
