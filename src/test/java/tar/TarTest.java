package tar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TarTest {

    private Tar tarobj = new Tar();
    private String absPath = "src/test/resources/";

    @Test
    public void filesToStorage() {
        try {
            String[] paths = {absPath+ "textIn1.txt", absPath+ "textIn2.txt", absPath+ "textIn3.txt"};
            tarobj.filesToStorage(paths, absPath+"tarredOut.tr");
        }
        catch (Exception e) { System.err.println("Exception: " + e.getMessage()); }
    }

    @Test
    public void storageToFiles() {
        try {// TODO double spaces
            tarobj.storageToFiles(absPath+"tarredIn.tr");
        }
        catch (Exception e) { System.err.println("Exception: " + e.getMessage()); }
    }

    @Test
    public void wholeTar() {
        try {
            String temp1 = tarobj.getFullFile(absPath+"textIn1.txt");
            String temp2 = tarobj.getFullFile(absPath+"textIn2.txt");
            String temp3 = tarobj.getFullFile(absPath+"textIn3.txt");
            String temp4 = tarobj.getFullFile(absPath+"textIn4.txt");

            String[] paths = {absPath+"textIn1.txt", absPath+ "textIn2.txt", absPath+"textIn3.txt", absPath+"textIn4.txt"};

            tarobj.filesToStorage(paths, absPath+"tarredOut2.tr");
            tarobj.storageToFiles(absPath+"tarredOut2.tr");

            assertEquals(temp1, tarobj.getFullFile(absPath+"textIn1.txt"));
            assertEquals(temp2, tarobj.getFullFile(absPath+"textIn2.txt"));
            assertEquals(temp3, tarobj.getFullFile(absPath+"textIn3.txt"));
            assertEquals(temp4, tarobj.getFullFile(absPath+"textIn4.txt"));
        }
        catch (Exception e) { System.err.println("Exception: " + e.getMessage()); }
    }
}