package l3q;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart 
{
	public static void plotSpeed(String fname, Data h5data)
	{
		XYDataset dataset = createSpeedDataset(h5data);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Speed", 
				"s", 
				"km/h", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, // legend
				true, // tooltips
				false // URLs
				); 

		BufferedImage image = chart.createBufferedImage(800, 400);

		try {
			saveToFile(image, fname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void plotBrakes(String fname, Data h5data)
	{
		XYDataset dataset = createBrakesDataset(h5data);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Braking", 
				"s", 
				"value", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, // legend
				true, // tooltips
				false // URLs
				); 

		BufferedImage image = chart.createBufferedImage(800, 400);

		try {
			saveToFile(image, fname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void plotCoordinates(String fname, Data h5data)
	{
		XYDataset dataset = createCoordinatesDataset(h5data);
		
		double maxLong = -Double.MAX_VALUE;
		double minLong = Double.MAX_VALUE;
		double maxLat = -Double.MAX_VALUE;
		double minLat = Double.MAX_VALUE;
		
		int size = 0;
		try{
			
			for(int i = 0; i < h5data.positioning.rows; i++)
			{
				Double lon = h5data.positioning.Longitude[i];
				Double lat = h5data.positioning.Latitude[i];
				if(!(lon.isNaN() || lat.isNaN())) 
				{
					size++;
					if (lon > maxLong) maxLong = lon;
					if (lon < minLong) minLong = lon;
					if (lat > maxLat) maxLat = lat;
					if (lat < minLat) minLat = lat;
				}
			}			
		}
		catch (Exception e)
		{			
		}
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"GPS Coordinates", 
				"Long", 
				"Lat", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, // legend
				true, // tooltips
				false // URLs
				); 

		XYPlot xyPlot = (XYPlot) chart.getXYPlot();
		if (size > 1)
		{
			xyPlot.getDomainAxis().setRange(minLong-0.05, maxLong+0.05);
			xyPlot.getRangeAxis().setRange(minLat-0.05, maxLat+0.05);
		}
		BufferedImage image = chart.createBufferedImage(800, 400);

		try {
			saveToFile(image, fname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static XYDataset createSpeedDataset(Data h5data) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("egoVehicle speed");
		XYSeries series2 = new XYSeries("positioning speed");

		double startTime = 0;
		
		if (h5data.egoVehicle.VehicleSpeed == null || h5data.egoVehicle.VehicleSpeed.length < 1)
			series1.add(1.0, 0.0);
		else
		{
			try{
				startTime = h5data.egoVehicle.UTCTime[0]; 
				for(int i = 0; i < h5data.egoVehicle.rows; i++)
					series1.add((h5data.egoVehicle.UTCTime[i]-startTime)/1000, h5data.egoVehicle.VehicleSpeed[i]*3.6);
			}
			catch (Exception e)
			{
				System.out.println("file error with UTCTime or VehicleSpeed");
				series1.add(1.0, 0.0);
			}
		}

		if (h5data.positioning.GNSSSpeed == null || h5data.positioning.GNSSSpeed.length < 1)
			series2.add(1.0, 0.0);
		else
		{
			try{
				startTime = h5data.positioning.UTCTime[0]; 
				for(int i = 0; i < h5data.positioning.rows; i++)
					series2.add((h5data.positioning.UTCTime[i]-startTime)/1000, h5data.positioning.GNSSSpeed[i]*3.6);
			}
			catch (Exception e)
			{
				System.out.println("file error with UTCTime or GNSSSpeed");
				series2.add(1.0, 0.0);
			}
		}	 
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		return dataset;
	}

	private static XYDataset createBrakesDataset(Data h5data) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("BrakeLight");
		XYSeries series2 = new XYSeries("BrakePedalPos");
		XYSeries series3 = new XYSeries("BrakePressure");
		
		double startTime = 0;
		
		if (h5data.egoVehicle.BrakeLight == null || h5data.egoVehicle.BrakeLight.length < 1)
			series1.add(1.0, 0.0);
		else
		{
			try{
				startTime = h5data.egoVehicle.UTCTime[0]; 
				for(int i = 0; i < h5data.egoVehicle.rows; i++)
					series1.add((h5data.egoVehicle.UTCTime[i]-startTime)/1000, h5data.egoVehicle.BrakeLight[i]);
			}
			catch (Exception e)
			{
				System.out.println("file error with UTCTime or BrakeLight");
				series1.add(1.0, 0.0);
			}
		}

		if (h5data.egoVehicle.BrakePedalPos == null || h5data.egoVehicle.BrakePedalPos.length < 1)
			series2.add(1.0, 0.0);
		else
		{
			try{
				startTime = h5data.egoVehicle.UTCTime[0]; 
				for(int i = 0; i < h5data.egoVehicle.rows; i++)
					series2.add((h5data.egoVehicle.UTCTime[i]-startTime)/1000, h5data.egoVehicle.BrakePedalPos[i]);
			}
			catch (Exception e)
			{
				System.out.println("file error with UTCTime or BrakePedalPos");
				series2.add(1.0, 0.0);
			}
		}

		if (h5data.egoVehicle.BrakePressure == null || h5data.egoVehicle.BrakePressure.length < 1)
			series3.add(1.0, 0.0);
		else
		{
			try{
				startTime = h5data.egoVehicle.UTCTime[0]; 
				for(int i = 0; i < h5data.egoVehicle.rows; i++)
					series3.add((h5data.egoVehicle.UTCTime[i]-startTime)/1000, h5data.egoVehicle.BrakePressure[i]);
			}
			catch (Exception e)
			{
				System.out.println("file error with UTCTime or BrakePressure");
				series3.add(1.0, 0.0);
			}
		}

	
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);

		return dataset;
	}

	private static XYDataset createCoordinatesDataset(Data h5data) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("positioning Lat, Long");
		
		if (h5data.positioning.Latitude == null || h5data.positioning.Latitude.length < 1
				|| h5data.positioning.Longitude == null || h5data.positioning.Longitude.length < 1	)
			series1.add(1.0, 1.0);
		else
		{
			try{
			
				for(int i = 0; i < h5data.positioning.rows; i++)
					series1.add(h5data.positioning.Longitude[i], h5data.positioning.Latitude[i]);
			}
			catch (Exception e)
			{
				System.out.println("file error with Positioning table");
				series1.add(1.0, 1.0);
			}
		}

		dataset.addSeries(series1);
		return dataset;
	}

	public static void saveToFile(BufferedImage img, String fname)
			throws FileNotFoundException, IOException
	{

		File outputfile = new File(Config.REPORT_DIRECTORY+"/"+fname+".png");
		ImageIO.write(img, "png", outputfile);
	}
}