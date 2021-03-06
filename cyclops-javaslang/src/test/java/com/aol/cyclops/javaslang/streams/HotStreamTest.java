package com.aol.cyclops.javaslang.streams;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import javaslang.collection.Stream;

import org.junit.Test;

import com.aol.cyclops.sequence.SequenceM;

import fj.data.Seq;

public class HotStreamTest {
	static final Executor exec = Executors.newFixedThreadPool(1);
	volatile Object value;
	@Test
	public void hotStream() throws InterruptedException{
		value= null;
		CountDownLatch latch = new CountDownLatch(1);
		StreamUtils.hotStream(Stream.ofAll(1,2,3)
				.peek(v->value=v)
				.peek(v->latch.countDown())
				,exec);
		
		latch.await();
		assertTrue(value!=null);
	}
	@Test
	public void hotStreamConnect() throws InterruptedException{
		
		
		for(int i=0;i<1_000;i++)
		{
			System.out.println(i);
			value= null;
			CountDownLatch latch = new CountDownLatch(1);
			StreamUtils.futureOperations(StreamUtils.hotStream(Stream.range(0,Integer.MAX_VALUE)
					.take(100)
					.peek(v->value=v)
					.peek(v->latch.countDown())
					.peek(System.out::println)
					,exec)
					.connect()
					.take(100)
					,ForkJoinPool.commonPool())
					.forEach(System.out::println);
			
			latch.await();
			assertTrue(value!=null);
		}
	}
	
	@Test
	public void hotStreamConnectBlockingQueue() throws InterruptedException{
		value= null;
		CountDownLatch latch = new CountDownLatch(1);
		StreamUtils.futureOperations(StreamUtils.hotStream(Stream.range(0,Integer.MAX_VALUE)
				.take(1000)
				.peek(v->value=v)
				.peek(v->latch.countDown())
				,exec)
				.connect(new LinkedBlockingQueue<>())
				.take(100)
				,ForkJoinPool.commonPool())
				.forEach(System.out::println);
		
		latch.await();
		assertTrue(value!=null);
	}
}
