package com.faria;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LeitorTxt {

    // Atributo para páginação entre as linhas do arquivo de entrada
    // É do tipo Linked pois devemos garantir a ordenação com base na inserção
    private List<String> linhasEntrada = new LinkedList<>();

    /**
     * Função que realizará leitura do arquivo .txt e converterá em String manipulável
     * 
     * @param nomeArquivo  Nome de referência do arquivo que será lido
     * @return             Arquivo de texto convertido para String
     */
    public String bufferTxt(String nomeArquivo) {

        StringBuilder sb = new StringBuilder();
        
        try {

            FileReader arquivoReader = new FileReader(nomeArquivo);
            BufferedReader bReader  = new BufferedReader(arquivoReader);
            String texto;

            // Faz a leitura linha por linha enquanto houver conteúdo
            // presente no arquivo .txt
            while ( (texto = bReader.readLine()) != null ) {
                sb.append(texto);
                linhasEntrada.add(texto + "$");
                sb.append("\n"); // Adicionar caso seja necessário considerar a quedra de linha
            }

            bReader.close();

        } catch (FileNotFoundException notFound) {

            // Imprimindo messagem de erro utilizando outro método
            // System.out.println( notFound.getMessage() );
            sb.append( notFound.getMessage() );

        } catch (IOException e) {

            // Imprimindo messagem de erro para análise
            e.printStackTrace();

        }
        finally {
            // Bloco que sempre é executado independentemente de erros capturados no try-catch
            // System.out.println("Saiu do try");
        }

        return sb.toString().trim();
    }

    /**
     * A ideia é gerar um log de erro contendo ponto exato que foi encontrado a falha
     * 
     * @return  Retorna a lista de linhas
     */
    public List<String> getLinhas() {

        if(this.linhasEntrada.size() <= 0) {
            return List.of("Lista vazia ;-;");
        } else {
            return this.linhasEntrada;
        }
        

    }
    
}
