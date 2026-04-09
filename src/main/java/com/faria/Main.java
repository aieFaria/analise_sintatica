package com.faria;


public class Main {
    public static void main(String[] args) {
        LeitorTxt l1 = new LeitorTxt();

        System.out.println(l1.getLinhas());
        System.out.println(l1.bufferTxt("untracked/exemplo.txt"));
        System.out.println(l1.getLinhas());
        System.out.println();

        TabelaM tm = new TabelaM();
        tm.gerarTabela();
        System.out.println("Inicio: " + tm.getPrimeiroNaoTerminal()); // Para adicionar a pilha

        for(int i=0; i<tm.getNaoTerminais().size(); i++) {
            for(int j=0; j<tm.getTerminais().size(); j++) {
               
                System.out.print((tm.getTabelaM()[i][j]));
                System.out.print(" | ");
            }
            System.out.println();
        }
    }
}