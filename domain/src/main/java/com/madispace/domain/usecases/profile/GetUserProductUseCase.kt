package com.madispace.domain.usecases.profile

import com.madispace.domain.models.ProductShort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Ivan Kholodov - nilairan@gmail.com
 * @date 12/6/20
 */
interface GetUserProductUseCase {
    operator fun invoke(): Flow<List<ProductShort>>
}

class GetUserProductUseCaseImpl : GetUserProductUseCase {
    override fun invoke(): Flow<List<ProductShort>> {
        return flow<List<ProductShort>> {
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
            }
            emit(productShortList)
        }
    }

}