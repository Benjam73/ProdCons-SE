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
	private int msgNumber = 0;

	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
	}

	@Override
	public int enAttente() {
		return queue.size();
	}

	public void setMessageId(MessageX msg) {
		msg.setId(msgNumber);
		msgNumber++;
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		while (!(enAttente() > 0)) {
			System.out.println(arg0.toString() + " waits");
			wait();
		}
		Message resultingMessage = queue.poll();
		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}
		System.out.println("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
				+ " messages remaining in buffer");
		notifyAll();
		return resultingMessage;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		while (!(enAttente() < taille())) {
			System.out.println(arg0.toString() + " waits");
			wait();
		}
		setMessageId((MessageX) arg1);
		System.out.println("put by " + arg0.toString() + " of " + arg1.toString() + " with remaining capacity = "
				+ (taille() - enAttente()));
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
