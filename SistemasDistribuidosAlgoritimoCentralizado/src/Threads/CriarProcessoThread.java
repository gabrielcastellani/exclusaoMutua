package Threads;

import java.util.Random;

import Lista.Listas;

public class CriarProcessoThread extends Thread {
	private int idProcesso;

	public CriarProcessoThread() {	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(40000);

				Random gerador = new Random();
				idProcesso = gerador.nextInt(Integer.MAX_VALUE);
				boolean primeiroProcessoLista = false;

				long quantidadeCoordenadorLista = Listas.listaProcessosGeral.stream()
						.filter(ProcessoThread::isCoordenador).count();
				if (quantidadeCoordenadorLista == 0 && Listas.listaProcessosGeral.isEmpty()) {
					primeiroProcessoLista = true;
				}

				if (Listas.listaProcessosGeral.stream().filter(processo -> processo.getIdProcesso() == idProcesso)
						.count() > 0) {
					while (true) {
						idProcesso = gerador.nextInt(Integer.MAX_VALUE);

						if (Listas.listaProcessosGeral.stream()
								.noneMatch(processo -> processo.getIdProcesso() == idProcesso)) {
							break;
						}
					}
				}

				ProcessoThread processo = new ProcessoThread(idProcesso, primeiroProcessoLista);
				processo.start();
				Listas.listaProcessosGeral.add(processo);

				if (primeiroProcessoLista) {
					System.out
							.println("O processo(Coordenador) com o id(" + processo.getIdProcesso() + ") foi criado.");
				} else {
					System.out.println("O processo com o id(" + processo.getIdProcesso() + ") foi criado.");
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
