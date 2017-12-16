package step4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

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

	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
		fifoProducer = new Semaphore(capacity, true);
		fifoConsumer = new Semaphore(0, true);
		mutex = new Semaphore(1, true);
	}

	@Override
	public int enAttente() {
		return queue.size();
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		Message resultingMessage;
		fifoConsumer.acquire();
		mutex.acquire();

		if (((MessageX) queue.element()).getCopyNumber() == 1) {
			resultingMessage = queue.poll();
			((MessageX) resultingMessage).getMessageProducer().getSemaphore().release();
			fifoProducer.release();

		} else {
			resultingMessage = queue.element();
			((MessageX) queue.element()).decreaseCopynumber();
		}

		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}

		mutex.release();

		return resultingMessage;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		fifoProducer.acquire();
		mutex.acquire();

		try {
			queue.add(arg1);

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