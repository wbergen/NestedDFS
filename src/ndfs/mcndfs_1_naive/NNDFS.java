package ndfs.mcndfs_1_naive;

import java.io.File;
import java.io.FileNotFoundException;

import ndfs.NDFS;

/**
 * Implements the {@link ndfs.NDFS} interface, mostly delegating the work to a
 * worker class.
 */
public class NNDFS implements NDFS {
    /*
        XXXXXXXXXXXXXXXXXXXXXXXXXX
        create an array of workers
        and shared data structures
    */
    private final Worker worker;

    /**
     * Constructs an NDFS object using the specified Promela file.
     *
     * @param promelaFile
     *            the Promela file.
     * @throws FileNotFoundException
     *             is thrown in case the file could not be read.
     */
    public NNDFS(File promelaFile, int nrWorkers) throws FileNotFoundException {
        /*
            XXXXXXXXXXXXXXXXXXXXXXXXXX
            We need to create locally nrWorkers
        */
        this.worker = new Worker(promelaFile);
    }

    @Override
    public boolean ndfs() {
        /*  
            XXXXXXXXXXXXXXXXXXXXXXXXXX
            We need to run nrWorkers
        */
        worker.run();
        return worker.getResult();
    }
}
