/*
 * 
 * TURTLE PLAYER
 * 
 * Licensed under MIT & GPL
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Created by Edd Turtle (www.eddturtle.co.uk)
 * More Information @ www.turtle-player.co.uk
 * 
 */

// Package
package turtle.player.preferences;


import android.content.Context;
import turtle.player.dirchooser.DirChooserConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Preferences
{

	public static final String TAG = "TurtlePlayer";

	final Context context;

	public Preferences(Context context)
	{
		this.context = context;
	}

	public <T> void set(Key<T> key, T object)
	{
		SharedPreferencesAccess.putValue(context, key, object);
		notify(key);
	}

	public <T> T get(Key<T> key)
	{
		return SharedPreferencesAccess.getValue(context, key);
	}

	/**
	 * Sideeffect: corrects the stored path if not existing and fires notification
	 * @return always an exiting File
	 */
	public File getExitstingMediaPath()
	{
		File existingPath = getExistingParentFolderFile(get(Keys.MEDIA_DIR));
		set(Keys.MEDIA_DIR, existingPath.getPath());
		return existingPath;
	}

	private File getExistingParentFolderFile(String path)
	{
		if (path == null || !path.startsWith(DirChooserConstants.PATH_SEPERATOR))
		{
			return new File(DirChooserConstants.PATH_SEPERATOR);
		}

		//Go up untill the string represents a valid directory
		while (!new File(path).isDirectory())
		{
			String pathWihoutTrailngSep = path.endsWith(DirChooserConstants.PATH_SEPERATOR) ?
					  path.substring(0, path.length() - DirChooserConstants.PATH_SEPERATOR.length()) :
					  path;

			path = pathWihoutTrailngSep.substring(0, path.lastIndexOf(DirChooserConstants.PATH_SEPERATOR));
		}
		return new File(path);
	}

	//Observable -------------------------------------------------

	final List<PreferencesObserver> observers = new ArrayList<PreferencesObserver>();

	private void notify(Key key)
	{
		for (PreferencesObserver observer : observers)
		{
			observer.changed(key);
		}
	}

	public void addObserver(PreferencesObserver observer)
	{
		observers.add(observer);
	}

	public void removeObserver(PreferencesObserver observer)
	{
		observers.remove(observer);
	}
}
