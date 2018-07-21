package com.example.taylor.gifbox.models

import com.example.taylor.gifbox.getGifListResponse
import com.example.taylor.gifbox.model.Gif
import com.example.taylor.gifbox.model.GifData
import com.example.taylor.gifbox.model.GifImageData
import org.junit.Assert.*
import org.junit.Test

class GifDataTest {

    @Test
    fun testMoshiLoadFromJson() {
        //TODO: Use getObjFromJsonFile instead
        val gifListResponse = getGifListResponse()
        val gifList = gifListResponse.gifList

        assertEquals("3vCtbqmOnksLu", gifList[0].id)
        assertEquals("https://giphy.com/gifs/after-carter-pedestrian-3vCtbqmOnksLu", gifList[0].url)

        assertNull(gifList[0].gifImageData.originalStill?.mp4)
        assertNull(gifList[0].gifImageData.originalStill?.mp4Size)
        assertEquals("https://media2.giphy.com/media/3vCtbqmOnksLu/giphy_s.gif", gifList[0].gifImageData.originalStill?.url)
        assertNull(gifList[0].gifImageData.originalStill?.size)
        assertEquals(320, gifList[0].gifImageData.originalStill?.width)
        assertEquals(235, gifList[0].gifImageData.originalStill?.height)

        assertNull(gifList[0].gifImageData.fixedWidthSmallStill?.mp4)
        assertNull(gifList[0].gifImageData.fixedWidthSmallStill?.mp4Size)
        assertEquals("https://media2.giphy.com/media/3vCtbqmOnksLu/100w_s.gif", gifList[0].gifImageData.fixedWidthSmallStill?.url)
        assertNull(gifList[0].gifImageData.fixedWidthSmallStill?.size)
        assertEquals(100, gifList[0].gifImageData.fixedWidthSmallStill?.width)
        assertEquals(73, gifList[0].gifImageData.fixedWidthSmallStill?.height)

        assertEquals("https://media2.giphy.com/media/3vCtbqmOnksLu/100w.mp4", gifList[0].gifImageData.fixedWidthSmall?.mp4)
        assertEquals(16501L, gifList[0].gifImageData.fixedWidthSmall?.mp4Size)
        assertEquals("https://media2.giphy.com/media/3vCtbqmOnksLu/100w.gif", gifList[0].gifImageData.fixedWidthSmall?.url)
        assertEquals(193750L, gifList[0].gifImageData.fixedWidthSmall?.size)
        assertEquals(100, gifList[0].gifImageData.fixedWidthSmall?.width)
        assertEquals(73, gifList[0].gifImageData.fixedWidthSmall?.height)

        assertEquals("https://media2.giphy.com/media/3vCtbqmOnksLu/giphy-preview.mp4", gifList[0].gifImageData.preview?.mp4)
        assertEquals(48304L, gifList[0].gifImageData.preview?.mp4Size)
        assertNull(gifList[0].gifImageData.preview?.url)
        assertNull(gifList[0].gifImageData.preview?.size)
        assertEquals(320, gifList[0].gifImageData.preview?.width)
        assertEquals(234, gifList[0].gifImageData.preview?.height)
    }

    @Test
    fun testEquals() {

        val gifListResponse1 = getGifListResponse()
        val gifListResponse2 = getGifListResponse()

        assertEquals(gifListResponse1.gifList[0], gifListResponse2.gifList[0])
        assertNotEquals(gifListResponse1.gifList[0], (gifListResponse1.gifList[1]))
    }
}