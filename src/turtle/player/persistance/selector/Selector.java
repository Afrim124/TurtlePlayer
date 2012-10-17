package turtle.player.persistance.selector;

import turtle.player.model.Instance;
import turtle.player.model.InstanceCreator;
import turtle.player.persistance.creator.Creator;
import turtle.player.persistance.filter.Filter;
import turtle.player.persistance.query.Query;
import turtle.player.util.InstanceAdapter;

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
 * @param <Q> eg sql String
 * @param <I> resulting instance
 * @param <C> eg cursor
 */
public interface Selector<Q, I, C> extends Creator<I, C>
{
	Q get();
	I create(C queryResult);
}
