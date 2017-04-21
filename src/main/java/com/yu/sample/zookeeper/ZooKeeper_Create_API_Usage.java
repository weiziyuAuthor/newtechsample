package com.yu.sample.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
 * @author weizy
 *
 */
public class ZooKeeper_Create_API_Usage implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	static ZooKeeper zooKeeper = null;
	/**
	 * 重写process, 该方法负责处理来自ZooKeeper服务端的Watcher通知，在收到服务端发来的SyncConnected事件之后，
	 * 接触主程序在CountDownLatch上的等待阻塞
	 */
	public void process(WatchedEvent event) {
		System.out.println("Receive watched event: " + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}else if (event.getType() == EventType.NodeChildrenChanged) {
			try {
				System.out.println("ReGetChild: " + zooKeeper.getChildren(event.getPath(), true));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		zooKeeper = new ZooKeeper(Constants.DOMAIN_ZOOKEEPER, 5000,
				new ZooKeeper_Create_API_Usage());
		System.out.println(zooKeeper.getState());

		try {
			connectedSemaphore.await();
		} catch (Exception e) {
			System.out.println("ZooKeeper session established");
		}

		// 创建节点
		String path1 = zooKeeper.create("/ziyu-test", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Sucess create znode: " + path1);

		// 创建顺序节点
		String path2 = zooKeeper.create("/ziyu-test", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Sucess create znode: " + path2);

		// zooKeeper.create("/ziyu-withCallback", "".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
		// new IStringCallback(), "I am context");

		// 删除节点
		try {
			zooKeeper.delete("/ziyu-test", 0);
			System.out.println("delete node(/ziyu-test) success");
		} catch (Exception e) {
			System.out.println("delete node failed");
			e.printStackTrace();
		}

		// 删除节点
//		try {
//			zooKeeper.delete("/ziyu-test0", 0);
//			System.out.println("delete node(/ziyu-test0) success");
//		} catch (Exception e) {
//			System.out.println("delete node(/ziyu-test0) failed");
//			e.printStackTrace();
//		}
		
		
		//get children test
		String bookPath = "/zk-book1";
//		zooKeeper.create(bookPath, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		//父节点不能是临时节点
		zooKeeper.create(bookPath, "111".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zooKeeper.create(bookPath + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		List<String> childrenList = zooKeeper.getChildren(bookPath, true);
		System.out.println(childrenList);
		
		zooKeeper.create(bookPath + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		//更新节点数据 (同异步)
//		zk.setData();
		
		
//		检查节点是否存在
//		zk.exists();
		

	}

	class IStringCallback implements AsyncCallback.StringCallback {

		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println("Create path result: [" + rc + ", " + path
					+ ", " + ctx + ", real path name: " + name);
		}

	}

}
