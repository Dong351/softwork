package softwork.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.dto.TeamCreateDTO;
import softwork.pojo.entities.User;
import softwork.service.TeamService;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/hello")
    public String hello(){
        return "Team hello";
    }

    @PostMapping("/create")
    public WebAsyncTask<Object> Create(@RequestBody @Validated TeamCreateDTO dto, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(teamService.Create(dto,user)));
    }

    @PostMapping("/join/{teamid}")
    public WebAsyncTask<Object> JoinTeamRequest(@PathVariable Integer teamid,@RequestBody String requestText,@Token User user){
        return new WebAsyncTask<>(()->{
            JSONObject Text = JSON.parseObject(requestText);
            return JsonResult.ok(teamService.JoinTeamRequest(Text.getString("requestText"),teamid,user));
        });
    }

    @PostMapping("/dealRequest/{messageid}/{deal}")
    public WebAsyncTask<Object> DealTeamRequest(@PathVariable Integer messageid,
                                                @PathVariable Integer deal,
                                                @Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.DealTeamRequest(messageid,deal,user));
        });
    }

    @GetMapping("/joined}")
    public WebAsyncTask<Object> JoinedTeam(@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.ShowJoinedTeam(user));
        });
    }

}
