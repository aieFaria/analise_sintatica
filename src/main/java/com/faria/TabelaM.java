package com.faria;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Classe representa a tabela M
 */
public class TabelaM {

    
    private List<String> terminais = new LinkedList<>();
    private List<String> naoTerminais = new LinkedList<>();
    private String[][] tabelaM = new String[999][999];
    private String primeiroNaoTerminal = "";

    @SuppressWarnings("unchecked")
    public void gerarTabela() {

        JSONParser parser = new JSONParser();
        formatarMatrizTabela();

        try {

            FileReader reader = new FileReader("untracked/dicionario_linguagem.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject tableM = (JSONObject) jsonObject.get("tableM");
            terminais = (List<String>) jsonObject.get("terminal");
            naoTerminais = (List<String>) jsonObject.get("nonterminal");
            Set<String> chaves = tableM.keySet();

            if(this.primeiroNaoTerminal.equals("")) {
                this.primeiroNaoTerminal = naoTerminais.getFirst();
            }

            for(String chaveNaoTerminal: chaves) {
                JSONObject aux = (JSONObject) tableM.get(chaveNaoTerminal);
                Set<String> chaves2 = aux.keySet();
                //System.out.print(chaveNaoTerminal + " | ");

                // Garantir que nenhum Não terminal ficará de fora
                if(!naoTerminais.contains(chaveNaoTerminal)) {
                    naoTerminais.add(chaveNaoTerminal);
                }
                
                for(String chaveTerminal: chaves2) {
                    // Garantir que nenhum terminal ficará de fora
                    if(!terminais.contains(chaveTerminal)) {
                        terminais.add(chaveTerminal);
                    }

                    String aux2 = (String) aux.get(chaveTerminal);
                    //System.out.print(aux2+"  | ");
                    tabelaM[naoTerminais.indexOf(chaveNaoTerminal)][terminais.indexOf(chaveTerminal)] = aux2;
                }
                //System.out.println();
            }

            System.out.println("  " + naoTerminais);
            System.out.println("  " + terminais);

        } catch (Exception e) {
            System.out.println("Algo deu errado!");
            System.out.println(e.getMessage());
        }
    }

    private void formatarMatrizTabela() {

        for(int i=0; i<tabelaM.length; i++) {
            for(int j=0; j<tabelaM[0].length; j++) {

                tabelaM[i][j] = "erro";
                // System.out.print((tabelaM[i][j]));
                // System.out.print(" | ");
            }
            // System.out.println();
        }

    }

    public List<String> getTerminais() {
        return terminais;
    }

    public void setTerminais(List<String> terminais) {
        this.terminais = terminais;
    }

    public List<String> getNaoTerminais() {
        return naoTerminais;
    }

    public void setNaoTerminais(List<String> naoTerminais) {
        this.naoTerminais = naoTerminais;
    }

    public String[][] getTabelaM() {
        return tabelaM;
    }

    public void setTabelaM(String[][] tabelaM) {
        this.tabelaM = tabelaM;
    }

    public String getPrimeiroNaoTerminal() {
        return primeiroNaoTerminal;
    }

    public void setPrimeiroNaoTerminal(String primeiroNaoTerminal) {
        this.primeiroNaoTerminal = primeiroNaoTerminal;
    }

    
    
}
