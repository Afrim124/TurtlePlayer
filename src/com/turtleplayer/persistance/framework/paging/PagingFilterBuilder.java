package com.turtleplayer.persistance.framework.paging;

import com.turtleplayer.persistance.framework.filter.FieldFilter;
import com.turtleplayer.persistance.framework.filter.Filter;
import com.turtleplayer.persistance.framework.filter.FilterSet;
import com.turtleplayer.persistance.framework.filter.Operator;
import com.turtleplayer.persistance.framework.sort.FieldOrder;
import com.turtleplayer.persistance.framework.sort.OrderSet;
import com.turtleplayer.persistance.framework.sort.OrderVisitor;
import com.turtleplayer.persistance.framework.sort.RandomOrder;
import com.turtleplayer.persistance.source.relational.FieldPersistable;

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

public class PagingFilterBuilder<I> implements OrderVisitor<I, Filter>
{
	final I instance;

	public PagingFilterBuilder(I instance)
	{
		this.instance = instance;
	}

	public <T> Filter visit(FieldOrder<I, T> fieldOrder)
	{
		FieldPersistable<I, ?> field = fieldOrder.getField();

		final Operator op;

		switch(fieldOrder.getOrder()){
			case ASC:
				op = Operator.GT;
				break;
			case DESC:
				op = Operator.LT;
				break;
		   default:
				throw new IllegalArgumentException();
		}
		return new FieldFilter(field, op, field.get(instance).toString());
	}

	public Filter visit(RandomOrder orderFilter)
	{
		return null;
	}

	public Filter visit(OrderSet orderFilter)
	{
		if(!orderFilter.isEmpty()){
			Filter filterSet = new FilterSet();
			for( int i = 0; i < orderFilter.getOrders().size() -1; i++)
			{
				final Filter finalFilterSet = filterSet;
				filterSet = orderFilter.getOrders().get(i).accept(new OrderVisitor<I, Filter>()
				{
					public Filter visit(RandomOrder orderFilter)
					{
						// :-)
						return null;
					}

					public <T> Filter visit(FieldOrder<I, T> fieldOrder)
					{
						FieldPersistable<I, T> field = fieldOrder.getField();
						return new FilterSet(
								  finalFilterSet,
								  new FieldFilter(fieldOrder.getField(), Operator.EQ, field.get(instance).toString()));
					}

					public Filter visit(OrderSet orderFilter)
					{
						return this.visit(orderFilter);
					}
				});
			}

			return new FilterSet(filterSet, orderFilter.getOrders().get(orderFilter.getOrders().size()-1).accept(this));
		}
		else
		{
			return null;
		}
	}

}
