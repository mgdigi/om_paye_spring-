package com.mgdev.om_paye.service;
import com.mgdev.om_paye.dto.response.UserResponseDto;
import com.mgdev.om_paye.dto.request.UserRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;


public interface IUserService {
    
    Page <UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto createUser(UserRequestDto request);
    UserResponseDto getUserById(UUID id);

}
