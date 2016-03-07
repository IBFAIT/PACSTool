package com.fourquant.riqae.pacs;

public class FindScuCommandExecutorDummy implements ThirdPartyToolExecutor {

  @Override
  public final String execute(final String command) {

    //findscu -c DCMQRSCP@localhost:11112 -m PatientName=Ashlee^Simpson
    final String name = command.substring(51);

    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
          "<NativeDicomModel xml:space=\"preserve\">\n" +
          "<DicomAttribute keyword=\"PatientID\" tag=\"00100020\" vr=\"LO\">" +
          "<Value number=\"1\">USB0003138461</Value>" +
          "</DicomAttribute>\n" +
          "<DicomAttribute keyword=\"PatientName\" tag=\"00100010\" vr=\"DA\">" +
          " <Value number=\"1\">" + name + "</Value>" +
          "</DicomAttribute>\n" +
          "</NativeDicomModel>";
  }

}