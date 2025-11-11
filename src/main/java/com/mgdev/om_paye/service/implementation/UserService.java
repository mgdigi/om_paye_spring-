package com.mgdev.om_paye.service.implementation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgdev.om_paye.dto.request.UserRequestDto;
import com.mgdev.om_paye.dto.response.UserResponseDto;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.enums.UserRole;
import com.mgdev.om_paye.enums.UserStatus;
import com.mgdev.om_paye.exception.ResourceNotFoundException;
import com.mgdev.om_paye.mapper.UserMapper;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.service.CodeGeneratorService;
import com.mgdev.om_paye.service.IUserService;
import com.mgdev.om_paye.validators.UserValidator;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        userValidator.validateUser(request);

        
        if ("ADMIN".equalsIgnoreCase(request.getRole()) && (request.getAdminCode() == null || request.getAdminCode().isEmpty())) {
            request.setAdminCode(codeGeneratorService.generateAdminCode());
        }

        if ("MARCHAND".equalsIgnoreCase(request.getRole()) && (request.getCodeMarchand() == null || request.getCodeMarchand().isEmpty())) {
            request.setCodeMarchand(codeGeneratorService.generateMerchantCode());
        }

        User user = userMapper.toEntity(request);

        
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
    return userMapper.toDto(user);
    }

   @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

}