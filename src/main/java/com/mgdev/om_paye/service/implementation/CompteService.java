package com.mgdev.om_paye.service.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgdev.om_paye.dto.request.CompteRequestDto;
import com.mgdev.om_paye.dto.response.CompteResponseDto;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.User;
import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.TypeCompte;
import com.mgdev.om_paye.enums.UserRole;
import com.mgdev.om_paye.enums.UserStatus;
import com.mgdev.om_paye.event.CompteCreatedEvent;
import com.mgdev.om_paye.mapper.CompteMapper;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.TransactionRepository;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.validators.CompteValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteService {

    private final CompteRepository compteRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CompteMapper compteMapper;
    private final CompteValidator compteValidator;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    public CompteResponseDto createCompte(CompteRequestDto request) {
     

        compteValidator.validateCompte(request);

        User user;

        if (request.getPhoneNumber() != null) {
            user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElse(null);
        } else if (request.getEmail() != null) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElse(null);
        } else {
            user = null;
        }

        if (user == null) {
            user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .nci(request.getNci())
                    .phoneNumber(request.getPhoneNumber())
                    .password(request.getPassword())
                    .role(UserRole.CLIENT)
                    .status(UserStatus.ACTIVE)
                    .build();
            user = userRepository.save(user);
        }

        TypeCompte typeCompte = TypeCompte.valueOf(request.getTypeCompte());

        Compte compte = compteMapper.toEntity(request);
        compte.setNumeroCompte(generateNumeroCompte());
        compte.setTitulaireCompte(user.getName());
        compte.setSolde(BigDecimal.ZERO);
        compte.setTypeCompte(typeCompte);
        compte.setStatus(CompteStatus.ACTIVE);
        compte.setUser(user);

        Compte savedCompte = compteRepository.save(compte);

      
        eventPublisher.publishEvent(new CompteCreatedEvent(this, savedCompte));

        return compteMapper.toDto(savedCompte);
    }

    public CompteResponseDto getCompteById(UUID id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));
        return mapToResponseDto(compte);
    }

    public CompteResponseDto getCompteByNumero(String numeroCompte) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));
        return mapToResponseDto(compte);
    }

    public List<CompteResponseDto> getComptesByUserId(UUID userId) {
        return compteRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateSolde(UUID compteId) {
        BigDecimal credits = transactionRepository.sumCredits(compteId);
        BigDecimal debits = transactionRepository.sumDebits(compteId);
        return credits.subtract(debits);
    }

    private String generateNumeroCompte() {
        String numero;
        do {
            numero = "221" + String.format("%09d", new Random().nextInt(1000000000));
        } while (compteRepository.existsByNumeroCompte(numero));
        return numero;
    }


    private CompteResponseDto mapToResponseDto(Compte compte) {
        CompteResponseDto dto = compteMapper.toDto(compte);
        dto.setSolde(calculateSolde(compte.getId()));
        return dto;
    }
    
}