package Threads;

import java.util.ArrayList;

import Lista.Listas;

public class EleicaoThread extends Thread{
	
	public EleicaoThread() {	}
	
	@Override
	public void run() {
		try {
			System.out.println("Começou a eleição entre os processos.");
			if(!Listas.listaProcessosCoordenador.isEmpty()) {
				for(ProcessoThread processo : Listas.listaProcessosCoordenador) {
					processo.interrupt();
					if(Listas.listaProcessosGeral.contains(processo)) {
						Listas.listaProcessosGeral.remove(processo);
					}
				}
				Listas.listaProcessosCoordenador = new ArrayList<ProcessoThread>();
			}
			
			Listas.listaProcessosEleicao.addAll(Listas.listaProcessosGeral);
			Listas.listaProcessosEleicao.get(0).realizaEleicao();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
