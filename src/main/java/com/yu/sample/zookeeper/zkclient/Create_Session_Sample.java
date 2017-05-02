package com.yu.sample.zookeeper.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.yu.sample.zookeeper.Constants;

public class Create_Session_Sample {

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkClient = new ZkClient(Constants.DOMAIN_ZOOKEEPER, 5000);
		System.out.println("ZooKeeper session established. ");
		
		zkClient.subscribeChildChanges("/zk-zy-book", new IZkChildListener(){

			public void handleChildChange(String parentPath, List<String> currentChilds)
					throws Exception {
				System.out.println(parentPath + "'s child changed, currentChilds: "+currentChilds);
			}
			
		});
		
		String path = "/zk-zy-book/c1";
		zkClient.createPersistent(path, true);
		
		String path1 = "/zk-zy-book/c2";
		zkClient.createPersistent(path1, true);
		
//		delete
//		zkClient.delete("");
//		zkClient.deleteRecursive("");

//		读取数据
		List<String> list = zkClient.getChildren("/zk-zy-book");
		System.out.println("read data list: " + list);
		
		
//		数据改变
		String delPath = "/zk-book/del";
		zkClient.subscribeDataChanges(delPath, new IZkDataListener(){

			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				System.out.println("Node "+dataPath+" changed, new data: " + data);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("Node " +dataPath+ " deleted.");
			}
			
		});
		
		zkClient.createEphemeral(delPath, "123");
		System.out.println(zkClient.readData(delPath));
		
		zkClient.writeData(delPath, "456");
		Thread.sleep(1000);
		zkClient.delete(delPath);
		
//		检查节点是否存在
//		boolean exists(final String path)
		
		
		
	}

}
