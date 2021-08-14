package com.everis.savingaccount.controller; 

import com.everis.savingaccount.dto.message;
import com.everis.savingaccount.model.*;
import com.everis.savingaccount.service.savingAccountService;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

@RestController
@CrossOrigin(
  origins = "*",
  methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE
  }
)
@RequestMapping
public class savingAccountController {
  @Autowired
  savingAccountService service;

  @PostMapping("/save")
  public Mono<Object> created(
    @RequestBody @Valid savingAccount model,
    BindingResult bindinResult
  ) {
    String msg = "";

    if (bindinResult.hasErrors()) {
      for (int i = 0; i < bindinResult.getAllErrors().size(); i++) msg =
        bindinResult.getAllErrors().get(0).getDefaultMessage();
      return Mono.just(new message(msg));
    }

    return service.save(model);
  }

  @PostMapping("/movememts")
  public Mono<Object> registedMovememts(
    @RequestBody @Valid movements model,
    BindingResult bindinResult
  ) {
    String msg = "";

    if (bindinResult.hasErrors()) {
      for (int i = 0; i < bindinResult.getAllErrors().size(); i++) msg =
        bindinResult.getAllErrors().get(0).getDefaultMessage();
      return Mono.just(new message(msg));
    }

    return service.saveMovements(model);
  }

  @GetMapping("/")
  public Flux<Object> findAll() {
    return service.getAll();
  }

  @GetMapping("/byNumberAccount/{number}")
  public Mono<Object> findOneByNumberAccount(@PathVariable("number") String number) {
    return service.getOne(number);
  }
  
  @GetMapping("/byCustomer/{id}")
  public Flux<Object> findByCustomer(@PathVariable("id") String id){
	  return service.getByCustomer(id);
  }

}
