package xivvic.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple class that allows you to run a sequence of tasks in 
 * the order they are added to the composite with a single scheduling effort.
 * 
 * @author Reid
 *
 */
public class CompositeRunnable
	implements Runnable
{
	private final List<Runnable> tasks = new ArrayList<>();

	@Override
	public void run()
	{
		Consumer<Runnable> run = r -> r.run();
		
		tasks.forEach(run);
	}
	
	
	public boolean add(Runnable r)
	{
		if (r == null)
			return false;
		
		tasks.add(r);
		return true;
	}
	

}
