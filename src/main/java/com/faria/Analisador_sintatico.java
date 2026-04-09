package com.faria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe para definir as funções do analisador descendente proposto
 */
public class Analisador_sintatico {

    private Stack<String> stack;
    private List<String> bufferEntrada;
    TabelaM tabela;

    // Atributos para saída formatada, servem de auxiliares
    private String entrada;
    private String status;
    private String stack_cronologico;

    // Construtor padrão tudo vazio
    public Analisador_sintatico() {
        this.status = "false";
        this.entrada = "";
        this.stack_cronologico = "";
    }

    public boolean analisar(List<String> arrayEntrada) {
        arrayEntrada.add("$");

        stack = new Stack<>();
        stack.push("$"); 
        stack.push(tabela.getPrimeiroNaoTerminal());

        int n = 0;
        StringBuilder transicao = new StringBuilder();

        while( !stack.isEmpty() ) {
            // Olha para o elemento do topo da pilha, não remove da pilha
            String elementPilha = stack.peek();
            String token = arrayEntrada.get(n);

            // Para omitir o "$" da pilha utilize: replace("[$", "[").replace(",", "")
            // isso não remove "$" da pilha pois ainda será usado na lógica
            transicao.append( stack.toString().replace(" ", "").replace("[$", "[").replace("[,", "[") );

            // Remoção elemento "null" da pilha
            if( elementPilha.equals("null") ) {
                stack.pop();
                transicao.append("  ");
                continue;
            }

            if(tabela.getTerminais().contains(elementPilha) || elementPilha.equals("$")) {

                if( elementPilha.equals(token) ) {
                    stack.pop();
                    n++;
                } else {
                    return false; // Caso elemento entrada não seja um dos terminais retorna false
                }

            } else {

                if(tabela.getNaoTerminais() == null || !tabela.getNaoTerminais().contains(elementPilha) ) {
                    // Caso não terminal seja invalido, não esteja contido na lista de naoTerminais retorna false
                    return false; 
                }

                //System.out.println("Token: " + token + "\n Topo: " + elementPilha);

                String producao = tabela.getTabelaM()[tabela.getNaoTerminais().indexOf(elementPilha)]
                                                     [tabela.getTerminais().indexOf(token)];
                stack.pop(); // Remove o Não-Terminal atual

                if (producao.equals("null")) {
                    // Coloca o null para aparecer no resultado
                    stack.push("null"); 
                } else {
                    List<String> simbolos = extrairSimbolosDaProducao(producao);
                    for (int i = simbolos.size() - 1; i >= 0; i--) {
                        stack.push(simbolos.get(i));
                    }
                }

            }

            transicao.append("  ");
            if( (n % 10) == 0 ) {
                //transicao.append("\n    ");
            } else {
                //transicao.append("  ");
            }
            

        }

        this.stack_cronologico = transicao.toString();
        return n == arrayEntrada.size();
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

    /**
     * Geração da saída convertida no formato estabelecido no enunciado
     * 
     * @return  Retorna a String que deve ser incluida no arquivo de saída
     */
    public String gerarSaida() {

        return "\nInput: " + this.entrada + "\n" +
                "Status: " + this.status + "\n" +
                "Stack: [\n    " + this.stack_cronologico + "\n]";
    }

    /**
     * Método para geração de um array de entrada para utilizar no analisador
     * criado com base nos simbolos não terminais da leitura de entrada
     * 
     * @return  Retorna a lista, o buffer de entrada para iterar
     */
    public void gerarBufferEntrada(String texto) {

        this.entrada = texto.replace("$", "");

        boolean finalt = false; // Indicativo de final da entrada caractere "$"
        List<String> bufferEntrada = new ArrayList<>();
        TabelaM tm = new TabelaM();
        tm.gerarTabela();
        this.tabela = tm;
        StringBuilder regex = new StringBuilder();
        List<String> metacaracteres = new ArrayList<>(Arrays.asList(".", "^", "$", "(", ")", "\\", "*", "+", "?", "[", "], {", "}", "|"));

        Pattern patternFinal = Pattern.compile("^\\$");
        Matcher matcherFinal = patternFinal.matcher(texto);
        // Pattern.compile("^(id|\\*|\\+|\\(|\\))");

        // Laço para criação da REGEX que localiza os terminais
        int tamanho = tm.getTerminais().size();
        for(int i=0; i<tamanho; i++) {
            
            if(i==0) {
                regex.append("^(");
            }

            if(tm.getTerminais().get(i).equals("$")) {
                // Pulando caractere de final da stack
            } else if(metacaracteres.contains(tm.getTerminais().get(i))){
                regex.append("\\" + tm.getTerminais().get(i) + "|");
            } else {
                regex.append(tm.getTerminais().get(i) + "|");
            }

            if(i==tamanho-1) {
                regex.append(")");
                //continue;
            }

        }

        //System.out.println(regex.toString().replace("|)", ")") + "(.*)"); // Para verificar a regex final
        Pattern pattern = Pattern.compile(regex.toString().replace("|)", ")") + "(.*)");

        while(!finalt) {
            Matcher matcherGeral = pattern.matcher(texto);
            matcherFinal = patternFinal.matcher(texto);

            // Final da linha encontrado
            if( matcherFinal.find() ) {
                finalt = true;
                break;
            }

            if( matcherGeral.find() ) {
                bufferEntrada.add(matcherGeral.group(1));
                texto = matcherGeral.group(2);
            }
        }

        this.bufferEntrada = bufferEntrada;
    }

    public Stack<String> getStack() {
        return stack;
    }

    public void setStack(Stack<String> stack) {
        this.stack = stack;
    }

    public List<String> getBufferEntrada() {
        return bufferEntrada;
    }

    public void setBufferEntrada(List<String> bufferEntrada) {
        this.bufferEntrada = bufferEntrada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
}
