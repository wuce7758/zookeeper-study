package com.shaoqing.zookeeper1;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
public class ConnetctionWatcher implements Watcher {

	private static final int SESSION_TIMEOUT = 5000;

	protected ZooKeeper zk = null;
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public void process(WatchedEvent event) {
		KeeperState state = event.getState();
		
		if(state == KeeperState.SyncConnected){
			countDownLatch.countDown();
		}
	}
	public void connection(String hosts) throws IOException, InterruptedException {
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
		countDownLatch.await();
	}
	public void close() throws InterruptedException {
		if (null != zk) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				throw e;
			}finally{
				zk = null;
				System.gc();
			}
		}
	}
}
