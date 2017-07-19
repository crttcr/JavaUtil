package xivvic.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
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

}
