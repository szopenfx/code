package life;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Library
{
	private static class Pattern extends ArrayList<String> 
	{
	}
	
	private static class PatternFile extends HashMap<String, Pattern> 
	{
		PatternFile(File file) throws IOException
		{
			BufferedReader r = new BufferedReader(new FileReader(file)); // lame.

			String name = null;
			String error = null;
			
			while (error == null)
			{
				String line = r.readLine();
				
				if (line == null)
				{
					break;
				}
				
				if (line.startsWith(":"))
				{
					name = line.substring(1); 
					put(name, new Pattern());
					continue; 
				}
				
				if (line.startsWith("#") && name != null)
				{
					get(name).add(line.substring(1));
					continue; 
				}
				
				error = String.format("not trusting file %s", file.getName());
			}
			
			if (error != null)
				throw new IOException(error);
		}
	}
	
	private static class PatternList extends HashMap<String, PatternFile> 
	{
		PatternList(String dirname) throws IOException
		{
			for (File f : new File(dirname).listFiles())
			{
				put(f.getName(), new PatternFile(f));
			}
		}
	}
	
	private PatternList patterns;
	
	public Library(String dirname) throws IOException
	{
		patterns = new PatternList("patterns");
	}
	
	public PatternFile get(String filename)
	{
		return patterns.get(filename);
	}
}
