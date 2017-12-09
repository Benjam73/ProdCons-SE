package step1;

import java.util.LinkedList;
import java.util.Queue;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	Integer capacity;
	Queue<Message> queue;

	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
	}

	@Override
	public int enAttente() {
		return queue.size();
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		while (!(enAttente() > 0)) {
			arg0.wait();
		}
		Message resultingMessage = queue.poll();
		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}
		notifyAll();
		return resultingMessage;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		while (!(enAttente() < taille())) {
			arg0.wait();
		}
		queue.add(arg1);
		notifyAll();
	}

	@Override
	public int taille() {
		return getCapacity();
	}

	// Getters
	private Integer getCapacity() {
		return capacity;
	}

}
