package com.everis.savingaccount.model;

import com.everis.savingaccount.consumer.*;
import java.util.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "saving-account")
public class savingAccount {
  @Id
  private String idSavingAccount;

  private String accountNumber = webclient.logic
    .get()
    .uri("/generatedNumberLong/12")
    .retrieve()
    .bodyToMono(String.class)
    .block();
  private String dateCreated = new Date().toString();
  private double amount = 0.0;
  private List<movements> movements = new ArrayList<movements>();

  @NotBlank(message = "Debe seleccionar un cliente.")
  private String idCustomer;

  public savingAccount(String idCustomer) {
    this.idCustomer = idCustomer;
  }
}
