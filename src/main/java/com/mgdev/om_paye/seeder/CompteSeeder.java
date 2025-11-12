package com.mgdev.om_paye.seeder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.entity.Client;
import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.Marchand;
import com.mgdev.om_paye.enums.CompteStatus;
import com.mgdev.om_paye.enums.TypeCompte;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class CompteSeeder implements CommandLineRunner {

    private final CompteRepository compteRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (compteRepository.count() > 0) {
            log.info("Comptes already seeded, skipping...");
            return;
        }

        log.info("Seeding comptes...");

        // Récupérer les utilisateurs créés
        List<Client> clients = userRepository.findAll().stream()
            .filter(user -> user instanceof Client)
            .map(user -> (Client) user)
            .toList();

        List<Marchand> marchands = userRepository.findAll().stream()
            .filter(user -> user instanceof Marchand)
            .map(user -> (Marchand) user)
            .toList();

        // Créer des comptes personnels pour les clients
        List<Compte> comptesPersonnels = clients.stream()
            .map(client -> Compte.builder()
                .numeroCompte(generateNumeroCompte())
                .titulaireCompte(client.getName())
                .solde(BigDecimal.valueOf(new Random().nextInt(50000) + 10000)) // Solde entre 10k et 60k
                .typeCompte(TypeCompte.PERSONNEL)
                .status(CompteStatus.ACTIVE)
                .user(client)
                .build())
            .toList();

        // Créer des comptes marchands pour les marchands
        List<Compte> comptesMarchands = marchands.stream()
            .map(marchand -> {
                Compte compte = Compte.builder()
                    .numeroCompte(generateNumeroCompte())
                    .titulaireCompte(marchand.getBusinessName())
                    .solde(BigDecimal.valueOf(new Random().nextInt(100000) + 50000)) // Solde entre 50k et 150k
                    .typeCompte(TypeCompte.MARCHAND)
                    .status(CompteStatus.PENDING)
                    .user(marchand)
                    .build();
                return compte;
            })
            .collect(java.util.stream.Collectors.toList());

        // Sauvegarder tous les comptes
        compteRepository.saveAll(comptesPersonnels);
        compteRepository.saveAll(comptesMarchands);

        log.info("Comptes seeded successfully:");
        log.info("- {} comptes personnels créés", comptesPersonnels.size());
        log.info("- {} comptes marchands créés", comptesMarchands.size());

        // Afficher les détails des comptes
        comptesPersonnels.forEach(compte ->
            log.info("Compte Personnel: {} - Numéro: {} - Solde: {} FCFA",
                compte.getTitulaireCompte(), compte.getNumeroCompte(), compte.getSolde())
        );

        comptesMarchands.forEach(compte ->
            log.info("Compte Marchand: {} - Numéro: {} - Solde: {} FCFA",
                compte.getTitulaireCompte(), compte.getNumeroCompte(), compte.getSolde())
        );
    }

    private String generateNumeroCompte() {
        String numero;
        do {
            numero = "221" + String.format("%09d", new Random().nextInt(1000000000));
        } while (compteRepository.existsByNumeroCompte(numero));
        return numero;
    }

    private String generateCodeMarchand() {
        return "MARCH" + String.format("%04d", new Random().nextInt(10000));
    }
}