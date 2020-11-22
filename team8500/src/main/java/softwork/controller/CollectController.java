package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.entities.User;
import softwork.service.CollectService;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    CollectService collectService;

    @GetMapping("/hello")
    public String hello(){
        return "collect Hello";
    }

    @GetMapping("/contest/{contestId}")
    public WebAsyncTask<Object> CollectContest(@PathVariable Integer contestId, @Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(collectService.CollectContest(contestId,user));
        });
    }

    @GetMapping("/certificate/{certificateId}")
    public WebAsyncTask<Object> CollectCertificate(@PathVariable Integer certificateId, @Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(collectService.CollectCertificate(certificateId,user));
        });
    }

    @GetMapping("/certificate/get")
    public WebAsyncTask<Object> GetCollectContest(@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(collectService.GetCollectContest(user));
        });
    }
}