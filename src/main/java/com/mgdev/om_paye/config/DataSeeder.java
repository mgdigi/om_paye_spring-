package com.mgdev.om_paye.config;

import com.mgdev.om_paye.entity.Admin;
import com.mgdev.om_paye.entity.Client;
import com.mgdev.om_paye.entity.Marchand;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.service.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initialisation des données par défaut...");

            
            // Création d'un admin par défaut
            Admin admin = new Admin();
            admin.setName("Mohamed Gueye");
            admin.setEmail("admin@ompaye.sn");
            admin.setNci("1234567890123");
            admin.setPhoneNumber("771234567");
            admin.setPassword(passwordEncoder.encode("Admin123"));
            admin.setAdminCode(codeGeneratorService.generateAdminCode());
            userRepository.save(admin);
            log.info("Admin créé: {} avec code {}", admin.getEmail(), admin.getAdminCode());

            // Création d'un client exemple
            Client client = new Client();
            client.setName("Client Exemple");
            client.setEmail("client@ompaye.sn");
            client.setNci("9876543210987");
            client.setPhoneNumber("782345678");
            client.setPassword(passwordEncoder.encode("Client123"));
            client.setAdresse("Dakar, Sénégal");
            userRepository.save(client);
            log.info("Client créé: {}", client.getEmail());

            // Création d'un marchand exemple
            Marchand marchand = new Marchand();
            marchand.setName("Marchand Exemple");
            marchand.setEmail("marchand@ompaye.sn");
            marchand.setNci("4567890123456");
            marchand.setPhoneNumber("765432109");
            marchand.setPassword(passwordEncoder.encode("Marchand123"));
            marchand.setBusinessName("Boutique Exemple");
            marchand.setCodeMarchand(codeGeneratorService.generateMerchantCode());
            userRepository.save(marchand);
            log.info("Marchand créé: {} avec code {}", marchand.getEmail(), marchand.getCodeMarchand());

            log.info("Initialisation terminée avec succès !");
        } else {
            log.info("Les données existent déjà, skipping initialisation.");
        }
    }
}