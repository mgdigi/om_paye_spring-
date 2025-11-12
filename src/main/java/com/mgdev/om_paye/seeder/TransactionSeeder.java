package com.mgdev.om_paye.seeder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.entity.Compte;
import com.mgdev.om_paye.entity.Transaction;
import com.mgdev.om_paye.enums.TransactionStatus;
import com.mgdev.om_paye.enums.TypeTransaction;
import com.mgdev.om_paye.repository.CompteRepository;
import com.mgdev.om_paye.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(4)
public class TransactionSeeder implements CommandLineRunner {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    @Override
    public void run(String... args) throws Exception {
        if (transactionRepository.count() > 0) {
            log.info("Transactions already seeded, skipping...");
            return;
        }

        log.info("Seeding transactions...");

        // Récupérer tous les comptes
        List<Compte> comptes = compteRepository.findAll();
        if (comptes.isEmpty()) {
            log.warn("No comptes found, skipping transaction seeding");
            return;
        }

        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();

        // Créer des transferts entre comptes
        for (int i = 0; i < 10; i++) {
            Compte expediteur = comptes.get(random.nextInt(comptes.size()));
            Compte destinataire;
            do {
                destinataire = comptes.get(random.nextInt(comptes.size()));
            } while (destinataire.getId().equals(expediteur.getId()));

            BigDecimal montant = BigDecimal.valueOf(random.nextInt(50000) + 1000); 

            Transaction transfert = new Transaction();
            transfert.setReferenceTransaction(generateReference());
            transfert.setMontantTransaction(montant);
            transfert.setFrais(BigDecimal.valueOf(random.nextInt(500) + 100)); // 100 à 600 FCFA de frais
            transfert.setTypeTransaction(TypeTransaction.TRANSFERT);
            transfert.setStatus(TransactionStatus.COMPLETED);
            transfert.setDescription("Transfert test");
            transfert.setCompteExpediteur(expediteur);
            transfert.setCompteDestinataire(destinataire);

            transactions.add(transfert);
        }

        // Créer des paiements aux marchands
        List<Compte> comptesMarchands = comptes.stream()
            .filter(c -> c.getTypeCompte().name().equals("MARCHAND"))
            .toList();

        List<Compte> comptesClients = comptes.stream()
            .filter(c -> c.getTypeCompte().name().equals("PERSONNEL"))
            .toList();

        if (!comptesMarchands.isEmpty() && !comptesClients.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                Compte client = comptesClients.get(random.nextInt(comptesClients.size()));
                Compte marchand = comptesMarchands.get(random.nextInt(comptesMarchands.size()));

                BigDecimal montant = BigDecimal.valueOf(random.nextInt(20000) + 500); // 500 à 20500

                Transaction paiement = new Transaction();
                paiement.setReferenceTransaction(generateReference());
                paiement.setMontantTransaction(montant);
                paiement.setFrais(BigDecimal.valueOf(random.nextInt(200) + 50)); // 50 à 250 FCFA de frais
                paiement.setTypeTransaction(TypeTransaction.PAIEMENT_MARCHAND);
                paiement.setStatus(TransactionStatus.COMPLETED);
                paiement.setDescription("Paiement marchand test");
                paiement.setCompteExpediteur(client);
                paiement.setCompteDestinataire(marchand);

                transactions.add(paiement);
            }
        }

        // Sauvegarder toutes les transactions
        transactionRepository.saveAll(transactions);

        log.info("Transactions seeded successfully:");
        log.info("- {} transactions créées", transactions.size());

        // Afficher quelques exemples
        transactions.stream().limit(5).forEach(transaction ->
            log.info("Transaction: {} - {} - {} FCFA - {}",
                transaction.getReferenceTransaction(),
                transaction.getTypeTransaction(),
                transaction.getMontantTransaction(),
                transaction.getStatus())
        );
    }

    private String generateReference() {
        return "TXN" + System.nanoTime() + new Random().nextInt(1000);
    }
}