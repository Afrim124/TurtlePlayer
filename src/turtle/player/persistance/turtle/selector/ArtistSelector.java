package turtle.player.persistance.turtle.selector;

import android.database.Cursor;
import turtle.player.model.Artist;
import turtle.player.persistance.turtle.db.TurtleDatabase;
import turtle.player.persistance.source.sql.SelectorDistinct;

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

public class ArtistSelector extends SelectorDistinct<Artist>
{
	public ArtistSelector()
	{
		super(TurtleDatabase.TABLE_NAME, TurtleDatabase.KEY_ARTIST);
	}

	@Override
	public Artist createPart(Cursor cursor)
	{
		return new Artist(cursor.getString(0));
	}
}
