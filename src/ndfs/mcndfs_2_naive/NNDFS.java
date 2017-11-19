package ndfs.mcndfs_2_naive;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread;
import java.lang.InterruptedException;

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
    private final Worker worker[];

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
        this.worker = new Worker[nrWorkers];
        
        for( int i = 0; i < nrWorkers ; i++){
            this.worker[i] = new Worker(promelaFile, i);     
        }
    }

    @Override
    public boolean ndfs() {
        /*  
            XXXXXXXXXXXXXXXXXXXXXXXXXX
            We need to run nrWorkers
        */
        for( int i = 0; i < this.worker.length; i++){
            this.worker[i].start();
        }
        
        // Sync barrier
        for( int i = 0; i < this.worker.length; i++){
            try {
                this.worker[i].join();
            } catch (InterruptedException e) {
                ;
            }
            
        }
        //return result
        return this.worker[0].getResult();
    }
}
