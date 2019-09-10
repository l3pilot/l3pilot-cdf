package l3q;

import java.lang.Math;
import java.sql.Timestamp;

public class ObjectPosition
{
	
	public double _fii = 0, _fii_prev = 0, _fii_start = 0;
	public double _lambda = 0, _lambda_prev = 0, _lambda_start = 0;
	
	Timestamp current_time_stamp = null;
	Timestamp previous_time_stamp = null;
	
	
	public void setPosition(double lat, double lon)
	{
		if (lat == -1 && lon == -1)
			return;

		_fii_prev = _fii; _fii = lat;
		_lambda_prev = _lambda; _lambda = lon;
		if (_lambda_start == 0 && lon != 0) _lambda_start = lon;
		if (_fii_start == 0 && lat != 0) _fii_start = lat;
	}
	
	
	public double getDistanceFromPrevious()
	{
		if (_lambda_prev == 0 || _lambda == 0) return 0;
		return DistanceInMeters(_fii_prev, _lambda_prev, _fii, _lambda); 
	}
	
	public double getDistanceFromStart()
	{
		if (_lambda_start == 0 || _lambda == 0) return 0;
		return DistanceInMeters(_fii_start, _lambda_start, _fii, _lambda);
	}
		
	// Modified from C++ to Java from source of 
	// http://blog.julien.cayzac.name/2008/10/arc-and-distance-between-two-points-on.html
	
	//	/ @brief The usual PI/180 constant 
	private static double DEG_TO_RAD = 0.017453292519943295769236907684886; 
	//	/ @brief Earth's quatratic mean radius for WGS-84 
	private static double EARTH_RADIUS_IN_METERS = 6372797.560856; 
	 
	/** @brief Computes the arc, in radian, between two WGS-84 positions. 
	  * 
	  * The result is equal to <code>Distance(from,to)/EARTH_RADIUS_IN_METERS</code> 
	  *    <code>= 2*asin(sqrt(h(d/EARTH_RADIUS_IN_METERS )))</code> 
	  * 
	  * where:<ul> 
	  *    <li>d is the distance in meters between 'from' and 'to' positions.</li> 
	  *    <li>h is the haversine function: <code>h(x)=sin²(x/2)</code></li> 
	  * </ul> 
	  * 
	  * The haversine formula gives: 
	  *    <code>h(d/R) = h(from.lat-to.lat)+h(from.lon-to.lon)+cos(from.lat)*cos(to.lat)</code> 
	  * 
	  * @sa http://en.wikipedia.org/wiki/Law_of_haversines 
	  */ 
	public static double ArcInRadians(double lat_from, double lon_from, double lat_to, double lon_to) 
	{ 
	    double latitudeArc  = (lat_from - lat_to) * DEG_TO_RAD; 
	    double longitudeArc = (lon_from - lon_to) * DEG_TO_RAD; 
	    double latitudeH = Math.sin(latitudeArc * 0.5); 
	    latitudeH *= latitudeH; 
	    double lontitudeH = Math.sin(longitudeArc * 0.5); 
	    lontitudeH *= lontitudeH; 
	    double tmp = Math.cos(lat_from*DEG_TO_RAD) * Math.cos(lat_to*DEG_TO_RAD); 
	    return 2.0 * Math.asin(Math.sqrt(latitudeH + tmp*lontitudeH)); 
	    
	} 
	 
	/** @brief Computes the distance, in meters, between two WGS-84 positions. 
	  * 
	  * The result is equal to <code>EARTH_RADIUS_IN_METERS*ArcInRadians(from,to)</code> 
	  * 
	  * @sa ArcInRadians 
	  */ 
	public static double DistanceInMeters(double lat_from, double lon_from, double lat_to, double lon_to) 
	{ 
		//double result = EARTH_RADIUS_IN_METERS*ArcInRadians(lat_from, lon_from, lat_to, lon_to);
		//if (result > 0) System.out.println("result: "+result+ " c: " + lat_from+ " "+ lon_from+ " "+ lat_to+ " "+ lon_to);
		return EARTH_RADIUS_IN_METERS*ArcInRadians(lat_from, lon_from, lat_to, lon_to); 
	}
	


}