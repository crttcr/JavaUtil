package xivvic.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class wraps an ExecutorCompletionService and keeps track of the number of jobs submitted and results retrieved.
 *
 * @author reid
 *
 * @param <V>
 */
public class CountingCompletionService<V>
extends ExecutorCompletionService<V>
{
	private final AtomicLong submits = new AtomicLong();
	private final AtomicLong takes	= new AtomicLong();

	public CountingCompletionService(Executor exec)
	{
		super(exec);
	}

	public CountingCompletionService(Executor exec, BlockingQueue<Future<V>> queue)
	{
		super(exec, queue);
	}

	@Override
	public Future<V> submit(Callable<V> task)
	{
		Future<V> future = super.submit(task);
		submits.incrementAndGet();
		return future;
	}

	@Override
	public Future<V> submit(Runnable task, V result)
	{
		Future<V> future = super.submit(task, result);
		submits.incrementAndGet();
		return future;
	}

	@Override
	public Future<V> take() throws InterruptedException
	{
		Future<V> future = super.take();
		takes.incrementAndGet();
		return future;
	}

	@Override
	public Future<V> poll()
	{
		Future<V> future = super.poll();

		if (future == null)
		{
			return null;
		}

		takes.incrementAndGet();
		return future;
	}

	@Override
	public Future<V> poll(long timeout, TimeUnit unit)
	throws InterruptedException
	{
		Future<V> future = super.poll(timeout, unit);

		if (future == null)
		{
			return null;
		}

		takes.incrementAndGet();
		return future;
	}

	public long inboundCount()
	{
		return submits.get();
	}

	public long outboundCount()
	{
		return takes.get();
	}

	public boolean hasUncompletedTasks()
	{
		long in = 0L;
		long out = 0L;

		synchronized (this)
		{
			out = takes.get();
			in = submits.get();
		}

		return out < in;
	}
}
