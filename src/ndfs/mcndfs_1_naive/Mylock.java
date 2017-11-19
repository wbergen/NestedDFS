package ndfs.mcndfs_1_naive;

import java.util.concurrent.atomic.AtomicBoolean;

public class Mylock {
	private AtomicBoolean lock;
	
	public Mylock(){
		this.lock = new AtomicBoolean(false);
	}

	public void lock(){
		while(!this.lock.compareAndSet(false,true)) { ; }
	}

	public void unlock(){
		this.lock.set(false);
	}
}