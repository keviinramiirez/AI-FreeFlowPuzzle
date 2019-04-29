package app;
import java.awt.Color;
import java.util.Iterator;

public class ColorManager implements Iterable<Color>
{
	
	int i = 0;
	public static final Color DEFAULT_COLOR = new Color(240, 240, 240);
	Color[] colors = {
			Color.BLUE, Color.DARK_GRAY, Color.GRAY, Color.GREEN, 
			Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, 
			Color.YELLOW, Color.CYAN
	};

	
	public ColorManager() {}
	public class ColorIterator implements Iterator<Color> 
	{
		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public Color next() {
			if (i >= colors.length)
				i = 0;
			return colors[i++];
		}
	}

	@Override
	public Iterator<Color> iterator() {
		return new ColorIterator();
	}	
}