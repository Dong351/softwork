package softwork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import softwork.service.impl.ChatMessageServiceImpl;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.User;
import softwork.service.impl.UserServiceImpl;
import softwork.utils.MapUnite;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  描述:
 *  一对一聊天
 *
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *                 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@RestController
@ServerEndpoint(value = "/webSocketOneToOne/{param}")
public class WebSocketOneToOne {
    // 这里使用静态，让 service 属于类
    private static UserServiceImpl userService;
    // 注入的时候，给类的 service 注入
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        WebSocketOneToOne.userService = userService;
    }
    private static ChatMessageServiceImpl messageService;
    @Autowired
    public void setChatMsgService(ChatMessageServiceImpl messageService) {
        WebSocketOneToOne.messageService = messageService;
    }

    @Autowired
    UserMapper userMapper;

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount;
    //实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key为用户标识
    private static final Map<String,WebSocketOneToOne> connections = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String sendId;
    private String roomId;


    /**
     * 连接建立成功调用的方法
     *
     * @param session
     * 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("param") String param, Session session) {
        this.session = session;
        String[] arr = param.split(",");
        this.sendId = arr[0];             //用户标识
        this.roomId = arr[1];         //会话标识
        connections.put(sendId,this);     //添加到map中
        addOnlineCount();               // 在线数加
        System.out.println(param);
        System.out.println(this.session);
        System.out.println("有新连接加入！新用户："+sendId+",当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        connections.remove(sendId);  // 从map中移除
        subOnlineCount();          // 在线数减
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     * @param session
     *            可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        JSONObject json= JSON.parseObject(message);
        String msg = (String) json.get("message");  //需要发送的信息
        String receiveId = (String) json.get("receiveId");      //发送对象的用户标识(接收者)
        String type = (String) json.get("type");      //发送对象的用户标识(接收者)
        send(msg,sendId,receiveId,roomId, Integer.valueOf(type));
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    //发送给指定角色
    public void send(String msg,String sendId,String receiveId,String roomId,Integer type){
        ChatMessage message = new ChatMessage();
        message.setContent(msg);
        message.setCreate_time(new Date());
        message.setReceive_id(Integer.valueOf(receiveId));
        message.setRoom_id(Integer.valueOf(roomId));
        message.setSend_id(Integer.valueOf(sendId));
        message.setType(type);

        try {
            User u = userMapper.selectByPrimaryKey(Integer.valueOf(sendId));
            System.out.println(u);
            //to指定用户
            WebSocketOneToOne con = connections.get(receiveId);
            if(con!=null){
                if(roomId.equals(con.roomId)){
                    //messageService.save(message); // 保存消息
                    Map map = MapUnite.getMap(message);
                    map.put("avatar",u.getAvatar_url());
                    con.session.getBasicRemote().sendText(JSON.toJSONString(map));
                }

            }
            //from具体用户
            WebSocketOneToOne confrom = connections.get(sendId);
            if(confrom!=null){
                if(roomId.equals(confrom.roomId)){
                    //messageService.save(message);
                    Map map = MapUnite.getMap(message);
                    map.put("avatar",u.getAvatar_url());
                    confrom.session.getBasicRemote().sendText(JSON.toJSONString(map));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketOneToOne.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketOneToOne.onlineCount--;
    }

}
