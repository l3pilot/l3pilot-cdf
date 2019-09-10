package l3q;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import l3q.Data.Objects;
import l3q.Data.SignalQuality;
import l3q.Data.sLaneLinePart;
import l3q.Data.sObjectPart;
import l3q.Indicators.PauseReply;
import hdf.hdf5lib.H5;
import hdf.object.Datatype;
import hdf.object.HObject;
import hdf.object.h5.H5Group;


public class HDF5Read
{

	public HDF5Read() {}

	class L3GeneralFileErrors
	{
		boolean file_not_opening = false;
		boolean egoVehicle_missing = false;
		boolean positioning_missing = false;
		boolean lanes_missing = false;
		boolean objects_missing = false;
		boolean events_missing = false;
		boolean external_data_missing = false;
		boolean metadata_missing = false;
		
		String combined_error_str = "";
	}
		
	public L3GeneralFileErrors test(String fname, String canonpathname)
	{
	
	    Data h5data = new Data();
	    L3GeneralFileErrors errors = new L3GeneralFileErrors();
	    H5CompoundDS ds = null;
	    List list1 = null;
	    String[] list_names = null;
	    Datatype[] list_datatypes = null;
	    long[] dims = null;
	    H5File file = null;
	        
	    try {
	    	file = new H5File(canonpathname, H5File.READ);
	    }
	    catch (Exception e) {
	    	errors.file_not_opening = true;
	    	e.printStackTrace();
	    }
         
	    try {
	    	ds = (H5CompoundDS)file.get("egoVehicle");
	    	list1 = (java.util.List)ds.getData();
	    	list_names = ds.getMemberNames();
	    	list_datatypes = ds.getMemberTypes();

	    	dims = ds.getDims();
	    	System.out.println("egovehicle dims "+dims[0]);
	    	h5data.egoVehicle.rows = (int)dims[0];

	    	//System.out.println("name "+list_names[0]);
	    	int field_nr = 0;
	    	for (String field_name : list_names)
	    	{
	    		for (String signalname : h5data.egoVehicle.signalQualityMap.keySet())
	    		{
	    			if (field_name.equals(signalname))
	    			{
	    				Field target_field = h5data.egoVehicle.getClass().getDeclaredField(signalname);
	    				h5data.assignSignal(h5data.egoVehicle, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
	    				break;
	    			}

	    		}
	    		field_nr++;

	    	}
	    }
	    catch (Exception e) {
	    	errors.egoVehicle_missing = true;
	    	System.out.println("Missing or problematic egoVehicle data table");
	    	//e.printStackTrace();
	    }
           
	    try {
            ds = (H5CompoundDS)file.get("positioning");
            list1 = (java.util.List)ds.getData();
            list_names = ds.getMemberNames();
            list_datatypes = ds.getMemberTypes();
            dims = ds.getDims();
            h5data.positioning.rows = (int)dims[0];
            System.out.println("positioning dims "+dims[0]);
                        
            int field_nr = 0;
            for (String field_name : list_names)
            {
            	for (String signalname : h5data.positioning.signalQualityMap.keySet())
            	{
            		if (field_name.equals(signalname))
            		{
            			Field target_field = h5data.positioning.getClass().getDeclaredField(signalname);
            			h5data.assignSignal(h5data.positioning, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
            			break;
            		}
            			
            	}
            	field_nr++;
            	
            }
	    }
	    catch (Exception e) {
	    	errors.positioning_missing = true;
	    	System.out.println("Missing or problematic Positioning data table");
	    	//e.printStackTrace();
	    }
    	
    	
	    try {
            ds = (H5CompoundDS)file.get("laneLines");
           	list1 = (java.util.List)ds.getData();
            list_names = ds.getMemberNames();
            list_datatypes = ds.getMemberTypes();
            dims = ds.getDims();
            h5data.laneLines.rows = (int)dims[0];
            System.out.println("laneLines dims "+dims[0]);
                        
            int field_nr = 0;
            for (String field_name : list_names)
            {
            	for (String signalname : h5data.laneLines.signalQualityMap.keySet())
            	{
            		if (field_name.equals(signalname))
            		{
            			Field target_field = h5data.laneLines.getClass().getDeclaredField(signalname);
            			if (list_datatypes[field_nr].getDatatypeClass() == Datatype.CLASS_ARRAY)
            			{
            				// not empty
            				if(list1.size() > field_nr) 
            				{
            					boolean supported = true;
            					if (supported)
            					{
            						Object objects[] = (Object[]) list1.get(field_nr);
            						List<Data.sLaneLinePart> sLaneLines = new Vector<sLaneLinePart>();
            						
            						// identify, which part goes where
            						List<String> list_aoc_names = list_datatypes[field_nr].getDatatypeBase().getCompoundMemberNames();
            						int place1 = -1, place2 = -1, place3 = -1, place4 = -1, place5 = -1, place6 = -1;
            						
            						int where = 0;
            						for (String compare: list_aoc_names)
            						{
            							if (compare.equalsIgnoreCase("Curvature")) place1 = where;
            							if (compare.equalsIgnoreCase("CurvatureDer")) place2 = where;
            							if (compare.equalsIgnoreCase("Dy")) place3 = where;
            							if (compare.equalsIgnoreCase("QualityIndex")) place4 = where;
            							if (compare.equalsIgnoreCase("LaneMarkingType")) place5 = where;
            							if (compare.equalsIgnoreCase("YawAngle")) place6 = where;
            							where++;
            						}
            						
            						boolean error_rep = false;
            						for (int i = 0; i < objects.length; i++)
            						{
            							Object parts[] = (Object[]) objects[i];
            							Data.sLaneLinePart one = new Data.sLaneLinePart();
            							
            							if (place1 >= 0 && parts[place1] != null) 
            							{
            								if (parts[place1] instanceof Double) one.Curvature = (double) parts[place1];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-Curvature should be Double"; }
            							}
            							if (place2 >= 0 && parts[place2] != null) 
            							{
            								if (parts[place2] instanceof Double) one.CurvatureDx = (double) parts[place2];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-CurvatureDer should be Double"; }
            							}
            							if (place3 >= 0 && parts[place3] != null) 
            							{
            								if (parts[place3] instanceof Double) one.Dy = (double) parts[place3];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-Dy should be Double"; }
            							}
            							if (place4 >= 0 && parts[place4] != null) 
            								
            							{
            								if (parts[place4] instanceof Integer) one.QualityIndex = (int) parts[place4];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-QualityIndex should be Integer"; }
            							}
            							if (place5 >= 0 && parts[place5] != null) 
            								
            							{
            								if (parts[place5] instanceof Byte) one.MarkingType = (byte) parts[place5];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-LaneMarkingType should be Byte"; }
            							}
            							if (place6 >= 0 && parts[place6] != null) 
            								
            							{
            								if (parts[place6] instanceof Double) one.YawAngle = (double) parts[place6];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + ", sLaneLine-YawAngle should be Double"; }
            							}
            							sLaneLines.add(one);
            						}
            						h5data.assignCompoundLanes(h5data.laneLines, field_name, target_field, sLaneLines, list_datatypes[field_nr]);
            					}
            				}
            			}
            			else h5data.assignSignal(h5data.laneLines, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
            			break;
            		}
            			
            	}
            	field_nr++;
            	
            }
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	errors.lanes_missing = true;
	    	System.out.println("Missing or problematic LaneLines data table");
	    	//e.printStackTrace();
	    }
	    
	    try {
	    	H5Group gr = (H5Group)file.get("/");
	    	List<hdf.object.Attribute> attrib = H5File.getAttribute(gr);
	    	int metadatapos = -1;
	    	for (int i = 0; i < attrib.size(); i++)
	    		//if (attrib.get(i).getName().contentEquals("metaData")) metadatapos = i;
	    		if (attrib.get(i).getName().contentEquals("metaData")) metadatapos = i;
	    	if (metadatapos > -1 && !(attrib.get(metadatapos).getData() instanceof String[]))
	    	{
	    		
	    		hdf.object.Attribute metaData = attrib.get(metadatapos);
	    	  	java.util.List compoundData = (java.util.List)metaData.getData();
	    	  	byte buffer[] = (byte[])compoundData.get(0);
	    	  	// sample has this, in string format: [ { {0.2, 0.8, ikaNOWN, 23-Jan-2019 09:01:39, 2} ,  {42aNOWN, COMPANY_EMPLOYEE} ,  {HORSE, OTHER, 1, 2.37, 1.37, MANUAL, 4.902, 1515, 2.09, 42HER} ,  {NOT_ELIGIBLE, NOT_BASELINE, DEKNOWN, 42, 1548230499, PRIVATE_PARKING, 17, 1548230399, 52074WN} } ]
	    	  		    	  	
	    	  	List<String> membernames = metaData.getDatatype().getCompoundMemberNames();
	    	  	long size = metaData.getDatatype().getDatatypeSize(); // total attribute data size
	    	  	List<Datatype> membertypes = metaData.getDatatype().getCompoundMemberTypes();
	    	  		    	  	
	    	  	int field_nr = 0;
	    	  	int overallpart_nr = 0;
	    	  	int start = 0;
	    	  	for (String mname : membernames)
	    	  	{
	    	  		List<String> lowermembernames = membertypes.get(field_nr).getCompoundMemberNames(); 
	    	  		List<Long> lowermemberoffsets = membertypes.get(field_nr).getCompoundMemberOffsets();
	    	  		int end = (int)membertypes.get(field_nr).getDatatypeSize() + start; // where it ends 
	    	  
	    	  		//System.out.println("end: "+end);
	    	  		Datatype group_datatype = membertypes.get(field_nr);
	    	  		
	    	  		int part_nr = 0;
	    	  		int lower_start = 0;
	    	  		
	    	  		for (Datatype datatype : group_datatype.getCompoundMemberTypes())
	    	  		{
	    	  			String partname = lowermembernames.get(part_nr);
	    	  			lower_start = lowermemberoffsets.get(part_nr).intValue();
	    	  			int lower_end = (int)datatype.getDatatypeSize() + lower_start;
	    	  			
    	  				byte[] content = new byte[(int)datatype.getDatatypeSize()];
    	  				for (int copy = 0; copy < (int)datatype.getDatatypeSize(); copy++)
    	  					content[copy] = buffer[start+lower_start+copy];
    	  				int number = content[0];
    	  				//System.out.println(partname+" start "+(start+lower_start)
	    	  			//		+" end: "+(start+lower_end)+ " content_start "+number); 


	    	  			try {
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_FLOAT && datatype.getDatatypeSize() == 8) // double
	    	  				{
	    	  					double[] result = new double[1];
	    	  					int i = 0;
	    	  					long accum = 0;
	    	  					for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
	    	  						accum |= ( (long)( content[i] & 0xff ) ) << shiftBy;
	    	  						i++;
	    	  					}
	    	  					result[0] = Double.longBitsToDouble(accum);
	    	  					/*
	    	  					long l = Double.doubleToRawLongBits(2.37);
	    	  					byte[] bytes = new byte[8];
	    	  					ByteBuffer write_test2 = ByteBuffer.wrap(bytes).putDouble(2.37);
	    	  					byte[] write_test3 = new byte[] {
	    	  					        (byte)((l >> 56) & 0xff),
	    	  					        (byte)((l >> 48) & 0xff),
	    	  					        (byte)((l >> 40) & 0xff),
	    	  					        (byte)((l >> 32) & 0xff),
	    	  					        (byte)((l >> 24) & 0xff),
	    	  					        (byte)((l >> 16) & 0xff),
	    	  					        (byte)((l >> 8) & 0xff),
	    	  					        (byte)((l >> 0) & 0xff),
	    	  					    };
	    	  					*/
	    	  					try {
	    	  						Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  						h5data.assignSignal(h5data.metaData, partname, target_field, result, datatype, errors);
	    	  					} catch (NoSuchFieldException e) {}
	    	  				}
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 8) // long
	    	  				{
	    	  					long[] result = new long[1];
	    	  					int i = 0;
	    	  					long accum = 0;
	    	  					for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
	    	  						accum |= ( (long)( content[i] & 0xff ) ) << shiftBy;
	    	  						i++;
	    	  					}
	    	  					result[0] = accum;
	    	  					try {
	    	  						Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  						h5data.assignSignal(h5data.metaData, partname, target_field, result, datatype, errors);
	    	  					} catch (NoSuchFieldException e) {}
	    	  				
	    	  				}
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 4) // 32-bit int
	    	  				{
	    	  					int[] result = new int[1];
	    	  					int i = 0;
	    	  					int accum = 0;
	    	  					for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
	    	  						accum |= ( (long)( content[i] & 0xff ) ) << shiftBy;
	    	  						i++;
	    	  					}
	    	  					result[0] = accum;
	    	  					try {
	    	  						Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  						h5data.assignSignal(h5data.metaData, partname, target_field, result, datatype, errors);
	    	  					} catch (NoSuchFieldException e) {}
	    	  				}
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_INTEGER && datatype.getDatatypeSize() == 1) // 8-bit int
	    	  				{
	    	  					try {
	    	  						Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  						h5data.assignSignal(h5data.metaData, partname, target_field, content, datatype, errors);
	    	  					} catch (NoSuchFieldException e) {}
	    	  				}
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_ENUM && datatype.getDatatypeSize() == 1) // enum
	    	  				{
	    	  					try {
	    	  						Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  						h5data.assignSignal(h5data.metaData, partname, target_field, content, datatype, errors);
	    	  					} catch (NoSuchFieldException e) {}
	    	  				}
	    	  				if (datatype.getDatatypeClass() == Datatype.CLASS_STRING) // string
	    	  				{
	    	  					if (partname.equals("VehicleID"))
	    	  					{
	    	  						int tryme = 3;
	    	  					}
	    	  							
	    	  					String[] ss=metaData.dataStr[0].split(","); // should do it better but it's not correct anyways
	    	  					for(int i=0;i<ss.length;i++)
	    	  					{
	    	  					    if (i == overallpart_nr)
	    	  					    {
	    	  					    	//System.out.println(ss[i]);
	    	  					    	String[] result = new String[1];
	    	  					    	result[0] = ss[i];
	    	  					    	try {
	    	  					    		Field target_field = h5data.metaData.getClass().getDeclaredField(partname);
	    	  					    		h5data.assignSignal(h5data.metaData, partname, target_field, result, datatype, errors);
	    	  					    	} catch (NoSuchFieldException e) {}
	    	  					    }
	    	  					}
	    	  				}
	    	  			} catch (Exception e) 
	    	  			{
	    	  				e.printStackTrace();
	    	  			}
	    	  			part_nr++;
	    	  			overallpart_nr++;
	    	  		}
	    	  		start = end;
	    	  		field_nr++;
	    	  	}
	    	}
	    	else
	    	{
	    		errors.metadata_missing = true;
		    	System.out.println("Missing metaData attributes");
	    	}
	  
	    }
	    catch (Exception e) {
	    	errors.metadata_missing = true;
	    	System.out.println("Missing or problematic metaData attributes");
	    	//e.printStackTrace();
	    }
            
	    try {
	    	ds = (l3q.H5CompoundDS)file.get("objects");
            
            list1 = (java.util.List)ds.getData();
            
            list_names = ds.getMemberNames();
            list_datatypes = ds.getMemberTypes();
            dims = ds.getDims();
            h5data.objects.rows = (int)dims[0];
            System.out.println("objects dims "+dims[0]);
                        
            int field_nr = 0;
            for (String field_name : list_names)
            {
            	for (String signalname : h5data.objects.signalQualityMap.keySet())
            	{
            		if (field_name.equals(signalname))
            		{
            			Field target_field = h5data.objects.getClass().getDeclaredField(signalname);
            	
            			if (list_datatypes[field_nr].getDatatypeClass() == Datatype.CLASS_ARRAY)
            			{
            				// not empty
            				if(list1.size() > field_nr) 
            				{
            					boolean supported = true; 
            					if (supported)
            					{
            						Object objects[] = (Object[]) list1.get(field_nr);;
            						List<Data.sObjectPart> sObjects = new Vector<sObjectPart>();
            						
            						// identify, which part goes where
            						List<String> list_aoc_names = list_datatypes[field_nr].getDatatypeBase().getCompoundMemberNames();
            						int place1 = -1, place2 = -1, place3 = -1, place4 = -1, place5 = -1, place6 = -1;
            						int place7 = -1, place8 = -1, place9 = -1, place10 = -1, place11 = -1;
            						
            						int where = 0;
            						for (String compare: list_aoc_names)
            						{
            							if (compare.equalsIgnoreCase("Classification")) place1 = where;
            							if (compare.equalsIgnoreCase("Height")) place2 = where;
            							if (compare.equalsIgnoreCase("ID")) place3 = where;
            							if (compare.equalsIgnoreCase("LatPosition")) place4 = where;
            							if (compare.equalsIgnoreCase("LatVelocity")) place5 = where;
            							if (compare.equalsIgnoreCase("Length")) place6 = where;
            							
            							if (compare.equalsIgnoreCase("LongPosition")) place7 = where;
            							if (compare.equalsIgnoreCase("LongVelocity")) place8 = where;
            							if (compare.equalsIgnoreCase("Width")) place9 = where;
            							if (compare.equalsIgnoreCase("YawAngle")) place10 = where;
            							if (compare.equalsIgnoreCase("YawRate")) place11 = where;
            							where++;
            						}
            						
            						boolean error_rep = false;
            						System.out.println("objects.length: "+objects.length);
            						for (int i = 0; i < objects.length; i++)
            						{
            							Object parts[] = (Object[]) objects[i];

            							Data.sObjectPart one = new Data.sObjectPart();
            							
            							if (place1 >= 0 && parts[place1] != null) 
            							{
            								if (parts[place1] instanceof Byte) one.Classification = (byte) parts[place1];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-Classification should be Byte.<br>"; }
            							}
            							if (place2 >= 0 && parts[place2] != null) 
            							{
            								if (parts[place2] instanceof Double) one.Height = (double) parts[place2];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-Height should be Double.<br>"; }
            							}
            							if (place3 >= 0 && parts[place3] != null) 
            							{
            								if (parts[place3] instanceof Integer) one.ID = (int) parts[place3];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-ID should be Integer.<br>";}
            							}
            							if (place4 >= 0 && parts[place4] != null) 
            							{
            								if (parts[place4] instanceof Double) one.LatPosition = (double) parts[place4];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-LatPosition should be Double.<br>";}
            							}
            							if (place5 >= 0 && parts[place5] != null) 
            							{
            								if (parts[place5] instanceof Double) one.LatVelocity = (double) parts[place5];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-LatVelocity should be Double.<br>";}
            							}
            							if (place6 >= 0 && parts[place6] != null) 
            							{
            								if (parts[place6] instanceof Double) one.Length = (double) parts[place6];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-Length should be Double.<br>";}
            							}
            							if (place7 >= 0 && parts[place7] != null) 
            							{
            								if (parts[place7] instanceof Double) one.LongPosition = (double) parts[place7];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-LongPosition should be Double.<br>";}
            							}
            							if (place8 >= 0 && parts[place8] != null) 
            							{
            								if (parts[place8] instanceof Double) one.LongVelocity = (double) parts[place8];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-LongVelocity should be Double.<br>";}
            							}
            							if (place9 >= 0 && parts[place9] != null) 
            							{
            								if (parts[place9] instanceof Double) one.Width = (double) parts[place9];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-Width should be Double.<br>";}
            							}
            							if (place10 >= 0 && parts[place10] != null) 
            							{
            								if (parts[place10] instanceof Double) one.YawAngle = (double) parts[place10];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-YawAngle should be Double.<br>";}
            							}
            							if (place11 >= 0 && parts[place11] != null) 
            							{
            								if (parts[place11] instanceof Double) one.YawRate = (double) parts[place11];
            								else if(!error_rep) { error_rep = true; errors.combined_error_str =  errors.combined_error_str + "sObject-YawRate should be Double.<br>";}
            							}

            							sObjects.add(one);
            						}
            						h5data.assignCompoundObjects(h5data.objects, field_name, target_field, sObjects, list_datatypes[field_nr]);
            					}
            				}
            				
            			}
            			else h5data.assignSignal(h5data.objects, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
            			break;
            		}
            			
            	}
            	field_nr++;
            	
            }
            
            //Indicators.checkDoubleObjectIDs(h5data.objects);
            
	    }
	    catch (ClassCastException e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	    }
	    catch (Exception e) {
	    	errors.objects_missing = true;
	    	System.out.println("Missing or problematic Objects data table");
	    	e.printStackTrace();
	    }
	    
	    try {
	    	H5Group externalData = (H5Group)file.get("externalData");
	    	List<HObject> list_members = externalData.getMemberList();
	    	
	    	for (int index = 0; index < list_members.size(); index++)
	    	{
	    		ds = (l3q.H5CompoundDS)externalData.getMember(index);
	    		list1 = (java.util.List)ds.getData();
	    		list_names = ds.getMemberNames();
	    		list_datatypes = ds.getMemberTypes();
	    		dims = ds.getDims();
	    		//h5data.externaldata.rows = (int)dims[0];
	    		System.out.println(ds.getName()+" dims "+dims[0]);

	    		int field_nr = 0;
	    		for (String field_name : list_names)
	    		{
	    			if (ds.getName().equalsIgnoreCase("map")) for (String signalname : h5data.map.signalQualityMap.keySet())
	    			{
	    				if (field_name.equals(signalname))
	    				{
	    					Field target_field = h5data.map.getClass().getDeclaredField(signalname);
	    					if (list_datatypes[field_nr].getDatatypeClass() == Datatype.CLASS_ARRAY)
	    					{
	    						// nothing
	    					}
	    					else h5data.assignSignal(h5data.map, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
	    					break;
	    				}

	    			}
	    			if (ds.getName().equalsIgnoreCase("weather")) for (String signalname : h5data.weather.signalQualityMap.keySet())
	    			{
	    				if (field_name.equals(signalname))
	    				{
	    					Field target_field = h5data.weather.getClass().getDeclaredField(signalname);
	    					if (list_datatypes[field_nr].getDatatypeClass() == Datatype.CLASS_ARRAY)
	    					{
	    						// nothing
	    					}
	    					else h5data.assignSignal(h5data.weather, field_name, target_field, list1.get(field_nr), list_datatypes[field_nr], errors);
	    					break;
	    				}

	    			}
	    			
	    			field_nr++;
	    		}
	    	}
	    }
	    catch (Exception e) {
	    	errors.external_data_missing = true;
	    	System.out.println("Missing or problematic externalData table");
	    	//e.printStackTrace();
	    }

	    try {


	    	String filename = "report-template-f.html";

	    	HtmlReader reader = new HtmlReader();

	    	if (reader.load(filename))
	    	{
	    		// check table dimensions
	    		// 
	    		if (h5data.map.SpeedLimit != null && h5data.egoVehicle.FileTime != null 
	    				&& h5data.egoVehicle.FileTime.length == h5data.map.SpeedLimit.length)
	    			reader.replaceAllStrings("%TABLE_DIMENSIONS_CHECK%", "Map matching check OK: egoVehicle row count ("+h5data.egoVehicle.FileTime.length+") matches with externalData-Map table's SpeedLimit row count ("+h5data.map.SpeedLimit.length+").");
	    		else
	    			reader.replaceAllStrings("%TABLE_DIMENSIONS_CHECK%", "Map matching ERROR: egoVehicle row count does not match with externalData-Map table's SpeedLimit row count.");
	    		
	    		for (SignalQuality q : h5data.egoVehicle.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}

	    		for (SignalQuality q : h5data.positioning.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}
	    		
	    		for (SignalQuality q : h5data.weather.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}
	    		
	    		for (SignalQuality q : h5data.map.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}

	    		for (SignalQuality q : h5data.laneLines.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}

	    		for (SignalQuality q : h5data.objects.signalQualityMap.values())
	    		{
	    			if (q.min_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.min)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"min%", q.available ? Double.toString(q.min) : "n/a");
	    			if (q.max_alert_active)
	    			{
	    				reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? "<span style='font-size:11.0pt;"
	    						+ "font-family:\"Calibri\",sans-serif;color:red'>"+Double.toString(q.max)+"</span>" : "n/a");
	    			}
	    			else reader.replaceAllStrings("%"+q.htmlname+"max%", q.available ? Double.toString(q.max) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"avg%", q.available ? Double.toString(q.avg) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"hz%", q.available ? Double.toString(q.hz) : "n/a");
	    			reader.replaceAllStrings("%"+q.htmlname+"maxpause%", q.available ? Double.toString(q.maxpause) : "n/a");
	    		}
	    		
	    		for (SignalQuality q : h5data.metaData.signalQualityMap.values())
	    		{
	    			reader.replaceAllStrings("%"+q.htmlname+"%", q.available ? "yes" : "no");
	    		}

	    		reader.replaceAllStrings("%TOTAL_DISTANCE_GPS%", Integer.toString((int)Indicators.totalDistanceGPS(h5data.positioning))+" m");
	    		reader.replaceAllStrings("%TOTAL_DISTANCE_ODO%", Integer.toString((int)Indicators.totalDistanceOdo(h5data.egoVehicle))+" m");
	    		reader.replaceAllStrings("%NR_TOR%", Integer.toString(Indicators.calculateTORs(h5data.egoVehicle)));
	    		reader.replaceAllStrings("%PERCENT_AD%", Double.toString(Indicators.calculatePercentAD(h5data.egoVehicle)));
	    		PauseReply r = Indicators.estimateLongestPauseInLogging(h5data.egoVehicle);
	    		reader.replaceAllStrings("%LongestPause%", Double.toString(r.maxpause/1000));
	    		reader.replaceAllStrings("%logging_pause_at%", Double.toString(r.time/1000));

	    		//reader.replaceAllStrings("%ID%", Long.toString(1));
	    		reader.replaceAllStrings("%ID%", "Unavailable");
	    		reader.replaceAllStrings("%POTI_MaxTimeDiff%", Long.toString(Indicators.maxTimeDiffBetweenGPSandSystem(h5data.positioning))+" ms");
	    		reader.replaceAllStrings("%POTI_AvgTimeDiff%", Long.toString(Indicators.avgTimeDiffBetweenGPSandSystem(h5data.positioning))+" ms");
	    		
	    		//reader.replaceAllStrings("%NUMBERLOGFILES%", Integer.toString(1));

	    		String errorStr = "Not detected";
	    		int parts = 0;
	    		if (errors.egoVehicle_missing) 
	    		{
	    			if(parts == 0) errorStr = "egoVehicle missing"; 
	    			else errorStr = errorStr + ", egoVehicle missing";
	    			parts++;
	    		}
	    		if (errors.positioning_missing) 
	    		{
	    			if(parts == 0) errorStr = "positioning missing"; 
	    			else errorStr = errorStr + ", positioning missing";
	    			parts++;
	    		}
	    		if (errors.lanes_missing) 
	    		{
	    			if(parts == 0) errorStr = "lanes missing"; 
	    			else errorStr = errorStr + ", lanes missing";
	    			parts++;
	    		}
	    		if (errors.objects_missing) 
	    		{
	    			if(parts == 0) errorStr = "objects missing"; 
	    			else errorStr = errorStr + ", objects missing";
	    			parts++;
	    		}
	    		if (errors.metadata_missing) 
	    		{
	    			if(parts == 0) errorStr = "metadata missing"; 
	    			else errorStr = errorStr + ", metadata missing";
	    			parts++;
	    		}
	    		if (errors.external_data_missing) 
	    		{
	    			if(parts == 0) errorStr = "externalData missing"; 
	    			else errorStr = errorStr + ", externalData missing";
	    			parts++;
	    		}
	    		if (errors.combined_error_str != null)
	    		{
	    			if(parts == 0) errorStr = errors.combined_error_str; 
	    			else errorStr = errorStr + ". "+errors.combined_error_str;
	    			errorStr = errorStr.replaceAll("\\$",".");
	    			parts++;
	    		}
	    		reader.replaceAllStrings("%ERRORS%", errorStr);
	    		
	    		String start = "";
	    		String end = "";
	    		Timestamp timestamp_start = new Timestamp(h5data.egoVehicle.time_start);
	    		if (timestamp_start == null) start = start + "no valid egoVehicle time";
	    		else start = start + timestamp_start.toString();
	    		Timestamp timestamp_end = new Timestamp(h5data.egoVehicle.time_end);
	    		if (timestamp_end == null) end = end + "no valid egoVehicle time";
	    		else end = timestamp_end.toString();
	    		reader.replaceAllStrings("%RPERIOD%", (start+" &#8211; " + end));

	    		reader.replaceAllStrings("%FNAME%", fname);
	    		
	    		// chart plots
	    		Chart.plotSpeed(fname+"-1", h5data);
	    		reader.replaceAllStrings("sample1.png", fname+"-1"+".png");
	    		
	    		Chart.plotBrakes(fname+"-2", h5data);
	    		reader.replaceAllStrings("sample2.png", fname+"-2"+".png");
	    		
	    		Chart.plotCoordinates(fname+"-3", h5data);
	    		reader.replaceAllStrings("sample3.png", fname+"-3"+".png");
	    		
	    		reader.saveToFile(Config.REPORT_DIRECTORY+"/l3q-"+fname+".html");
	    		System.out.println("** Validation report created.");

	    	}

	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return errors;

	}

}