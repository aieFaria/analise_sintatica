package com.faria;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LeitorTxt l1 = new LeitorTxt();
        Analisador_sintatico analisador = new Analisador_sintatico();

        System.out.println(l1.getLinhas());
        System.out.println(l1.bufferTxt("untracked/exemplo.txt"));
        System.out.println(l1.getLinhas());
        System.out.println();

        TabelaM tm = new TabelaM();
        // tm.gerarTabela();
        // System.out.println("Inicio: " + tm.getPrimeiroNaoTerminal()); // Para adicionar a pilha

        // for(int i=0; i<tm.getNaoTerminais().size(); i++) {
        //     for(int j=0; j<tm.getTerminais().size(); j++) {
               
        //         System.out.print((tm.getTabelaM()[i][j]));
        //         System.out.print(" | ");
        //     }
        //     System.out.println();
        // }


        StringBuilder sbSaida = new StringBuilder();

        for(String entrada: l1.getLinhas()) {
            // Gerar buffer de entrada, um array de String
            analisador.gerarBufferEntrada(entrada);
            System.out.println("Buffer: " +  analisador.getBufferEntrada());
            List<String> entrada2 = analisador.getBufferEntrada();

            // Analisar a entrada
            if( analisador.analisar(entrada2) ) {
                analisador.setStatus("true");
            } else {
                analisador.setStatus("false");
            }


            // Escrever a saída para cada entrada
            sbSaida.append(analisador.gerarSaida());
        }

        // Gerando arquivo de saída completo
        LeitorTxt.writeSaida( sbSaida.toString() );
    }
}