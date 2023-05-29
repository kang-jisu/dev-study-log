package com.dev.kotlin

import org.apache.commons.io.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.InputStream

@RestController
class JmeterController {

    @PostMapping("/fileupload")
    fun fileUpload(multipartFile: MultipartFile) : String {
        return multipartFile.name
    }

    @PostMapping("/fileupload_image")
    fun fileUploadImage(multipartFile: MultipartFile) : String {
//        val bytes = multipartFile.bytes
        val bytes = IOUtils.toByteArray(multipartFile.inputStream)
        if (bytes.size > 1) {
            val pngSignature = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
            val webpSignature = byteArrayOf('R'.toByte(), 'I'.toByte(), 'F'.toByte(), 'F'.toByte(), 0x00, 0x00, 0x00, 0x00, 'W'.toByte(), 'E'.toByte(), 'B'.toByte(), 'P'.toByte())
            val gifSignature = byteArrayOf('G'.toByte(), 'I'.toByte(), 'F'.toByte(), '8'.toByte())

            if (startsWith(bytes, pngSignature) ||
                startsWith(bytes, webpSignature) ||
                startsWith(bytes, gifSignature)) {
                // PNG, WebP, GIF 파일 시그니처와 일치하는 경우
                return "OK"
            }
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private fun startsWith(bytes: ByteArray, signature: ByteArray): Boolean {
        if (bytes.size < signature.size) {
            return false
        }
        for (i in signature.indices) {
            if (bytes[i] != signature[i]) {
                return false
            }
        }
        return true
    }
}