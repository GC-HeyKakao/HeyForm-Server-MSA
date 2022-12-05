package heykakao.HeyForm.controller;

import heykakao.HeyForm.model.User;
import heykakao.HeyForm.model.dto.UserDto;
import heykakao.HeyForm.repository.UserRepository;
import heykakao.HeyForm.service.DtoService;
import heykakao.HeyForm.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DtoService dtoService;

    UserDto userDto;

    @Autowired
    KakaoService kakaoService;

    @GetMapping("/test")
    public String test(){
        return "test connection";
    }

    @GetMapping("/all")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("/id/{userEmail}")
//    @ApiOperation(value = "사용자 id 조회", notes = "사용자의 이메일을 반환한다.")
    public Long getIdByEmail(@PathVariable String userEmail){
        return dtoService.getIdByEmail(userEmail);
    }

    @PostMapping("/token/request")
//    @ApiOperation(value = "유저 토큰 요청", notes = "유저 토큰 요청")
    public User getToken(@RequestParam String Kakaotoken){
        kakaoService.getInfoByKakaoToken(Kakaotoken);
        return dtoService.deleteJWTToken(Kakaotoken);
    }

    @GetMapping("/token/service/{userToken}")
    public String getTokenByService(@PathVariable String userToken){
        return String.valueOf(dtoService.getUserIdByToken(userToken));
    }

    @GetMapping("/info/{userId}")
    public String getInfoByUserId(@PathVariable Long userId){
        return dtoService.getUserInfoByUserId(userId);
    }


}

