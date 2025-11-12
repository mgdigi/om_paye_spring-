package com.mgdev.om_paye.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.entity.Compte;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    // CompteMapper INSTANCE = Mappers.getMapper(CompteMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "creationDate", source = "creationDate")
    CompteResponseDto toDto(Compte compte);

    @Mapping(target = "numeroCompte", ignore = true) 
    @Mapping(target = "titulaireCompte", ignore = true) 
    @Mapping(target = "solde", ignore = true)
    @Mapping(target = "status", ignore = true) 
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "transactionsEmises", ignore = true)
    @Mapping(target = "transactionsRecues", ignore = true)
    Compte toEntity(CompteRequestDto request);
}