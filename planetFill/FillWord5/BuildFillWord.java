package FillWord5 ;

import java.io.* ;
import java.util.* ;
import java.util.zip.* ;

/**
 *  BuildFillWord is a utility class that students can use to build
 *  Assigments 4 & 5 on their home machines. CSC 243, Spring 2013.
 *  Java Programming.
 *  @author Dr. Parson
**/
public class BuildFillWord {
    /**
     *  main executes the command line passed via args[] and
     *  prints logging messages to file dataminelog.txt in append mode.
    **/
    public static void main(String [] args) {
        int exitstatus = 0 ;
        if (args.length < 1) {
            System.err.println(
                "USAGE: java FillWord5.BuildFillWord CMD-LINE-STRINGS");
            System.exit(1);
        }
        exitstatus = logSessionData(args, true);
        System.exit(exitstatus);
    }
    // Package-level static helper for logging data for later mining.
    static int logSessionData(String [] args, boolean useArgs) {
        int exitstatus = 0 ;
        PrintWriter log = null ;
        // ZipOutputStream zos = null ;
        // ZipEntry ze = null ;
        try {
            Date date = new Date();
            /** TODO DO THIS LATER
            zos = new ZipOutputStream(new FileOutputStream(
                new File("data_do_not_lose_this_file_by_unzipping.zip"),
                    true)); // append to the output file
            ze  = new ZipEntry("" + System.currentTimeMillis()
                + ".txt");
            zos.putNextEntry(ze);
            zos.flush();
            log = new PrintWriter(zos, false);  // no auto-flush
            **/
            log = new PrintWriter(new FileOutputStream(
                new File("data_do_not_lose_this_file.txt"), true), false);
            log.println("~~~~~~~~");
            log.println("LOG TIME: " + date.toString());
            log.println("LOG MS: " + System.currentTimeMillis());
            if (useArgs) {
                log.print("LOG BUILD:");
                for (int i = 0 ; i < args.length ; i++) {
                    log.print(" " + args[i]);
                }
                log.println();
                Process p = Runtime.getRuntime().exec(args);
                Scanner stdout = new Scanner(new BufferedInputStream(
                    p.getInputStream()));
                Scanner stderr = new Scanner(new BufferedInputStream(
                    p.getErrorStream()));
                while (stderr.hasNextLine()) {
                    String line = stderr.nextLine();
                    System.err.println(line);
                    log.println("STDERR: " + line);
                }
                while (stdout.hasNextLine()) {
                    String line = stdout.nextLine();
                    System.out.println(line);
                    log.println("STDOUT: " + line);
                }
                exitstatus = p.waitFor();
                if (exitstatus != 0) {
                    log.println("WARNING, EXIT STATUS = " + exitstatus + ".");
                    System.err.println("WARNING, EXIT STATUS = "
                        + exitstatus + ".");
                }
            } else {
                log.println("LOG TEST:");
            }
            File pwd = new File(".");
            String [] sourceFiles = pwd.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    String [] nameparts = name.trim().split("\\.");
                    if (nameparts != null && nameparts.length > 0
                            && "java".equals(nameparts[nameparts.length-1])) {
                        return true ;
                    }
                    return false ;
                }
            });
            for (int ii = 0 ; sourceFiles != null && ii < sourceFiles.length
                    ; ii++) {
                String name = sourceFiles[ii];
                File jfile = new File(name);
                if (jfile.isFile()) {
                    log.println("FILE: " + name + ": "
                        + jfile.length());
                    /** TODO DO THIS LATER
                    Scanner copyin = null ;
                    try {
                        copyin = new Scanner(jfile);
                        while (copyin.hasNextLine()) {
                            log.println(copyin.nextLine());
                        }
                    } catch (Exception xxx) {
                        System.err.println(
                            "Data mining warning, student may ignore: "
                                + xxx.getClass().getName() + ": "
                                + xxx.getMessage());
                    } finally {
                        if (copyin != null) {
                            copyin.close();
                        }
                    }
                    **/
                }
            }
        } catch (Exception xxx) {
            String msg = "ERROR, execution fails with exception type: "
                + xxx.getClass().getName() + ": " + xxx.getMessage();
            xxx.printStackTrace();
            System.err.println(msg);
            if (log != null) {
                xxx.printStackTrace(log);
                log.println(msg);
            }
        } finally {
            if (log != null) {
                // TODO try {
                    log.flush();
                    /** TODO
                    zos.flush();
                    zos.closeEntry();
                    **/
                    log.close();
                // TODO } catch (IOException iox) {
                    // TODO System.err.println(
                        // TODO "Warning, data mining output error: "
                        // TODO + iox.getMessage());
                // TODO }
            }
        }
        return(exitstatus);
    }
}
