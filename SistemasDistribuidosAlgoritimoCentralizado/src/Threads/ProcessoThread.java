package Threads;

import java.util.ArrayList;
import java.util.Optional;

import Lista.Listas;
import Status.StatusRecurso;

public class ProcessoThread extends Thread {
	private int id;
	private boolean coordenador;
	private StatusRecurso situacao = StatusRecurso.REQUISICAO;

	public ProcessoThread(int id, boolean coordenador) {
		this.id = id;
		this.coordenador = coordenador;
	}

	public int getIdProcesso() {
		return id;
	}

	public void setIdProcesso(int id) {
		this.id = id;
	}

	public boolean isCoordenador() {
		return coordenador;
	}

	public void setCoordenador(boolean coordenador) {
		this.coordenador = coordenador;
	}

	public StatusRecurso getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusRecurso situacao) {
		this.situacao = situacao;
	}

	@Override
	public void run() {
		if (coordenador) {
			while (true) {
				if(Listas.listaProcessosCoordenador.isEmpty()) {
					situacao = StatusRecurso.REQUISICAO;
				}
				
				if (situacao.equals(StatusRecurso.LIBERACAO)) {
					ProcessoThread processo = Listas.listaProcessosCoordenador.get(0);

					if (processo.enviarRequisicaoCoordenador()) {
						processo.consumirRecurso();
						Listas.listaProcessosGeral.remove(processo);
						System.out.println("O processo com o id("+processo.getIdProcesso()+") saiu da fila do coordenador.");
					}
				}
			}
		}
	}

	public void consumirRecurso() {
		try {
			System.out.println("Processo com o id(" + this.id + ") está consumindo um recurso.");
			Thread.sleep((int) (Math.random() * 10000 + 25000));
			System.out.println("Processo com o id(" + this.id + ") terminou de consumir um recurso.");
			alterarSituacaoCoordenador();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void alterarSituacaoCoordenador() {
		Optional<ProcessoThread> processoCoordenador = Listas.listaProcessosGeral.stream()
				.filter(ProcessoThread::isCoordenador).findAny();

		if (!processoCoordenador.isPresent()) {
			EleicaoThread eleicao = new EleicaoThread();
			eleicao.start();
		} else {
			processoCoordenador.get().setSituacao(StatusRecurso.LIBERACAO);
		}
	}

	public boolean enviarRequisicaoCoordenador() {
		Optional<ProcessoThread> processoCoordenador = Listas.listaProcessosGeral.stream()
				.filter(ProcessoThread::isCoordenador).findAny();

		if (!processoCoordenador.isPresent()) {
			EleicaoThread eleicao = new EleicaoThread();
			eleicao.start();
		} else {
			if (processoCoordenador.get().getSituacao().equals(StatusRecurso.REQUISICAO)) {
				System.out.println(
						"O processo com o id (" + this.id + ") enviou uma requisicao para o coordenador com o id("
								+ processoCoordenador.get().getIdProcesso() + ").");
				processoCoordenador.get().setSituacao(StatusRecurso.CONCESSAO);
				return true;
			}
		}

		return false;
	}

	private boolean compararIdProcesso(int idOutroProcesso) {
		return this.id > idOutroProcesso;
	}

	public void realizaEleicao() {
		Listas.listaProcessosEleicao.remove(this);
		ArrayList<String> resultadoComparacao = new ArrayList<>();

		for (ProcessoThread processo : Listas.listaProcessosEleicao) {
			System.out.println("O processo com o id(" + this.id
					+ "), mandou mensagem de eleição para o processo com o id(" + processo.getIdProcesso() + ").");
			if (Listas.listaProcessosGeral.contains(processo)) {
				resultadoComparacao.add(processo.compararIdProcesso(this.id) + "");
			} else {
				Listas.listaProcessosEleicao.remove(processo);
			}
		}

		if (resultadoComparacao.contains("true")) {
			Listas.listaProcessosEleicao.get(0).realizaEleicao();
		} else {
			Listas.listaProcessosEleicao.clear();
			coordenador = true;

			if (Listas.listaProcessosGeral.contains(this)) {
				System.out.println("O processo com o id(" + this.id + ") se tornou o novo coordenador.");
			}
		}
	}
}
