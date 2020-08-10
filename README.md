# L3Pilot Common Data Format (L3Pilot CDF)

The Common Data Format is developed in the [L3Pilot](https://l3pilot.eu) project.  
L3Pilot joins the forces of 13 European car manufacturers to boost the deployment of automated driving in-vehicle functions on European roads.
Together, European automotive industry, suppliers and researchers will pave the way for large-scale field operational tests on public roads creating a harmonized Europe-wide testing environment.

Some background information on the format can be found in our publication for the 26th ESV:

```
@inproceedings{Hiller2019,
  title={{The L3Pilot Common Data Format - Enabling Efficient Automated Driving Data Analysis}},
  author={Hiller, Johannes and Svanberg, Erik and Koskinen, Sami and Bellotti, Francesco and Osman, Nisrine},
  booktitle={26th International Technical Conference on the Enhanced Safety of Vehicles (ESV)},
  year={2019}
}
```

The paper can be downloaded here: [https://www-esv.nhtsa.dot.gov/Proceedings/26/26ESV-000043.pdf](https://www-esv.nhtsa.dot.gov/Proceedings/26/26ESV-000043.pdf)

## Format

The format is based on files using the [HDF5](https://www.hdfgroup.org/solutions/hdf5/) file format.
HDF5 is a hierarchical file format developed by the [HDF Group](https://www.hdfgroup.org/).
It organizes data in a hierarchical fashion using datasets and groups that can contain various predefined signals and metadata.
Additionally, it also supports the generation of custom data types and thereby the storage of almost arbitrary data.

In the L3Pilot CDF, we make use of datasets for mandatory signals that are needed for analysis.
Groups are used for the storing of signals that need to be handled in a more flexible fashion, e.g. key performance indicators that are calculated from mandatory signals.

The datasets that are always contained in the L3Pilot CDF are:

| Name | Description |
|:---|:---|
|`egoVehicle`| Dataset containing all the signals originating directly from the recording vehicle, e.g. ego vehicle speed. |
|`objects`| Dataset containing all the signals concerning dynamic objects in an object list fashion.|
|`laneLines`| Dataset containing information on the lane markings of the road the ego vehicle is currently travelling on. |
|`positioning`| Dataset with GNSS information that can be used for referencing and linking to other data sources.|

In the L3Pilot project, these four datasets are the ones that are always provided by the vehicle owners to the analysis partners.
More details on the signals and their data types can be found in the `format.md`.

Additionally, further data is often added in a group called `externalData`.
This group contains signals from various data sources, such as map or weather providers.
In the L3Pilot project, currently one additional dataset is specified in this group:

| Name | Description |
|:---|:---|
|`map`| Dataset containing various signals from a map provider, e.g. speed limit and number of lanes |

## Sample Implementations

In the `code` folder we provide sample implementations for different languages.
These can be used as basis for your own implementation of the format on your platform.

Currently, samples for the following languages are provided:

| Language | Version | Contributor |
|:---|:---|:---|
| C/C++ | 0.8 | Johannes Hiller |
| Matlab | 0.8 | Johannes Hiller |
| Python | 0.8 | Johannes Hiller |

## Tools

Various tools were developed around the L3Pilot CDF.
Some of them are available in this repository.

### L3Q

L3Q is a tool written by Sami Koskinen in Java.
It takes an .h5 file and checks its compatibility with the CDF.
Details can be found in `tools/L3Q`.

### Contributions

If you have implemented the format for any other programming language or have found a bug in one of the provided implementations, we would be happy to hear about it.
For the case of new implementations, we would also be happy to add it to our repository of existing implementations.
Of course we will mark your contribution, however we would like it to be under the same license as all the other contributions.
Just create a pull request, we will review it and eventually add it.

## License

The L3Pilot Common Data Format and all surrounding source code is published under the MIT license.
Find more details in `LICENSE`.

We would however appreciate a short notice, feedback and maybe just general thoughts if you use the format.

## Contact

In case of questions regarding the format, feel free to contact Johannes Hiller (johannes.hiller@ika.rwth-aachen.de).  

## L3Pilot

This project has received funding from the European Union's Horizon 2020 research and innovation programme under grant agreement No 723051. 
The sole responsibility of this publication lies with the author(s). 
The author(s) would like to thank all partners within L3Pilot for their cooperation and valuable contribution.  

Additional information can be found on the [website](https://l3pilot.eu/).
