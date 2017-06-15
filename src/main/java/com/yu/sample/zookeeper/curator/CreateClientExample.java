package com.yu.sample.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CreateClientExample {

	public static CuratorFramework createSimple(String connectionString) {
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
		return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
	}
	
	public static CuratorFramework createWithOptions(String connectString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs){
		return CuratorFrameworkFactory.builder()
				.connectString(connectString)
				.retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(sessionTimeoutMs)
				.build();
	}
}
