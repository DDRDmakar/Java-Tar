package tar;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

// tar textexample1.txt textexample2.txt textexample3.txt -out tarred.tr
// tar -u tarred2.tr

class Console {
    @Argument(metaVar = "filename", handler = StringArrayOptionHandler.class, usage = "File name(s)")
    private String[] innames;

    @Option(name = "-u", metaVar = "untar", usage = "Tarred file name")
    private String tarname;

    @Option(name = "-out", metaVar = "outname", usage = "Output file name")
    private String outname;

    //=========================================================================

    void commandReader(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
            parser.parseArgument(args);

            Tar stringProcessor = new Tar();

            if (
                    innames != null     &&
                    innames.length != 0 &&
                    outname != null     &&
                    tarname == null
                ) stringProcessor.filesToStorage(innames, outname);
            else if (
                    innames == null     &&
                    outname == null     &&
                    tarname != null
                ) stringProcessor.storageToFiles(tarname);
            else System.err.println("Error - incorrect arguments.");

    }
}
