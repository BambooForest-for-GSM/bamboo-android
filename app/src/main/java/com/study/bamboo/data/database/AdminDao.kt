//package com.study.bamboo.data.database
//
//import androidx.paging.DataSource
//import androidx.paging.PagingSource
//import androidx.room.*
//import com.study.bamboo.data.database.entity.DeleteEntity
//import com.study.bamboo.data.database.entity.PendingEntity
//import com.study.bamboo.data.database.entity.RejectEntity
//
//@Dao
//interface AdminDao {
//
////    @Insert(onConflict = OnConflictStrategy.REPLACE)
////    suspend fun insertAcceptPost(adminAccept: AcceptEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPendingPost(adminPending: PendingEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRejectPost(adminReject: RejectEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertDeletePost(adminDelete: DeleteEntity)
//
//
////    @Query("SELECT * FROM acceptTable ORDER BY id ASC")
////    fun getAllAccept( ): DataSource.Factory<Int,AcceptEntity>
//
//    @Query("SELECT * FROM pendingTable ORDER BY id ASC")
//    fun getAllPending(): DataSource.Factory<Int,PendingEntity>
//
//    @Query("SELECT * FROM rejectTable ORDER BY id ASC")
//    fun getAllReject(): DataSource.Factory<Int,RejectEntity>
//
//    @Query("SELECT * FROM deleteTable ORDER BY id ASC")
//    fun getAllDelete(): DataSource.Factory<Int,DeleteEntity>
////
////    @Query("DELETE FROM acceptTable ")
////    suspend fun acceptPostClear()
//
//    @Query("DELETE FROM deleteTable ")
//    suspend fun deletePostClear()
//
//    @Query("DELETE FROM rejectTable ")
//    suspend fun rejectPostClear()
//
//    @Query("DELETE FROM pendingTable ")
//    suspend fun pendingPostClear()
//
//}