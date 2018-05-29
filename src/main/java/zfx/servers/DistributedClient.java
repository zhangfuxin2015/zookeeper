package zfx.servers;

import org.apache.log4j.or.ThreadGroupRenderer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfx on 2016/12/14.
 */
public class DistributedClient {
    public static void main(String[] args) throws Exception {
        DistributedClient distributedClient=new DistributedClient();
        // Access to the server list
        distributedClient.getList();
        //handle ower logic
        distributedClient.handle();
    }
    ZooKeeper zooKeeper=null;
    List<String> serverList=null;
    public void getList() throws Exception {
         zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher()  {
            public void process(WatchedEvent watchedEvent) {
                try {
                    getNode();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        getNode();
    }
    public  void getNode() throws KeeperException, InterruptedException {
        List<String> nodes=zooKeeper.getChildren("/servers",true);
        List<String> serverList=new ArrayList<String>();
        for(String node:nodes){
            byte[] child= zooKeeper.getData("/servers/"+node,false,null);
            serverList.add(child.toString());
            System.out.println("当前的服务节点有："+node);
        }
        System.out.println("服务节点信息更新完毕");
        serverList=serverList;
    }
    public  void handle() throws InterruptedException {
        System.out.println("客户端处理自己的业务功能");
        Thread.sleep(Long.MAX_VALUE);
    }
}
