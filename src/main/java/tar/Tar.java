package tar;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class Tar {

    private String getFullFile(String path) throws IOException {
            StringBuilder target = new StringBuilder();
            BufferedReader currentFile = new BufferedReader(new FileReader(path));
            String currentLine;
            while ((currentLine = currentFile.readLine()) != null) target.append(currentLine + "\n");
            if (target.indexOf("\n") != -1)target.deleteCharAt(target.lastIndexOf("\n"));
            return target.toString();
    }

    void filesToStorage(String[] filenames, String outname) {
        StringBuilder target = new StringBuilder(); // Will contain all files and separators

        try {
            for(String e : filenames) {
                String tempFileLine = getFullFile(e);
                tempFileLine = tempFileLine.replace("\\", "\\\\");
                tempFileLine = tempFileLine.replace("\"", "\\\"");
                target.append("\"" + e + "\" \"" + tempFileLine + "\" \n");
            }

            FileUtils.writeStringToFile(new File(outname), target.toString());
        }
        catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
        }

    }

    void storageToFiles(String path) {
        List<String> splittedLines = new ArrayList<String>();
        StringBuffer tempTarget = new StringBuffer();
        boolean quote = false;
        boolean escaped = false;

        try {
            String storageFileText = getFullFile(path);

            for (int i = 0; i < storageFileText.length(); ++i) {
                char e = storageFileText.charAt(i);

                if (e == '\"' && !escaped) {
                    if (quote) {
                        splittedLines.add(tempTarget.toString());
                        tempTarget = new StringBuffer();
                    }
                    quote = !quote;
                    continue;
                }

                if (e == '\\' && !escaped) { escaped = true; continue; }
                else escaped = false;

                if (quote) tempTarget.append(e);
            }

        } catch (IOException e) { System.err.println("IO error: " + e.getMessage()); }

        if(splittedLines.size() % 2 == 1) throw new IllegalTarFormatException("File contains incorrect parts \\\"name\\\"-\\\"text\\\"");

        for(int i = 0; i < splittedLines.size(); i += 2) {
            try { FileUtils.writeStringToFile(new File(splittedLines.get(i)), splittedLines.get(i+1)); }
            catch (IOException e) {
                System.err.println("Error writing out file \"" + splittedLines.get(i) + "\": " + e.getMessage());
            }
        }
    }

    // TODO equals / hashcode / toString
}