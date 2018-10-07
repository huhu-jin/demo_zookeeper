package com.demo._1api;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * 获取节点(同步)
 *
 * @author jerome_s@qq.com
 */
public class _31GetChildrenSync implements Watcher {

    private static ZooKeeper zooKeeper;

    private static String address = "111.231.84.99:2181";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(address, 5000, new _31GetChildrenSync());
        System.out.println(zooKeeper.getState().toString());
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            // 保证客户端与服务端建立连接后 Dosomething的内容只执行一次
            // todo 这里有问题 网络 断连重连后 会再次触发
            if (event.getType() == EventType.None && null == event.getPath()) {
                createNode();
                doSomething(zooKeeper);
            } else {
                // doSomething 有关注节点的变化
                if (event.getType() == EventType.NodeChildrenChanged) {
                    try {
                        System.out.println(zooKeeper.getChildren(event.getPath(), true));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void createNode() {
        try {
            zooKeeper.create("/node2/node21", "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/node2/node22", "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/node2/node23", "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSomething(ZooKeeper zooKeeper) {
        try {
            // 返回节点下面的所有子节点的列表
            // true false 要不要关注这个节点的子节点的变化
            List<String> children = zooKeeper.getChildren("/node2", true);
            System.out.println(children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/*
	输出：
	CONNECTING
	[node21, node22]
	在服务器新增一个节点：
	[zk: 27.154.242.214:5091(CONNECTED) 7] create /node2/node23 23
	Created /node2/node23
	可以看到控制台实时输出
	[node21, node22, node23]
	*/

}
