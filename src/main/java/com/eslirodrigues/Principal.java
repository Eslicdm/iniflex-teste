package com.eslirodrigues;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out; // SonarQube erro de mantenabilidade: Replace this use of System.out by a logger

public class Principal {
    public static void main(String[] args) {
        enum Funcao {
            OPERADOR("Operador"),
            COORDENADOR("Coordenador"),
            DIRETOR("Diretor"),
            RECEPCIONISTA("Recepcionista"),
            CONTADOR("Contador"),
            GERENTE("Gerente"),
            ELETRICISTA("Eletricista");

            private final String tipo;

            Funcao(String tipo) {
                this.tipo = tipo;
            }

            public String getTipo() {
                return tipo;
            }
        }

        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), Funcao.OPERADOR.getTipo()));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), Funcao.OPERADOR.getTipo()));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), Funcao.COORDENADOR.getTipo()));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), Funcao.DIRETOR.getTipo()));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), Funcao.RECEPCIONISTA.getTipo()));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), Funcao.OPERADOR.getTipo()));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), Funcao.CONTADOR.getTipo()));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), Funcao.GERENTE.getTipo()));
        funcionarios.add(new Funcionario("Heloisa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), Funcao.ELETRICISTA.getTipo()));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), Funcao.GERENTE.getTipo()));

        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        out.println("Lista de Funcionários:");
        DateTimeFormatter dataFormatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numeroFormatador = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        funcionarios.forEach(funcionario -> out.printf(
                "Nome: %s, Data de Nascimento: %s, Salário: %s, Função: %s%n",
                funcionario.getNome(), 
                funcionario.getDataNascimento().format(dataFormatador),
                numeroFormatador.format(funcionario.getSalario()),
                funcionario.getFuncao()
        ));

        out.println("\nSalários atualizados (10% de aumento):");
        BigDecimal aumento = new BigDecimal("1.10");
        funcionarios.forEach(funcionario ->
                funcionario.setSalario(
                        funcionario.getSalario().multiply(aumento).setScale(2, RoundingMode.HALF_UP)
                )
        );
        funcionarios.forEach(funcionario -> out.printf(
                "Nome: %s, Novo Salário: %s%n",
                funcionario.getNome(), numeroFormatador.format(funcionario.getSalario())
        ));

        out.println("\nFuncionários agrupados por função:");
        Map<String, List<Funcionario>> listaFuncionariosPorFuncao = 
                funcionarios.stream().collect(Collectors.groupingBy(Funcionario::getFuncao));
        listaFuncionariosPorFuncao.forEach((funcao, listaFuncionarios) -> {
            out.println("  Função: " + funcao);
            listaFuncionarios.forEach(funcionario ->
                    out.println("    - " + funcionario.getNome())
            );
        });

        out.println("\nAniversariantes de Outubro (10) e Dezembro (12):");
        funcionarios.stream()
                .filter(funcionario ->
                        funcionario.getDataNascimento().getMonth() == Month.OCTOBER ||
                                funcionario.getDataNascimento().getMonth() == Month.DECEMBER
                )
                .forEach(funcionario -> out.printf(
                        "  - Nome: %s, Data de Nascimento: %s%n",
                        funcionario.getNome(), funcionario.getDataNascimento().format(dataFormatador))
                );

        out.println("\nFuncionário com a maior idade:");
        funcionarios.stream()
                .min(Comparator.comparing(Pessoa::getDataNascimento))
                .ifPresent(funcionario -> {
                    Period idade = Period.between(funcionario.getDataNascimento(), LocalDate.now());
                    out.printf(
                            "  - Nome: %s, Idade: %d anos%n",
                            funcionario.getNome(), idade.getYears()
                    );
                });

        out.println("\nLista de Funcionários em ordem alfabética:");
        funcionarios.stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .forEach(funcionario -> out.println("  - " + funcionario.getNome()));

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        out.printf("%nTotal dos salários: %s%n", numeroFormatador.format(totalSalarios));

        out.println("\nQuantidade de salários mínimos por funcionário:");
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        funcionarios.forEach(funcionario -> out.printf(
                "  - %s: %s salários mínimos%n",
                funcionario.getNome(),
                numeroFormatador.format(funcionario.getSalario().divide(salarioMinimo, 2, RoundingMode.DOWN))
        ));
    }
}