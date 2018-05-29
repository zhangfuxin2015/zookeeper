package zfx.zookeper;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Test the zookeeper listeners
 */
public class TestZKWatch {
    static  ZooKeeper zooKeeper;
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
         zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher() {
            /**
             * Listener callback methods
             * Continue to monitor
             */
            public void process(WatchedEvent watchedEvent) {
                System.out.println("节点"+watchedEvent.getPath()+"发生了"+watchedEvent.getType());
                try {
                    //Add a child node cannot be listening to
                    //zooKeeper.exists("/javaListen",true);
                    //GetChildren and there exits monitoring scope is different
                    zooKeeper.getChildren("/javaListen",true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // create a node
        zooKeeper.create("/javaListen","testListen".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //Judge whether there is any, and registered to monitor,Listen to an event, however, will fail
        zooKeeper.exists("/javaListen",true);
        Thread.sleep(Long.MAX_VALUE);

    }
}
