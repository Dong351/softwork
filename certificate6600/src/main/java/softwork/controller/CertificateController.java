package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.pojo.dto.PageDTO;
import softwork.service.CertificateService;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @GetMapping()
    public WebAsyncTask<Object> getCertificate(PageDTO pageDTO){
        return new WebAsyncTask<>(()-> JsonResult.ok(certificateService.getBaseInfo(pageDTO)));
    }

    @GetMapping("/{id}")
    public WebAsyncTask<Object> getCertificateById(@PathVariable String id){
        return new WebAsyncTask<>(()-> JsonResult.ok(certificateService.getInfoByID(id)));
    }

    @GetMapping("/search")
    public WebAsyncTask<Object> searchByKeyword(String keyword,PageDTO dto){
        return new WebAsyncTask<>(()-> JsonResult.ok(certificateService.searchByKeyWord(keyword,dto)));
    }

}
