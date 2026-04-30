    package sptech.school.backend.entity;

    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.persistence.*;
    import java.time.LocalDate;

    @Entity
    @Table(name = "usuario")
    @Schema(name = "Usuario", description = "Representa um usuário do sistema")
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Schema(description = "ID do usuário", example = "1")
        private Long id;

        @Schema(example = "Felipe")
        private String nome;

        private String telefone;
        private String email;
        private String senha;

        @Schema(example = "2000-05-10")
        @Column(name = "data_nascimento")
        private LocalDate dataNascimento;

        private Boolean ativo;

        @ManyToOne(optional = false)
        @JoinColumn(name = "fkEndereco", nullable = false)
        private Endereco endereco;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public LocalDate getDataNascimento() {
            return dataNascimento;
        }

        public void setDataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
        }

        public Boolean getAtivo() {
            return ativo;
        }

        public void setAtivo(Boolean ativo) {
            this.ativo = ativo;
        }

        public Endereco getEndereco() {
            return endereco;
        }

        public void setEndereco(Endereco endereco) {
            this.endereco = endereco;
        }
    }
