package com.yu.sample.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeper_Custructor_Usage_SID_PASSWD implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event: " + event);
		if(KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zookeeper = new ZooKeeper(Constants.DOMAIN_ZOOKEEPER, 
								5000, 
								new ZooKeeper_Custructor_Usage_SID_PASSWD());
		
		connectedSemaphore.await();
		
		long sessionId = zookeeper.getSessionId();
		byte[] passwd = zookeeper.getSessionPasswd();
		
		// use illegal sessionId and sessionPasswd
		zookeeper = new ZooKeeper(Constants.DOMAIN_ZOOKEEPER, 
						5000, 
						new ZooKeeper_Custructor_Usage_SID_PASSWD(),
						1l,
						"test".getBytes());
		
		//use correct sessionId and sessionPasswd
		zookeeper = new ZooKeeper("",
						5000, 
						new ZooKeeper_Custructor_Usage_SID_PASSWD(),
						sessionId,
						passwd);
	}

}
