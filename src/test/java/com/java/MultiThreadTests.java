package com.java;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread{
	
	private Integer tid;
	
	public MyThread(Integer tid){
		this.tid=tid; 
	}
	
	@Override
	public void run(){
		try{
			for(int i=0;i<10;i++){
				Thread.sleep(1000);
				System.out.println(tid+":"+i);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable{
	
	private BlockingQueue<String> q;
	
	public Consumer(BlockingQueue<String> q){
		this.q=q;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true){
				System.out.println(Thread.currentThread().getName()+":"+q.take());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class Producer implements Runnable{
	
	private BlockingQueue<String> q;
	
	public Producer(BlockingQueue<String> q){
		this.q=q;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			for(int i=0;i<100;i++){
				Thread.sleep(10);
				q.put(String.valueOf(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
 
public class MultiThreadTests {
	
	public static void testThread(){
//		for(int i=0;i<10;i++){
//			new MyThread(i).start();
//		}
		
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						for(int i=0;i<10;i++){
							Thread.sleep(1000);
							System.out.println("T2:"+i);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	private static final Object obj=new Object();
	
	public static void testSynchronized1(){
		synchronized (obj) {
			try{
				for(int i=0;i<10;i++){
					Thread.sleep(100);
					System.out.println("S1:"+i);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void testSynchronized2(){
		synchronized (obj) {
			try{
				for(int i=0;i<10;i++){
					Thread.sleep(100);
					System.out.println("S2:"+i);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void testSynchronized(){
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					testSynchronized1();
					testSynchronized2();
				}
			}).start();;
		}
	}
	
	public static void testBlockingQueue(){
		BlockingQueue<String> q=new ArrayBlockingQueue<>(10);
		new Thread(new Producer(q)).start();
		new Thread(new Consumer(q),"Consumer1").start();
		new Thread(new Consumer(q),"Consumer2").start();
	}
	
	private static ThreadLocal<Integer> threadLocalUserIds=new ThreadLocal<>();
	private static int userId;
	
	public static void testThreadLocal(){
		for(int i=0;i<10;i++){
			final int finalI=i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						threadLocalUserIds.set(finalI);
						Thread.sleep(100);
						System.out.println("ThreadLocal:"+threadLocalUserIds.get());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		for(int i=0;i<10;i++){
			final int finalI=i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						userId=finalI;
						Thread.sleep(100);
						System.out.println("UserId:"+userId);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public static void textExecutor(){
//		ExecutorService service=Executors.newSingleThreadExecutor();
		ExecutorService service=Executors.newFixedThreadPool(2);
		service.submit(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++){
					try{
						Thread.sleep(100);
						System.out.println("Executor1:"+i);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		service.submit(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++){
					try{
						Thread.sleep(100);
						System.out.println("Executor2:"+i);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		service.shutdown();
		
		while(!service.isTerminated()){
			try{
				Thread.sleep(100);
				System.out.println("Wait for terminate.");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static int counter=0;
	private static AtomicInteger atomicInteger=new AtomicInteger(0);
	
	public static void testWithoutAtomic(){
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						Thread.sleep(100);
						for(int i=0;i<10;i++){
							counter++;
							System.out.println(counter);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public static void testWithAtomic(){
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						Thread.sleep(100);
						for(int i=0;i<10;i++){
							counter++;
							System.out.println(atomicInteger.incrementAndGet());
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public static void testAtomic(){
//		testWithoutAtomic();
		testWithAtomic();
	}
	
	public static void textFuture(){
		ExecutorService service=Executors.newSingleThreadExecutor();
//		ExecutorService service=Executors.newFixedThreadPool(2);
		Future<Integer> future=service.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Thread.sleep(1000);
				return 1;
			}
		});
		
		service.shutdown();
		try{
//			System.out.println(future.get());
			System.out.println(future.get(100, TimeUnit.MILLISECONDS));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		testThread();
//		testSynchronized();
//		testBlockingQueue();
//		testThreadLocal();
//		textExecutor();
//		testAtomic();
		textFuture();
	}

}
