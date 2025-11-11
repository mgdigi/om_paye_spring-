package com.mgdev.om_paye.mapper;

import com.mgdev.om_paye.dto.request.UserRequestDto;
import com.mgdev.om_paye.dto.response.UserResponseDto;
import com.mgdev.om_paye.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toDto(User user);

    @Mapping(target = "role", expression = "java(com.mgdev.om_paye.enums.UserRole.valueOf(request.getRole()))")
    @Mapping(target = "status", expression = "java(com.mgdev.om_paye.enums.UserStatus.ACTIVE)")
    @Mapping(target = "comptes", ignore = true)
    User toEntity(UserRequestDto request);
}