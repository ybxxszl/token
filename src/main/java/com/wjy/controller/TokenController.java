package com.wjy.controller;

import com.wjy.result.JSONResult;
import com.wjy.token.JWT;
import com.wjy.util.URLUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class TokenController {

    @GetMapping(value = "/getToken")
    public JSONResult getToken(HttpServletRequest request, long mills) {

        try {

            Map<String, Object> map = URLUtil.getParamsByParam(request.getQueryString());

            map.remove("mills");

            return JSONResult.ok(JWT.createJWT(map, mills));

        } catch (Exception e) {

            return JSONResult.errorException(e.getMessage());

        }

    }

    @GetMapping(value = "/refreshToken")
    public JSONResult refreshToken(String token, long mills) {

        try {

            return JSONResult.ok(JWT.refreshJWT(token, mills));

        } catch (Exception e) {

            return JSONResult.errorException(e.getMessage());

        }

    }

    @GetMapping(value = "/verifyToken")
    public JSONResult verifyToken(String token, boolean returnValue) {

        try {

            Map<String, Object> map = JWT.verifyJWT(token);

            if (returnValue) {

                return JSONResult.ok(map);

            } else {

                return JSONResult.ok();

            }

        } catch (Exception e) {

            return JSONResult.errorException(e.getMessage());

        }

    }

}
