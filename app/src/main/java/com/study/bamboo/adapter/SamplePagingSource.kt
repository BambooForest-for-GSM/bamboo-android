package com.study.bamboo.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState

/*
class SamplePagingSource constructor(private val service: SampleBackendService) :
    PagingSource<Int, String>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val next = params.key ?: 0
            val response = service.getPagingData(next)
            LoadResult.Page(
                data =
                response.data, prevKey = if (next == 0) null else next - 1, nextKey = next + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(
                anchorPosition
            )?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
*/

