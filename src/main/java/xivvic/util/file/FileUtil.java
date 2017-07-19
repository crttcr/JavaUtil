package xivvic.util.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil
{
	// TODO Test this class
	public static List<String> filesMatchingPatterns(Path path, String[] patterns, boolean caseSensitive)
	{
		if (path == null || patterns == null || patterns.length == 0)
		{
			return Collections.emptyList();
		}

		final Predicate<String> test = t ->
		{
			for (String p : patterns)
			{
				if (t.matches(p))
				{
					return true;
				}
			}
			return false;
		};

		List<String> rv;
		try
		{
			rv = Files
			.walk(path)
			.map(p -> p.getFileName().toString())
			.filter(test)
			.collect(Collectors.toList());

			return rv;
		}
		catch (IOException e)
		{
			log.warn("Excetion reading directory list {}: {}", path, e.getMessage());
			return Collections.emptyList();
		}
	}


	@SuppressWarnings("resource")
	public static FileChannel openFileOutputChannel(Path path)
	{
		Objects.requireNonNull(path);

		String name = path.normalize().toString();

		RandomAccessFile raf = null;

		try
		{
			raf = new RandomAccessFile(name, "rwd");
		}
		catch (Exception e)
		{
			final String msg = "Failed to open file [{}] for writing: [{}]: ";
			log.warn(msg, path.toString(), e.getMessage());
			return null;
		}

		return raf.getChannel();
	}

	public static void closeOutputChannel(FileChannel c)
	{
		Objects.requireNonNull(c);

		if (! c.isOpen())
		{
			return;
		}

		try
		{
			c.close();
		}
		catch (IOException e)
		{
			final String msg = "Failed to close output channel [{}]: {} ";
			log.warn(msg, c, e.getMessage());
		}
	}

	public static int string2file(String content, FileChannel fc)
	{
		Objects.requireNonNull(content);
		Objects.requireNonNull(fc);

		if (content.length() == 0)
		{
			return 0;
		}

		int written = 0;
		ByteBuffer buf = ByteBuffer.wrap(content.getBytes());

		try
		{
			written = fc.write(buf);
		}
		catch (IOException e)
		{
			final String msg = "Exception writing output to channel [{}]: {} ";
			log.warn(msg, fc, e.getMessage());
			return 0;
		}

		return written;
	}
}
