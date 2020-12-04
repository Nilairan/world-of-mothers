package com.madispace.domain.usecases

import com.madispace.domain.models.Category
import com.madispace.domain.models.ProductShort
import com.madispace.domain.models.ui.CatalogModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/2/20
 */
interface GetCatalogModelUseCase {
    operator fun invoke(): Observable<CatalogModel>
}

class GetCatalogModelUseCaseImpl : GetCatalogModelUseCase {

    override fun invoke(): Observable<CatalogModel> {
        val categoryList: ArrayList<Category> = arrayListOf()
        categoryList.apply {
            add(Category("Для малышей", ""))
            add(Category("Дети до 3-х лет", ""))
            add(Category("Дети до 7-ми лет", ""))
            add(Category("Дети до 10-ми лет", ""))
            add(Category("Дети до 11-ми лет", ""))
            add(Category("Дети до 12-ми лет", ""))
            add(Category("Дети до 13-ми лет", ""))
            add(Category("Дети до 15-ми лет", ""))
            add(Category("Дети до 18-ми лет", ""))
        }
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
        return Observable.just(CatalogModel(categoryList, productShortList))
                .subscribeOn(Schedulers.io())
                .delay(1300, TimeUnit.MILLISECONDS)
    }
}