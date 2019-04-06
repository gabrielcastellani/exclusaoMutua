package Main;

import java.util.concurrent.Semaphore;

import Threads.CriarProcessoThread;
import Threads.EncerrarCoordenadorThread;
import Threads.RecursoThread;

public class Main {

	public static void main(String[] args) {
		try {
			Semaphore semaforo = new Semaphore(1);
			
			EncerrarCoordenadorThread encerrarCoordenador = new EncerrarCoordenadorThread(semaforo);
			RecursoThread recurso = new RecursoThread(semaforo);
			CriarProcessoThread processoThread = new CriarProcessoThread();
			
			System.out.println("O programa começou.");
			encerrarCoordenador.start();
			recurso.start();
			processoThread.start();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
