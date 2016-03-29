# PACSTool

Helper tool for querying a PACS server:
* Takes patient names as input (either as command line parameters or from an input file)
* resolves ids and other patient specificas by querying a PACS sever
* returns these values either on standard output or written into an output file

# Usages

```

java -jar PACS-Tool.jar -h

Usage: PACSTool [options]
  Options:
    -h, --help
       This usage summary

    -c, --command
       RESOLVE_PATIENT_IDS, RESOLVE_STUDY_INSTANCE_UIDS or RESOLVE_SERIES_INSTANCE_UIDS

    -output-file, -o
       Output file name
       
    -patient-name, -pn
       Patient name
       
    -input-file, -i
       e.g. Patient names file
       
    -port, -p
       PACS server port number
       Default: 9090
       
    -server, -s
       PACS server IP address
       Default: localhost

    -bind, -b
      <aet[@ip][:port]>
```

# Examples

```java -jar PACS-Tool.jar -patient-name Verdi -patient-name Neri -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS```

```java -jar PACS-Tool.jar -input-file test-classes/osirixNames.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS```

```java -jar PACS-Tool.jar -input-file test-classes/osirixNames.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_PATIENT_IDS -o /tmp/out.csv```

```java -jar PACS-Tool.jar -input-file test-classes/osirixNamesAndIds.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_STUDY_INSTANCE_UIDS -o /tmp/out.csv```

```java -jar PACS-Tool.jar -input-file test-classes/osirixNamesIdsAndStudyInstanceUIDs.csv -s localhost -u OSIRIX -p 11112 -c RESOLVE_SERIES_INSTANCE_UIDS -o /tmp/out.csv```


## Input File Example

```
Patient Name;Patient ID;Study Instance UID;Study Date;Study Description;Series Instance UID;Series Description;Result
Verdi Anna Fasula;;;;;;;
Neri Mario Fasulo;;;;;;;
```

## Output File Example
```
Patient Name;Patient ID;Study Instance UID;Study Date;Study Description;Series Instance UID;Series Description;Result
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014090113251401600000625,1.3.12.2.1107.5.1.4.65362.30000014090113251401600000701;Localizers;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014090113255231000012241;Thorax Nativ 5.0 sagittal Weichteil;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014090113255231000011779;Thorax Nativ 1.0 axial I50f LCAD;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;CT Thorax;1.3.12.2.1107.5.1.4.65362.30000014090113255231000011270;Thorax Nativ 1.0 I26f 3;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014090113255231000011633;Thorax Nativ 5.0 I26f 3;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24022366;20140415;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014041503061104900037202;Thorax 1.0 I50f 3 LCAD;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24022366;20140415;Ct Thorax;1.3.12.2.1107.5.1.4.65362.30000014041503061104900036910;Thorax 1.0 I26f 3;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24206395;20150316;Ct Thorax;1.3.12.2.1107.5.1.4.73395.30000015031606155975500010450;Thorax 1.0 I26f 3;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.2.55.3.1544161810.500.1394691251.680;20140314;PET CT Onko Infekt;1.2.840.113619.2.131.1544161810.1394784249.811101;WB_3D;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24206395;20150316;Ct Thorax;1.3.12.2.1107.5.1.4.73395.30000015031600000435500000958;Localizers;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;1.2.276.0.7238010.5.1.3.0.13456.1458130641.73;OsiriX Report SR;
```

## Installation

* Install OsirixLite
* Import test data set for ```Neri Mario Fasulo``` and ```Verdi Anna Fasula``` into Osirix
* Set username ```OSIRIX```
* mvn package

## Tests

Class ```com.fourquant.riqae.pacs.LocalOsirixTest``` simulates workflow (patient names to images) and usage of ```dcm2xml``` and ```dcm2jpg```