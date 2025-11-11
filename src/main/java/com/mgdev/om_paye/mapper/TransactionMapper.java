package com.mgdev.om_paye.mapper;

import com.mgdev.om_paye.dto.request.PaiementRequestDto;
import com.mgdev.om_paye.dto.request.TransfertRequestDto;
import com.mgdev.om_paye.dto.response.TransactionResponseDto;
import com.mgdev.om_paye.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "numeroCompteExpediteur", source = "compteExpediteur.numeroCompte")
    @Mapping(target = "numeroCompteDestinataire", expression = "java(transaction.getCompteDestinataire() != null ? transaction.getCompteDestinataire().getNumeroCompte() : null)")
    @Mapping(target = "creationDate", source = "creationDate")
    TransactionResponseDto toDto(Transaction transaction);

    @Mapping(target = "referenceTransaction", ignore = true) // Généré automatiquement
    @Mapping(target = "montantTransaction", source = "montant")
    @Mapping(target = "typeTransaction", ignore = true) // Défini dans le service
    @Mapping(target = "status", ignore = true) // Défini dans le service
    @Mapping(target = "frais", ignore = true) // Défini dans le service
    @Mapping(target = "compteExpediteur", ignore = true) // Défini dans le service
    @Mapping(target = "compteDestinataire", ignore = true) // Défini dans le service
    Transaction transfertToEntity(TransfertRequestDto request);

    @Mapping(target = "referenceTransaction", ignore = true) // Généré automatiquement
    @Mapping(target = "montantTransaction", source = "montant")
    @Mapping(target = "typeTransaction", ignore = true) // Défini dans le service
    @Mapping(target = "status", ignore = true) // Défini dans le service
    @Mapping(target = "frais", ignore = true) // Défini dans le service
    @Mapping(target = "compteExpediteur", ignore = true) // Défini dans le service
    @Mapping(target = "compteDestinataire", ignore = true) // Défini dans le service
    Transaction paiementToEntity(PaiementRequestDto request);
}