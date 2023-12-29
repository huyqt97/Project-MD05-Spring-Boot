package ra.web_shop_modun05.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.web_shop_modun05.exception.BlockUser;
import ra.web_shop_modun05.exception.LoginException;
import ra.web_shop_modun05.exception.RegisterException;
import ra.web_shop_modun05.model.dto.request.UserLoginReq;
import ra.web_shop_modun05.model.dto.request.UserRegisterReq;
import ra.web_shop_modun05.model.dto.response.UserRes;
import ra.web_shop_modun05.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String s = encoder.encode("admin");
        System.out.println("passWord :" + s);
    }
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterReq userRegisterReq) throws RegisterException {
        return new ResponseEntity<>(userService.register(userRegisterReq), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserRes>> all() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserRes> login(@Valid @RequestBody UserLoginReq userLoginReq) throws LoginException, BlockUser {

        return new ResponseEntity<>(userService.login(userLoginReq), HttpStatus.OK);
    }
}
