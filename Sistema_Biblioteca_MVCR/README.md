# Sistema de Gestão de Biblioteca

![Java](https://img.shields.io/badge/Java-21-orange)
![Swing](https://img.shields.io/badge/Swing-GUI-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-5.6.15-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)

Sistema completo de gerenciamento de biblioteca desenvolvido em Java com interface gráfica Swing, seguindo a arquitetura MVCR (Model-View-Controller-Repository) e utilizando Hibernate ORM para persistência de dados.

## 📋 Descrição do Projeto

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de Linguagem de Programação III. O sistema permite gerenciar o acervo de livros, cadastro de usuários e controle de empréstimos de uma biblioteca, implementando regras de negócio como limite de empréstimos por usuário, controle de prazos e disponibilidade de exemplares.

## ✨ Funcionalidades

### 📚 Gerenciamento de Livros
- ✅ Cadastrar novos livros com informações completas
- ✅ Editar dados de livros existentes
- ✅ Excluir livros do acervo
- ✅ Buscar livros por título, autor ou tema
- ✅ Listar todos os livros cadastrados
- ✅ Visualizar livros disponíveis para empréstimo
- ✅ Controle de quantidade de exemplares

### 👥 Gerenciamento de Usuários
- ✅ Cadastrar novos usuários
- ✅ Editar dados de usuários existentes
- ✅ Excluir usuários do sistema
- ✅ Buscar usuários por nome, celular ou e-mail
- ✅ Listar todos os usuários cadastrados
- ✅ Validação de e-mail e celular com máscaras

### 📖 Gerenciamento de Empréstimos
- ✅ Registrar novos empréstimos
- ✅ Registrar devoluções de livros
- ✅ Listar empréstimos ativos
- ✅ Listar empréstimos atrasados
- ✅ Visualizar histórico de empréstimos por usuário
- ✅ Controle automático de prazos (14 dias)
- ✅ Limite de 5 empréstimos simultâneos por usuário
- ✅ Cálculo automático de dias de atraso

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21 (LTS)
- **Interface Gráfica**: Java Swing com IntelliJ GUI Designer (.form)
- **ORM**: Hibernate 5.6.15.Final
- **Banco de Dados**: MySQL 8.0
- **Driver JDBC**: MySQL Connector/J 8.0.33
- **Gerenciamento de Dependências**: Maven 3.x
- **IDE Recomendada**: IntelliJ IDEA (Community ou Ultimate)

## 🏗️ Arquitetura MVCR

O projeto segue a arquitetura MVCR, uma variação do MVC tradicional:

```
┌─────────────────────────────────────────────────┐
│                    VIEW                         │
│  (Formulários .form + Classes Java Swing)       │
│  - Principal, MenuLivro, CadastroLivro, etc.    │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│                 CONTROLLER                      │
│           (Lógica de Negócio)                   │
│  - LivroController                              │
│  - UsuarioController                            │
│  - EmprestimoController                         │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│                 REPOSITORY                      │
│          (Acesso a Dados - Hibernate)           │
│  - LivroRepository                              │
│  - UsuarioRepository                            │
│  - EmprestimoRepository                         │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│                   MODEL                         │
│            (Entidades JPA)                      │
│  - Livro, Usuario, Emprestimo                   │
└─────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│              BANCO DE DADOS                     │
│                MySQL 8.0                        │
└─────────────────────────────────────────────────┘
```

## 📁 Estrutura do Projeto

```
Sistema_Biblioteca_MVCR/
├── src/
│   └── main/
│       ├── java/
│       │   ├── controller/
│       │   │   ├── LivroController.java
│       │   │   ├── UsuarioController.java
│       │   │   └── EmprestimoController.java
│       │   ├── model/
│       │   │   ├── Livro.java
│       │   │   ├── Usuario.java
│       │   │   └── Emprestimo.java
│       │   ├── repository/
│       │   │   ├── JPAUtil.java
│       │   │   ├── LivroRepository.java
│       │   │   ├── UsuarioRepository.java
│       │   │   └── EmprestimoRepository.java
│       │   ├── view/
│       │   │   ├── Principal.form / Principal.java
│       │   │   ├── Livro/
│       │   │   │   ├── MenuLivro.form / MenuLivro.java
│       │   │   │   └── CadastroLivro.form / CadastroLivro.java
│       │   │   ├── Usuario/
│       │   │   │   ├── MenuUsuario.form / MenuUsuario.java
│       │   │   │   └── CadastroUsuario.form / CadastroUsuario.java
│       │   │   └── Emprestimo/
│       │   │       ├── MenuEmprestimo.form / MenuEmprestimo.java
│       │   │       └── RegistrarEmprestimo.form / RegistrarEmprestimo.java
│       │   └── Main.java
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
├── pom.xml
├── README.md
├── INSTALACAO.md
├── DOCUMENTACAO_TECNICA.md
├── database_setup.sql
└── .gitignore
```

## 🚀 Como Executar

### Pré-requisitos

1. **Java Development Kit (JDK) 21** ou superior
2. **IntelliJ IDEA** (Community ou Ultimate)
3. **MySQL Server 8.0** ou superior
4. **Maven** (geralmente incluído no IntelliJ)

### Passo a Passo

1. **Clone ou baixe o projeto**
   ```bash
   git clone <url-do-repositorio>
   cd Sistema_Biblioteca_MVCR
   ```

2. **Configure o banco de dados**
   - Inicie o MySQL Server
   - O banco será criado automaticamente pelo Hibernate
   - Ou execute manualmente: `mysql -u root -p < database_setup.sql`

3. **Configure as credenciais do MySQL**
   - Edite `src/main/resources/META-INF/persistence.xml`
   - Ajuste usuário e senha do MySQL:
   ```xml
   <property name="javax.persistence.jdbc.user" value="root"/>
   <property name="javax.persistence.jdbc.password" value="sua_senha"/>
   ```

4. **Abra o projeto no IntelliJ IDEA**
   - File → Open → Selecione a pasta do projeto
   - Aguarde o download das dependências Maven

5. **Configure o GUI Designer**
   - File → Settings → Editor → GUI Designer
   - Marque: "Generate GUI into: Java source code"

6. **Execute a aplicação**
   - Abra o arquivo `Main.java`
   - Clique com botão direito → Run 'Main.main()'

📖 **Para instruções detalhadas, consulte [INSTALACAO.md](INSTALACAO.md)**

## 📚 Documentação

- **[INSTALACAO.md](INSTALACAO.md)**: Guia completo de instalação e configuração
- **[DOCUMENTACAO_TECNICA.md](DOCUMENTACAO_TECNICA.md)**: Documentação técnica detalhada da arquitetura, camadas e regras de negócio
- **[database_setup.sql](database_setup.sql)**: Script SQL para criação manual do banco de dados

## 🎯 Regras de Negócio Implementadas

1. **RN01**: Um usuário pode ter no máximo **5 livros emprestados** simultaneamente
2. **RN02**: Um livro só pode ser emprestado se houver **exemplares disponíveis**
3. **RN03**: O prazo máximo de empréstimo é de **14 dias**
4. **RN04**: O sistema sinaliza automaticamente **empréstimos atrasados**
5. **RN05**: Ao emprestar, **decrementa** a quantidade disponível; ao devolver, **incrementa**
6. **RN06**: Não pode haver dois livros com o mesmo **ISBN**
7. **RN07**: Não pode haver dois usuários com o mesmo **e-mail**

## 🎨 Máscaras e Validações

- **Data**: Formato `dd/MM/yyyy` com máscara `##/##/####`
- **Celular**: Formato `(XX) XXXXX-XXXX` com máscara `(##) #####-####`
- **E-mail**: Validação com regex para formato válido
- **ISBN**: Aceita ISBN-10 (10 dígitos) ou ISBN-13 (13 dígitos)

## 🗄️ Banco de Dados

### Tabelas Principais

- **livros**: Armazena informações dos livros (título, autor, ISBN, quantidade, etc.)
- **usuarios**: Armazena dados dos usuários (nome, sexo, celular, e-mail)
- **emprestimos**: Registra empréstimos com relacionamentos para usuários e livros

### Relacionamentos

- `emprestimos.usuario_id` → `usuarios.id` (ManyToOne)
- `emprestimos.livro_id` → `livros.id` (ManyToOne)

## 🧪 Testando o Sistema

1. **Cadastre alguns livros** através do menu "Gerenciar Livros"
2. **Cadastre usuários** através do menu "Gerenciar Usuários"
3. **Registre empréstimos** selecionando usuário e livro disponível
4. **Teste a devolução** selecionando um empréstimo ativo
5. **Visualize empréstimos atrasados** (se houver)

## 🐛 Solução de Problemas

### Erro: "Communications link failure"
- Verifique se o MySQL está rodando
- Confirme as credenciais no `persistence.xml`

### Formulários .form não aparecem
- Vá em Settings → GUI Designer
- Marque "Generate GUI into: Java source code"
- Rebuild Project

### Erro: "No Persistence provider"
- Verifique se `persistence.xml` está em `src/main/resources/META-INF/`

## 👨‍💻 Autor

Projeto desenvolvido como trabalho acadêmico para a disciplina de **Linguagem de Programação III**.

## 📄 Licença

Este projeto é de uso acadêmico e educacional.

---

**Desenvolvido com ☕ Java e 💙 dedicação**
