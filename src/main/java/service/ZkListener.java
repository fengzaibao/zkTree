package service;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

@WebListener
public class ZkListener implements HttpSessionListener {

    private Logger logger=Logger.getLogger(ZkListener.class);
    
    public ZkListener() {
       
    }

	
    public void sessionCreated(HttpSessionEvent se)  { 
        
    }

	//由容器tomcat 在session超时时自动调用的方法     调用将session包装到Event中传入
    public void sessionDestroyed(HttpSessionEvent se)  { 
    	//首先取出zk
    	HttpSession session=se.getSession();
        ZooKeeper zk=(ZooKeeper) session.getAttribute("zk");
    	if(zk!=null&&zk.getState()==ZooKeeper.States.CONNECTED){
    		try {
				zk.close();
				logger.info("zk关闭联接.....");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
	
}
