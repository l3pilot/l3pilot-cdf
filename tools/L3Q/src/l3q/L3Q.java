// L3Q is a software that assists pilot sites in validating collected log files in EU project L3Pilot.
//
// Usage: 1. Place log files in ./logs folder or set the directory in ./config/l3q.properties file.
//        2. Run l3q.bat
//        3. Reports are stored in .html format in foder ./reports (or as set in the l3q.properties)
//
// Contact information: Sami Koskinen, VTT, sami.koskinen@vtt.fi
//

package l3q;

import java.io.File;
import java.util.Arrays;

public class L3Q
{
	//static String fname   = "l3pilot_cdf_filled_example.h5";
	
	public static void main(String[] args) throws Exception
	{
		
		//--- Initialize ---
		Config.load();
		
		System.out.println("-- Searching for L3Pilot's *.h5 log files");
		File parentdir = new File(Config.INPUT_DIRECTORY);
		processDirectory(parentdir);
		System.out.println("-- Quality check complete!");
		
	}
	
	public static void processDirectory(File parentdir) 
	{
		File[] filesAndDirs = parentdir.listFiles();

		if (filesAndDirs == null || filesAndDirs.length == 0) 
		{
			// Either dir does not exist or is not a directory
			System.out.println("-- Quality check cannot be performed on nonexisting/empty directory");
		} 
		else 
		{
			Arrays.sort(filesAndDirs); // alphabetical order
			
	
			int files_processed = 0;

			for (int k=0; k<filesAndDirs.length; k++) 
			{
				if (filesAndDirs[k].isDirectory())
				{
					System.out.println("-- Recursive processing in subdirectory "+filesAndDirs[k].getName());
					processDirectory(filesAndDirs[k]);
				}
				else
				{
					try 
					{
						if (filesAndDirs[k].getName().endsWith(".h5"))
						{
							System.out.println("** Quality check started for "+filesAndDirs[k].getName());
							HDF5Read h5tester = new HDF5Read();
							h5tester.test(filesAndDirs[k].getName(), filesAndDirs[k].getCanonicalPath());
							
							files_processed++;
							if (files_processed % 10 == 0) System.out.println("files processed: "+k);
						}
					}
					catch (Exception e) 
					{
						e.printStackTrace();
						break;
					}
				}

			}
		}
	}
}
	