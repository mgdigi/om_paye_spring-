package com.mgdev.om_paye.seeder;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.entity.Admin;
import com.mgdev.om_paye.entity.Client;
import com.mgdev.om_paye.entity.Marchand;
import com.mgdev.om_paye.enums.UserRole;
import com.mgdev.om_paye.enums.UserStatus;
import com.mgdev.om_paye.repository.UserRepository;
import com.mgdev.om_paye.service.CodeGeneratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Users already seeded, skipping...");
            return;
        }

        log.info("Seeding users...");

        
        List<Admin> admins = Arrays.asList(
            createAdmin("Mohamed Gueye", "admin@ompaye.sn", "1234567890123", "771234567"),
            createAdmin("Fatou Diop", "admin2@ompaye.sn", "9876543210987", "782345678")
        );

      
        List<Client> clients = Arrays.asList(
            createClient("Client Test 2", "seydamariama206@gmail.com", "2222222222222", "git log --oneline
", "Thiès, Centre"),
            createClient("Client Test 3", "ndiayeseydinamohamed43@gmail.com", "3333333333333", "785678901", "Saint-Louis, Centre")
        );


      
        List<Marchand> marchands = Arrays.asList(
            createMarchand("Marchand Électronique", "marchand1@ompaye.sn", "4444444444444", "786789012", "TechShop Dakar"),
            createMarchand("Marchand Alimentaire", "marchand2@ompaye.sn", "5555555555555", "787890123", "SuperMarché Express"),
            createMarchand("Marchand Service", "marchand3@ompaye.sn", "6666666666666", "788901234", "Service Plus")
        );

        
        userRepository.saveAll(admins);
        userRepository.saveAll(clients);
        userRepository.saveAll(marchands);

        log.info("Users seeded successfully:");
        log.info("- {} admins created", admins.size());
        log.info("- {} clients created", clients.size());
        log.info("- {} marchands created", marchands.size());

    
        admins.forEach(admin ->
            log.info("Admin: {} - Email: {} - Code: {}", admin.getName(), admin.getEmail(), admin.getAdminCode())
        );
    }

    private Admin createAdmin(String name, String email, String nci, String phoneNumber) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setNci(nci);
        admin.setPhoneNumber(phoneNumber);
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setAdminCode(codeGeneratorService.generateAdminCode());
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);
        return admin;
    }

    private Client createClient(String name, String email, String nci, String phoneNumber, String adresse) {
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setNci(nci);
        client.setPhoneNumber(phoneNumber);
        client.setPassword(passwordEncoder.encode("Client123"));
        client.setAdresse(adresse);
        client.setRole(UserRole.CLIENT);
        client.setStatus(UserStatus.ACTIVE);
        return client;
    }

    private Marchand createMarchand(String name, String email, String nci, String phoneNumber, String businessName) {
        Marchand marchand = new Marchand();
        marchand.setName(name);
        marchand.setEmail(email);
        marchand.setNci(nci);
        marchand.setPhoneNumber(phoneNumber);
        marchand.setPassword(passwordEncoder.encode("Marchand123"));
        marchand.setBusinessName(businessName);
        marchand.setCodeMarchand(codeGeneratorService.generateMerchantCode());
        marchand.setRole(UserRole.MARCHAND);
        marchand.setStatus(UserStatus.ACTIVE);
        return marchand;
    }
}