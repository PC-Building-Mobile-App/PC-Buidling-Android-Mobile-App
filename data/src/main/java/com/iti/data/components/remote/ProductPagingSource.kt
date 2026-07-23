package com.iti.data.components.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iti.data.components.datasource.ComponentRemoteDataSource
import com.iti.data.components.model.ComponentDataModel
import com.iti.domain.components.model.SearchParams

class ProductPagingSource(
    private val remoteDataSource: ComponentRemoteDataSource,
    private val params: SearchParams
) : PagingSource<Int, ComponentDataModel>() {

    override fun getRefreshKey(state: PagingState<Int, ComponentDataModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComponentDataModel> {
        val page = params.key ?: 0
        val size = params.loadSize

        return try {
            val result = if (isSearchActive()) {
                remoteDataSource.searchProducts(
                    keyword = this.params.query,
                    category = this.params.category?.name,
                    minPrice = this.params.minPrice,
                    maxPrice = this.params.maxPrice,
                    page = page,
                    size = size
                )
            } else {
                remoteDataSource.getAllProducts(
                    page = page,
                    size = size,
                    category = this.params.category?.name
                )
            }

            result.fold(
                onSuccess = { pageResponse ->
                    LoadResult.Page(
                        data = pageResponse.content,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page >= pageResponse.totalPages - 1 || pageResponse.content.isEmpty()) null else page + 1
                    )
                },
                onFailure = { throwable ->
                    Log.e("ProductPagingSource", "Load failure: ${throwable.message}", throwable)
                    LoadResult.Error(throwable)
                }
            )
        } catch (e: Exception) {
            Log.e("ProductPagingSource", "Unexpected error: ${e.message}", e)
            LoadResult.Error(e)
        }
    }

    private fun isSearchActive(): Boolean {
        return !params.query.isNullOrBlank() || 
               params.minPrice != null || 
               params.maxPrice != null
    }
}
