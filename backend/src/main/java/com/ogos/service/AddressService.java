package com.ogos.service;

import com.ogos.dto.AddressRequest;
import com.ogos.dto.AddressResponse;
import com.ogos.entity.User;

import java.util.List;

public interface AddressService {

    List<AddressResponse> getUserAddresses(User user);

    AddressResponse getAddress(User user, Long id);

    AddressResponse createAddress(User user, AddressRequest request);

    AddressResponse updateAddress(User user, Long id, AddressRequest request);

    void deleteAddress(User user, Long id);
}
