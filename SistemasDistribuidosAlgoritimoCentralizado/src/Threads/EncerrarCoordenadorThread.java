package Threads;

import java.util.Optional;
import java.util.concurrent.Semaphore;

import Lista.Listas;

public class EncerrarCoordenadorThread extends Thread {
	private Semaphore semaforo;

	public EncerrarCoordenadorThread(Semaphore semaforo) {
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60000);

				Optional<ProcessoThread> processoCoordenador = Listas.listaProcessosGeral.stream()
						.filter(ProcessoThread::isCoordenador).findAny();

				if (!processoCoordenador.isPresent()) {
					semaforo.acquire();
				} else {
					processoCoordenador.get().interrupt();
					Listas.listaProcessosGeral.remove(processoCoordenador.get());
					System.out.println(
							"O coordenador com o id(" + processoCoordenador.get().getIdProcesso() + ") foi removido.");
				}

			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} finally {
				semaforo.release();
			}
		}
	}
}
