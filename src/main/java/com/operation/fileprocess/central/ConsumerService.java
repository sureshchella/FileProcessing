package com.operation.fileprocess.central;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.operation.fileprocess.helper.ProcessWordCount;
import com.operation.fileprocess.helper.ResourcePoolManager;
import com.operation.fileprocess.mapper.FileProcessMapper;

public class ConsumerService {

	public static void main(String args[]) throws IOException {

		BlockingQueue<FileProcessMapper> queue = new ArrayBlockingQueue<FileProcessMapper>(50);
		ProcessWordCount processWordCountObj = new ProcessWordCount();
		ResourcePoolManager resourcePool = new ResourcePoolManager(5, 10, 10, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(5));

		resourcePool.createProducerThreads(resourcePool, queue);
		resourcePool.createConsumerThreads(resourcePool, queue);
		resourcePool.waitForFileProcessingToComplete(resourcePool);
		processWordCountObj.writeToCSV();
		processWordCountObj.wordAnalyzer();
	}

}
