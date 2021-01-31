package fr.istic.mob.star.star1adrk.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import fr.istic.mob.star.star1adrk.R
import fr.istic.mob.star.star1adrk.database.models.Route


class LineAdapter(
    mContext: Context,
    resourceId: Int,
    routes: List<Route>,
) : ArrayAdapter<Route>(mContext, resourceId, routes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return renderView(convertView, parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return renderView(convertView, parent, position)
    }

    private fun renderView(
        convertView: View?,
        parent: ViewGroup,
        position: Int
    ): View {
        val layoutView: View
        val viewHolder: ViewHolder
        val inflater = LayoutInflater.from(context);
        if (convertView == null) {
            layoutView = inflater.inflate(R.layout.route_line, parent, false)
            // Do some initialization

            //Retrieve the view on the item layout and set the value.
            viewHolder = ViewHolder(layoutView)
            layoutView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            layoutView = convertView
        }

        //Retrieve your object
        val route = getItem(position) as Route
        viewHolder.txt.text = route.routeShortName
        viewHolder.txt.setTextColor(Color.parseColor("#" + route.routeColor))
        return layoutView
    }

    private class ViewHolder(view: View) {
        val txt: TextView = view.findViewById<View>(R.id.line) as TextView
    }
}