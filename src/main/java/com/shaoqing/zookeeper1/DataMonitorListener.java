package com.shaoqing.zookeeper1;

/**
 *  The DataMonitor object uses it to communicate back to its container
 *  which is also the the Executor object
 */
public interface DataMonitorListener {
	/**
    * The existence status of the node has changed.
    */
    void exists(byte data[]);

    /**
    * The ZooKeeper session is no longer valid.
    * 
    * @param rc
    * the ZooKeeper reason code
    */
    void closing(int rc);
}
