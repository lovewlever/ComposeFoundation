package com.gq.basic.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gq.basic.compose.PVUris
import com.gq.basic.viewmodel.repository.PictureVideoRepository

class PictureVideoPagingSource(
    private val pictureVideoRepository: PictureVideoRepository
): PagingSource<Int, PVUris>() {
    override fun getRefreshKey(state: PagingState<Int, PVUris>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PVUris> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 0
            val response = pictureVideoRepository.queryVideoAndPicUriList(nextPageNumber, 30)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = if (response.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }
}