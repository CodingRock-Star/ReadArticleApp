package com.project.readarticleapp.repository

import com.project.readarticleapp.data.database.ArticleEntity
import com.project.readarticleapp.data.network.api.ArticleService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


open class BaseRepository : ArticleInterface {

    private lateinit var service: ArticleService

    private val saveArticleList = mutableListOf<ArticleEntity>()

    private lateinit var mockWebService: MockWebServer


    fun setUp() {
        mockWebService = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebService.url("/"))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(ArticleService::class.java)

    }


     fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebService.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }

    public fun tearDown() {
        mockWebService.shutdown()
    }

    override suspend fun getArticlesFromRemoteServer(): Response<String> {
        enqueueResponse("articles.json")
        return service.getArticles()
    }

    override suspend fun saveArticlesDataToDataBase(articleData: List<ArticleEntity>) {
        saveArticleList.addAll(articleData)
    }

    override suspend fun getArticleWithIdFromRemoteServer(itemId: Int): Response<String> {
        enqueueResponse("articleId.json")
        return service.getArticlesWithArticleId(itemId)
    }

    override suspend fun saveArticleDataToDataBaseWithItemId(articleData: ArticleEntity) {
        saveArticleList.add(articleData)
    }

    override fun getArticleDataListFromDataBase(): List<ArticleEntity> {
        return saveArticleList
    }

    override fun getArticleDataDetailsFromDataBase(itemId: Int): ArticleEntity {
        TODO("Not yet implemented")
    }

}