package sptech.school.backend.controller;

import static sptech.school.backend.config.SecurityConstants.COOKIE_NOME;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.service.UsuarioService;

@Tag(name = "Autenticacao e Usuarios", description = "Cadastro, login e logout de usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Criar usuario", description = "Cria um novo usuario com dados pessoais e endereco.")
    @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @PostMapping
    public ResponseEntity<Void> criar(@Valid @RequestBody UsuarioCriacaoDto dto) {

        service.criar(dto);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Login do usuario", description = "Autentica credenciais e retorna token JWT no corpo e em cookie HTTP-only.")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Credenciais invalidas")
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(
            @Valid @RequestBody UsuarioLoginDto dto,
            HttpServletResponse response) {

        UsuarioTokenDto tokenDto = service.login(dto);

        Cookie cookie = new Cookie(COOKIE_NOME, tokenDto.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "Logout do usuario", description = "Remove o cookie de autenticacao do usuario.")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie(COOKIE_NOME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
