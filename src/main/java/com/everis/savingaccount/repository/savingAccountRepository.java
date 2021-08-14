package com.everis.savingaccount.repository;

import com.everis.savingaccount.model.savingAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface savingAccountRepository extends MongoRepository<savingAccount, String> {
  boolean existsByAccountNumber(String number);
  boolean existsByIdCustomer(String id);

  savingAccount findByAccountNumber(String number);
  savingAccount findByIdSavingAccount(String id);
}
