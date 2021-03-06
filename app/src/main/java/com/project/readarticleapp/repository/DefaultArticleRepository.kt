package com.project.readarticleapp.repository

import com.project.readarticleapp.data.database.ArticleDao
import com.project.readarticleapp.data.database.ArticleEntity
import com.project.readarticleapp.data.network.api.ArticleService
import retrofit2.Response
import javax.inject.Inject


class DefaultArticleRepository @Inject constructor(
    private val dao: ArticleDao,
    private val service: ArticleService,
) : ArticleInterface {
    override suspend fun getArticlesFromRemoteServer(): Response<String> {
        return service.getArticles()
    }

    override suspend fun saveArticlesDataToDataBase(articleData: List<ArticleEntity>) {
        return dao.insertArticles(articleData)
    }

    override suspend fun getArticleWithIdFromRemoteServer(itemId: Int): Response<String> {
        return service.getArticlesWithArticleId(itemId)
    }

    override suspend fun saveArticleDataToDataBaseWithItemId(articleData: ArticleEntity) {
        return dao.insertArticlesWithId(articleData)
    }

    override fun getArticleDataListFromDataBase(): List<ArticleEntity> {
        return dao.getArticlesFromDataBase()
    }

    override fun getArticleDataDetailsFromDataBase(itemId: Int): ArticleEntity {
        return dao.getArticleWithId(itemId)
    }
}