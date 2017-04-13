package tar;

import org.kohsuke.args4j.CmdLineException;

/**
 * Created by DDRDmakar on 4/5/17.
 */
public class Main {
    public static void main(String[] args) {
        Console console = new Console();
        try { console.commandReader(args); }
        catch (CmdLineException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            return;
        }
    }
}
