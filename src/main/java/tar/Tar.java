package tar;

import org.kohsuke.args4j.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public final class Tar {

    @Option(name = "-u", metaVar = "untar", usage = "Encode files back")
    private boolean untar;

    @Argument(required = true, metaVar = "filename", usage = "File name(s)")
    private List filename = new ArrayList();

    @Option(name = "-out", metaVar = "outopt", usage = "Output file required")
    private boolean outputOpt;

    @Argument(required = true, metaVar = "outname", usage = "Output file name")
    private String outname;

    //=========================================================================

    // TODO should be static
    public void main(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        }
        catch (CmdLineException e) {
            System.err.println("e = " + e.toString());
        }
        // TODO call other functions
    }

    private String getFullFile(String path) {
        try {
            StringBuilder target = new StringBuilder();
            BufferedReader currentFile = new BufferedReader(new FileReader(path));
            String currentLine;
            while ((currentLine = currentFile.readLine()) != null) target.append(currentLine);
            return target.toString();
        }
        // TODO move catch to working code
        catch (java.io.IOException e) {
            System.err.println("Error - IO error: " + e.getMessage());
            return "";
        }
    }

    private void filesToStorage(String[] filenames) {
        String target = ""; // Will contain all files and separators
        for(String e : filenames) {
            String tempFileLine = getFullFile(e);
            tempFileLine = tempFileLine.replace("\\", "\\\\");
            tempFileLine = tempFileLine.replace("\"", "\\\"");
            target.concat("\"" + e + "\" \"" + tempFileLine + "\" ");
        }

        // TODO create output file, containing 'target' string
    }

    private void storageToFiles(String path) {
        List<String> splittedLines = new ArrayList<String>();
        StringBuffer tempTarget = new StringBuffer();
        boolean quote = false;
        boolean escaped = false;

        String storageFileText = getFullFile(path);
        for(int i = 0; i < storageFileText.length(); ++i) {
            char e = storageFileText.charAt(i);

            if(e == '\"' && !escaped) {
                if(tempTarget.length() != 0) splittedLines.add(tempTarget.toString());
                tempTarget = new StringBuffer();
                quote = !quote;
                continue;
            }

            if(e == '\\' && !escaped) { escaped = true; continue; }

            if(e == ' ' && !quote) {
                if(tempTarget.length() != 0) splittedLines.add(tempTarget.toString());
                tempTarget = new StringBuffer();
                continue;
            }

            tempTarget.append(e);
        }
        if(tempTarget.length() != 0) splittedLines.add(tempTarget.toString());

        // TODO write files back
    }

    // TODO equals / hashcode / toString
}