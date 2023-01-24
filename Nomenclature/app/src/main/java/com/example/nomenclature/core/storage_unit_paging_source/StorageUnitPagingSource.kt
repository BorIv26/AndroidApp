package com.example.nomenclature.core.storage_unit_paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nomenclature.data.remote.NomenclatureApi
import com.example.nomenclature.data.remote.dto.StorageUnitItemDto
import com.example.nomenclature.data.remote.dto.filters
import com.example.nomenclature.data.remote.dto.pagination_scroll
import kotlinx.coroutines.*

class StorageUnitPagingSource(
    private val filters: filters?,
    private val nomenclatureApi: NomenclatureApi,
): PagingSource<Int, StorageUnitItemDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StorageUnitItemDto> {
        val limit = params.loadSize
        val offset = params.key ?: 0
        val paginationScroll = pagination_scroll(
            offset = offset,
            limit = limit,
        )

        var deferredPagingSource: Deferred<LoadResult<Int, StorageUnitItemDto>>

        coroutineScope {
            deferredPagingSource = async(Dispatchers.IO) {
                try {
                    val storageUnits = nomenclatureApi.getStorageUnits(
                        filters = filters,
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

    override fun getRefreshKey(state: PagingState<Int, StorageUnitItemDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}