package com.operation.fileprocess.consumer;

import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.operation.fileprocess.central.ConsumerService;
import com.operation.fileprocess.helper.ProcessWordCount;
import com.operation.fileprocess.helper.ResourcePoolManager;
import com.operation.fileprocess.mapper.FileProcessMapper;

public class FileProcessConsumer implements Runnable {
	ProcessWordCount consumerProcessCount = new ProcessWordCount();
	BlockingQueue<FileProcessMapper> queue;
	ConsumerService ConsumerConsumerService = new ConsumerService();
	private final String EXITMESSAGE = "#@EXIT!@#";

	public FileProcessConsumer(BlockingQueue<FileProcessMapper> obj) {
		this.queue = obj;
	}

	public void run() {
		System.out.println("Consumer created : " + Thread.currentThread().getName());
		FileProcessMapper input;
		AtomicInteger count = new AtomicInteger();
		try {
			long startTime = System.currentTimeMillis();
			while (true) {
				input = queue.poll();
				if (input == null) {
					if (ResourcePoolManager.abruptThreadHandler.get())
						break;
					continue;
				}
				if (input.getLine().equalsIgnoreCase(EXITMESSAGE))
					break;
				count.set(count.incrementAndGet());
				StringTokenizer stringParser = new StringTokenizer(input.getLine(), " \t\n\r\f.,;:)(*-!?'\"");
				while (stringParser.hasMoreTokens()) {
					ProcessWordCount.wordProcess(stringParser.nextToken());
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Consumed " + count.get() + " rows by " + Thread.currentThread().getName() + " in "
					+ (endTime - startTime) / 1000 + " seconds");
		} catch (Exception e) {
			ResourcePoolManager.abruptThreadHandler.set(true);
			ResourcePoolManager.numberofConsumerThreadsCompleted
					.set(ResourcePoolManager.numberofConsumerThreadsCompleted.incrementAndGet());
		}
		System.out.println("Consumer thread exited : " + Thread.currentThread().getName());
		ResourcePoolManager.numberofConsumerThreadsCompleted.set(ResourcePoolManager.numberofConsumerThreadsCompleted
				.incrementAndGet());

	}
}
