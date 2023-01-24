package com.example.nomenclature.core.product_paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nomenclature.data.remote.NomenclatureApi
import com.example.nomenclature.data.remote.dto.ProductDto
import com.example.nomenclature.data.remote.dto.pagination_scroll
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ProductPagingSource(
    private val search: String?,
    private val nomenclatureApi: NomenclatureApi,
): PagingSource<Int, ProductDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDto> {
        val limit = params.loadSize
        val offset = params.key ?: 0
        val paginationScroll = pagination_scroll(
            offset = offset,
            limit = limit,
        )

        var deferredPagingSource: Deferred<LoadResult<Int, ProductDto>>

        coroutineScope {
            deferredPagingSource = async(Dispatchers.IO) {
                try {
                    val storageUnits = nomenclatureApi.getProducts(
                        search = search,
                        pagination_scroll = paginationScroll,
                    )
                    LoadResult.Page(
                        data = storageUnits.items,
                        nextKey = if(storageUnits.has_next) {
                            offset + limit.coerceAtMost(NomenclatureApi.MAX_STORAGE_UNITS_ITEMS_PAGE_SIZE)
                        } else {
                            null
                        },
                        prevKey = if(offset == 0) null else offset,
                    )
                }
                catch (throwable: Throwable) {
                    LoadResult.Error(
                        throwable
                    )
                }
            }
        }

        return deferredPagingSource.await()
    }

    override fun getRefreshKey(state: PagingState<Int, ProductDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}