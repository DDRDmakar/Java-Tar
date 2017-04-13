package tar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Основной класс, реализует "склеивание" файлов в один и их разделение.

final class Tar {

    /**
     * Function for easy getting file as one solid string.
     * @param path - text file location
     * @return one string - all content of given file
     * @throws IOException if file reading failed
     */
    String getFullFile(String path) throws IOException {
        return FileUtils.readFileToString(new File(path), "utf-8");
    }

    /**
     * Converts many text files into one, which stores
     * their names and contents.
     * @param filenames - array of file locations
     * @param outname - name of new file
     */
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
            return;
        }

    }

    /**
     * Converts one tar file into many text files.
     * @param path - tar file location
     */
    void storageToFiles(String path) {
        // File contents and names storage
        List<String> splittedLines = new ArrayList<String>();
        // Temporary buffer to get name/value of file char-by-char
        StringBuffer tempTarget = new StringBuffer();

        boolean quote = false; // If current character is in quotes
        boolean escaped = false; // If current character is escaped

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

        // Check, if we can have pairs name-text
        if(splittedLines.size() % 2 == 1) throw new IllegalTarFormatException("File contains incorrect parts \\\"name\\\"-\\\"text\\\"");

        // Write files out
        for(int i = 0; i < splittedLines.size(); i += 2) {
            try { FileUtils.writeStringToFile(new File(splittedLines.get(i)), splittedLines.get(i+1)); }
            catch (IOException e) {
                System.err.println("Error writing out file \"" + splittedLines.get(i) + "\": " + e.getMessage());
            }
        }
    }

    //=================================================================

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;
        if(other == null || !(other instanceof Tar)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[Tar object]";
    }

    @Override
    public int hashCode() { return 23; }
}