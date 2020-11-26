package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.dto.RouteEditDTO;
import softwork.pojo.entities.User;
import softwork.service.RouteService;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    RouteService routeService;

    @GetMapping("/hello")
    public String hello(){
        return "route hello";
    }

    @PutMapping("/add/{contestId}")
    public WebAsyncTask<Object> AddRoute(@PathVariable Integer contestId,@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(routeService.AddRoute(contestId,user)));
    }

    @GetMapping("/get")
    public WebAsyncTask<Object> GetRoutes(@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(routeService.GetRoutes(user)));
    }


    @PostMapping("/remarks/edit/{routeId}")
    public WebAsyncTask<Object> EditRemarks(@PathVariable Integer routeId,@RequestBody RouteEditDTO dto,
                                            @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(routeService.EditRemarks(routeId,dto,user)));
    }

    @GetMapping("/alert")
    public WebAsyncTask<Object> RouteAlert(@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(routeService.RouteAlert(user)));
    }

    @DeleteMapping("/remove/{type}/{data_id}")
    public WebAsyncTask<Object> RemoveRoute(@PathVariable Integer type,@PathVariable Integer data_id,@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(routeService.RemoveRoute(type,data_id,user)));
    }
}
