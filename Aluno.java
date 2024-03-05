package org.example;

import javax.persistence.*;

@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double nota;

    public Aluno() {}

    public Aluno(String nome, double nota) {
        this.nome = nome;
        this.nota = nota;
    }

    // Getters e setters omitidos para brevidade
}
