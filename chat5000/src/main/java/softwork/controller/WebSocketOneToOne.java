package softwork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.User;
import softwork.service.impl.ChatMessageServiceImpl;
import softwork.service.impl.UserServiceImpl;
import softwork.utils.MapUnite;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:
 * 一对一聊天
 *
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@RestController
@ServerEndpoint(value = "/chat/single/{param}")
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

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount;
    //实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key为用户标识
    private static final Map<String, WebSocketOneToOne> connections = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static final Map<Integer, WebSocketOneToOne> clients = new ConcurrentHashMap<>();
    // tid => uid map
    // 整形加速map
    private static final Map<Integer, List<Integer>> teamCache = new ConcurrentHashMap<>();
    private Session session;
    private String sendId;
    private String roomId;
    private int uid; // 用户的自增键

    // private static final Map<

    @Autowired
    UserMapper userMapper;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("param") String param, Session session) {
        this.session = session;
        this.uid = Integer.valueOf(param); // 该session对应的用户uid，可以用token拦截器代替获取
        // 将该session加入map中，这里的map是线程安全类，如果原来map中存在需要注销即close
        if (clients.containsKey(this.uid)) {
            WebSocketOneToOne oldSocket = clients.get(this.uid);
            try {
                // 尝试关闭之前的客户端
                oldSocket.session.close();
            } catch (IOException ioException) {
                // Logging 但是我不知道Java如何实现
            }

        }
        clients.put(this.uid, this);// 将新的客户端加入map
//        String[] arr = param.split(",");
//        this.sendId = arr[0];             //用户标识
        // 会话标识
//        if(Integer.valueOf(arr[0]) < Integer.valueOf(arr[1])){
//            this.roomId = arr[0]+"-"+arr[1];
//        }
//        else this.roomId = arr[1]+"-"+arr[0];
//        System.out.println(this.roomId);
//        connections.put(sendId,this);     //添加到map中
//        addOnlineCount();               // 在线数加
//        System.out.println(this.session);
//        System.out.println("有新连接加入！新用户："+sendId+",当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        clients.remove(this.uid);
//        connections.remove(sendId);  // 从map中移除
//        subOnlineCount();          // 在线数减
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 接收到客户端的消息，通常是用户发送某个数据
        System.out.println("来自客户端的消息:" + message);
        JSONObject json = JSON.parseObject(message); // 设定是json格式的
        // 包含属性有tid,userid => 对应当前用户是联系哪个小队的tid，以及该小队的哪个用户
        Integer tid = (Integer) json.get("tid"); // tid自增键，队伍的id
        Integer uid = (Integer) json.get("uid"); // uid，目标用户，如果uid为空，则为发送整个队伍，是否会null异常，或者parse异常?
        String msg = (String) json.get("message"); // 消息
        // 判断一下该队伍是否包含该用户，是否是队长
        // 具体的实现（利用static 缓冲）

        // 判断本次发送的用户是否为该小队，如果不是则判断联系的用户是否为队长，如果不是，返回deny
        // 具体的实现（可利用static 缓冲）
        // 涉及数据库暂无实现

        // 这里忽略tid，tid查找涉及数据库，需要维护一个线程安全map<tid,Array<User>>类

        send(msg,tid,uid);

//        String msg = (String) json.get("message");  //需要发送的信息
//        String receiveId = (String) json.get("receiveId");      //发送对象的用户标识(接收者)
//        String type = "0";
//        send(msg, sendId, receiveId, roomId, type);
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
    public void send(String msg, Integer tid, Integer uid) {
        // msg 需要自定义，需要带有tid,uid,datetime等等参数，一般为json
        // 如果uid为空则是整个队伍，否则是私聊
        if (uid == null) {
            // 这里需开设一个全局类批量加入消息缓冲队列录入数据库(队伍消息），不能直接录入防止卡顿
            // 不管有没有在线客户都需要存到数据库里
            for (Integer clientId : teamCache.get(tid)) {
                WebSocketOneToOne client = clients.getOrDefault(clientId,null);

                if (client != null) {

                    client.session.getAsyncRemote().sendText(msg);
                }
            }
        }else{
            // 发送单独的用户的
            WebSocketOneToOne client = clients.getOrDefault(uid,null);
            // 这里需开设一个全局类批量加入消息缓冲队列录入数据库(私人消息），不能直接录入防止卡顿
            // 不管有没有都要加入数据库的历史记录
            if (client!=null){
                client.session.getAsyncRemote().sendText(msg);
            }
        }

//        ChatMessage message = new ChatMessage();
//        message.setContent(msg);
//        message.setCreate_time(new Date());
//        System.out.println(new Date());
//        message.setReceive_id(Integer.valueOf(receiveId));
//        message.setRoom_id(roomId);
//        message.setSend_id(Integer.valueOf(sendId));
//        message.setType(0);
//        System.out.println(message);
//
//        try {
//            Integer send_uid = Integer.valueOf(sendId);
//            System.out.println(send_uid);
////            User u = userMapper.selectByPrimaryKey(send_uid);
//            User u = userService.SelectByKey(send_uid);
//            System.out.println(u);
//            //to指定用户
//            WebSocketOneToOne con = connections.get(receiveId);
//            if (con != null) {
//                if (roomId.equals(con.roomId)) {
//                    Map map = MapUnite.getMap(message);
//                    map.put("avatar", u.getAvatar_url());
//                    con.session.getBasicRemote().sendText(JSON.toJSONString(map));
//                }
//
//            }
//            //from具体用户
//            WebSocketOneToOne confrom = connections.get(sendId);
//            if (confrom != null) {
//                if (roomId.equals(confrom.roomId)) {
//                    Map map = MapUnite.getMap(message);
//                    map.put("avatar", u.getAvatar_url());
//                    confrom.session.getBasicRemote().sendText(JSON.toJSONString(map));
//                }
//
//            }
//            messageService.save(message); // 保存消息
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
