package com.ogos.service.impl;

import com.ogos.dto.AddressRequest;
import com.ogos.dto.AddressResponse;
import com.ogos.entity.Address;
import com.ogos.entity.User;
import com.ogos.exception.ResourceNotFoundException;
import com.ogos.mapper.EntityMapper;
import com.ogos.repository.AddressRepository;
import com.ogos.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<AddressResponse> getUserAddresses(User user) {
        return addressRepository.findByUser(user).stream()
                .map(EntityMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddress(User user, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address", "id", id);
        }

        return EntityMapper.toAddressResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse createAddress(User user, AddressRequest request) {
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .user(user)
                .build();

        Address saved = addressRepository.save(address);
        return EntityMapper.toAddressResponse(saved);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(User user, Long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address", "id", id);
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        Address saved = addressRepository.save(address);
        return EntityMapper.toAddressResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAddress(User user, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Address", "id", id);
        }

        addressRepository.delete(address);
    }
}
