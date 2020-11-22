package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.entities.Contest;
import softwork.pojo.dto.PageDTO;
import softwork.service.ContestService;
import softwork.utils.JsonResult;

@RestController()
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    ContestService contestService;

    @GetMapping()
    public WebAsyncTask<Object> getContest(PageDTO pageDTO, Contest contest){
        return new WebAsyncTask<>(()-> JsonResult.ok(contestService.getBaseInfo(pageDTO, contest)));
    }

    @GetMapping("/{id}")
    public WebAsyncTask<Object> getContestById(@PathVariable String id){
        return new WebAsyncTask<>(()-> JsonResult.ok(contestService.getInfoByID(id)));
    }

    @GetMapping("/type")
    public WebAsyncTask<Object> getContestType(){
        return new WebAsyncTask<>(()-> JsonResult.ok(contestService.getAllType()));
    }

    @GetMapping("/level")
    public WebAsyncTask<Object> getContestLevel(){
        return new WebAsyncTask<>(()-> JsonResult.ok(contestService.getAllLevel()));
    }

    @GetMapping("/search")
    public WebAsyncTask<Object> searchByKeyword(String keyword,PageDTO dto,Contest contest){
        return new WebAsyncTask<>(()-> JsonResult.ok(contestService.searchByKeyWord(keyword,dto,contest)));
    }


}
