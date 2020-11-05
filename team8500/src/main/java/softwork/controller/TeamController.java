package softwork.controller;

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
    public WebAsyncTask<Object> JoinTeamRequest(@PathVariable Integer teamid,@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(teamService.JoinTeamRequest(teamid,user)));
    }

}
