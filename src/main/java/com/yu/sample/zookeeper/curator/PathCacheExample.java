package com.yu.sample.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;

/**
 * 
 * @author weizy
 *
 * 2017年6月15日    下午3:48:07
 * 
 * https://github.com/apache/curator/tree/master/curator-examples/src/main/java
 */
public class PathCacheExample {

	private static final String PATH = "/example/cache";
	
	public static void main(String[] args) throws Exception {
//		TestingServer server = new TestingServer();
		CuratorFramework client = null;
		PathChildrenCache cache = null;
		
		try{
			client = CuratorFrameworkFactory.newClient("", new ExponentialBackoffRetry(1000, 3));
			client.start();
			
			cache = new PathChildrenCache(client, PATH, true);
			cache.start();
			
			processCommands(client, cache);
			
		}finally{
			CloseableUtils.closeQuietly(cache);
			CloseableUtils.closeQuietly(client);
		}
	}
	
	private static void addListener(PathChildrenCache cache) {
		PathChildrenCacheListener listener = new PathChildrenCacheListener(){

			public void childEvent(CuratorFramework arg0,
					PathChildrenCacheEvent event) throws Exception {
				switch(event.getType()){
				case CHILD_ADDED:
					System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				case CHILD_UPDATED:
					System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				case CHILD_REMOVED:
					System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}
			}
			
		};
		cache.getListenable().addListener(listener);
	}
	
	private static void processCommands(CuratorFramework client, PathChildrenCache cache) {
		
	}
}
