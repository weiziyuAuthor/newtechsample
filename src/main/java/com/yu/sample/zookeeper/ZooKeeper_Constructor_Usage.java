package com.yu.sample.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author weizy
 *
 * 2017年4月19日    下午3:46:15
 */
public class ZooKeeper_Constructor_Usage implements Watcher{

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	/**
	 * 重写process, 该方法负责处理来自ZooKeeper服务端的Watcher通知，在收到服务端发来的SyncConnected事件之后，接触主程序在CountDownLatch上的等待阻塞
	 */
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event: " + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}
	
	public static void main(String args[]) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(Constants.DOMAIN_ZOOKEEPER, 5000, new ZooKeeper_Constructor_Usage());
		System.out.println(zooKeeper.getState());
		
		try {
			connectedSemaphore.await();
		} catch (Exception e) {
			System.out.println("ZooKeeper session established");
		}
		
	}

	
}
