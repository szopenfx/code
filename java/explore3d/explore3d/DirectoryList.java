package explore3d;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import explore3d.progress.Progress;

/**
 * Class that represents the current directory of the browser
 * @author J.J. Molenaar, J. Dooper
 */
public class DirectoryList
{
	/**
	 * This class is a singleton; access instance to get the supposed only instance of this class 
	 */
	public static DirectoryList instance = new DirectoryList();
	
	/**
	 * Current directory
	 */
	private File _currentdir = null;
	
	/**
	 * Subdirectories of current directory
	 */
	private Vector _directories = new Vector();
	
	/**
	 * Files in current directory
	 */
	private Vector _files = new Vector();

	/**
	 * Constructor
	 */
	private DirectoryList()
	{
		populateVectors();
	}
	
	/**
	 * Fill vectors with files and directories
	 */
	private void populateVectors()
	{
		if(_currentdir != null)
			Progress.report(this, "Scanning " + _currentdir);
		else
			Progress.report(this, "Scanning root");
		
		File[] files;
		boolean isdir = false;

		// empty vectors
		_files.clear();
		_directories.clear();
		
		// find files and dirs
		if(_currentdir == null)
		{
			files = File.listRoots();
			isdir = true;
		}
		else
		{
			files = _currentdir.listFiles();
		}
		
		// put files in vectors
		for(int i = 0; i < files.length; i++)
			if(isdir || files[i].isDirectory())
				_directories.add(files[i]);
			else
				this._files.add(files[i]);
		
		//Progress.report(this, "Sorting " + dirCount() + " directories and " + fileCount() + " files");
		sortFileVector(_directories);
		sortFileVector(_files);
	}

	/**
	 * Get number of directories
	 * @return Directory count
	 */
	public int dirCount()
	{
		return _directories.size();
	}
	
	/**
	 * Get number of files
	 * @return File count
	 */
	public int fileCount()
	{
		return _files.size();
	}
	
	/**
	 * Get a certain file
	 * @param i number of file
	 * @return File object for file
	 */
	public File getFile(int i)
	{
		return (File) _files.get(i);
	}
	
	/**
	 * Get a certain directory
	 * @param i number of directory
	 * @return File object for directory
	 */
	public File getDir(int i)
	{
		return (File) _directories.get(i);
	}
	
	/**
	 * Return current directory
	 * @return File object for current directory
	 */
	public File getCurrentDirectory()
	{
		return _currentdir;
	}
	
	/**
	 * Navigate to a subdirectory
	 * @param diridx Index of directory
	 * @throws ArrayIndexOutOfBoundsException if illegal index
	 * @return True if refresh of scene is necessary (always, for this method)
	 */
	public boolean navigate(int diridx)
	{
		_currentdir = (File) _directories.get(diridx);
		populateVectors();
		return true;
	}
	
	/**
	 * Navigate to a subdirectory or open a file
	 * @param file
	 * @return True if refresh of scene is necessary
	 */
	public boolean navigate(File file)
	{
		if(file == null || file.isDirectory())
		{
			_currentdir = file;
			populateVectors();
			return true;
		}
		else
			try
			{
				Runtime.getRuntime().exec(new String[] { 
						"cmd.exe", 
						"/K", 
						file.getAbsolutePath()
				});
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return false;
	}
	
	/**
	 * Navigate to parent directory
	 * @return True if refresh of scene is necessary
	 */
	public boolean navigateToParent()
	{
		_currentdir = _currentdir.getParentFile();
		populateVectors();
		return true;
	}
	
	/**
	 * Print a list of files and directories on stdout
	 */
	public void printList()
	{
		for(int i = 0; i < fileCount(); i++)
			System.out.println("File " + i + ": " + getFile(i).toString());
		for(int i = 0; i < dirCount(); i++)
			System.out.println("Dir " + i + ": " + getDir(i).toString());
	}

	/**
	 * Bubble-sort a list of File objects
	 * @param v Vector to sort
	 */
	private void sortFileVector(Vector v)
	{
		int i = 0;
		while(i < v.size() -1)
		{
			int j = i + 1;
			File a = (File) v.get(i);
			File b = (File) v.get(j);
			if(a.toString().compareTo(b.toString()) > 0)
			{
				v.set(i, b);
				v.set(j, a);
				if(i > 0)
					i--;
			}
			else
				i++;
		}
	}

	/**
	 * Test main method
	 * @param args Ignored arguments
	 */
	public static void main(String args[])
	{
		DirectoryList root = new DirectoryList();
		root.navigate(0);
		root.navigate(7);
		root.navigate(122);
		
		root.sortFileVector(root._directories);
		
		root.printList();
	}
	
}
