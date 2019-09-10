package l3q;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class HtmlReader {

	private String _html;
	
	public HtmlReader()
	{
	}

	public String getHtml()
	{
		return _html;
	}

	public boolean load(String filename)
	{
		boolean result = false;
		
		File file = new File("./config", filename);

		if (file.exists() && !file.isDirectory())
		{
			BufferedInputStream bis = null;

			try
			{
				bis = new BufferedInputStream(new FileInputStream(file));
				int bytes = bis.available();
				byte[] raw = new byte[bytes];

				bis.read(raw);
				_html = new String(raw, "UTF-8");
			}
			catch (Exception e)
			{
				//_log.warning("HtmlReader error: " + e);
			}
			finally
			{
				try
				{
					bis.close();
					result = true;
				}
				catch (Exception e1)
				{
					//_log.warning("HtmlReader error: " + e1);
				}
			}
		}

		return result;
	}

	public void replaceAllStrings(String pattern, String replacewith)
	{
		_html = _html.replaceAll(pattern, replacewith);
	}
	
	public boolean saveToFile(String filename)
	{
		boolean result = false;

		File file = new File(filename);

		if (!file.isDirectory())
		{
			BufferedOutputStream bos = null;

			try
			{
				bos = new BufferedOutputStream(new FileOutputStream(file));
				bos.write(_html.getBytes());

			}
			catch (Exception e)
			{
				//_log.warning("HtmlReader error in save: " + e);
			}
			finally
			{
				try
				{
					bos.flush();
					bos.close();
					result = true;
				}
				catch (Exception e1)
				{
					//_log.warning("HtmlReader error in save: " + e1);
				}
			}
		}

		return result;
	}

}