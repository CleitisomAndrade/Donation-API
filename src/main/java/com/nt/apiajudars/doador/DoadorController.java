package com.nt.apiajudars.doador;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("Doador")
public class DoadorController {

    @Autowired
    private DoadorRepository repository;

    @PostMapping
    public void criarDoador(@RequestBody CreateDoadorDTO data) {
        Doador doadorData = new Doador(data);
        repository.save(doadorData);
    }


    @GetMapping
    public List<DoadorResponseDTO> listarDoadores() {
        List<DoadorResponseDTO> doadores = repository.findAll().stream().map(DoadorResponseDTO::new).collect(Collectors.toList());
        return doadores;
    }

    @PutMapping("/{id}")
    public DoadorResponseDTO atualizarDoador(@PathVariable Long id, @RequestBody UpdateDoadorDTO data) {

        Optional<Doador> optionalDoador = repository.findById(id);

        Doador doador = optionalDoador.orElseThrow(() -> new EntityNotFoundException("Doador não existe"));

        if (data.nome() != null) {
            doador.setNome(data.nome());
        }
        if (data.instagram() != null) {
            doador.setInstagram(data.instagram());
        }
        Doador doadorAtualizado = repository.save(doador);
        DoadorResponseDTO doadorResponseDTO = new DoadorResponseDTO(doadorAtualizado);
        return doadorResponseDTO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirDoador(@PathVariable Long id) {
        try {
            Optional<Doador> optionalDoador = repository.findById(id);
            Doador doador = optionalDoador.orElseThrow(() -> new EntityNotFoundException("Doador não existe"));
            repository.delete(doador);
            return ResponseEntity.ok().body("Doador deletado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
