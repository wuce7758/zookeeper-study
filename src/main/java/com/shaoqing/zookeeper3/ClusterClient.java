package com.shaoqing.zookeeper3;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ClusterClient implements Watcher, Runnable {
	private static String membershipRoot = "/Members";
	ZooKeeper zk;

	public ClusterClient(String hostPort, Long pid) {
		String processId = pid.toString();
		try {
			zk = new ZooKeeper(hostPort, 2000, this);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (zk != null) {
			try {
				zk.create(membershipRoot + '/' + processId, processId.getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL);
			} catch (KeeperException | InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public synchronized void close() {
		try {
			zk.close();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		System.out.println("\nEvent Received:%s" + event.toString());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			synchronized (this) {
				while (true) {
					wait();
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			this.close();
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage:ClusterClient<Host:Port>");
			System.exit(0);
		}

		String hostPort = args[0];
		// Get the process id
		String name = ManagementFactory.getRuntimeMXBean().getName();
		int index = name.indexOf('@');
		Long processId = Long.parseLong(name.substring(0, index));
		new ClusterClient(hostPort, processId).run();
	}

}
