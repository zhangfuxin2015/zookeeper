package zfx.zkLock;

import org.apache.log4j.or.ThreadGroupRenderer;
import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by Duo Nuo on 2016/12/15.
 */
public class DemoServer {
    ZooKeeper zooKeeper=null;
    boolean haveLock=false;
    private String parentNode="/locks";
    private  String myNodePath="";
    static   String hostname="";
    public static void main(String[] args) throws  Exception {
        hostname=args[0];
        DemoServer demoServer=new DemoServer();
        //Acquiring a lock and business processing

        demoServer.getLockAndDoSomthing();
        Thread.sleep(Long.MAX_VALUE);
    }
    public  void getLockAndDoSomthing() throws  Exception{
         zooKeeper=new ZooKeeper("zhangfuxindeMacBook-Pro.local:2181", 2000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try {
                    if (watchedEvent.getType()!= Event.EventType.NodeChildrenChanged) return;
                    haveLock=gainLock();
                    if(haveLock){
                        System.out.println(hostname+"拿到了锁");
                        doSomthing();
                        //释放锁权限
                        releaseLock();
                        //注册锁
                        registerLock();
                    }
                }catch (Exception e){
                }

            }
        });
        //注册自己的锁节点
        registerLock();
        //让线程 休息一下， 方便测试用
        Thread.sleep((long) Math.random()*500+500);
        //获取锁权限
         haveLock=gainLock();
        //拿到锁，调用业务方法进行业务处理
        if(haveLock){
            System.out.println(hostname+"拿到了锁");
            doSomthing();
            //释放锁权限
            releaseLock();
            //注册锁
            registerLock();
        }

    }

    /**
     * 获取锁  最小的一个获取锁权限
     * @return
     */
    public  boolean gainLock() throws KeeperException, InterruptedException {
       List<String> listNode=zooKeeper.getChildren(parentNode,true);
        Collections.sort(listNode);
        String node=listNode.get(0);
        if(myNodePath.substring(parentNode.length()+1).equals(node)){
            return true;
        }
        return false;
    }
    public  void registerLock() throws KeeperException, InterruptedException {
        myNodePath= zooKeeper.create(parentNode+"/lock",null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        zooKeeper.getChildren(parentNode,true);
    }
    /**
     * 模拟访问共享资源，并进行业务处理的方法
     */
    public  void doSomthing() throws InterruptedException {
        System.out.println("begin working ....");
        Thread.sleep((long)Math.random()*1000+500);
        System.out.println("work has complished...");
    }

    /**
     * 释放锁权限，删除自己的所节点
     */
    public void releaseLock() throws KeeperException, InterruptedException {
        zooKeeper.delete(myNodePath,-1);
    }
}

