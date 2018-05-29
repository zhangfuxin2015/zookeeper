package zfx.zookeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 创建客户端，获取zookeeper下的根节点所有信息
 */
public class SimpleZkClient {
    public  ZooKeeper zooKeeper=null;
    @Before
    public  void init() throws  Exception{
         zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher() {
            /**
             * zookeeper服务器集群监听到某个数据节点上发生的时间，会通知监听注册者
             * @param watchedEvent
             */
            public void process(WatchedEvent watchedEvent) {
                System.out.println("节点："+watchedEvent.getPath()+"发生了事件"+watchedEvent.getType());
            }
        });
    }
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //connctString zk服务器地址
        // sessionTimeOut 超时 的时间
        //watch 为监听器，接收zookeeper集群通知，一旦发现节点发生改变，就会通知
            ZooKeeper zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher() {
                /**
                 * zookeeper服务器集群监听到某个数据节点上发生的时间，会通知监听注册者
                 * @param watchedEvent
                 */
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("节点："+watchedEvent.getPath()+"发生了事件"+watchedEvent.getType());
                }
            });
            List<String> children=zooKeeper.getChildren("/",false);
            for(String child:children){
                System.out.println(child);
            }

    }

    /**
     * create a node in the zookeeper
     */
    @Test
    public   void createNode() throws KeeperException, InterruptedException {
        //创建永久节点
        String nodePermanent=zooKeeper.create("/javaClient","permanent".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        //创建临时节点
        String nodeTmp=zooKeeper.create("/javaClient/javaClientTmp","tmp".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
        System.out.println("创建一个永久节点："+nodePermanent);
        System.out.println("创建一个临时节点"+nodeTmp);
    }

    /**
     * Retrieve the data content in the znode
     */
    @Test
    public  void testGetNode() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] contents= zooKeeper.getData("/javaClient",false,null);
        System.out.println(new String (contents,"utf-8"));
    }

    /**
     *Modify the node data
     */
    @Test
    public  void testModifyNode() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        zooKeeper.setData("/javaClient","test".getBytes(),0);
        byte[] contents= zooKeeper.getData("/javaClient",false,null);
        System.out.println(new String (contents,"utf-8"));
    }

    /**
     *Delete the data node
     */
    @Test
    public  void deleteNode() throws KeeperException, InterruptedException {
        zooKeeper.delete("/javaClient",-1);
        Stat stat= zooKeeper.exists("/javaClient",false);
        System.out.println(stat.getDataLength());
    }
}
