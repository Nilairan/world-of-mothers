package com.madispace.domain.usecases

import com.madispace.domain.models.ProductShort
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/3/20
 */
interface GetFavoritesProductUseCase {
    operator fun invoke(): Observable<List<ProductShort>>
}

class GetFavoritesProductUseCaseImpl : GetFavoritesProductUseCase {
    override fun invoke(): Observable<List<ProductShort>> {
        val productShortList: ArrayList<ProductShort> = arrayListOf()
        productShortList.apply {
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 240.0))
            add(ProductShort(1, "Соска силиконовая", "", 250.0))
            add(ProductShort(1, "Соска силиконовая", "", 260.0))
            add(ProductShort(1, "Соска силиконовая", "", 270.0))
            add(ProductShort(1, "Соска силиконовая", "", 210.0))
            add(ProductShort(1, "Соска силиконовая", "", 220.0))
            add(ProductShort(1, "Соска силиконовая", "", 290.0))
            add(ProductShort(1, "Соска силиконовая", "", 2320.0))
            add(ProductShort(1, "Соска силиконовая", "", 2430.0))
            add(ProductShort(1, "Соска силиконовая", "", 2230.0))
            add(ProductShort(1, "Соска силиконовая", "", 2340.0))
            add(ProductShort(1, "Соска силиконовая", "", 130.0))
            add(ProductShort(1, "Соска силиконовая", "", 2230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
            add(ProductShort(1, "Соска силиконовая", "", 230.0))
        }
        return Observable.just(productShortList.toList())
            .subscribeOn(Schedulers.io())
            .delay(1300, TimeUnit.MILLISECONDS)
    }

}