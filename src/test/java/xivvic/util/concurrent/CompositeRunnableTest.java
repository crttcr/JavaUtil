package xivvic.util.concurrent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
/*
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;
*/

@RunWith(MockitoJUnitRunner.class)
public class CompositeRunnableTest
		extends CompositeRunnable
{
	@Mock private Runnable run_one;
	@Mock private Runnable run_two;

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Test
	public void testRunWithNoTasks()
	{
		// Arrange
		//
		CompositeRunnable cr = new CompositeRunnable();
		
		// Act
		//
		cr.run();
		
		
		// Assert
		//
		// Not sure what I can assert here.
	}

	@Test
	public void testAddNull()
	{
		// Arrange
		//
		CompositeRunnable cr = new CompositeRunnable();
		
		// Act
		//
		boolean ok = cr.add(null);
		
		
		// Assert
		//
		assertFalse(ok);
	}

	@Test
	public void testAdd()
	{
		// Arrange
		//
		CompositeRunnable cr = new CompositeRunnable();
		
		// Act
		//
		boolean ok_a = cr.add(run_one);
		boolean ok_b = cr.add(run_two);
		boolean ok_c = cr.add(run_one);
		
		
		// Assert
		//
		assertTrue(ok_a);
		assertTrue(ok_b);
		assertTrue(ok_c);
	}

	@Test
	public void testTwoRunnablesCalledThreeTimes()
	{
		// Arrange
		//
		CompositeRunnable cr = new CompositeRunnable();
		cr.add(run_one);
		cr.add(run_two);
		cr.add(run_one);
		
		// Act
		//
		cr.run();
		
		// Assert
		//
		verify(run_one, times(2)).run();
		verify(run_two, times(1)).run();
	}

}
