package com.example.apisessoes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apisessoes.dto.InformationDTO;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.services.InformationService;


@RestController
@RequestMapping(value = "/information")
public class InformationController {
	
	@Autowired	
	private InformationService service_information;	
	
	@PostMapping
	  public ResponseEntity<Information> criarProduto(@RequestBody InformationDTO dto) {
		Information novoProduto = service_information.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }
	
	 @GetMapping
	 private ResponseEntity<List<Information >> getInfo(){
	       List<Information> list = service_information.getAllInformations();
	       return ResponseEntity.ok(list);
	 }
}
