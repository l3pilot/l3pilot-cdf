% Close ego types
H5T.close(type_ego_compound);
H5T.close(active_enum);
H5T.close(available_enum);
H5T.close(brake_enum);
H5T.close(indicator_enum);
H5T.close(on_enum);
H5T.close(wiper_enum);
H5T.close(hands_enum);

% Close lane types
H5T.close(type_lane_slane);
H5T.close(type_lane_compound);
H5T.close(type_lane_array);
H5T.close(lane_enum);

% Close object types
H5T.close(type_object_sobject);
H5T.close(type_object_compound);
H5T.close(obj_classification_enum);
H5T.close(type_object_array);

% Close positioning types
H5T.close(type_positioning_compound);

% Close external data types
%H5T.close(type_weather_compound);
H5T.close(precipitation_type_enum);
H5T.close(road_surface_condition_enum);
%H5T.close(weather_enum);
H5T.close(type_map_compound);
H5T.close(road_type_enum);
H5T.close(rules_intersection_enum);
H5T.close(intersection_type_enum);

% Close meta data types
H5T.close(type_meta_general);
H5T.close(type_meta_driver);
H5T.close(type_meta_car);
H5T.close(type_meta_experiment);

% General types
H5T.close(type_str);
