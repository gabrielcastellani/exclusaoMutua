package Threads;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import Lista.Listas;

public class RecursoThread extends Thread {
	private Semaphore semaforo;
	private ProcessoThread processo;

	public RecursoThread(Semaphore semaforo) {
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Random gerador = new Random();
				Thread.sleep((int) (Math.random() * 5000 + 15000));

				if (Listas.listaProcessosGeral.isEmpty()) {
					semaforo.acquire();
				} else {
					ArrayList<ProcessoThread> processos = (ArrayList<ProcessoThread>) Listas.listaProcessosGeral
							.stream().filter(processo -> !processo.isCoordenador()).collect(Collectors.toList());

					if (!processos.isEmpty()) {
						processo = processos.get(gerador.nextInt(processos.size()));
						boolean situacaoCoordenador = processo.enviarRequisicaoCoordenador();

						if (situacaoCoordenador) {
							processo.consumirRecurso();
						} else {
							System.out.println("O processo com o id("+processo.getIdProcesso()+") entrou na fila do coordenador.");
							Listas.listaProcessosGeral.add(processo);
						}
					}
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} finally {
				semaforo.release();
			}
		}
	}
}
