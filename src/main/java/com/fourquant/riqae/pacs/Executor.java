package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.CSVDoc.Row;

public class Executor {
  public Executor(final String user, final String server, final int port) {

  }

  public final CSVDoc execute(final String findScuCommand) {
/*
todo implement call to 'native' findscu and get back xml

<?xml version="1.0" encoding="UTF-8"?>
<NativeDicomModel xml:space="preserve">
  <DicomAttribute keyword="SpecificCharacterSet" tag="00080005" vr="CS">
    <Value number="1">ISO_IR 100</Value>
  </DicomAttribute>
  <DicomAttribute keyword="PatientName" tag="00100010" vr="DA">
    <Value number="1">RossiMario</Value>
  </DicomAttribute>
  <DicomAttribute keyword="StudyDate" tag="00080020" vr="DA">
    <Value number="1">20110308</Value>
  </DicomAttribute><DicomAttribute keyword="QueryRetrieveLevel" tag="00080052" vr="CS">
    <Value number="1">STUDY</Value>
  </DicomAttribute>
  <DicomAttribute keyword="RetrieveAETitle" tag="00080054" vr="AE">
    <Value number="1">OSIRIX</Value>
  </DicomAttribute>
  <DicomAttribute keyword="StudyDescription" tag="00081030" vr="LO">
    <Value number="1">Ct Thorax</Value>
  </DicomAttribute>
  <DicomAttribute keyword="PatientID" tag="00100020" vr="LO">
    <Value number="1">USB0003138461</Value>
  </DicomAttribute>
  <DicomAttribute keyword="StudyInstanceUID" tag="0020000D" vr="UI">
    <Value number="1">1.2.840.113619.6.95.31.0.3.4.1.4285.13.23320240</Value>
  </DicomAttribute>
</NativeDicomModel>
<NativeDicomModel xml:space="preserve">
  ...
</NativeDicomModel>

 */
    final CSVDoc CSVDoc = new CSVDoc();
    final Row row1 = new Row();
    row1.setPatientName("John^Doe");
    row1.setPatientId("242323");
    row1.setStudyInstanceUid("adfasdf");

    final Row row2 = new Row();
    row2.setPatientName("John^Doe");
    final Row row3 = new Row();
    row3.setPatientName("John^Doe");
    CSVDoc.add(row1);
    CSVDoc.add(row2);
    CSVDoc.add(row3);
    return CSVDoc;
  }

}
