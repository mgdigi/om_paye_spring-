package com.mgdev.om_paye.mapper;

import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.entity.Compte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    CompteMapper INSTANCE = Mappers.getMapper(CompteMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "creationDate", source = "creationDate")
    CompteResponseDto toDto(Compte compte);

    @Mapping(target = "numeroCompte", ignore = true) // Généré automatiquement
    @Mapping(target = "titulaireCompte", ignore = true) // Défini dans le service
    @Mapping(target = "solde", ignore = true) // Défini dans le service
    @Mapping(target = "status", ignore = true) // Défini dans le service
    @Mapping(target = "user", ignore = true) // Défini dans le service
    @Mapping(target = "transactionsEmises", ignore = true)
    @Mapping(target = "transactionsRecues", ignore = true)
    Compte toEntity(CompteRequestDto request);
}