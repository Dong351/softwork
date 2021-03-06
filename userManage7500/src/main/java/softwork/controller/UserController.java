package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;
import softwork.pojo.dto.*;
import softwork.pojo.entities.User;
import softwork.service.impl.UserServiceImpl;
import softwork.token.Token;
import softwork.utils.JsonResult;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/register")
    public WebAsyncTask<Object> register(@RequestBody @Validated UserRegisterDTO dto){
        return new WebAsyncTask<>(()->{
            userService.register(dto);
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            return JsonResult.ok(userService.getUserInfoVO(user));
        });
    }

    @PostMapping("/login")
    public WebAsyncTask<Object> login(@RequestBody @Validated UserLoginDTO dto){
        return new WebAsyncTask<>(()-> JsonResult.ok(userService.login(dto)));
    }

    @PostMapping("/update")
    public WebAsyncTask<Object> update(@RequestBody @Validated UserUpdateDTO dto,
                                       @Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(userService.updateUserInfo(dto,user)));

    }

    @PutMapping("/modifyPassword")
    public WebAsyncTask<Object> modifyPassword(@RequestBody @Validated UserModifyPasswordDTO dto,
                                               @Token User user){
        return new WebAsyncTask<>(()->{
            userService.modifyPassword(dto,user);
            return JsonResult.ok();
        });
    }

    @PostMapping("/resetPassword")
    public WebAsyncTask<Object> resetPassword(@RequestBody @Validated UserResetPasswordDTO dto){
        return new WebAsyncTask<>(()->{
            userService.resetPassword(dto);
            return JsonResult.ok();
        });
    }

    @GetMapping("/getInfo/{uid}")
    public WebAsyncTask<Object> getInfo(@PathVariable Integer uid,@Token User user){
        return new WebAsyncTask<>(()-> JsonResult.ok(userService.getInfo(uid,user)));
    }

    @PostMapping("/uploadAvatar")
    public WebAsyncTask<Object> UploadAvatar(@RequestParam("avatar") MultipartFile avatar, @Token User user){
        return new WebAsyncTask<>(()->{
            userService.UploadAvatar(avatar,user);
            return JsonResult.ok();
        });
    }

    @PostMapping("/uploadCertificate")
    public WebAsyncTask<Object> UploadCertifcate(String name,@RequestParam("certificate") MultipartFile certificate,
                                                 @Token User user){
        return new WebAsyncTask<>(()->{
            userService.UploadCertifcate(certificate,name,user);
            return JsonResult.ok();
        });
    }

    @GetMapping("/getCertificates/{uid}")
    public WebAsyncTask<Object> getCertificates(@PathVariable Integer uid){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(userService.getCertificates(uid));
        });
    }

    @DeleteMapping("/deleteCertificate/{id}")
    public WebAsyncTask<Object> DeleteCertificates(@PathVariable Integer id,@Token User user){
        return new WebAsyncTask<>(()->{
            return JsonResult.ok(userService.DeleteCertificates(id,user));
        });
    }

}
