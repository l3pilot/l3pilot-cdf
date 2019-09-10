package l3q;

import java.util.Iterator;
import java.util.List;

public class Utilities
{
	public static double findMax(double[] vec)
	{
		double max = -Double.MAX_VALUE;
		for (double a : vec)
		{
			if(a > max) max = a;
		}
		if (max == -Double.MAX_VALUE) return -1;
		return max;
	}
	
	public static long findMax(long[] vec)
	{
		long max = Long.MIN_VALUE;
		for (long a : vec)
		{
			if(a > max) max = a;
		}
		if (max == Long.MIN_VALUE) return -1;
		return max;
	}
	
	public static byte findMax(byte[] vec)
	{
		byte max = Byte.MIN_VALUE;
		for (byte a : vec)
		{
			if(a > max) max = a;
		}
		if (max == Byte.MIN_VALUE) return -1;
		return max;
	}
	
	public static int findMax(int[] vec)
	{
		int max = Integer.MIN_VALUE;
		for (int a : vec)
		{
			if(a > max) max = a;
		}
		if (max == Integer.MIN_VALUE) return -1;
		return max;
	}
	
	public static short findMax(short[] vec)
	{
		short max = Short.MIN_VALUE;
		for (short a : vec)
		{
			if(a > max) max = a;
		}
		if (max == Short.MIN_VALUE) return -1;
		return max;
	}
	
	public static double findMin(double[] vec)
	{
		double min = Double.MAX_VALUE;
		for (double a : vec)
		{
			if(a < min) min = a;
		}
		if (min == Double.MAX_VALUE) return -1;
		return min;
	}
	
	public static long findMin(long[] vec)
	{
		long min = Long.MAX_VALUE;
		for (long a : vec)
		{
			if(a < min) min = a;
		}
		if (min == Long.MAX_VALUE) return -1;
		return min;
	}
	
	public static long findMin(short[] vec)
	{
		short min = Short.MAX_VALUE;
		for (short a : vec)
		{
			if(a < min) min = a;
		}
		if (min == Short.MAX_VALUE) return -1;
		return min;
	}
	
	public static byte findMin(byte[] vec)
	{
		byte min = Byte.MAX_VALUE;
		for (byte a : vec)
		{
			if(a < min) min = a;
		}
		if (min == Byte.MAX_VALUE) return -1;
		return min;
	}
	
	public static int findMin(int[] vec)
	{
		int min = Integer.MAX_VALUE;
		for (int a : vec)
		{
			if(a < min) min = a;
		}
		if (min == Integer.MAX_VALUE) return -1;
		return min;
	}
	
	public static double calcAvg(double[] vec)
	{
		double avg = 0;
		int t = 1;
		for (double x : vec) {
			if (Double.isNaN(x)) continue;
			avg += (x - avg) / t;
			++t;
		}
		return avg;
	}
	
	public static double calcAvg(int[] vec)
	{
		double avg = 0;
		int t = 1;
		for (double x : vec) {
			avg += (x - avg) / t;
			++t;
		}
		return avg;
	}
	
	public static double calcAvg(byte[] vec)
	{
		double avg = 0;
		int t = 1;
		for (double x : vec) {
			avg += (x - avg) / t;
			++t;
		}
		return avg;
	}
	
	public static double calcAvg(long[] vec)
	{
		double avg = 0;
		int t = 1;
		for (double x : vec) {
			avg += (x - avg) / t;
			++t;
		}
		return avg;
	}

	public static double detectHz(long[] vec, double time_duration) {

		long size = 0;
		double changes = 0;
		long prev = 0;
		for (long a : vec)
		{
			if (size == 0) prev = a;
			size++;
			if (a != prev) changes++;
			prev = a;
		}
		if (size == 0 || changes == 0) return 0;
		return changes/time_duration;
		
	}
	
	public static double detectHz(int[] vec, double time_duration) {

		long size = 0;
		double changes = 0;
		long prev = 0;
		for (int a : vec)
		{
			if (size == 0) prev = a;
			size++;
			if (a != prev) changes++;
			prev = a;
		}
		if (size == 0 || changes == 0) return 0;
		return changes/time_duration;
		
	}
	
	public static double detectHz(byte[] vec, double time_duration) {

		long size = 0;
		double changes = 0;
		long prev = 0;
		for (byte a : vec)
		{
			if (size == 0) prev = a;
			size++;
			if (a != prev) changes++;
			prev = a;
		}
		if (size == 0 || changes == 0) return 0;
		return changes/time_duration;
		
	}
	
	public static double detectHz(double[] vec, double time_duration) {
		// TODO Auto-generated method stub
		long size = 0;
		double changes = 0;
		double prev = 0;
		for (double a : vec)
		{
			if (size == 0) prev = a;
			size++;
			if (a != prev) changes++;
			prev = a;
		}
		if (size == 0 || changes == 0) return 0;
		return changes/time_duration;
		
	}
	
	public static double detectPause(double[] vec, double time_duration) {
		
		double maxpause = 0;
		long pause = 0;
		double prev = 0;
		long size = 0;
		for (double a : vec)
		{
			if (size == 0) { prev = a; size++; }			
			else
			{
				size++;
				if (a == prev) pause++;
				if (a != prev) { if (pause > maxpause) maxpause = pause; pause = 0; }
				prev = a;
			}
		}
		if (pause > maxpause) maxpause = pause;
		if (maxpause == 0 || size == 0) return 0;
		return (maxpause/size)*time_duration;
	}

	public static double detectPause(int[] vec, double time_duration) {

		double maxpause = 0;
		long pause = 0;
		int prev = 0;
		long size = 0;
		for (int a : vec)
		{
			if (size == 0) { prev = a; size++; }			
			else
			{
				size++;
				if (a == prev) pause++;
				if (a != prev) { if (pause > maxpause) maxpause = pause; pause = 0; }
				prev = a;
			}
		}
		if (pause > maxpause) maxpause = pause;
		if (maxpause == 0 || size == 0) return 0;
		return (maxpause/size)*time_duration;
	}

	public static double detectPause(byte[] vec, double time_duration) {

		double maxpause = 0;
		long pause = 0;
		byte prev = 0;
		long size = 0;
		for (byte a : vec)
		{
			if (size == 0) { prev = a; size++; }			
			else
			{
				size++;
				if (a == prev) pause++;
				if (a != prev) { if (pause > maxpause) maxpause = pause; pause = 0; }
				prev = a;
			}
		}
		if (pause > maxpause) maxpause = pause;
		if (maxpause == 0 || size == 0) return 0;
		return (maxpause/size)*time_duration;
	}

	public static double detectPause(long[] vec, double time_duration) {

		double maxpause = 0;
		long pause = 0;
		long prev = 0;
		long size = 0;
		for (long a : vec)
		{
			if (size == 0) { prev = a; size++; }			
			else
			{
				size++;
				if (a == prev) pause++;
				if (a != prev) { if (pause > maxpause) maxpause = pause; pause = 0; }
				prev = a;
			}
		}
		if (pause > maxpause) maxpause = pause;
		if (maxpause == 0 || size == 0) return 0;
		return (maxpause/size)*time_duration;
	}

	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	public static short[] convertShorts(List<Short> shorts)
	{
	    short[] ret = new short[shorts.size()];
	    Iterator<Short> iterator = shorts.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().shortValue();
	    }
	    return ret;
	}
	
	public static double[] convertDoubles(List<Double> doubles)
	{
	    double[] ret = new double[doubles.size()];
	    Iterator<Double> iterator = doubles.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().doubleValue();
	    }
	    return ret;
	}

}