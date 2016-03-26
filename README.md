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
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;CT Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;;;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24022366;20140415;Ct Thorax;;;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24022366;20140415;Ct Thorax;;;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24206395;20150316;Ct Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.2.55.3.1544161810.500.1394691251.680;20140314;PET CT Onko Infekt;;;
Verdi Anna Fasula;STLZ00081;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24206395;20150316;Ct Thorax;;;
Neri Mario Fasulo;STLZ00102;1.2.840.113619.6.95.31.0.3.4.1.4285.13.24109562;20140901;Ct Thorax;;;
```
