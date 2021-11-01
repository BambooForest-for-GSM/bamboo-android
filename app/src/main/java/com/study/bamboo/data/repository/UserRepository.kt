package com.study.bamboo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.study.bamboo.data.network.models.user.request.EmojiRequest
import com.study.bamboo.data.network.models.user.UserPostDTO
import com.study.bamboo.data.network.models.user.postcreate.PostCreateRequest
import com.study.bamboo.data.network.models.user.report.ReportRequest
import com.study.bamboo.data.network.user.UserApi
import com.study.bamboo.data.paging.GetPostSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    suspend fun getPost(page: Int, status: String) = userApi.getPost(page, status)
    suspend fun transferPostCreate(request: PostCreateRequest) = userApi.transferPostCreate(request)
    suspend fun getVerify() = userApi.getVerify()
    suspend fun getCount() = userApi.getCount()
    fun getPagingData(): Flow<PagingData<UserPostDTO>> {
        return Pager(PagingConfig(pageSize = 20)) { GetPostSource(userApi) }.flow
    }

    suspend fun report(id: String, body: ReportRequest) = userApi.report(id, body)

    suspend fun postEmoji(token: String,emoji:String, body: EmojiRequest?) = userApi.postEmoji(token,emoji, body)
    suspend fun deleteEmoji(token: String, emoji:String,body: EmojiRequest?) = userApi.deleteEmoji(token,emoji,body)
    suspend fun postLogin(token : String)= userApi.postLogin(token)
}