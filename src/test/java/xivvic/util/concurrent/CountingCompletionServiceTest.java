package xivvic.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CountingCompletionServiceTest
{
	private static final String CALL_RESULT = "Call completed";

	private Executor							 exec;

	@Before
	public void before()
	{
		exec = Executors.newFixedThreadPool(1);
	}

	@After
	public void after()
	{
		exec = null;
	}

	@Test
	public void onCreate_withNoTasks_thenCountsAreZero()
	{
		// Arrange
		//
		CountingCompletionService<String> subject = new CountingCompletionService<>(exec);

		// Act
		//
		long  in = subject.inboundCount();
		long out = subject.outboundCount();
		boolean incomplete = subject.hasUncompletedTasks();

		// Assert
		//
		assertEquals(0L, in);
		assertEquals(0L, out);
		assertFalse(incomplete);
	}

	@Test
	public void onCreate_withSubmitsOnly_thenCountsAreCorrect()
	{
		// Arrange
		//
		CountingCompletionService<String> subject = new CountingCompletionService<>(exec);
		Callable<String> c = createCallable();
		Runnable         r = createRunnable();
		createRunnable();
		subject.submit(c);
		subject.submit(r, "Done");

		// Act
		//
		long  in = subject.inboundCount();
		long out = subject.outboundCount();
		boolean incomplete = subject.hasUncompletedTasks();

		// Assert
		//
		assertEquals(2L, in);
		assertEquals(0L, out);
		assertTrue(incomplete);
	}

	@Test
	public void onCreate_withMatchedCalls_thenCountsAreCorrect() throws Exception
	{
		// Arrange
		//
		CountingCompletionService<String> subject = new CountingCompletionService<>(exec);
		createCallable();
		createRunnable();
		createRunnable();
		Future<String> future = subject.submit(createCallable());
		Future<String> f2 = subject.take();
		String result = future.get();
		String r2 = f2.get();

		// Act
		//
		long  in = subject.inboundCount();
		long out = subject.outboundCount();
		boolean incomplete = subject.hasUncompletedTasks();

		// Assert
		//
		assertEquals(1L, in);
		assertEquals(1L, out);
		assertFalse(incomplete);
		assertNotNull(result);
		assertEquals(CALL_RESULT, result);
		assertNotNull(r2);
		assertEquals(CALL_RESULT, r2);
	}

	@Test
	public void onCreate_withMatchedCallsUsingPoll_thenCountsAreCorrect() throws Exception
	{
		// Arrange
		//
		CountingCompletionService<String> subject = new CountingCompletionService<>(exec);
		createCallable();
		createRunnable();
		createRunnable();
		Future<String> future = subject.submit(createCallable());
		String result = future.get();

		// Note this poll has to occur after the future.get() call above
		// or there is a good chance the completion queue will be empty and
		// poll will simply return null.
		//
		Future<String> f2 = subject.poll();
		String r2 = f2.get();

		// Act
		//
		long  in = subject.inboundCount();
		long out = subject.outboundCount();
		boolean incomplete = subject.hasUncompletedTasks();

		// Assert
		//
		assertEquals(1L, in);
		assertEquals(1L, out);
		assertFalse(incomplete);
		assertNotNull(result);
		assertEquals(CALL_RESULT, result);
		assertNotNull(r2);
		assertEquals(CALL_RESULT, r2);
	}

	////////////////////////////////////
	// Helper Methods                 //
	////////////////////////////////////

	private Runnable createRunnable()
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() {}
		};

		return r;
	}

	private Callable<String> createCallable()
	{
		Callable<String> c = new Callable<String>()
		{
			@Override
			public String call()
			{
				return CALL_RESULT;
			}
		};

		return c;
	}
}
