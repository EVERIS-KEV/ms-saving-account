package com.everis.savingaccount.service;

import com.everis.savingaccount.consumer.webclient;
import com.everis.savingaccount.dto.message;
import com.everis.savingaccount.map.customer;
import com.everis.savingaccount.model.*;
import com.everis.savingaccount.repository.savingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.*;

@Service
@Transactional
public class savingAccountService {
  @Autowired
  savingAccountRepository repository;

  static final int LIMIT_MOVEMENT = 5;

  private Boolean verifyCustomer(String id) {
    return webclient.customer
      .get()
      .uri("/verifyId/{id}", id)
      .retrieve()
      .bodyToMono(Boolean.class)
      .block();
  }

  private customer customerFind(String id) {
    return webclient.customer
      .get()
      .uri("/{id}", id)
      .retrieve()
      .bodyToMono(customer.class)
      .block();
  }

  private double getAmountByNumber(String number) {
    return repository.findByAccountNumber(number).getAmount();
  }

  private String setAmount(movements movement) {
    double val = getAmountByNumber(movement.getAccountNumber());
    savingAccount model = repository.findByAccountNumber(movement.getAccountNumber());

    if (movement.getType().equals("Deposito")) {
      model.setAmount(movement.getAmount() + val);
      model.getMovements().add(movement);
    } else {
      if (movement.getAmount() > val) return "Cantidad insuficiente."; else {
        model.setAmount(val - movement.getAmount());
        model.getMovements().add(movement);
      }
    }

    repository.save(model);
    return "Movimiento realizado";
  }

  public Mono<Object> save(savingAccount model) {
    String msg = "Cuenta creada.";

    if (verifyCustomer(model.getIdCustomer())) {
      String typeCustomer = customerFind(model.getIdCustomer()).getType();

      if (typeCustomer.equals("personal")) {
        if (!repository.existsByIdCustomer(model.getIdCustomer())) repository.save(
          model
        ); else msg = "Usted ya no puede tener mas cuentas de ahorro.";
      } else msg = "Las cuentas empresariales no deben tener cuentas de ahorro.";
    } else msg = "Cliente no registrado";

    return Mono.just(new message(msg));
  }

  public Mono<Object> saveMovements(movements model) {
    String msg = "Movimiento realizado";
    int movementcant = (int) repository
      .findByAccountNumber(model.getAccountNumber())
      .getMovements()
      .size();

    if (movementcant < LIMIT_MOVEMENT) {
      if (repository.existsByAccountNumber(model.getAccountNumber())) {
        if (model.getType().equals("Deposito") || model.getType().equals("Retiro")) msg =
          setAmount(model); else msg = "Selecione una operacion correcta.";
      } else msg = "Numero de cuenta incorrecto.";
    } else msg = "Llego a su limite de movimientos.";

    return Mono.just(new message(msg));
  }

  public Flux<Object> getAll() {
    return Flux.fromIterable(repository.findAll());
  }

  public Mono<Object> getOne(String id) {
    return Mono.just(repository.findByAccountNumber(id));
  }

  public Flux<Object> getByCustomer(String id) {
    return Flux.fromIterable(
      repository.findAll().stream().filter(c -> c.getIdCustomer().equals(id)).toList()
    );
  }
}
