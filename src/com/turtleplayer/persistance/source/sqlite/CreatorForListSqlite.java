package com.turtleplayer.persistance.source.sqlite;

import com.turtleplayer.persistance.framework.creator.Creator;
import com.turtleplayer.persistance.framework.creator.CreatorForList;

import android.database.Cursor;

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

/**
 * @param <I> resulting set contains instance I
 */
public class CreatorForListSqlite<I> extends CreatorForList<I, Cursor, Cursor>
{
    public CreatorForListSqlite(Creator<I, Cursor> creator)
    {
        super(creator);
    }

    @Override
    public boolean hasNext(Cursor queryResult)
    {
        return !queryResult.isLast() && !queryResult.isAfterLast();
    }

    @Override
    public Cursor next(Cursor queryResult)
    {
        queryResult.moveToNext();
        return queryResult;
    }
}
