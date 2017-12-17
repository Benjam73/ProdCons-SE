package step1;

import java.util.LinkedList;
import java.util.Queue;

import common.Debugger;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	Integer capacity;
	Queue<Message> queue;
	private int msgNumber = 0;

	/**
	 * 
	 * @param capacity
	 *            The maximum size of the buffer where the messages are put
	 */
	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
	}

	/**
	 * @return the number of awaiting messages in the buffer
	 */
	@Override
	public int enAttente() {
		return queue.size();
	}

	public void setMessageId(MessageX msg) {
		msg.setId(msgNumber);
		msgNumber++;
	}

	/**
	 * Allow a Consommateur to get the first awaiting message in the buffer
	 * 
	 * NotifyAll the threads whom might by in a waiting state
	 */
	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		while (!(enAttente() > 0)) {
			Debugger.log(arg0.toString() + " waits");
			wait();
		}
		Message resultingMessage = queue.poll();
		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}
		Debugger.log("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
				+ " messages remaining in buffer");
		notifyAll();
		return resultingMessage;
	}

	/**
	 * Allow a Produteur to put a new Message in the buffer
	 * 
	 * NotifyAll the threads whom might by in a waiting state
	 */
	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		while (!(enAttente() < taille())) {
			Debugger.log(arg0.toString() + " waits");
			wait();
		}
		setMessageId((MessageX) arg1);
		Debugger.log("put by " + arg0.toString() + " of " + arg1.toString() + " with remaining capacity = "
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
