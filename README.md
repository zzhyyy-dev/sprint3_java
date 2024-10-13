### Recapitulação do Projeto até o Momento

O sistema acadêmico está em desenvolvimento utilizando o padrão **MVC** (Model-View-Controller). A interface web foi criada em **React**, e as APIs serão desenvolvidas com **Spring Boot** para permitir a interação com a camada Model, que utiliza Java com **JDBC** para manipulação direta do banco de dados Oracle. 

### Camada Model

A camada Model contém as representações das entidades do sistema e executa as operações CRUD (Create, Read, Update, Delete) diretamente no banco de dados. Seguindo a estrutura proposta, cada classe será descrita com suas funções, atributos e operações disponíveis.

#### Classe `Users`:
Função: Representa os usuários do sistema, responsáveis pela autenticação e gerenciamento básico de informações.

- **Atributos:**
  - `String userType` - Define o tipo de usuário (teacher, student, administrator).
  - `int userId` - Armazena o ID do usuário autenticado.

- **Operações:**
  - `+ boolean login(String userType, String email, String password)`
    - Autentica o usuário com base no email e senha fornecidos.
  - `+ void createUser(String userType, String name, String email, String password, Integer classId)`
    - Cria um novo usuário, seja professor ou aluno.
  - `+ void readUser(String userType, int id)`
    - Lê e exibe as informações de um usuário específico.
  - `+ void updateUser(String userType, int id, String newName, String newEmail, String newPassword)`
    - Atualiza os dados de um usuário existente.
  - `+ void deleteUser(String userType, int id)`
    - Exclui um usuário do sistema, removendo suas associações.

#### Classe `Student`:
Função: Gerencia os dados e interações de um estudante no sistema.

- **Atributos:**
  - `int studentId` - Armazena o ID do estudante.

- **Operações:**
  - `+ void viewProfile()`
    - Exibe o perfil do estudante, incluindo nome e email.
  - `+ void viewCompetences()`
    - Exibe as competências e scores do estudante a partir da tabela `student_competences`.
  - `+ void viewChallengeScores()`
    - Mostra os challenges realizados pelo estudante e os scores associados.
  - `+ void viewClass()`
    - Exibe a classe à qual o estudante está associado.

#### Classe `Teacher`:
Função: Controla as funcionalidades disponíveis para os professores, como gerenciar exercícios e criar provas.

- **Atributos:**
  - `int teacherId` - Armazena o ID do professor.

- **Operações:**
  - `+ void readStudents()`
    - Exibe informações sobre os alunos, como nome, email e ID da classe.
  - `+ void listExercises()`
    - Lista todos os exercícios disponíveis para serem usados nas provas.
  - `+ void createChallenge()`
    - Cria uma nova prova (challenge) com base nos exercícios selecionados pelo professor.

#### Classe `Administrator`:
Função: Gerencia todas as operações administrativas, como a criação e manutenção de turmas e usuários.

- **Atributos:**
  - Nenhum específico para a classe.

- **Operações:**
  - `+ void createClass(String name, String description, int teacherId)`
    - Cria uma nova turma associada a um professor.
  - `+ void deleteClass(int classId)`
    - Exclui uma turma do sistema.
  - `+ void updateClass(int classId, Integer newTeacherId)`
    - Atualiza o professor associado a uma turma.
  - `+ void updateStudentClass(int studentId, Integer classId)`
    - Atualiza a classe de um estudante.
  - `+ void viewAllClasses()`
    - Exibe todas as turmas registradas no sistema.

#### Classe `SchoolClass`:
Função: Representa uma turma no sistema, associada a um professor e a vários alunos.

- **Atributos:**
  - `String name` - Nome da turma.
  - `String description` - Descrição da turma.
  - `int teacherId` - ID do professor responsável pela turma.

- **Operações:**
  - `+ void createClass(String name, String description, int teacherId)`
    - Cria uma nova turma e associa um professor a ela.
  - `+ void deleteClass(int classId)`
    - Exclui uma turma do sistema.
  - `+ void updateClass(int classId, Integer newTeacherId)`
    - Atualiza o professor responsável por uma turma.
  - `+ void updateStudentClass(int studentId, Integer classId)`
    - Atualiza a classe associada a um aluno.
  - `+ void viewAllClasses()`
    - Exibe todas as turmas cadastradas no sistema.

#### Classe `Challenge`:
Função: Representa uma prova (challenge), criada por um professor, e associada a uma sessão de exercícios.

- **Atributos:**
  - `String name` - Nome da prova.
  - `String description` - Descrição da prova.
  - `int teacherId` - ID do professor que criou a prova.
  - `int challengeSessionId` - ID da sessão de exercícios associada à prova.

- **Operações:**
  - `+ int createChallenge()`
    - Cria uma nova prova e retorna seu ID.

#### Classe `ChallengeSession`:
Função: Representa a sessão de uma prova, contendo os exercícios e a ordem em que devem ser apresentados.

- **Atributos:**
  - `String exercisesJson` - JSON contendo os exercícios e sua ordem.

- **Operações:**
  - `+ int createChallengeSession()`
    - Cria uma nova sessão de prova e retorna seu ID.

#### Classe `Exercise`:
Função: Representa um exercício disponível no sistema, utilizado para compor provas.

- **Atributos:**
  - `int id` - ID do exercício.
  - `String name` - Nome do exercício.
  - `String description` - Descrição do exercício.
  - `String difficulty` - Dificuldade do exercício.

- **Operações:**
  - `+ static List<Exercise> getAllExercises()`
    - Retorna uma lista de todos os exercícios disponíveis.

### Camada Controller

A camada **Controller** será responsável por fornecer as APIs REST que permitirão a comunicação entre a camada View e a Model. As operações CRUD serão expostas por meio de endpoints que serão consumidos pela interface React.

#### Funções da Camada Controller:
1. **Autenticação e Gerenciamento de Usuários**:
   - API para autenticar usuários (alunos, professores e administradores) com base em suas credenciais.
   - Endpoints para criar, ler, atualizar e deletar usuários.

2. **Gerenciamento de Classes**:
   - API para criar, atualizar, excluir e visualizar turmas (SchoolClass).
   - Funcionalidade para associar alunos a turmas.

3. **Exercícios**:
   - API para listar todos os exercícios disponíveis no sistema.

4. **Provas (Challenges)**:
   - API para criar provas associadas a professores e suas respectivas sessões de exercícios.

5. **Competências e Scores dos Estudantes**:
   - API para visualizar as competências e scores dos alunos.
   - Endpoints para consultar as provas realizadas pelos estudantes e seus scores.

### Camada View (React)

A camada View foi desenvolvida em **React** e serve como a interface web que os usuários (administradores, professores e alunos) utilizam para interagir com o sistema.

#### Funcionalidades da Interface React:

1. **Administrador**:
   - Adicionar, atualizar, excluir e visualizar usuários e turmas.
   - Acesso completo às operações administrativas.

2. **Professor**:
   - Criar novas provas, selecionar exercícios e visualizar informações sobre alunos.
   - Gerenciar provas e acompanhar o desempenho dos alunos.

3. **Estudante**:
   - Visualizar perfil, competências, scores de provas e a classe associada.
   - Acesso aos resultados detalhados de provas e exercícios.

### Conclusão

Com a camada **Model** já implementada e a interface web disponível, o próximo passo é a construção da **camada Controller** usando **Spring Boot**. Essa camada fornecerá APIs para integrar a lógica da Model com a interface React, finalizando a arquitetura MVC e proporcionando uma aplicação web funcional e robusta.
