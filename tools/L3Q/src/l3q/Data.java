package l3q;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hdf.object.Datatype;
import l3q.HDF5Read.L3GeneralFileErrors;

public class Data
{
	EgoVehicle egoVehicle = new EgoVehicle();
	Positioning positioning = new Positioning();
	Objects objects = new Objects();
	LaneLines laneLines = new LaneLines();
	Metadata metaData = new Metadata();
	Map map = new Map();
	Weather weather = new Weather();

	// For each logged signal or saved information field, a matching SignalQuality will be created
	class SignalQuality {
		String tablename;
		String signalname;
		Object signalclass;

		boolean available;

		double min; // note there could be bigger long values
		double max; // note there could be bigger long values

		long different_values;
		double avg;
		double hz;
		double maxpause;
		public String htmlname;

		boolean alert_set = false;
		double trigger_OK_low;
		double trigger_OK_high;
		public boolean max_alert_active = false;
		public boolean min_alert_active = false;
	}

	// All data tables have these common fields
	private abstract class L3PilotDataTable
	{
		public int rows;
		public long time_start = 0; // nanoseconds
		public long time_end = 0; // nanoseconds
		public double time_duration = 0; // seconds
		public HashMap<String, SignalQuality> signalQualityMap;
		public String tablename;

	}

	public class EgoVehicle extends L3PilotDataTable {
		public EgoVehicle() {
			tablename = "egoVehicle";
			initSignalQualityMap();
		}

		public long[] UTCTime; 
		public double[] FileTime; 
		public byte[] ABSIntervention; 
		public byte[] ADFunctionActive; 
		public byte[] ADFunctionAvailable;
		public double[] AmbientTemperature;
		public int[] BaselineADASActive;
		public int[] BaselineADASIntervention;
		public byte[] BrakeLight;
		public int[] BrakePedalPos;
		public int[] BrakePressure;
		public byte[] DirectionIndicator;
		public double[] EnergyConsumption;
		//public int[] EngineSpeed;
		public byte[] ESCIntervention;
		public double[] FuelConsumption;
		//public int[] Gear;
		public byte[] HandsOnDetection;
		//public double[] Heading;
		public double[] LatAcceleration;
		public double[] LongAcceleration;
		public double[] Odometer;
		public double[] SteeringAngle;
		//public double[] SteeringAngleRate;
		//public double[] SteeringRackTorque;
		public int[] ThrottlePedalPos;
		public byte[] TOR;
		public double[] TorsionBarTorque;
		//public byte[] VehicleBasedNDRA;
		public double[] VehicleSpeed;
		//public double[] WiperState;
		public double[] YawRate;
		
		public double[] AmbientLightLevel;
		public byte[] FrontFogLightStatus;
		public byte[] FrontWiperStatus;
		public byte[] RearFogLightStatus;
		//public byte[] SeatBeltDriver;
		//public byte[] SeatBeltPassenger;
		//public byte[] SeatBeltRearSeats;
		public double[] SteeringAngleADF;


		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "0";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "1";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ABSIntervention";
			signalq.htmlname = "2";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ADFunctionActive";
			signalq.htmlname = "3";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ADFunctionAvailable";
			signalq.htmlname = "4";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "AmbientTemperature";
			signalq.htmlname = "5";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -10;
			signalq.trigger_OK_high = 40;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "BaselineADASActive";
			signalq.htmlname = "6";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "BaselineADASIntervention";
			signalq.htmlname = "7";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "BrakeLight";
			signalq.htmlname = "8";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "BrakePedalPos";
			signalq.htmlname = "9";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "BrakePressure";
			signalq.htmlname = "10";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "DirectionIndicator";
			signalq.htmlname = "11";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "EnergyConsumption";
			signalq.htmlname = "12";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			/*signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "EngineSpeed";
			signalq.htmlname = "13";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			 */

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ESCIntervention";
			signalq.htmlname = "14";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);


			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FuelConsumption";
			signalq.htmlname = "15";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);

			/*
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Gear";
			signalq.htmlname = "16";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_alert_low = -99;
			signalq.trigger_alert_high = 6;
			signalQualityMap.put(signalq.signalname, signalq);
			 */
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "HandsOnDetection";
			signalq.htmlname = "17";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
/*
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Heading";
			signalq.htmlname = "18";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_alert_low = -1;
			signalq.trigger_alert_high = 7;
			signalQualityMap.put(signalq.signalname, signalq);
*/
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "LatAcceleration";
			signalq.htmlname = "19";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -15;
			signalq.trigger_OK_high = 15;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "LongAcceleration";
			signalq.htmlname = "20";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -15;
			signalq.trigger_OK_high = 15;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Odometer";
			signalq.htmlname = "21";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SteeringAngle";
			signalq.htmlname = "22";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ThrottlePedalPos";
			signalq.htmlname = "25";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TOR";
			signalq.htmlname = "26";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TorsionBarTorque";
			signalq.htmlname = "27";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);


			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "VehicleSpeed";
			signalq.htmlname = "29";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 200;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "YawRate";
			signalq.htmlname = "31";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "AmbientLightLevel";
			signalq.htmlname = "32";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FrontFogLightStatus";
			signalq.htmlname = "33";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FrontWiperStatus";
			signalq.htmlname = "34";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "RearFogLightStatus";
			signalq.htmlname = "35";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			/*signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SeatBeltDriver";
			signalq.htmlname = "36";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_alert_low = -1;
			signalq.trigger_alert_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SeatBeltPassenger";
			signalq.htmlname = "37";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_alert_low = -1;
			signalq.trigger_alert_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SeatBeltRearSeats";
			signalq.htmlname = "38";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_alert_low = -1;
			signalq.trigger_alert_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			*/
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SteeringAngleADF";
			signalq.htmlname = "39";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			

		}

	}

	public class Positioning extends L3PilotDataTable 
	{
		public Positioning() {
			tablename = "positioning";
			initSignalQualityMap();
		}

		public long[] UTCTime; 
		public double[] FileTime; 
		public double[] Altitude; 
		public double[] GNSSSpeed;
		public long[] GNSSTime; // new
		public double[] Heading;
		public double[] Latitude;
		public double[] Longitude;
		public int[] NumberOfSatellites;
		
		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "80";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "81";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Altitude";
			signalq.htmlname = "82";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "GNSSSpeed";
			signalq.htmlname = "84";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 200;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Latitude";
			signalq.htmlname = "86";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Longitude";
			signalq.htmlname = "87";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "NumberOfSatellites";
			signalq.htmlname = "88";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "GNSSTime";
			signalq.htmlname = "90";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Heading";
			signalq.htmlname = "91";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		
		}
	}
	
	public class Map extends L3PilotDataTable 
	{
		public Map() {
			tablename = "map";
			initSignalQualityMap();
		}

		public long[] UTCTime; 
		public double[] FileTime; 
		//public byte[] ConstructionSite; 
		//public double[] Curvature;
		public double[] DistIntersection;
		public int[] NumberOfLanes;
		public byte[] RoadType;
		public byte[] RulesIntersection;
		public int[] SpeedLimit;
		public byte[] TypeIntersection;

		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "e20";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "e21";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);
/*
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ConstructionSite";
			signalq.htmlname = "e22";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Curvature";
			signalq.htmlname = "e23";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
*/
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "DistIntersection";
			signalq.htmlname = "e24";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "NumberOfLanes";
			signalq.htmlname = "e25";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "RoadType";
			signalq.htmlname = "e26";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "RulesIntersection";
			signalq.htmlname = "e27";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "SpeedLimit";
			signalq.htmlname = "e28";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TypeIntersection";
			signalq.htmlname = "e29";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		}

	}
	
	public class Weather extends L3PilotDataTable 
	{
		public Weather() {
			tablename = "weather";
			initSignalQualityMap();
		}

		public long[] UTCTime; 
		public double[] FileTime; 
		public byte[] Precipitation;
		public byte[] PrecipitationType;
		public byte[] RoadSurfaceCondition;
		public double[] Temperature; 
		public byte[] Weather;
		
		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "e80";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "e81";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Precipitation";
			signalq.htmlname = "e82";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "PrecipitationType";
			signalq.htmlname = "e83";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "RoadSurfaceCondition";
			signalq.htmlname = "e84";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Temperature";
			signalq.htmlname = "e85";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Weather";
			signalq.htmlname = "e86";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		}

	}
	
	
	public static class sObjectPart
	{
		
		//sObject = Array of Compound {Classification = 8-bit enum (OTHER=0, CAR=1, TRUCK=2, MOTORCYCLE=3, BICYCLE=4, NOT_APPLICABLE=-1, PEDESTRIAN=5, UNKNOWN=9), 
		//Height = 64-bit floating-point, ID = 32-bit integer, LatPosition = 64-bit floating-point, 
		//LatVelocity = 64-bit floating-point, Length = 64-bit floating-point, LongPosition = 64-bit floating-point, 
		//LongVelocity = 64-bit floating-point, Width = 64-bit floating-point, YawAngle = 64-bit floating-point, 
		//YawRate = 64-bit floating-point} [32]
		
		byte Classification;
		double Height;
		int ID;
		double LatPosition;
		double LatVelocity;
		double Length;
		double LongPosition;
		double LongVelocity;	
		double Width;
		double YawAngle;
		double YawRate;
		
	}

	class Objects extends L3PilotDataTable {
		public Objects() {
			tablename = "objects";
			initSignalQualityMap();
		}

		long[] UTCTime;
		double[] FileTime;
		int[] LeadVehicleID;
		int[] NumberOfObjects;
		
		List<Data.sObjectPart> sObject;

		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "60";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "61";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "LeadVehicleID";
			signalq.htmlname = "62";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "NumberOfObjects";
			signalq.htmlname = "63";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject";
			signalq.htmlname = "xx";
			signalq.signalclass = List.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-Classification";
			signalq.htmlname = "64";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-Height";
			signalq.htmlname = "65";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-ID";
			signalq.htmlname = "66";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9999;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-LatPosition";
			signalq.htmlname = "67";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -100;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-LatVelocity";
			signalq.htmlname = "68";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -100;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-Length";
			signalq.htmlname = "69";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 20;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-LongPosition";
			signalq.htmlname = "70";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -300;
			signalq.trigger_OK_high = 300;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-LongVelocity";
			signalq.htmlname = "71";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -100;
			signalq.trigger_OK_high = 100;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-Width";
			signalq.htmlname = "72";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 5;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-YawAngle";
			signalq.htmlname = "73";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -Math.PI;
			signalq.trigger_OK_high = Math.PI;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sObject-YawRate";
			signalq.htmlname = "74";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -10;
			signalq.trigger_OK_high = 10;
			signalQualityMap.put(signalq.signalname, signalq);
		}
	}

	public static class sLaneLinePart
	{
		double Curvature;
		double CurvatureDx;
		double Dy;
		int QualityIndex;
		byte MarkingType;
		double YawAngle;
		
		
		
		
	}
	
	public class LaneLines extends L3PilotDataTable {
		public LaneLines() {
			tablename = "laneLines";
			initSignalQualityMap();
		}

		long[] UTCTime;
		double[] FileTime;
		double[] EgoLaneWidth;

		List<Data.sLaneLinePart> sLaneLine;
		

		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCTime";
			signalq.htmlname = "40";
			signalq.signalclass = long.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = Double.MAX_VALUE;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FileTime";
			signalq.htmlname = "41";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = 0;
			signalq.trigger_OK_high = 86400;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "EgoLaneWidth";
			signalq.htmlname = "42";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine";
			signalq.htmlname = "xx";
			signalq.signalclass = List.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-Curvature";
			signalq.htmlname = "43";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-CurvatureDx";
			signalq.htmlname = "44";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-Dy";
			signalq.htmlname = "45";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-QualityIndex";
			signalq.htmlname = "46";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-MarkingType";
			signalq.htmlname = "47";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalq.alert_set = true;
			signalq.trigger_OK_low = -1;
			signalq.trigger_OK_high = 9;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "sLaneLine-YawAngle";
			signalq.htmlname = "48";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);


		}

	}
	public class Metadata extends L3PilotDataTable {
		public Metadata() {
			tablename = "metaData";
			initSignalQualityMap();
		}
		/* Originally:
		String[] Partner;
		String[] RecordDate;
		int[] UTCOffset;
		double[] Version;
		int[] ID;
		byte[] Type;
		byte[] Skill;
		byte[] Age;
		byte[] Gender;
		int[] BaselineADASAvailable;
		byte[] DriveType;
		byte[] FuelType;
		double[] PositionFrontBumper;
		double[] PositionRearBumper;
		byte[] Transmission;
		double[] VehicleLength;
		int[] VehicleWeight;
		double[] VehicleWidth;
		byte[] Function;
		byte[] ExperimentType;
		*/
		// updated 22.1.2019		
		double[] ADFVersion;
		double[] FormatVersion;
		String[] Partner;
	    String[] RecordDate;
	    int[] UTCOffset;
	    String[] DriverID;
	    byte[] DriverType;
	    byte[] DriveType;
	    byte[] FuelType;
	    int[] NumberOfOccupants;
	    double[] PositionFrontBumper;
	    double[] PositionRearBumper;
	    byte[] Transmission;
	    double[] VehicleLength;
	    int[] VehicleWeight;
	    double[] VehicleWidth;
	    String[] VehicleID;
	    byte[] AnalysisEligible;
	    byte[] Baseline;
	    String[] Country;
	    int[] TestEndOdo;
	    int[] TestEndTime;
	    byte[] TestSiteType;
	    int[] TestStartOdo;
	    int[] TestStartTime;
	    String[] TripID;

		private void initSignalQualityMap()
		{
			signalQualityMap = new HashMap<String, SignalQuality>(); 

			SignalQuality signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "ADFVersion";
			signalq.htmlname = "m01";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FormatVersion";
			signalq.htmlname = "m02";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Partner";
			signalq.htmlname = "m03";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "RecordDate";
			signalq.htmlname = "m04";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "UTCOffset";
			signalq.htmlname = "m05";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "DriverID";
			signalq.htmlname = "m10";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "DriverType";
			signalq.htmlname = "m11";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "DriveType";
			signalq.htmlname = "m20";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "FuelType";
			signalq.htmlname = "m21";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "NumberOfOccupants";
			signalq.htmlname = "m22";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "PositionFrontBumper";
			signalq.htmlname = "m23";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "PositionRearBumper";
			signalq.htmlname = "m24";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Transmission";
			signalq.htmlname = "m25";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "VehicleLength";
			signalq.htmlname = "m26";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "VehicleWeight";
			signalq.htmlname = "m27";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		   
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "VehicleWidth";
			signalq.htmlname = "m28";
			signalq.signalclass = double.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "VehicleID";
			signalq.htmlname = "m29";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "AnalysisEligible";
			signalq.htmlname = "m40";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Baseline";
			signalq.htmlname = "m41";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
		    
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "Country";
			signalq.htmlname = "m42";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TestEndOdo";
			signalq.htmlname = "m43";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TestEndTime";
			signalq.htmlname = "m44";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TestSiteType";
			signalq.htmlname = "m45";
			signalq.signalclass = byte.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TestStartOdo";
			signalq.htmlname = "m46";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TestStartTime";
			signalq.htmlname = "m47";
			signalq.signalclass = int.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);
			
			signalq = new SignalQuality();
			signalq.tablename = this.tablename;
			signalq.signalname = "TripID";
			signalq.htmlname = "m48";
			signalq.signalclass = String.class;
			signalq.available = false;
			signalQualityMap.put(signalq.signalname, signalq);

		}


	}

	
	// Copies data from log file into program structures and performs basic checks
	public void assignCompoundObjects(L3PilotDataTable table, String signalname, Field signal, List<Data.sObjectPart> sObjects, Datatype datatype) 
	{
		try{
			SignalQuality signalq = table.signalQualityMap.get(signalname); // should always exist after init

			if (datatype.getDatatypeClass() != Datatype.CLASS_ARRAY) // Array of compound
				return;
			
			//System.out.println(signalname+" datatype "+datatype.getDatatypeClass()+" and size "+datatype.getDatatypeSize()+" and name "+datatype.getDatatypeDescription());

			signal.set(table, sObjects);
			
			// now, since this signal is actually an array of compound, we go each signal separately
			ArrayList<Integer> IDlist = new ArrayList<Integer>();
			for (sObjectPart sObjectOne : sObjects)
				IDlist.add(sObjectOne.ID);
			int[] IDs = Utilities.convertIntegers(IDlist);
			SignalQuality signalq1 = table.signalQualityMap.get(signalname+"-"+"ID"); // should always exist after init
 			signalq1.min = (double)Utilities.findMin(IDs);
			signalq1.max = (double)Utilities.findMax(IDs);
			if (signalq1.alert_set && signalq1.max > signalq1.trigger_OK_high) signalq1.max_alert_active = true;
			if (signalq1.alert_set && signalq1.min < signalq1.trigger_OK_low) signalq1.min_alert_active = true;
			signalq1.available = true;
			table.signalQualityMap.put(signalname+"-"+"ID", signalq1); // overwrite
			
			ArrayList<Double> doublelist = new ArrayList<Double>();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.LongPosition);
			double[] doubles = Utilities.convertDoubles(doublelist);
			SignalQuality signalq2 = table.signalQualityMap.get(signalname+"-"+"LongPosition"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"LongPosition", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.LatPosition);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"LatPosition"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"LatPosition", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.Length);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"Length"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Length", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.Width);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"Width"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Width", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.Height);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"Height"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Height", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.LatVelocity);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"LatVelocity"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"LatVelocity", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.LongVelocity);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"LongVelocity"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"LongVelocity", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.YawAngle);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"YawAngle"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"YawAngle", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add(sObjectOne.YawRate);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"YawRate"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"YawRate", signalq2); // overwrite
			
			doublelist.clear();
			for (sObjectPart sObjectOne : sObjects)
				doublelist.add((double) sObjectOne.Classification);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"Classification"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Classification", signalq2); // overwrite
			
			signalq.available = true;
			table.signalQualityMap.put(signalname, signalq); // overwrite
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error: signal "+signalname+" within table "+table);
		}
	}
	
	public void assignCompoundLanes(L3PilotDataTable table, String signalname, Field signal, List<Data.sLaneLinePart> sLaneLines, Datatype datatype) 
	{
		try{
			SignalQuality signalq = table.signalQualityMap.get(signalname); // should always exist after init

			if (datatype.getDatatypeClass() != Datatype.CLASS_ARRAY) // Array of compound
				return;
			
			//System.out.println(signalname+" datatype "+datatype.getDatatypeClass()+" and size "+datatype.getDatatypeSize()+" and name "+datatype.getDatatypeDescription());

			signal.set(table, sLaneLines);
			
			// now, since this signal is actually an array of compound, we go each signal separately
			ArrayList<Double> doublelist = new ArrayList<Double>();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add(sObjectOne.Curvature);
			double[] doubles = Utilities.convertDoubles(doublelist);
			SignalQuality signalq2 = table.signalQualityMap.get(signalname+"-"+"Curvature"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Curvature", signalq2); // overwrite
			
			doublelist.clear();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add(sObjectOne.CurvatureDx);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"CurvatureDx"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"CurvatureDx", signalq2); // overwrite
			
			doublelist.clear();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add(sObjectOne.Dy);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"Dy"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"Dy", signalq2); // overwrite
			
			doublelist.clear();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add((double) sObjectOne.QualityIndex);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"QualityIndex"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"QualityIndex", signalq2); // overwrite
			
			doublelist.clear();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add((double) sObjectOne.MarkingType);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"MarkingType"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"MarkingType", signalq2); // overwrite
			
			doublelist.clear();
			for (sLaneLinePart sObjectOne : sLaneLines)
				doublelist.add(sObjectOne.YawAngle);
			doubles = Utilities.convertDoubles(doublelist);
			signalq2 = table.signalQualityMap.get(signalname+"-"+"YawAngle"); // should always exist after init
 			signalq2.min = (double)Utilities.findMin(doubles);
			signalq2.max = (double)Utilities.findMax(doubles);
			if (signalq2.alert_set && signalq2.max > signalq2.trigger_OK_high) signalq2.max_alert_active = true;
			if (signalq2.alert_set && signalq2.min < signalq2.trigger_OK_low) signalq2.min_alert_active = true;
			signalq2.available = true;
			table.signalQualityMap.put(signalname+"-"+"YawAngle", signalq2); // overwrite
					
			signalq.available = true;
			table.signalQualityMap.put(signalname, signalq); // overwrite
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error: signal "+signalname+" within table "+table);
		}
	}

	// Copies data from log file into program structures and performs basic checks
	public void assignSignal(L3PilotDataTable table, String signalname, Field signal, Object h5object, Datatype datatype, L3GeneralFileErrors errors) 
	{
		try{
			SignalQuality signalq = table.signalQualityMap.get(signalname); // should always exist after init

			//System.out.println(signalname+" datatype "+datatype.getDatatypeClass()+" and size "+datatype.getDatatypeSize()+" and name "+datatype.getDatatypeDescription());
			boolean assigned = false;
			if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 8) // long
			{
				long[] copy = (long[]) h5object;
				if (!(signal.getType().equals(long[].class)))
				{
					if (signal.getType().equals(int[].class))
					{
						errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type (long) than specified (int), but could be checked.<br>";
						System.out.println("Signal "+signalname+" within table "+table+": different type (long) than specified (int), but could be checked.");

						int[] icopy = new int[copy.length];
						for (int i = 0; i < copy.length; i++)
						{
							if (copy[i] > Integer.MAX_VALUE)
								System.out.println("Also, signal value too large for the intended format.");
							icopy[i] = (int)copy[i];
						}
						signal.set(table, icopy);
					}
					else
						signal.set(table, copy);
				}
				else 
					signal.set(table, copy); 

				signalq.min = (double)Utilities.findMin(copy);
				signalq.max = (double)Utilities.findMax(copy);
				if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
				if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;
				if (signalname.equals("UTCTime"))
				{
					table.time_start = Utilities.findMin(copy); // nanoseconds
					table.time_end = Utilities.findMax(copy);
					table.time_duration = ((double)(table.time_end - table.time_start))/(1000);
				}
				signalq.avg = (double)Utilities.calcAvg(copy);
				signalq.hz = (double)Utilities.detectHz(copy, table.time_duration);
				signalq.maxpause = (double)Utilities.detectPause(copy, table.time_duration);
				signalq.available = true;
				assigned = true;
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 4) // 32-bit int
			{
				int[] copy = (int[]) h5object;
				if (!(signal.getType().equals(int[].class)))
				{
					if (signal.getType().equals(double[].class))
					{
						errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type (int) than specified (double), but could be checked.<br>";
						System.out.println("Signal "+signalname+" within table "+table+": different type (int) than specified (double), but could be checked.");

						double[] dcopy = new double[copy.length];
						for (int i = 0; i < copy.length; i++) dcopy[i] = copy[i];
						signal.set(table, dcopy);
					}
					else if (signal.getType().equals(long[].class))
					{
						errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type (int) than specified (long), but could be checked.<br>";
						System.out.println("Signal "+signalname+" within table "+table+": different type (int) than specified (long), but could be checked.");

						double[] lcopy = new double[copy.length];
						for (int i = 0; i < copy.length; i++) lcopy[i] = copy[i];
						signal.set(table, lcopy);
					}
					else
						signal.set(table, copy);
				}
				else 
					signal.set(table, copy);

				signalq.min = (double)Utilities.findMin(copy);
				signalq.max = (double)Utilities.findMax(copy);
				if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
				if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;

				signalq.avg = (double)Utilities.calcAvg(copy);
				signalq.hz = (double)Utilities.detectHz(copy, table.time_duration);
				signalq.maxpause = (double)Utilities.detectPause(copy, table.time_duration);
				signalq.available = true;
				assigned = true;
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 1) // 8-bit int
			{
				byte[] copy = (byte[]) h5object;
				int[] copyAsInt = new int[copy.length];
				for (int i = 0; i < copy.length; i++) copyAsInt[i] = copy[i];
				
				if (signal.getType() == int[].class)
				{
					errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type than specified (should be integer instead of byte), but could be checked.<br>";
					System.out.println("Signal "+signalname+" within table "+table+": different integer type than specified (should be integer instead of byte), but could be checked.");

					signal.set(table, copyAsInt);

					signalq.min = (double)Utilities.findMin(copyAsInt);
					signalq.max = (double)Utilities.findMax(copyAsInt);
					if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
					if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;

					signalq.avg = (double)Utilities.calcAvg(copyAsInt);
					signalq.hz = (double)Utilities.detectHz(copyAsInt, table.time_duration);
					signalq.maxpause = (double)Utilities.detectPause(copyAsInt, table.time_duration);
					signalq.available = true;
					assigned = true;
				}
				else
				{
					signal.set(table, copy);

					signalq.min = (double)Utilities.findMin(copy);
					signalq.max = (double)Utilities.findMax(copy);
					if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
					if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;

					signalq.avg = (double)Utilities.calcAvg(copy);
					signalq.hz = (double)Utilities.detectHz(copy, table.time_duration);
					signalq.maxpause = (double)Utilities.detectPause(copy, table.time_duration);
					signalq.available = true;
					assigned = true;
				}
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_FLOAT && datatype.getDatatypeSize() == 8) // double
			{
				double[] copy = (double[]) h5object;
				
				if (!(signal.getType().equals(double[].class)))
				{
					if (signal.getType().equals(int[].class))
					{
						errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type (double) than specified (int), but could be checked.<br>";
						System.out.println("Signal "+signalname+" within table "+table+": different type (double) than specified (int), but could be checked.");

						int[] icopy = new int[copy.length];
						for (int i = 0; i < copy.length; i++) icopy[i] = (int)copy[i];
						signal.set(table, icopy);
					}
					else
						signal.set(table, copy);
				}
				else 
					signal.set(table, copy);

				signalq.min = (double)Utilities.findMin(copy);
				signalq.max = (double)Utilities.findMax(copy);
				if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
				if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;

				signalq.avg = (double)Utilities.calcAvg(copy);
				signalq.hz = (double)Utilities.detectHz(copy, table.time_duration);
				signalq.maxpause = (double)Utilities.detectPause(copy, table.time_duration);
				signalq.available = true;
				assigned = true;
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_ENUM && datatype.getDatatypeSize() == 1) // enum
			{
				byte[] copy = (byte[]) h5object;
				signal.set(table, copy);

				signalq.min = (double)Utilities.findMin(copy);
				signalq.max = (double)Utilities.findMax(copy);
				if (signalq.alert_set && signalq.max > signalq.trigger_OK_high) signalq.max_alert_active = true;
				if (signalq.alert_set && signalq.min < signalq.trigger_OK_low) signalq.min_alert_active = true;

				signalq.avg = (double)Utilities.calcAvg(copy);
				signalq.hz = (double)Utilities.detectHz(copy, table.time_duration);
				signalq.maxpause = (double)Utilities.detectPause(copy, table.time_duration);
				signalq.available = true;
				assigned = true;
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_ARRAY) // Array of compound
			{
				System.out.println("error: should not come here, are to be processed earlier.");
			}
			if (datatype.getDatatypeClass() == Datatype.CLASS_STRING) // Array of Strings
			{
				String[] copy = (String[]) h5object;
				signal.set(table, copy);
				
				for(int j = 0; j < copy.length; j++)
				{
					if (copy[j].length() > 0)
					{
						signalq.available = true;	
					}
				}
				assigned = true;
			}	
			if (!assigned) 
			{ 
				errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": different type than specified.<br>";
				System.out.println("Signal "+signalname+" within table "+table+": different type than specified.");
			}
			table.signalQualityMap.put(signalname, signalq); // overwrite
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			errors.combined_error_str = errors.combined_error_str + "Signal "+signalname+" within table "+table+": "+e.getMessage()+".<br>";
			System.out.println("Logfile error: signal "+signalname+" within table "+table+": "+e.getMessage());
		}
	}

}