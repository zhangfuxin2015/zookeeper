package zfx.servers;

import org.apache.zookeeper.*;

import java.util.UUID;

/**
 * Created by zfx on 2016/12/14.
 */
public class DistributedServer {
    public static void main(String[] args) throws Exception  {
        DistributedServer distributedServe=new DistributedServer();
        //Create a zookeeper client, define a monitoring logic
        distributedServe.registerZK(args[0]);
        // add a node
        distributedServe.handle(args[0]);

        //Handle their own business
    }
    public  static  final  String groupNode="/servers";

    public  void registerZK(String hostname) throws Exception{
        ZooKeeper zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher() {
            /**
             * zookeeper服务器集群监听到某个数据节点上发生的时间，会通知监听注册者
             * @param watchedEvent
             */
            public void process(WatchedEvent watchedEvent) {

            }
        });
        String path=zooKeeper.create(groupNode+"/server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("server:注册了一个子节点"+path);
    }
    public  void handle(String hostName) throws InterruptedException {
        System.out.println(hostName+":-----startWork");
        Thread.sleep(Long.MAX_VALUE);
    }
}
