package com.fourquant.riqae.pacs;

import java.io.FileFilter;
import java.io.IOException;
import java.io.File;

/**
 * Execute a Findscu command and process results.
 * Findscu command will create an XML output file which
 * is read and sent as a String to caller.
 * Created by tomjre on 3/10/16.
 *
 */
public class FindscuExecuter implements ThirdPartyToolExecutor  {

    private final String TEMPFILE_PREFIX="FINDSCU_";
    private final String TEMPFILE_POSTFIX="_TMP";
    private final String FINDSCU= "findscu";
    private final String OUT_DIR_OPTION= "--out-dir";
    private final String OUT_XML= "--xml";
    private final String SPACE= " ";

    private String _tempPath;

    /**
     *   Constructor
      */
   public FindscuExecuter() {
       // Find a safe location for temporary output files
      try {
          File outFile = File.createTempFile(TEMPFILE_PREFIX, TEMPFILE_POSTFIX);
          _tempPath = outFile.getAbsolutePath();
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


        return new String[10];
    }

    /**
     *
      * @return path being used for temporary storage
     */
    public String getTempPath() {
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

    /**
     *  Provide File Filter for expected output files generated from command.
      * @return filter to recognize command output files
     */
   protected static FileFilter getOutputFileFilter() {
       return new FileFilter() {
           @Override
           public boolean accept(File pathname) {
               if (pathname.getName().endsWith(".dcm")
                   && (pathname.getName().startsWith("r")))return true;
               if (pathname.getName().endsWith("TMP")) return true;
               return false;
           }
       };
       
   }
}
