package com.swpu.ylq.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.swpu.ylq.sunnyweather.R
import com.swpu.ylq.sunnyweather.logic.model.Place
import com.swpu.ylq.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.place_item.view.*

class PlaceAdapter(private  val fragment:PlaceFragment,private val placeList:List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName : TextView = view.findViewById(R.id.plaveName)
        val placeAddress :TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position  = holder.adapterPosition
            val place = placeList[position]
            //判断placeFragment所处的Activity
            val activity = fragment.activity
            if(activity is WeatherActivity){
                activity.drawerLayout.closeDrawers() //如果在weatherActivity的中就关闭滑动菜单
                activity.viewModel.locationLng=place.location.lng
                activity.viewModel.locationLat=place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            }else {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return holder
    }

    override fun getItemCount()=placeList.size

    override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text = place.address
    }
}