package com.shaoqing.zookeeper1;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class CreateGroup implements Watcher {
	//会话延时
	private static final int SESSION_TIMEOUT = 1000;
	//zk对象
	private ZooKeeper zk = null;
	//同步计数器
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	//客户端连接到服务器时会触发观察者进行调用
	public void process(WatchedEvent event) {
		if(event.getState() == KeeperState.SyncConnected){
			countDownLatch.countDown();//计数器减一
		}
	}

	public void connect(String hosts) throws IOException, InterruptedException {
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
		countDownLatch.await();//阻塞程序继续执行
	}
	//创建GROUP
	public void create(String groupName) throws KeeperException, InterruptedException{
		String path = "/" + groupName;
		//允许任何客户端对该znode进行读写,以及znode进行持久化
		String createPath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("Created "+createPath);
	}
	//关闭zk
	public void close() throws InterruptedException{
		if(zk != null){
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
	
	//测试主类
	public static void main(String args[]){
		String host = "127.0.0.1:4399";
		String groupName = "test";
		CreateGroup createGroup = new CreateGroup();
		try {
			createGroup.connect(host);
			createGroup.create(groupName);
			createGroup.close();
			createGroup = null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
		
	}

}
