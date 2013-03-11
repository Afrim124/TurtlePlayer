/**
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
 * More Information @ www.turtle-player.co.uk
 *
 * @author Simon Honegger (Hoene84)
 */


package turtle.player.persistance.turtle;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import turtle.player.common.filefilter.FileFilters;
import turtle.player.model.Album;
import turtle.player.model.Artist;
import turtle.player.model.Genre;
import turtle.player.model.Track;
import turtle.player.persistance.turtle.db.TurtleDatabase;
import turtle.player.preferences.Preferences;
import turtle.player.util.Shorty;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FsReader
{
	public static final int MAX_DIR_SCAN_DEPTH = 50;


	public static void scanFiles(Collection<String> mediaFilePaths, TurtleDatabase db, String rootPath)
	{
		MediaMetadataRetriever metaDataReader = new MediaMetadataRetriever();

		try{
			for(String mediaFilePath : mediaFilePaths)
			{
				try
				{
					scanFile(mediaFilePath, rootPath, db, metaDataReader);
				}
				catch (IOException e)
				{
					//log and go on with next File
					Log.v(Preferences.TAG, "failed to process " + mediaFilePath);
				}
			}
		}
		finally {
			metaDataReader.release();
		}
	}

	private static void scanFile(String filePath,
										  String rootPath,
										  TurtleDatabase db,
										  MediaMetadataRetriever metaDataReader) throws IOException
	{
		// http://www.exampledepot.com/egs/java.io/GetFiles.html

		Log.i(Preferences.TAG, "register " + filePath);

		long start = System.currentTimeMillis();

		metaDataReader.setDataSource(filePath);

		Log.v(Preferences.TAG, "init   " + (System.currentTimeMillis() - start) + "ms");

		String rootSrc = filePath.substring(0, filePath.lastIndexOf("/"));

		String title = extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_TITLE);
		int number = parseTrackNumber(extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
		double length = parseDuration(extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_DURATION));
		String artist = extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_ARTIST);
		String album = extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_ALBUM);
		String genre = extractMetadata(metaDataReader, MediaMetadataRetriever.METADATA_KEY_GENRE);

		Log.v(Preferences.TAG, "md     " + (System.currentTimeMillis() - start) + "ms");

		String albumArt = getAlbumArt(rootSrc, rootPath);

		Log.v(Preferences.TAG, "albumAr" + (System.currentTimeMillis() - start) + "ms");

		if (Shorty.isVoid(title))
		{
			title = "Unknown";
		}
		if (Shorty.isVoid(album))
		{
			number = 0; //tracknumbers with no album results in strange sorting
		}

		Track t = new Track(
				  title,
				  number,
				  new Artist(artist),
				  new Album(album),
				  new Genre(genre),
				  length,
				  filePath,
				  rootSrc,
				  albumArt
		);
		Log.v(Preferences.TAG, "created " + (System.currentTimeMillis() - start) + "ms");
		db.push(t);
		Log.v(Preferences.TAG, "pushed  " + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * calls {@link MediaMetadataRetriever#extractMetadata(int)} and removes
	 * chunk after 0 terminated string
	 *
	 * @param keyCode see {@link MediaMetadataRetriever#extractMetadata(int)}
	 * @return
	 */
	static String extractMetadata(MediaMetadataRetriever metaDataReader,
											int keyCode)
	{
		String metaData = Shorty.avoidNull(metaDataReader.extractMetadata(keyCode));

		//replace all chars exept letters and digits, space and dash
		//return metaData.replaceAll("^\\w\\s-,:;?$[]\"]","");

		int indexOfZeroTermination = metaData.indexOf(0);
		return indexOfZeroTermination < 0 ? metaData : metaData.substring(0, indexOfZeroTermination);
	}

	static public Set<String> getMediaFilesPaths(String mediaPath, List<? extends FilenameFilter> filters, boolean recursive, boolean getFirstMatch){

		Set<String> candidates = new HashSet<String>();

		long start = System.currentTimeMillis();

		try
		{
			String[] arguments = recursive ? new String[]{"ls", "-R", mediaPath} : new String[]{"ls", mediaPath};
			Process p = Runtime.getRuntime().exec(arguments);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String currPath = mediaPath + "/";
			String line = "";
			while (line != null)
			{
				line = br.readLine();
				if (line != null)
				{
					if(line.startsWith("/")){
						currPath = line;
						currPath = currPath.lastIndexOf(":") == currPath.length()-1 ? currPath.substring(0, currPath.length()-1) : currPath;
						currPath = currPath.indexOf(".") == 0 ? currPath.replaceFirst(".", mediaPath) : currPath;
						currPath = currPath.lastIndexOf("/") != currPath.length()-1 ? currPath + "/" : currPath;
					}
					else
					{
						if(!Shorty.isVoid(line))
						{
							candidates.add(currPath + line);
						}
					}
				}
			}
		} catch (IOException e)
		{
			//Empty
		}

		//Log.v(Preferences.TAG, "found " + candidates.size() + " "+ (System.currentTimeMillis() - start) + "ms");

		Set<String> acceptedPaths = new HashSet<String>();

		for (FilenameFilter filenameFilter : filters)
		{
			for(String path : candidates)
			{
				//Log.v(Preferences.TAG, "filter " + path + (System.currentTimeMillis() - start) + "ms " + path);
				if(filenameFilter.accept(null, path)){

					acceptedPaths.add(path);
					if(getFirstMatch){
						//Log.v(Preferences.TAG, "completed " + (System.currentTimeMillis() - start) + "ms " + path);
						return acceptedPaths;
					}
				}
			}
		}
		return acceptedPaths;
	}

	static private String getAlbumArt(String mediaFileDir, String rootDir)
	{
		if (mediaFileDir.contains(rootDir)){
			Set<String> albumArtStrings = FsReader.getMediaFilesPaths(mediaFileDir, FileFilters.folderArtFilters, false, true);
			if(!albumArtStrings.isEmpty())
			{
				return albumArtStrings.iterator().next();
			}

			return getAlbumArt(mediaFileDir.substring(0, mediaFileDir.lastIndexOf("/")), rootDir);
		}

		return null;
	}

	static int parseTrackNumber(String trackNumber)
	{
		//strips all chars beginning at first non digit (e.g. 5/10)
		String strippedTrackNumber = trackNumber.replaceAll("\\D.*", "");

		if (strippedTrackNumber.length() > 0)
		{
			return Integer.parseInt(strippedTrackNumber);
		}
		return 0;
	}

	static double parseDuration(String duration)
	{
		try
		{
			return Double.parseDouble(duration);
		} catch (NumberFormatException e)
		{
			Log.v(Preferences.TAG, "Not able to parse duration '" + duration + "': " + e.getMessage());
		}
		return 0;
	}

	final static FileFilter isDIR = new FileFilter()
	{
		final String[] IGNORED_DIRS = new String[]{
				  "/proc/",
				  "/sys/",
				  "/system/",
				  "/proc/",
				  "/root/",
		};

		public boolean accept(File file)
		{
			for (String ignoredDir : IGNORED_DIRS)
			{
				if(file.toString().startsWith(ignoredDir)){
					return false;
				}
			}

			return file.canRead() && file.isDirectory();
		}
	};
}
