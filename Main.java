import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("AlunoPU");
        em = emf.createEntityManager();

        int opcao;
        do {
            System.out.println("** CADASTRO DE ALUNOS **");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Excluir aluno");
            System.out.println("3 - Alterar aluno");
            System.out.println("4 - Buscar aluno pelo nome");
            System.out.println("5 - Listar alunos (com status de aprovação)");
            System.out.println("6 - FIM");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (opcao) {
                case 1:
                    cadastrarAluno();
                    break;
                case 2:
                    excluirAluno();
                    break;
                case 3:
                    alterarAluno();
                    break;
                case 4:
                    buscarAlunoPeloNome();
                    break;
                case 5:
                    listarAlunosComStatusAprovacao();
                    break;
                case 6:
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        } while (opcao != 6);

        em.close();
        emf.close();
    }

    private static void cadastrarAluno() {
        System.out.println("Opção 'Cadastrar aluno' selecionada.");
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a nota do aluno: ");
        double nota = scanner.nextDouble();
        scanner.nextLine(); // Limpar o buffer do scanner

        Aluno aluno = new Aluno(nome, nota);
        em.getTransaction().begin();
        em.persist(aluno);
        em.getTransaction().commit();
        System.out.println("Aluno cadastrado com sucesso.");
    }

    private static void excluirAluno() {
        System.out.println("Opção 'Excluir aluno' selecionada.");
        System.out.print("Digite o ID do aluno a ser excluído: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Limpar o buffer do scanner

        Aluno aluno = em.find(Aluno.class, id);
        if (aluno != null) {
            em.getTransaction().begin();
            em.remove(aluno);
            em.getTransaction().commit();
            System.out.println("Aluno excluído com sucesso.");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private static void alterarAluno() {
        System.out.println("Opção 'Alterar aluno' selecionada.");
        System.out.print("Digite o ID do aluno a ser alterado: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Limpar o buffer do scanner

        Aluno aluno = em.find(Aluno.class, id);
        if (aluno != null) {
            System.out.print("Digite o novo nome do aluno: ");
            String nome = scanner.nextLine();
            System.out.print("Digite a nova nota do aluno: ");
            double nota = scanner.nextDouble();
            scanner.nextLine(); // Limpar o buffer do scanner

            aluno.setNome(nome);
            aluno.setNota(nota);

            em.getTransaction().begin();
            em.merge(aluno);
            em.getTransaction().commit();
            System.out.println("Aluno alterado com sucesso.");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private static void buscarAlunoPeloNome() {
        System.out.println("Opção 'Buscar aluno pelo nome' selecionada.");
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();

        TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a WHERE a.nome = :nome", Aluno.class);
        query.setParameter("nome", nome);
        List<Aluno> alunos = query.getResultList();

        if (!alunos.isEmpty()) {
            for (Aluno aluno : alunos) {
                System.out.println("ID: " + aluno.getId() + ", Nome: " + aluno.getNome() + ", Nota: " + aluno.getNota());
            }
        } else {
            System.out.println("Nenhum aluno encontrado com o nome informado.");
        }
    }

    private static void listarAlunosComStatusAprovacao() {
        System.out.println("Opção 'Listar alunos com status de aprovação' selecionada.");
        TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a", Aluno.class);
        List<Aluno> alunos = query.getResultList();

        if (!alunos.isEmpty()) {
            for (Aluno aluno : alunos) {
                String status = aluno.getNota() >= 6 ? "Aprovado" : "Reprovado";
                System.out.println("ID: " + aluno.getId() + ", Nome: " + aluno.getNome() + ", Nota: " + aluno.getNota() + ", Status: " + status);
            }
        } else {
            System.out.println("Nenhum aluno cadastrado.");
        }
    }
}
