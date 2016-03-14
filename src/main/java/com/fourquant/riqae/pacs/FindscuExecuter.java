package com.fourquant.riqae.pacs;

import java.io.*;

/**
 * Execute a Findscu command and process results.
 * This class is specific to the dcm4che findscu function
 * accounting for the specific io of this command.
 *
 * Findscu command will create an XML output file which
 * is read and sent as a String to caller.
 * Created by tomjre on 3/10/16.
 *
 */
public class FindscuExecuter implements ThirdPartyToolExecutor  {

    private static final String TEMPFILE_PREFIX="FINDSCU_";
    private static final String TEMPFILE_POSTFIX="_TMP";
    private static final String FINDSCU= "findscu";
    private static final String OUT_DIR_OPTION= "--out-dir";
    private static final String OUT_XML= "--xml";
    private static final String SPACE= " ";
   private static final String[] EMPTY_RESULTS= {""};
   protected static String DO_NOT_EXECUTE= "DO_NOT_EXECUTE" ;  // used for testing
   protected static String DO_NOT_EXECUTE_DIR= "/tmp/" ;  // used for testing




    private String _tempPath;
    /**
     *   Constructor
      */
   public FindscuExecuter() {
       // Find a safe location for temporary output files
      try {
          File outFile = File.createTempFile(TEMPFILE_PREFIX, TEMPFILE_POSTFIX);
          _tempPath = outFile.getParent()+"/";
          outFile.delete(); // cleanup
          cleanup(); // make sure directory does not contain any old output files
      } catch (IOException e) {
          System.out.print(e);
         _tempPath= null;
      }

   }

    /**
     *  Constructor with temporary path setting.  Useful primarily for testing class.
     *
      * @param tempPath path to use for temporary storage of output files
     */
    public FindscuExecuter(String tempPath) {
        // Find a safe location for temporary output files
        _tempPath = tempPath;
     }

  /**
   * Provide File Filter for expected output files generated from command.
   *
   * @return filter to recognize command output files
   */
  protected static FileFilter getOutputFileFilter() {
    return new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(".dcm");
      }
    };

  }

    /**
     *  reset resources
     *  delete temporary files
      */
    protected void cleanup() {
         File[] outFileList= (new File(_tempPath).listFiles(getOutputFileFilter()));
        for (File file : outFileList) file.delete();
    }

    /**
     * Executes dcm4che findscu command or similar which produces a specific output file of xml data.
     * Returns the xml code of findscu
     *
     * @param command The actual command
     *                (e.g. findscu -c DCMQRSCP@localhost:11112 -r PatientID -m PatientName=John^Doe)
     */
    public String[] execute(String command) throws IOException {
        String actualCommand= getFinalCommand(command);
        if (actualCommand.indexOf(DO_NOT_EXECUTE)<0) {
            Process p = Runtime.getRuntime().exec(actualCommand);
        }
       else  {
           _tempPath= DO_NOT_EXECUTE_DIR; // look in default directory for MOC files created for testing.
        }
        String[] results= readOutputFiles();
       cleanup();
       return results;
    }

    /**
     *  Reads output files generated by FINDSCU command and returns content in String[]
     *
      * @return output file content - one File to one String
     */
   protected String[] readOutputFiles(){
      try {
          File[] outFileList = (new File(_tempPath).listFiles(getOutputFileFilter()));
          if (outFileList.length==0) return  EMPTY_RESULTS;
          String[] results = new String[outFileList.length];
          int i = 0;
          for (File file : outFileList) {
              results[i++] = readFile(file);
          }
          return results;
      } catch (IOException e) {
         return EMPTY_RESULTS;
      }
   }

    /**
     *  load file into a String
     * @param file file to process
     * @return String with contents of file
     * @throws IOException
     */
    private String readFile(File file) throws IOException {
        BufferedReader buffy = new BufferedReader(new FileReader(file));
        try {
            StringBuilder result = new StringBuilder();
            String line = buffy.readLine();

            while (line != null) {
                result.append(line);
                // result.append("\n");
                line = buffy.readLine();
            }
            return result.toString();
        } finally {
            buffy.close();
        }
    }

    /**
     *
      * @return path being used for temporary storage
     */
    protected String getTempPath() {
       return _tempPath;
    }

    /**
     *
     *
     * @param command original command line
     * @return  command line with extra output options
     */
    public String getFinalCommand(String command) {
        StringBuffer buffy= new StringBuffer(command);
        String outExtraOptions= SPACE+OUT_DIR_OPTION+SPACE+_tempPath+SPACE+OUT_XML+SPACE;

        // Insert Extra Finddsc options in command
        // (dcm4che treats first found command line option as override of matching options)
        int posOutDir= command.indexOf(OUT_DIR_OPTION);
        if (posOutDir<0) {
            buffy.append(outExtraOptions);
        }
       else { // first option in command line is the one obeyed
            buffy.insert(posOutDir, outExtraOptions);
        }
       return buffy.toString();
    }

   public String toString() {
     return this.getClass().toString()+":"+this.getTempPath();
   }
}
