package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    public void addAddressBook(AddressBook addressBook) {
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);
        addressBook.setIsDefault(0);

        addressBookMapper.add(addressBook);

    }

    @Override
    public List<AddressBook> getAddressList() {
        Long currentId = BaseContext.getCurrentId();
        return addressBookMapper.list(currentId);
    }

    @Override
    public AddressBook getDefaultAddressList() {
        Long currentId = BaseContext.getCurrentId();
        return addressBookMapper.defaultAddress(currentId);
    }

//    设置默认地址
    @Transactional
    @Override
    public void setDefaultAddress(AddressBook addressBook) {
//        设置默认地址之前 需要取消其他默认地址
        Long currentId = BaseContext.getCurrentId();
        addressBook.setUserId(currentId);
        addressBookMapper.cancelDefault(addressBook);
//        之后再设置当前地址为默认地址
        addressBook.setIsDefault(1);

        addressBookMapper.update(addressBook);

    }

    @Override
    public void update(AddressBook addressBook) {
        Long currentId = BaseContext.getCurrentId();
        Long userId = addressBook.getUserId();
        if (userId == null || userId != currentId){
            throw new AddressBookBusinessException("????");
        }

        addressBookMapper.update(addressBook);
    }

    @Override
    public AddressBook getAddress(Long id) {
        Long currentId = BaseContext.getCurrentId();
        AddressBook build = AddressBook.builder().id(id).userId(currentId).build();
        AddressBook address = addressBookMapper.getAddress(build);
        return address;
    }
}
