package softwork.controller;

import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ServerEndpoint("/chat")
public class WebSocketChat {
    private static final Map<Integer, Set<Session>> rooms = new ConcurrentHashMap<>();
}
