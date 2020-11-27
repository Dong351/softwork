package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.entities.User;
import softwork.service.ChatMessageService;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {
    @Autowired
    ChatMessageService chatMessageService;

    @GetMapping("/hello")
    public String hello(){
        return "chat hello";
    }

    @GetMapping("/singleHistoryMessage/{uid}")
    public WebAsyncTask<Object> SingleGetHistoryMessage(@PathVariable Integer uid,
                                                        @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(chatMessageService.Single(uid,user)));
    }


    @GetMapping("/chatPreview")
    public WebAsyncTask<Object> GetChatMessagePreview(@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(chatMessageService.GetChatMessagePreview(user)));
    }



}
