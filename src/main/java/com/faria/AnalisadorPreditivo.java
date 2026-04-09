package com.faria;

import java.util.*;

public class AnalisadorPreditivo {

    private static final Map<String, Map<String, String>> tableM = new HashMap<>();
    private static final Set<String> terminais = new HashSet<>(Arrays.asList("+", "*", "(", ")", "id", "$"));

    static {
        // Inicializando a Tabela M com os seus dados
        tableM.put("E", Map.of("id", "TX", "(", "TX"));
        tableM.put("X", Map.of("+", "+TX", ")", "null", "$", "null"));
        tableM.put("T", Map.of("id", "FY", "(", "FY"));
        tableM.put("Y", Map.of("+", "null", "*", "*FY", ")", "null", "$", "null"));
        tableM.put("F", Map.of("id", "id", "(", "(E)"));
    }

    public static void main(String[] args) {
        List<String> bufferEntrada = Arrays.asList("(", "id", "+", "id", "*", "id", "$");
        boolean sucesso = analisar(bufferEntrada, "E"); 
        System.out.println("\nResultado final: " + (sucesso ? "CADEIA ACEITA!" : "ERRO DE SINTAXE!"));
    }

    public static boolean analisar(List<String> buffer, String simboloInicial) {
        Stack<String> pilha = new Stack<>();
        pilha.push("$"); 
        pilha.push(simboloInicial);

        int ponteiroEntrada = 0;
        
        // Aumentei um pouco o espaçamento para caber o "null" na formatação
        System.out.println(String.format("%-20s | %-20s | %s", "PILHA", "ENTRADA", "AÇÃO/PRODUÇÃO"));
        System.out.println("-".repeat(70));

        while (!pilha.isEmpty()) {
            String topoPilha = pilha.peek();
            String tokenAtual = buffer.get(ponteiroEntrada);
            
            imprimirEstado(pilha, buffer, ponteiroEntrada);

            // NOVO: Trata o desempilhamento do "null" visual
            if (topoPilha.equals("null")) {
                pilha.pop();
                System.out.println("  -> Desempilha ε (vazio)");
                continue; // Pula para a próxima iteração sem consumir a entrada
            }

            // CASO 1: O topo da pilha é um Terminal ou o fim do arquivo '$'
            if (terminais.contains(topoPilha) || topoPilha.equals("$")) {
                if (topoPilha.equals(tokenAtual)) {
                    pilha.pop(); // Consome da pilha
                    ponteiroEntrada++; // Avança a entrada
                    System.out.println("  -> Match: " + tokenAtual);
                } else {
                    System.out.println("  -> ERRO: Esperava '" + topoPilha + "' mas encontrou '" + tokenAtual + "'");
                    return false;
                }
            } 
            // CASO 2: O topo da pilha é um Não-Terminal
            else {
                Map<String, String> transicoes = tableM.get(topoPilha);
                
                if (transicoes == null || !transicoes.containsKey(tokenAtual)) {
                    System.out.println("  -> ERRO: Nenhuma regra na Tabela M para [" + topoPilha + "][" + tokenAtual + "]");
                    return false;
                }

                String producao = transicoes.get(tokenAtual);
                pilha.pop(); // Remove o Não-Terminal atual

                if (producao.equals("null")) {
                    System.out.println("  -> " + topoPilha + " -> ε (null)");
                    // NOVO: Empilha o "null" para aparecer no console no próximo ciclo
                    pilha.push("null"); 
                } else {
                    System.out.println("  -> " + topoPilha + " -> " + producao);
                    List<String> simbolos = extrairSimbolosDaProducao(producao);
                    for (int i = simbolos.size() - 1; i >= 0; i--) {
                        pilha.push(simbolos.get(i));
                    }
                }
            }
        }
        
        return ponteiroEntrada == buffer.size();
    }

    private static List<String> extrairSimbolosDaProducao(String producao) {
        List<String> simbolos = new ArrayList<>();
        for (int i = 0; i < producao.length(); i++) {
            if (producao.startsWith("id", i)) {
                simbolos.add("id");
                i++; 
            } else {
                simbolos.add(String.valueOf(producao.charAt(i)));
            }
        }
        return simbolos;
    }

    private static void imprimirEstado(Stack<String> pilha, List<String> buffer, int ponteiro) {
    String p = pilha.toString().replace(" ", "").replace("[$", "[").replace("[,", "[");
        //p = " ";
        String b = String.join("", buffer.subList(ponteiro, buffer.size()));
        b = " -";
        System.out.print(String.format("%-20s | %-20s |", p, b));
    }
}