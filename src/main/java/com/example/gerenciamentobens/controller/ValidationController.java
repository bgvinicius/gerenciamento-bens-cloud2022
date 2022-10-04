package com.example.gerenciamentobens.controller;

import com.example.gerenciamentobens.entity.assets.AssetsRepository;
import com.example.gerenciamentobens.entity.validations.Validation;
import com.example.gerenciamentobens.entity.validations.ValidationDTO;
import com.example.gerenciamentobens.service.DynamoUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/validation")
public class ValidationController {

    @Autowired
    private DynamoUtilsService dynamoUtilsService;
    @Autowired
    private AssetsRepository assetsRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Validation> getValidation(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable("id") String id){

        Validation validation = dynamoUtilsService.getItem(id);

        if(assetsRepository.findByIdAndUserUsername(validation.getIdAsset(), userDetails.getUsername()).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log de validação não encontrado.");
        }

        return ResponseEntity.ok(validation);
    }

    @PostMapping("")
    public ResponseEntity<Validation> postValidation(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody @Valid ValidationDTO assetValidation){

        if(assetsRepository.findByIdAndUserUsername(assetValidation.getIdAsset(), userDetails.getUsername()).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bem associado à validação não encontrado.");
        }

        Validation newValidation = dynamoUtilsService.putItem(assetValidation.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newValidation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Validation> deleteValidation(@PathVariable("id") String id){
        dynamoUtilsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
