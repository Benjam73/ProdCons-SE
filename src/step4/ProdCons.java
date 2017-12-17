package step4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import common.Debugger;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	Integer capacity;
	Queue<Message> queue;

	Semaphore fifoProducer;
	Semaphore fifoConsumer;
	Semaphore mutex;

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
		fifoProducer = new Semaphore(capacity, true);
		fifoConsumer = new Semaphore(0, true);
		mutex = new Semaphore(1, true);
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
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		Message resultingMessage;
		fifoConsumer.acquire();
		mutex.acquire();

		if (((MessageX) queue.element()).getCopyNumber() == 1) {

			resultingMessage = queue.poll();
			Debugger.log("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
					+ " messages remaining in buffer");
			((MessageX) resultingMessage).getMessageProducer().getSemaphore().release();
			fifoProducer.release();

		} else {
			resultingMessage = queue.element();
			Debugger.log("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
					+ " messages remaining in buffer");
			((MessageX) queue.element()).decreaseCopynumber();
		}

		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}

		mutex.release();

		return resultingMessage;
	}

	/**
	 * Allow a Produteur to put a new Message in the buffer
	 * 
	 * NotifyAll the threads whom might by in a waiting state
	 */
	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		fifoProducer.acquire();
		mutex.acquire();

		try {
			setMessageId((MessageX) arg1);
			queue.add(arg1);
			Debugger.log("put by " + arg0.toString() + " of " + arg1.toString() + " with remaining capacity = "
					+ (taille() - enAttente()));

		} catch (Exception e) {
			e.getMessage();
		}

		mutex.release();
		fifoConsumer.release(((MessageX) arg1).getCopyNumber());
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
