package softwork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import softwork.exception.CommonException;
import softwork.mapper.UserMapper;
import softwork.service.impl.ChatMessageServiceImpl;
import softwork.service.impl.UserServiceImpl;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
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

    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public static void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        WebSocketOneToOne.rabbitTemplate = rabbitTemplate;
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

//     private static final Map<Integer, Set<User>> TUsers= new ConcurrentHashMap<>();

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
        Integer receive_id = (Integer) json.get("uid"); // uid，目标用户，如果uid为空，则为发送整个队伍，是否会null异常，或者parse异常?
        String msg = (String) json.get("message"); // 消息

        List<Integer> uids;
        if(teamCache.containsKey(tid)){
            uids = teamCache.get(tid);

        }
        else {
            uids = messageService.GetTeamUidList(tid);
            teamCache.put(tid,uids);

        }
        System.out.println(uids);

        // 判断一下该队伍是否包含该用户，是否是队长
        // 具体的实现（利用static 缓冲）
        int in = 0;
        for(int i = 0;i < uids.size();i++){
//            System.out.println(uids.get(i));
            if(uids.get(i) == receive_id){
                if(i == 0){
                    System.out.println("发送的目标用户是该队队长");
                }
                System.out.println("发送的目标用户在该队");
                break;
            }

            // 判断本次发送的用户是否为该小队，如果不是则判断联系的用户是否为队长，如果不是，返回deny
            // 具体的实现（可利用static 缓冲）
            // 涉及数据库暂无实现
            if(uids.get(i) == uid){
                System.out.println("发送用户在该队");
                in = 1;
            }
        }
        if(in != 1){
            System.out.println("deny");
            throw new CommonException("无效联系");
        }


        // 这里忽略tid，tid查找涉及数据库，需要维护一个线程安全map<tid,Array<User>>类

        send(msg,tid,receive_id);

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
    public void send(String msg, Integer tid, Integer receive_id) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> map = new HashMap<>();
        map.put("message", msg);
        map.put("receive_id", receive_id);
        map.put("send_id", uid);
        map.put("tid",tid);
        messageService.SendQueue(map);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chat",map);
        // msg 需要自定义，需要带有tid,uid,datetime等等参数，一般为json
        // 如果uid为空则是整个队伍，否则是私聊
        if (uid == 0) {
            // 这里需开设一个全局类批量加入消息缓冲队列录入数据库(队伍消息），不能直接录入防止卡顿



            // 不管有没有在线客户都需要存到数据库里
            for (Integer clientId : teamCache.get(tid)) {
                WebSocketOneToOne client = clients.getOrDefault(clientId, null);

                if (client != null) {

                    client.session.getAsyncRemote().sendText(jsonObject.toString());
                }
            }
        } else {
            // 发送单独的用户的
            WebSocketOneToOne client = clients.getOrDefault(receive_id, null);
            // 这里需开设一个全局类批量加入消息缓冲队列录入数据库(私人消息），不能直接录入防止卡顿
            // 不管有没有都要加入数据库的历史记录

            if (client != null) {
                client.session.getAsyncRemote().sendText(jsonObject.toJSONString());
            }
        }
    }
}
