package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.entities.User;
import softwork.service.MessageService;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @GetMapping("/hello")
    public String hello(){
        return "message hello";
    }

    @GetMapping("/list")
    public WebAsyncTask<Object> GetMessageList(@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(messageService.GetList(user)));
    }

    @GetMapping("/get/{mid}")
    public WebAsyncTask<Object> GetMessage(@PathVariable Integer mid, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(messageService.GetMessage(mid,user)));
    }
}
