package sptech.school.backend.controller;

import static sptech.school.backend.config.SecurityConstants.COOKIE_NOME;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.UsuarioDto.UsuarioCriacaoDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioLoginDto;
import sptech.school.backend.dto.UsuarioDto.UsuarioTokenDto;
import sptech.school.backend.service.UsuarioService;

import java.time.Duration;

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

        adicionarCookieAutenticacao(response, tokenDto.getToken(), Duration.ofHours(1));

        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "Logout do usuario", description = "Remove o cookie de autenticacao do usuario.")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        adicionarCookieAutenticacao(response, "", Duration.ZERO);

        return ResponseEntity.ok().build();
    }

    private void adicionarCookieAutenticacao(HttpServletResponse response, String token, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(maxAge)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
