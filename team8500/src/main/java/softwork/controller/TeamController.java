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

    @PostMapping("/create/{contestId}")
    public WebAsyncTask<Object> Create(@PathVariable Integer contestId,@RequestBody @Validated TeamCreateDTO dto, @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(teamService.Create(contestId,dto,user)));
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

    @GetMapping("/joined")
    public WebAsyncTask<Object> JoinedTeam(@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.ShowJoinedTeam(user));
        });
    }

    @GetMapping("/{teamid}")
    public WebAsyncTask<Object> GetTeamInfo(@PathVariable Integer teamid,@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.GetTeamInfo(teamid,user));
        });
    }

    @GetMapping("/get/{contestId}")
    public WebAsyncTask<Object> GetListByContestId(@PathVariable Integer contestId){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.GetListByContestId(contestId));
        });
    }

    @DeleteMapping("/delete/{teamid}")
    public WebAsyncTask<Object> BreakTeam(@PathVariable Integer teamid,@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(teamService.BreakTeam(teamid,user));
        });
    }

}
