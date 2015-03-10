package com.operation.fileprocess.producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;

import com.operation.fileprocess.helper.FileDownloadProcess;
import com.operation.fileprocess.helper.ProcessWordCount;
import com.operation.fileprocess.helper.ResourcePoolManager;
import com.operation.fileprocess.mapper.FileProcessMapper;

public class FileProcessProducer implements Runnable {
	FileProcessMapper map = null;
	long LINES = 0;
	int POOLSIZE = 0;
	private final String EXITMESSAGE = "#@EXIT!@#";
	private BlockingQueue<FileProcessMapper> queue;

	// ResourcePoolManager producerConsumerService = new ResourcePoolManager();

	public FileProcessProducer(BlockingQueue<FileProcessMapper> obj, int size) {
		this.queue = obj;
		this.POOLSIZE = size;
	}

	@SuppressWarnings("resource")
	public void run() {
		System.out.println("Producer created : " + Thread.currentThread().getName());
		try {
			long startTime = System.currentTimeMillis();
			String fileName = FileDownloadProcess.downloadFile();
			ProcessWordCount.outputFile = fileName;
			BufferedReader br = new BufferedReader(new FileReader(fileName + "/DownloadedFile.txt"));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				if (!currentLine.isEmpty()) {
					LINES = LINES + 1;
					map = new FileProcessMapper(currentLine);
					queue.put(map);
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Producing completed in : " + (endTime - startTime) / 1000 + " seconds" + " for "
					+ LINES + " rows by thread " + Thread.currentThread().getName());
		} catch (InterruptedException e) {

		} catch (Exception e) {
			ResourcePoolManager.abruptThreadHandler.set(true);
			ResourcePoolManager.producerThreadExited.set(true);
			e.printStackTrace();
		}

		FileProcessMapper map = new FileProcessMapper(EXITMESSAGE);
		try {
			for (int i = 0; i < POOLSIZE; i++) {
				queue.put(map);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Producer Thread exited : " + Thread.currentThread().getName());
		ResourcePoolManager.producerThreadExited.set(true);

	}

}
