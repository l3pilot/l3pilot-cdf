package l3q;

import java.util.ArrayList;
import java.util.List;

import l3q.Data.EgoVehicle;
import l3q.Data.Positioning;
import l3q.Data.Objects;

class Indicators 
{
	
	static int calculateTORs(EgoVehicle egoVehicleData)
	{
		int answer = 0;
		byte prev = -1;
		if (egoVehicleData.TOR == null) return 0;
		for (byte b : egoVehicleData.TOR)
		{
			if (b == 1 && prev == 0) answer++;
			prev = b;
		}
		return answer;
	}
	
	static class PauseReply
	{
		double maxpause;
		double time;
	}
	
	static PauseReply estimateLongestPauseInLogging(EgoVehicle egoVehicleData)
	{
		double maxpause = 0;
		double timestamp_of_pause = 0;

		double filetime = 0, longacceleration = 0, vehiclespeed = 0;
		double pausetime = 0;
		
		double derivedFileTimeStart = 0;
		double derivedFileTime = 0; 
		
		try {

			for (int i = 0; i < egoVehicleData.rows; i++)
			{
				if (i == 0) derivedFileTimeStart = egoVehicleData.UTCTime[0]; //init
				derivedFileTime = egoVehicleData.UTCTime[i] - derivedFileTimeStart;
				
				boolean pause_candidate = true;
				if (egoVehicleData.LongAcceleration != null && longacceleration != egoVehicleData.LongAcceleration[i])
				{
					pause_candidate = false;
					longacceleration = egoVehicleData.LongAcceleration[i];
				}
				if (egoVehicleData.VehicleSpeed != null && vehiclespeed != egoVehicleData.VehicleSpeed[i])
				{
					pause_candidate = false;
					vehiclespeed = egoVehicleData.VehicleSpeed[i];
				}
				if (egoVehicleData.VehicleSpeed == null && egoVehicleData.LongAcceleration == null) pause_candidate = false;
				if (Double.isNaN(egoVehicleData.VehicleSpeed[i]) || Double.isNaN(egoVehicleData.LongAcceleration[i])) pause_candidate = false;
				//if ((derivedFileTime - filetime) > maxpause) // from single row
				//{
				//	maxpause = (derivedFileTime - filetime);
				//	timestamp_of_pause = derivedFileTime;
				//}

				if (pause_candidate) // from multiple rows
				{
					pausetime += (derivedFileTime - filetime);
					filetime = derivedFileTime;
				}
				else
				{
					if (pausetime > maxpause) {
						maxpause = pausetime;
						timestamp_of_pause = derivedFileTime;
					}
					pausetime = 0;
					filetime = derivedFileTime;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		PauseReply r = new PauseReply();
		r.maxpause = maxpause;
		r.time = timestamp_of_pause;
		return r;

	}
	public static double calculatePercentAD(EgoVehicle egoVehicle) {
		// TODO Auto-generated method stub
		double percent = 0;
		double count = 0;
		int rows = 0;
		if (egoVehicle.ADFunctionActive == null) return 0;
		for (byte b : egoVehicle.ADFunctionActive)
		{
			if (b == 1) count++;
			rows++;
		}
		percent = (count/rows)*100;
		return percent;
	}
	public static double totalDistanceGPS(Positioning positioning) {

		if (positioning.rows == 0 || positioning.Latitude == null || positioning.Longitude == null) return 0;
		double increment = 0;
		double prev_lat = 0, prev_lon = 0;
		for(int i = 0; i < positioning.rows; i++)
		{
			if (i == 0 || !Double.isFinite(prev_lat)) { prev_lat = positioning.Latitude[i]; prev_lon = positioning.Longitude[i]; }
			else
			{
				increment += ObjectPosition.DistanceInMeters(prev_lat, prev_lon, positioning.Latitude[i], positioning.Longitude[i]);
				prev_lat = positioning.Latitude[i]; prev_lon = positioning.Longitude[i];
			}
		}
		return increment;
	}
	public static double totalDistanceOdo(EgoVehicle egoVehicle) {
	
		double distance = 0;
		if (egoVehicle.Odometer == null) return 0;
		Double maxdist = (double) 0, mindist = Double.MAX_VALUE;
		for (int i = 0; i < egoVehicle.rows; i++)
		{
			Double odo = egoVehicle.Odometer[i];
			if (odo.isNaN()) continue;
			if (odo > maxdist) maxdist = odo;
			if (odo < mindist) mindist = odo;
		}
		distance = maxdist - mindist;
		return distance*1000; // to meters
	}
	
	public static long maxTimeDiffBetweenGPSandSystem(Positioning positioning)
	{
		if (positioning.rows == 0 || positioning.GNSSTime == null || positioning.UTCTime == null) return 0;
		
		long maxdiff = 0;
		
		for(int i = 0; i < positioning.rows; i++)
		{
			long timediff = positioning.GNSSTime[i] - positioning.UTCTime[i];
			if (positioning.NumberOfSatellites[i] > 2)
			{
				if (Math.abs(timediff) > Math.abs(maxdiff))
				{
					maxdiff = timediff;
				}
			}
		}
		return maxdiff;
	}
	
	public static long avgTimeDiffBetweenGPSandSystem(Positioning positioning)
	{
		if (positioning.rows == 0 || positioning.GNSSTime == null || positioning.UTCTime == null) return 0;
		
		long avgdiff = 0;
		int count = 0;
		for(int i = 0; i < positioning.rows; i++)
		{
			long timediff = positioning.GNSSTime[i] - positioning.UTCTime[i];
			if (positioning.NumberOfSatellites[i] > 2)
			{
				avgdiff = avgdiff + timediff;
				count++;
			}
		}
		if (count == 0) return 0;
		else return avgdiff/count;
	}
	
	public static boolean checkDoubleObjectIDs(Objects objects) // does not work since ID read from file doesn't work
	{
		if (objects.sObject == null || objects.sObject.isEmpty() 
				|| objects.NumberOfObjects == null || (objects.NumberOfObjects.length < 1)) return false;
		int i = 0;
		ArrayList<Integer> IDs = new ArrayList<Integer>(64);
		int round = 0;
		for (int num_objects : objects.NumberOfObjects)
		{
			round++;
			IDs.clear();
			//System.out.print("\n");
			//System.out.print("round "+round);
			for (int j = 0; j < num_objects; j++)
			{
				if(objects.sObject.size() < i) {
					System.out.println("Error: NumberOfObjects and provided list of objects do not match in size");
					return true;
				}
				if(IDs.contains(objects.sObject.get(i).ID)) {
					System.out.println("Error: log contains duplicate object IDs for a single timestep at row "+round+", ID "+objects.sObject.get(i).ID);
					return true;
				}
				if (objects.sObject.get(i).ID > 0) IDs.add(objects.sObject.get(i).ID);
				else System.out.print(objects.sObject.get(i).ID+" ");
				i++;
			}
		}
		return false;
		
	}
}