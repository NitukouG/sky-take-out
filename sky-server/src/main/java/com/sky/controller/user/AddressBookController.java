package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api("地址铺相关接口")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping
    public Result add(@RequestBody AddressBook addressBook){
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }


//    查询当前用户地址所有地址
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        List<AddressBook> addressBooks = addressBookService.getAddressList();
        return Result.success(addressBooks);
    }

//    查询默认地址

    @GetMapping("/default")
    public Result<AddressBook> defaultAddress(){
        AddressBook addressBook = addressBookService.getDefaultAddressList();
        return Result.success(addressBook);
    }

//    设置默认地址
    @PutMapping("/default")
    public Result setAddress(@RequestBody AddressBook addressBook){
        addressBookService.setDefaultAddress(addressBook);
        return Result.success();
    }

    @PutMapping
    public Result updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getAddress(@PathVariable Long id){
        AddressBook address = addressBookService.getAddress(id);
        return Result.success(address);
    }




}
