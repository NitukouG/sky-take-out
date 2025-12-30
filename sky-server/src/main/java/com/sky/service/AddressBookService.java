package com.sky.service;


import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void addAddressBook(AddressBook addressBook);

    List<AddressBook> getAddressList();

    AddressBook getDefaultAddressList();

    void setDefaultAddress(AddressBook addressBook);

    void update(AddressBook addressBook);

    AddressBook getAddress(Long id);
}
