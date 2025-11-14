package com.Surakuri.Controller;

import com.Surakuri.Response.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HomeController {

    @GetMapping
    public APIResponse HomeControllerHandler(){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage("WELCOME TO e-COMMERCE Hardware!");


        return apiResponse;

    }


}


