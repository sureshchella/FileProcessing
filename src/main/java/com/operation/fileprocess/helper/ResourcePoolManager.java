package com.operation.fileprocess.helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.operation.fileprocess.consumer.FileProcessConsumer;
import com.operation.fileprocess.mapper.FileProcessMapper;
import com.operation.fileprocess.producer.FileProcessProducer;

public class ResourcePoolManager extends ThreadPoolExecutor{
	
	public static AtomicBoolean abruptThreadHandler = new AtomicBoolean(false);
	public static AtomicBoolean producerThreadExited = new AtomicBoolean(false);
	public static AtomicInteger numberOfConsumerThreadsCreated = new AtomicInteger(0);
	public static AtomicInteger numberofConsumerThreadsCompleted = new AtomicInteger(0);
	


	public ResourcePoolManager(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public void createProducerThreads(ResourcePoolManager resourcePool, BlockingQueue<FileProcessMapper> queue){
		resourcePool.execute(new FileProcessProducer(queue, resourcePool.getMaximumPoolSize()));
	}

	public void createConsumerThreads(ResourcePoolManager resourcePool, BlockingQueue<FileProcessMapper> queue) {
		for(int i=0; i<5; i++){
				 resourcePool.execute(new FileProcessConsumer(queue));
				 numberOfConsumerThreadsCreated.set(numberOfConsumerThreadsCreated.incrementAndGet());	
			 }
	}
	public void waitForFileProcessingToComplete(ResourcePoolManager resourcePool) {
		while(true){
			if(producerThreadExited.get()){
				if((numberOfConsumerThreadsCreated.get()==numberofConsumerThreadsCompleted.intValue()) && resourcePool.getActiveCount()==0){
					resourcePool.shutdown();
					System.out.println("*** ResourcePool - Shutted Down! ***");
					break;
				}			
			}
		}
		
	}
	

	
	
}
