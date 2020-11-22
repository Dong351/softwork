package softwork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.User;
import softwork.service.ChatMessageService;
import softwork.service.UserService;
import softwork.utils.MapUnite;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ServerEndpoint("/chat/team/{param}")
public class WebSocketTeam {

    // 使用map来收集session，key为roomName，value为同一个房间的用户集合
    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();
    //缓存session对应的用户
    private static final Map<String, String> users = new ConcurrentHashMap<>();
    private Integer uid;
    private String roomId;

    private static UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        WebSocketTeam.userService = userService;
    }

    private static ChatMessageService chatMessageService;
    @Autowired
    public void setChatMessageService(ChatMessageService chatMessageService){
        WebSocketTeam.chatMessageService = chatMessageService;
    }

    @OnOpen
    public void connect(@PathParam("param") String param, Session session){
        String[] arr = param.split(",");
        this.roomId = String.valueOf(arr[0]);
        this.uid= Integer.valueOf(arr[1]);
        System.out.println("uid:"+uid);
        System.out.println("roomid:"+roomId);
        //目前使用随机名称，可以整合自己的session管理
        User user = userService.SelectByKey(uid);
        // 将session按照房间名来存储，将各个房间的用户隔离
        if (!rooms.containsKey(roomId)) {
            // 创建房间不存在时，创建房间
            Set<Session> room = new HashSet<>();
            // 添加用户
            room.add(session);
            rooms.put(roomId, room);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            rooms.get(roomId).add(session);
        }
        System.out.println(roomId+" 房间有一人进入，当前房间人数："+rooms.get(roomId).size());
        users.put(session.getId(),user.getUsername());
    }

    @OnClose
    public void CloseConnect(Session session){
        rooms.get(roomId).remove(session);
        users.remove(session.getId());  // 从map中移除
//        subOnlineCount();          // 在线数减
        System.out.println(roomId+"房间有一人下线！");
    }



    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        JSONObject json= JSON.parseObject(message);
        String msg = (String) json.get("message");  //需要发送的信息
        String roomid = (String) json.get("team_id");      //发送对象的用户标识(接收者)
        String sendid = (String) json.get("send_id");
        User user = userService.SelectByKey(Integer.valueOf(sendid));
        System.out.println(user);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCreate_time(new Date());
        chatMessage.setRoom_id(roomid);
        chatMessage.setContent(msg);
        chatMessage.setType(2);
        chatMessage.setSend_id(Integer.valueOf(sendid));

        Map map = MapUnite.getMap(chatMessage);
        map.put("avatar_url",user.getAvatar_url());

        rooms.get(roomId).forEach(s -> {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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


}
