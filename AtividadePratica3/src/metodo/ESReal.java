/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodo;

import java.util.Collections;
import java.util.Random;

import problema.ProblemaRastringin;
import solucao.Individuo;
import solucao.IndividuoDouble;
import solucao.PopulacaoDouble;

/**
 *
 * @author Deivyson
 */
public class ESReal implements Metodo {

    // Parametros - problema - DeJong
    private Double minimo;
    private Double maximo;
    private Integer nVariaveis;
    private ProblemaRastringin problema;
    
    // Parametros - ES
    private Integer mu; // Tamanho da populacao
    private Integer lambda; // numero de descendentes
    private Integer geracoes; // criterio de parada
    private Double pMutacao; // mutacao - aplicacao ao descendente - variacao/perturbacao

    public ESReal(Double minimo, Double maximo, Integer nVariaveis, ProblemaRastringin problema, Integer mu, Integer lambda, Integer geracoes, Double pMutacao) {
        this.minimo = minimo;
        this.maximo = maximo;
        this.nVariaveis = nVariaveis;
        this.problema =  problema;
        this.mu = mu;
        this.lambda = lambda;
        this.geracoes = geracoes;
        this.pMutacao = pMutacao;
    }
    
    @Override
    public Individuo executar() {
        
         long time1 = System.nanoTime();

        // Geracao da populacao inicial - aleatoria - tamanho mu
        PopulacaoDouble populacao = new PopulacaoDouble( problema, minimo, maximo, nVariaveis, mu);     
        populacao.criar();
        
        // Avaliar
        populacao.avaliar();
        
        // Populacao - descendentes
        PopulacaoDouble descendentes = new PopulacaoDouble();
        
        // Criterio de parada - numero de geracoes
        for(int g = 1; g <= this.geracoes; g++) {
            
            // Para cada individuo, gerar lambda/mu descendentes
            for(int i = 0; i < populacao.getIndividuos().size(); i++) {
                
                // Gerar lambda/mu descendentes
                for(int d = 0; d < lambda / mu; d++) {
                    
                    // Clonar individuo
                    IndividuoDouble filho = (IndividuoDouble)populacao.getIndividuos().get(i).clone();
                    
                    // Aplicar mutacao
                    mutacaoPorVariavel(filho);
                    // Avaliar
                    problema.calcularFuncaoObjetivo(filho);
                    // Inserir na nova populacao
                    descendentes.getIndividuos().add(filho);
                    
                   
                }
                
            }
            // mu,lamb
            
            populacao.getIndividuos().clear();
            
            
            
        populacao.getIndividuos().addAll(descendentes.getIndividuos());
        Collections.sort(populacao.getIndividuos());
        populacao.getIndividuos().subList(this.mu, populacao.getIndividuos().size()).clear();
        descendentes.getIndividuos().clear();
        //System.out.println("G = " + g + "\t" + populacao.getMelhorIndividuo().getFuncaoObjetivo());
      
            
        }
        
    long time2 = System.nanoTime();
      double seconds = (double)(time2-time1) / 1000000000.0;
       
        System.out.println(populacao.getIndividuos().get(0).getFuncaoObjetivo() + " ; " + populacao.getIndividuos().get(populacao.getIndividuos().size()-1).getFuncaoObjetivo()+";"+seconds);
       
        
    return populacao.getMelhorIndividuo();
    
}
           private void mutacaoPorVariavel(Individuo individuo) {

        Random rnd = new Random();

        for (int i = 0; i < individuo.getCromossomos().size(); i++) {
            if (rnd.nextDouble() <= this.pMutacao) {

                // Mutacao aritmetica
                // Multiplicar rnd e inverter ou nao o sinal
                Double valor = (Double)individuo.getCromossomos().get(i);
                // Multiplica por rnd
                valor *= rnd.nextDouble();

                // Inverter o sinal
                if (!rnd.nextBoolean()) {
                    valor = -valor;
                }

                if (valor >= this.minimo
                        && valor <= this.maximo) {
                    individuo.getCromossomos().set(i, valor);

                }

            }
        }

    }

}


