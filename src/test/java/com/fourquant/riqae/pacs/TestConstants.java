package com.fourquant.riqae.pacs;

import com.fourquant.riqae.pacs.csv.CSVDataRow;

public class TestConstants {

  public final static String nameAnna = "Verdi Anna Fasula";
  public final static String nameMario = "Neri Mario Fasulo";

  public final static String nameDonatella = "Donatella Versace";
  public final static String nameAshlee = "Ashlee Simpson";
  public final static String nameKate = "Kate Moss";

  public final static String userName = "OSIRIX";
  public final static String server = "localhost";
  public final static String port = "11112";
  public final static String bind = "Osirix";

  public static CSVDataRow getCSVDataRow(final String name) {

    final CSVDataRow csvDataRow = new CSVDataRow();

    switch (name) {
      case nameAnna:
        csvDataRow.setPatientName(nameAnna);
        csvDataRow.setPatientID("STLZ00081");
        csvDataRow.setStudyInstanceUID("1.2.840.113619.6.95.31.0.3.4.1.4285.13.24022366;20140415");
        csvDataRow.setStudyDescription("Ct Thorax");
        csvDataRow.setSeriesInstanceUID("1.3.12.2.1107.5.1.4.65362.30000014041503061104900037202");
        csvDataRow.setSeriesDescription("Thorax 1.0 I50f 3 LCAD");
        break;

      case nameMario:
        csvDataRow.setPatientName(nameMario);
        csvDataRow.setPatientID("STLZ00102");
        csvDataRow.setStudyInstanceUID("1.2.840.113619.2.55.3.1544161810.500.1394691251.680;20140314");
        csvDataRow.setStudyDescription("PET CT Onko Infekt");
        csvDataRow.setSeriesInstanceUID("1.2.840.113619.2.131.1544161810.1394784249.811101");
        csvDataRow.setSeriesDescription("WB_3D");
        break;
    }

    return csvDataRow;
  }
}