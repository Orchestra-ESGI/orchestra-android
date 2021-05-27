package core.rest.services

import core.rest.model.House
import io.reactivex.Observable
import retrofit2.http.*

interface HouseServices {
    @GET("/houses/get/all")
    fun getAllHouses() : Observable<List<House>>

    @DELETE("/houses/remove/group")
    fun removeScene(@Body idHouse : String) : Observable<List<House>>
}