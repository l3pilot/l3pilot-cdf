package l3q;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Config
{
	public static final String  CONFIG_FILE = "./config/l3q.properties";
	
    public static String	    INPUT_DIRECTORY;
    public static String	    REPORT_DIRECTORY;
        
    /**
     * This class initializes all global configuration variables.<br>
     * If a key doesn't appear in property file(s), a default value is set.
     */
    public static void load()
    {
    	InputStream is = null;
	        
    	try {
    		Properties serverSettings	= new Properties();
    		is                          = new FileInputStream(new File(CONFIG_FILE));
    		serverSettings.load(is);
    		
    		INPUT_DIRECTORY        = serverSettings.getProperty("InputDirectory", ".//logs");
    		REPORT_DIRECTORY        = serverSettings.getProperty("ReportDirectory", ".//reports");
    		
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		throw new Error("Failed to load configuration file: "+CONFIG_FILE);
    	}
    }

    private Config() {}
 
}