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
<<<<<<< HEAD
    private final Worker worker[];
=======
    // private final Worker worker;

    private int nrWorkers;
    private final Worker[] workers;
>>>>>>> 19b4de792952908b927c5acf5bbae1158a6db99a

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
<<<<<<< HEAD
            this.worker = new Worker[nrWorkers];
        for( int i = 0; i < nrWorkers ; i++){
            this.worker[i] = new Worker(promelaFile);     
=======
           
        this.workers = new Worker[nrWorkers];
        this.nrWorkers = nrWorkers;

        for (int i = 0; i < nrWorkers; i++) {
            this.workers[i] = new Worker(promelaFile);
>>>>>>> 19b4de792952908b927c5acf5bbae1158a6db99a
        }
    }

    @Override
    public boolean ndfs() {
        /*  
            XXXXXXXXXXXXXXXXXXXXXXXXXX
            We need to run nrWorkers
        */
<<<<<<< HEAD
        for( int i = 0; i < this.worker.length; i++){
            this.worker[i].run();
        }
        return worker.getResult();
=======
        for (int i = 0; i < nrWorkers; i++) {
            workers[i].run();
        }

        // Need some way to collect the results..
        boolean res = false;
        for (int i = 0; i < nrWorkers; i++) {
            if (workers[i].getResult()) {
                res = true;
            }
        }
        return res;
        

        // worker.run();
        // return worker.getResult();
>>>>>>> 19b4de792952908b927c5acf5bbae1158a6db99a
    }
}
