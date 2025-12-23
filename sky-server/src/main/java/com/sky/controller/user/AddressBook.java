package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/addressBook")
@Api("地址铺相关接口")
@Slf4j
public class AddressBook {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping
    public Result add(AddressBook addressBook){
        return Result.success();
    }

}
