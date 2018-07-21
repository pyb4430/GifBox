package com.example.taylor.gifbox

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.example.taylor.gifbox.adapter.TrendingBoundaryCallback
import com.example.taylor.gifbox.controller.DataController
import com.example.taylor.gifbox.module.AppComponent
import com.example.taylor.gifbox.response.GifListResponse
import com.example.taylor.gifbox.viewmodel.MainActivityVM
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.disposables.Disposable
import io.reactivex.Scheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


fun readJsonFile(testClass: Any, filename: String): String {
    val stream = testClass.javaClass.classLoader.getResource(filename).openStream()
    var jsonString: String? = null
    stream.bufferedReader().use {
        jsonString = it.readText()
    }
    if (jsonString == null) {
        throw IOException("Error reading json string from file")
    }
    return jsonString!!
}


fun <T> getObjFromJsonFile(clazz: Class<T>, filename: String): T {
    val jsonString = readJsonFile(clazz, filename)
    val jsonAdapter = moshi.adapter(clazz)
    return jsonAdapter.fromJson(jsonString) ?: throw IOException("Error parsing string to json")
}

fun <T> getObjFromJsonString(clazz: Class<T>, jsonString: String): T {
    val jsonAdapter = moshi.adapter(clazz)
    return jsonAdapter.fromJson(jsonString) ?: throw IOException("Error parsing string to json")
}

class RxImmediateSchedulerRule : TestRule {
    private val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            // this prevents StackOverflowErrors when scheduling with a delay
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler { _ -> immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler { _ -> immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}


// Found here: [http://kotlinblog.com/2017/11/17/testing-room-and-livedata/]
@Throws(InterruptedException::class)
fun <T> LiveData<T>.getValueBlocking(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(2, TimeUnit.SECONDS)
    return value
}

class TestAppComponent: AppComponent {

    val dataController: DataController = mock()

    override fun inject(mainActivityVM: MainActivityVM) {
        mainActivityVM.dataController = dataController
    }

    override fun inject(trendingBoundaryCallback: TrendingBoundaryCallback) {
        trendingBoundaryCallback.dataController = dataController
    }
}

/**
 * TODO: Move the jsonString here to a file and use [getObjFromJsonFile] to parse it,
 * current implementation with source sets is not working
 **/
fun getGifListResponse(): GifListResponse {
    val jsonString = "{\"data\":[{\"type\":\"gif\",\"id\":\"3vCtbqmOnksLu\",\"slug\":\"after-carter-pedestrian-3vCtbqmOnksLu\",\"url\":\"https:\\/\\/giphy.com\\/gifs\\/after-carter-pedestrian-3vCtbqmOnksLu\",\"bitly_gif_url\":\"https:\\/\\/gph.is\\/1PN9vv9\",\"bitly_url\":\"https:\\/\\/gph.is\\/1PN9vv9\",\"embed_url\":\"https:\\/\\/giphy.com\\/embed\\/3vCtbqmOnksLu\",\"username\":\"ufc\",\"source\":\"https:\\/\\/www.lipstickalley.com\\/showthread.php\\/643470-Richard-Sherman-amp-Doug-Baldwin-Dissing-Chris-Carter-After-Calling-Their-Receivers-Pedestrian!\",\"rating\":\"g\",\"content_url\":\"\",\"source_tld\":\"www.lipstickalley.com\",\"source_post_url\":\"https:\\/\\/www.lipstickalley.com\\/showthread.php\\/643470-Richard-Sherman-amp-Doug-Baldwin-Dissing-Chris-Carter-After-Calling-Their-Receivers-Pedestrian!\",\"is_sticker\":0,\"import_datetime\":\"2015-08-24 17:51:49\",\"trending_datetime\":\"2018-06-22 15:21:22\",\"user\":{\"avatar_url\":\"https:\\/\\/media0.giphy.com\\/channel_assets\\/ufc\\/ejy3O7Yzea89.png\",\"avatar\":\"https:\\/\\/media0.giphy.com\\/channel_assets\\/ufc\\/ejy3O7Yzea89.png\",\"banner_url\":\"https:\\/\\/media0.giphy.com\\/headers\\/ufc\\/JqCYBEtpE1gO.jpg\",\"banner_image\":\"https:\\/\\/media0.giphy.com\\/headers\\/ufc\\/JqCYBEtpE1gO.jpg\",\"profile_url\":\"https:\\/\\/giphy.com\\/ufc\\/\",\"username\":\"ufc\",\"display_name\":\"UFC\"},\"images\":{\"fixed_height_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_s.gif\",\"width\":\"272\",\"height\":\"200\"},\"original_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy_s.gif\",\"width\":\"320\",\"height\":\"235\"},\"fixed_width\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.gif\",\"width\":\"200\",\"height\":\"147\",\"size\":\"702760\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.mp4\",\"mp4_size\":\"43562\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.webp\",\"webp_size\":\"568274\"},\"fixed_height_small_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100_s.gif\",\"width\":\"136\",\"height\":\"100\"},\"fixed_height_downsampled\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_d.gif\",\"width\":\"272\",\"height\":\"200\",\"size\":\"180093\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_d.webp\",\"webp_size\":\"77688\"},\"preview\":{\"width\":\"320\",\"height\":\"234\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.mp4\",\"mp4_size\":\"48304\"},\"fixed_height_small\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.gif\",\"width\":\"136\",\"height\":\"100\",\"size\":\"354433\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.mp4\",\"mp4_size\":\"24612\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.webp\",\"webp_size\":\"337304\"},\"downsized_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy_s.gif\",\"width\":\"320\",\"height\":\"235\"},\"downsized\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"downsized_large\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"fixed_width_small_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w_s.gif\",\"width\":\"100\",\"height\":\"73\"},\"preview_webp\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.webp\",\"width\":\"154\",\"height\":\"113\",\"size\":\"48690\"},\"fixed_width_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_s.gif\",\"width\":\"200\",\"height\":\"147\"},\"fixed_width_small\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.gif\",\"width\":\"100\",\"height\":\"73\",\"size\":\"193750\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.mp4\",\"mp4_size\":\"16501\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.webp\",\"webp_size\":\"216836\"},\"downsized_small\":{\"width\":\"320\",\"height\":\"234\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-downsized-small.mp4\",\"mp4_size\":\"129614\"},\"fixed_width_downsampled\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_d.gif\",\"width\":\"200\",\"height\":\"147\",\"size\":\"99881\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_d.webp\",\"webp_size\":\"47616\"},\"downsized_medium\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"original\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\",\"frames\":\"88\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.mp4\",\"mp4_size\":\"182206\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.webp\",\"webp_size\":\"1241316\"},\"fixed_height\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.gif\",\"width\":\"272\",\"height\":\"200\",\"size\":\"1281313\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.mp4\",\"mp4_size\":\"65572\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.webp\",\"webp_size\":\"873856\"},\"looping\":{\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-loop.mp4\",\"mp4_size\":\"912430\"},\"original_mp4\":{\"width\":\"480\",\"height\":\"352\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.mp4\",\"mp4_size\":\"182206\"},\"preview_gif\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.gif\",\"width\":\"166\",\"height\":\"122\",\"size\":\"49101\"},\"480w_still\":{\"url\":\"https:\\/\\/media3.giphy.com\\/media\\/3vCtbqmOnksLu\\/480w_s.jpg\",\"width\":\"480\",\"height\":\"353\"}},\"title\":\"u mad silva GIF by UFC\",\"_score\":0}, {\"type\":\"gif\",\"id\":\"3vCtbqmOnksLu\",\"slug\":\"after-carter-pedestrian-3vCtbqmOnksLu\",\"url\":\"https:\\/\\/giphy.com\\/gifs\\/after-carter-pedestrian-3vCtbqmOnksLu\",\"bitly_gif_url\":\"https:\\/\\/gph.is\\/1PN9vv9\",\"bitly_url\":\"https:\\/\\/gph.is\\/1PN9vv9\",\"embed_url\":\"https:\\/\\/giphy.com\\/embed\\/3vCtbqmOnksLu\",\"username\":\"ufc\",\"source\":\"https:\\/\\/www.lipstickalley.com\\/showthread.php\\/643470-Richard-Sherman-amp-Doug-Baldwin-Dissing-Chris-Carter-After-Calling-Their-Receivers-Pedestrian!\",\"rating\":\"g\",\"content_url\":\"\",\"source_tld\":\"www.lipstickalley.com\",\"source_post_url\":\"https:\\/\\/www.lipstickalley.com\\/showthread.php\\/643470-Richard-Sherman-amp-Doug-Baldwin-Dissing-Chris-Carter-After-Calling-Their-Receivers-Pedestrian!\",\"is_sticker\":0,\"import_datetime\":\"2015-08-24 17:51:49\",\"trending_datetime\":\"2018-06-22 15:21:22\",\"user\":{\"avatar_url\":\"https:\\/\\/media0.giphy.com\\/channel_assets\\/ufc\\/ejy3O7Yzea89.png\",\"avatar\":\"https:\\/\\/media0.giphy.com\\/channel_assets\\/ufc\\/ejy3O7Yzea89.png\",\"banner_url\":\"https:\\/\\/media0.giphy.com\\/headers\\/ufc\\/JqCYBEtpE1gO.jpg\",\"banner_image\":\"https:\\/\\/media0.giphy.com\\/headers\\/ufc\\/JqCYBEtpE1gO.jpg\",\"profile_url\":\"https:\\/\\/giphy.com\\/ufc\\/\",\"username\":\"ufc\",\"display_name\":\"UFC\"},\"images\":{\"fixed_height_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_s.gif\",\"width\":\"272\",\"height\":\"200\"},\"original_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy_s.gif\",\"width\":\"320\",\"height\":\"235\"},\"fixed_width\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.gif\",\"width\":\"200\",\"height\":\"147\",\"size\":\"702760\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.mp4\",\"mp4_size\":\"43562\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w.webp\",\"webp_size\":\"568274\"},\"fixed_height_small_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100_s.gif\",\"width\":\"136\",\"height\":\"100\"},\"fixed_height_downsampled\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_d.gif\",\"width\":\"272\",\"height\":\"200\",\"size\":\"180093\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200_d.webp\",\"webp_size\":\"77688\"},\"preview\":{\"width\":\"320\",\"height\":\"234\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.mp4\",\"mp4_size\":\"48304\"},\"fixed_height_small\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.gif\",\"width\":\"136\",\"height\":\"100\",\"size\":\"354433\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.mp4\",\"mp4_size\":\"24612\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100.webp\",\"webp_size\":\"337304\"},\"downsized_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy_s.gif\",\"width\":\"320\",\"height\":\"235\"},\"downsized\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"downsized_large\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"fixed_width_small_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w_s.gif\",\"width\":\"101\",\"height\":\"73\"},\"preview_webp\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.webp\",\"width\":\"154\",\"height\":\"113\",\"size\":\"48690\"},\"fixed_width_still\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_s.gif\",\"width\":\"200\",\"height\":\"147\"},\"fixed_width_small\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.gif\",\"width\":\"100\",\"height\":\"73\",\"size\":\"193750\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.mp4\",\"mp4_size\":\"16501\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/100w.webp\",\"webp_size\":\"216836\"},\"downsized_small\":{\"width\":\"320\",\"height\":\"234\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-downsized-small.mp4\",\"mp4_size\":\"129614\"},\"fixed_width_downsampled\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_d.gif\",\"width\":\"200\",\"height\":\"147\",\"size\":\"99881\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200w_d.webp\",\"webp_size\":\"47616\"},\"downsized_medium\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\"},\"original\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.gif\",\"width\":\"320\",\"height\":\"235\",\"size\":\"1522101\",\"frames\":\"88\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.mp4\",\"mp4_size\":\"182206\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.webp\",\"webp_size\":\"1241316\"},\"fixed_height\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.gif\",\"width\":\"272\",\"height\":\"200\",\"size\":\"1281313\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.mp4\",\"mp4_size\":\"65572\",\"webp\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/200.webp\",\"webp_size\":\"873856\"},\"looping\":{\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-loop.mp4\",\"mp4_size\":\"912430\"},\"original_mp4\":{\"width\":\"480\",\"height\":\"352\",\"mp4\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy.mp4\",\"mp4_size\":\"182206\"},\"preview_gif\":{\"url\":\"https:\\/\\/media2.giphy.com\\/media\\/3vCtbqmOnksLu\\/giphy-preview.gif\",\"width\":\"166\",\"height\":\"122\",\"size\":\"49101\"},\"480w_still\":{\"url\":\"https:\\/\\/media3.giphy.com\\/media\\/3vCtbqmOnksLu\\/480w_s.jpg\",\"width\":\"480\",\"height\":\"353\"}},\"title\":\"u mad silva GIF by UFC\",\"_score\":0}],\"pagination\":{\"total_count\":98224,\"count\":1,\"offset\":0},\"meta\":{\"status\":200,\"msg\":\"OK\",\"response_id\":\"5b32f31a574149383208f212\"}}"
    return getObjFromJsonString(GifListResponse::class.java, jsonString)
}