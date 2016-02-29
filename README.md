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
       
    -output-file, -o
       Output file name
       
    -patient-name, -pn
       Patient name
       
    -patient-names-file, -pnf
       Patient names file
       
    -port, -p
       PACS server port number
       Default: 9090
       
    -server, -s
       PACS server IP address
       Default: localhost

```

# Examples

```java -jar PACS-Tool.jar -patient-name "Donatella Versace" -patient-name "Wolfgang Joop" -server 10.10.10.10 -port 9523```

```java -jar PACS-Tool.jar -patient-names-file in.csv -output-file out.csv```

## Input File Example

```
Patient Name,Patient ID,Exam ID,Series ID,Technique,Type
Kate Moss,,,,,
Ashlee Simpson,,,,,
Donatella Versace,,,,,
```

## Output File Example
```
Patient Name,Patient ID,Exam ID,Series ID,Technique,Type
Kate Moss,1309,336,3624,CT,Thorax
Kate Moss,1309,3795,1472,CT,Thorax
Ashlee Simpson,2496,783,40,CT,Thorax
Donatella Versace,2065,3483,2214,MRI,Breast
Donatella Versace,2065,3679,3172,CT,Breast
Donatella Versace,2065,3035,1004,MRI,Thorax
```
