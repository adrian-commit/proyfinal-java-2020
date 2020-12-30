package com.example.ProjectoFinalInfo2020.controller;

import com.example.ProjectoFinalInfo2020.entity.Usuario;
import com.example.ProjectoFinalInfo2020.repository.UsuarioRepository;
import com.example.ProjectoFinalInfo2020.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuario")

public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    //Dar de alta nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario)); 
    }

    //Consultar todos los usuarios de la tabla
    @GetMapping()
    public ResponseEntity<?> getUsuario() {
        return new ResponseEntity<>(usuarioService.findAll(), HttpStatus.OK); 
    }

    //Eliminar usuario por id
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deleteUsuario (@PathVariable Long usuarioId) {
        usuarioService.deleteById(usuarioId);
        return ResponseEntity.ok().build(); 
    }

    //Modificar usuario por id
    @PutMapping("/{usuarioId}")
    public ResponseEntity<?> editUsuario (@RequestBody Usuario usuarioDetails, @PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.findById(usuarioId);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.get().setNombre(usuarioDetails.getNombre());
        usuario.get().setApellido(usuarioDetails.getApellido());
        usuario.get().setEmail(usuarioDetails.getEmail());
        usuario.get().setPassword(usuarioDetails.getPassword());
        usuario.get().setCiudad(usuarioDetails.getCiudad());
        usuario.get().setProvincia(usuarioDetails.getProvincia());
        usuario.get().setPais(usuarioDetails.getPais());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario.get())); 
    }

    //Consulta usuarios por ciudad
    @GetMapping("/filtra") // ~ /api/v1/usuario/filtra?ciudad=Resistencia
    public ResponseEntity<?> buscarUsuariosPorCiudad (@RequestParam String ciudad) {
        List<Usuario> usuario = usuarioService.findByCiudad(ciudad);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    //Consulta todos los usuarios creados despues de una fecha dada
    @GetMapping("/filtro") // ~ /api/v1/usuario/filtro?fecha=2020/12/28
    public ResponseEntity<?> buscarUsuariosPorFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Usuario> usuario = usuarioService.findByFechaIsAfter(fecha);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

}
