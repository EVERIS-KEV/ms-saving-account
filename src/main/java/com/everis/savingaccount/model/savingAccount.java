package com.everis.savingaccount.model;

import com.everis.savingaccount.consumer.*;
import java.util.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter 
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
  private Date dateCreated = new Date(); // Deposito Retiro Transferencia ComisiÃƒÆ’Ã‚Â³n
  private double amount = 0.0;
  private List<movements> movements = new ArrayList<movements>();
  private String profile;
  @NotBlank(message = "Debe seleccionar un cliente.")
  private String idCustomer;

  public savingAccount() {
    this.profile = ""; 
  }

  public savingAccount(String idCustomer) {
	    this.profile = "";
    this.idCustomer = idCustomer;
  }

  public savingAccount(String profile, String idCustomer) {
    this.profile = profile;
    this.idCustomer = idCustomer;
  }
}
