package turtle.player.persistance.turtle.db.structure;

import turtle.player.model.Artist;
import turtle.player.model.Track;
import turtle.player.persistance.source.relational.Field;
import turtle.player.persistance.source.relational.FieldPersistable;
import turtle.player.persistance.source.relational.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * TURTLE PLAYER
 * <p/>
 * Licensed under MIT & GPL
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 * <p/>
 * More Information @ www.turtle-player.co.uk
 *
 * @author Simon Honegger (Hoene84)
 */

public class Tables
{
	public final static Tracks TRACKS = new Tracks();

	public static final class Tracks extends Table<Track>
	{

		public final Field ID = new Field("id");

		public final FieldPersistable<Track, String> TITLE = new FieldPersistable<Track, String>("title")
		{
			public String get(Track instance)
			{
				return instance.GetTitle();
			}
		};

		public final FieldPersistable<Track, Integer> NUMBER = new FieldPersistable<Track, Integer>("number")
		{
			public Integer get(Track instance)
			{
				return instance.GetNumber();
			}
		};

		public final FieldPersistable<Track, String> ARTIST = new FieldPersistable<Track, String>("artist")
		{

			public String get(Track instance)
			{
				return instance.GetArtist().getName();
			}
		};

		public final FieldPersistable<Track, String> ALBUM = new FieldPersistable<Track, String>("album")
		{
			public String get(Track instance)
			{
				return instance.GetAlbum().getName();
			}
		};

		public final FieldPersistable<Track, Double> LENGTH = new FieldPersistable<Track, Double>("length")
		{
			public Double get(Track instance)
			{
				return instance.GetLength();
			}
		};

		public final FieldPersistable<Track, String> SRC = new FieldPersistable<Track, String>("src")
		{
			public String get(Track instance)
			{
				return instance.GetSrc();
			}
		};

		public final FieldPersistable<Track, String> ROOTSRC = new FieldPersistable<Track, String>("rootSrc")
		{

			public String get(Track instance)
			{
				return instance.GetRootSrc();
			}
		};

		public final FieldPersistable<Track, String> ALBUMART = new FieldPersistable<Track, String>("hasAlbumArt")
		{
			public String get(Track instance)
			{
				return instance.albumArt();
			}
		};

		public Tracks()
		{
			super("Tracks");
		}

		@Override
		public List<Field> getFields()
		{
			List<Field> fields = new ArrayList<Field>();
			fields.add(TITLE);
			fields.add(NUMBER);
			fields.add(ARTIST);
			fields.add(ALBUM);
			fields.add(LENGTH);
			fields.add(SRC);
			fields.add(ROOTSRC);
			fields.add(ALBUMART);
			return fields;
		}

	}
}
