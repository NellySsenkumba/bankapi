package org.nelly.bankapi.contollers;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

//    @PostMapping("admin-login/")
//    public ResponseEntity<JSONObject> adminLogin(@RequestBody JSONObject request) {
//        return new ResponseEntity<>(authService.adminLogin(request), HttpStatus.OK);
//    }

     @PostMapping("login/")
    public ResponseEntity<JSONObject> login(@RequestBody JSONObject request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
