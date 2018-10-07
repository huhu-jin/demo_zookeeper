package com.demo._1api;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 建立连接
 * 
 * @author jerome_s@qq.com
 */
public class _1CreateSession implements Watcher {

    private static String address = "111.231.84.99:2181";


    private static CountDownLatch countDownLatch = new CountDownLatch(1);

	private static ZooKeeper zookeeper;

	public static void main(String[] args) throws IOException, InterruptedException {
	    // new
		zookeeper = new ZooKeeper(address, 5000, new _1CreateSession());

		// 获取zk状态并输出事件接收到的数据
		System.out.println(zookeeper.getState());
        countDownLatch.await();

	}

    /**
     *  出现的事件
     *  EventType.NodeCreate  节点创建
        EventType.NodeDataChange  节点数据改变
        EventType.NodeChildrenChange 节点的子节点变更
        EventType.NodeDeleted  节点 删除
        连接类型
        KeeperState.Disconnected 连接诶失败
        KeeperState.AuthFailed 授权失败
        KeeperState.Expired  连接超时
        KeeperState.SyncConnected连接成功
     * @param event
     */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("收到事件：" + event);
		if (event.getState() == KeeperState.SyncConnected) {
			doSomething();
            countDownLatch.countDown();
        }
	}

	private void doSomething() {
		System.out.println("do something");
	}

}
